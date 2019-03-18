package org.big.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.common.CommUtils;
import org.big.common.ConnDB;
import org.big.common.FilesUtils;
import org.big.entity.Citation;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entityVO.BirdListComparisonExcelVO;
import org.big.entityVO.NametypeEnum;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 2019鸟类名录对比
 * new vs old 新版比对旧版结果
 * @author BIGIOZ
 *
 */
@Controller
public class BirdListComparisonController {
	@Autowired
	private TaxonRepository taxonRepository;

	@RequestMapping("/tobirdListComparison")
	public String jump2Page() {
		// 页面跳转 do nothing
		return "birdListComparison";
	}

	@RequestMapping("/importbirdListComparison")
	public void ImportbirdListComparison(HttpServletResponse response)
			throws Exception {
		List<Citation> citationlist = new ArrayList<>();
		// 第一步，读取excel
		List<BirdListComparisonExcelVO> list = readExcel("E:\\003采集系统\\新版比对旧版结果.xlsx");
		// 第二步，解析文件，返回解析结果（true|成功/失败|false）
		parseAndSaveList(list, citationlist);
		System.out.println("解析完毕...");
		for (Citation c : citationlist) {
			if(CommUtils.isStrEmpty(c.getOrderNameL())) {
				System.out.println(c.getRemark() +"||"+c.getSciname()+"||"+c.getOrderNameL());
			}
		}
		// 第三步，引证导出excel
		if (true) {
			// 导出操作
			FilesUtils.exportExcel(citationlist, "引证", "引证", Citation.class, URLEncoder.encode("Citation.xls", "UTF-8"),
					response);
			FilesUtils.exportExcel(list, "taxon", "taxon", BirdListComparisonExcelVO.class, URLEncoder.encode("taxon.xls", "UTF-8"),
					response);
		}
		
	}

	private boolean parseAndSaveList(List<BirdListComparisonExcelVO> list, List<Citation> citationlist)
			throws Exception {
		boolean result = true;
		// 逐条循环并解析，遇到错误返回false,并在控制台输出错误信息
		int j = 0;
		for (int i = 0; i < list.size(); i++) {
			BirdListComparisonExcelVO birdListComparison = list.get(i);
			j++;
			Citation citation = new Citation();
			String newGenus = birdListComparison.getNewGenus();
			String newEpithet = birdListComparison.getNewEpithet();
			String oldGenus = birdListComparison.getOldGenus();
			String oldEpithet = birdListComparison.getOldEpithet();
			birdListComparison.getChineseName();
			String remark = birdListComparison.getRemark();
			String oldSciName = oldGenus + " " + oldEpithet;
			String newSciName = newGenus.trim() + " " + newEpithet.trim();
			citation.setRemark(remark);//备注（新增种/属名不同，种加词相同  [新组合]/属名不同，种加词不同  [新组合并修改种名 或 拼写错误]）
			validateAndGetParentInfo(newSciName,null,birdListComparison);//BirdListComparisonExcelVO增加目、科信息
			if (remark.trim().equals("新增种")) {
				citation.setTaxonName(newSciName);
				citation.setNametype(NametypeEnum.acceptedName.getIndex());
				citation.setSciname(newSciName);
				//在新系统中 验证taxon是否存在，并获取科、目信息
				validateAndGetParentInfo(newSciName,citation,null);
				citationlist.add(citation);
				continue;
			}
			citation.setTaxonName(newSciName);// 接受名（引证名称的接受名）
			//在新系统中 验证taxon是否存在，并获取科、目信息
			validateAndGetParentInfo(newSciName,citation,null);
			
			// 在旧系统中查询taxon
			// 0113DE14-FB9C-46AF-958F-CA9387AFF4EC
			String sql = "select *  from taxa where TreeID = 'EF162D52-81E5-4279-B35D-E066616D3FBE' and StatusID = '67280F4A-D8D6-4CAD-BCDA-843866010852' and RankID in (select id from Rank_List where RankEN like '%species%')";
			sql = sql + " and (Latin_Name like '%" + oldSciName + "%' or fullname like '%" + oldSciName + "%')";
			ResultSet resultSet = this.query(sql);
			resultSet.last();
			int rowCount = resultSet.getRow(); // 获得ResultSet的总行数
			citation.setSciname(oldSciName);// 引证名称
			citation.setNametype(NametypeEnum.synonym.getIndex());// 名称状态(类型)
			if (rowCount != 1) {
				result = false;
				System.out.println(
						j + "、鸟纲-郑-雷-sp2000：无法根据阶元和学名唯一匹配一条数据,匹配到的数据条数：" + rowCount + "\t oldTaxon:" + oldSciName);
			} else {
				resultSet.beforeFirst();
				while (resultSet.next()) {
					Map<String, String> mapColum = getCitationstrAndAuthorship(resultSet.getString("id"), oldSciName);
					String authorship = mapColum.get("authorship");
					authorship = authorship.replace("Unknown.", "");
					citation.setAuthorship(authorship);// 命名信息
					
					String citationstr = mapColum.get("citationstr");
					citationstr = citationstr.replaceAll("<br>", "\r\n");
					citationstr = citationstr.replace("Unknown.", "");
					citation.setCitationstr(citationstr);// 完整引证
				}

			}
			citationlist.add(citation);
			//接受名
			Citation citationAccept = new Citation();
			citationAccept.setTaxonName(newSciName);
			citationAccept.setNametype(NametypeEnum.acceptedName.getIndex());
			citationAccept.setSciname(newSciName);
			citationAccept.setRemark(remark);
			validateAndGetParentInfo(newSciName,citationAccept,null);
			citationlist.add(citationAccept);

		}
		return result;

	}

	private void validateAndGetParentInfo(String newSciName, Citation citation,BirdListComparisonExcelVO b) {
		List<Taxon> newTaxons = taxonRepository.findByTaxonSciNameAndRankAndDSid(newSciName, "7",
				"7da1c0ac-18c6-4710-addd-c9d49e8a2532");
		
		if (newTaxons.size() != 1) {
			System.out.println( "中国鸟类分类与分布名录第三版：无法根据阶元和学名唯一匹配一条数据,匹配到的数据条数：" + newTaxons.size() + "\t newTaxon:"
					+ newSciName);
		}else {
			//查询此种的上级科、目信息
			Taxon taxon = newTaxons.get(0);
			String familyIdremark = taxon.getRemark();
			JSONObject jsonObject = CommUtils.strToJSONObject(familyIdremark);
			String familyId = String.valueOf(jsonObject.get("familyId"));//科id
			Taxon family = taxonRepository.findOneById(familyId);//科
			if(family.getRank().getId().trim().equals("5")) {
				Taxaset taxaset = family.getTaxaset();
				String tsname = taxaset.getTsname();
				String tsinfo = taxaset.getTsinfo();
				if(citation!=null) {
					//目信息
					citation.setOrderNameL(tsinfo);
					citation.setOrderNameC(tsname);
					//科信息
					citation.setFamilyNameC(family.getChname());
					citation.setFamilyNameL(family.getScientificname());
				}else if (b!=null) {
					//目信息
					b.setOrderNameL(tsinfo);
					b.setOrderNameC(tsname);
					//科信息
					b.setFamilyNameC(family.getChname());
					b.setFamilyNameL(family.getScientificname());
				}
				
			}else {
				System.out.println("非科"+family.getRank().getId());
			}
		}
		
	}

	private Map<String, String> getCitationstrAndAuthorship(String taxaId, String sciName) throws Exception {
		Map<String, String> mapColum = new HashMap<>();
		String authorship = "";
		String citationstr = "";
		String typeLocation = "";
		// 命名人和年代
		String sql = "select * from Species where cast(TaxaID as varchar(50))   = '" + taxaId + "'";
		ResultSet rs = this.query(sql);
		while (rs.next()) {
			String namedPerson = rs.getString("Named_Person");
			String namedDate = rs.getString("Named_Date");
			String remark = rs.getString("Remark");
			typeLocation = rs.getString("Type_Location");
			if (CommUtils.isStrEmpty(namedDate)) {// Named_Date为空
				authorship = namedPerson;
			} else if (CommUtils.isStrNotEmpty(namedDate) && namedPerson.contains(namedPerson)) {// Named_Date非空且Named_Person包含Named_Date
				authorship = namedPerson;
			} else {
				authorship = namedPerson + "," + namedDate;
			}
			if (CommUtils.isStrNotEmpty(typeLocation) && CommUtils.isStrNotEmpty(remark)) {
				citationstr = remark + "(" + typeLocation + ")";
			} else if (CommUtils.isStrEmpty(typeLocation) && CommUtils.isStrNotEmpty(remark)) {
				citationstr = remark;
			}
			break;
		}
		if (rs != null) {
			rs.close();
		}
		if (CommUtils.isStrNotEmpty(authorship)) {
			authorship = authorship.replace("(", "");
			authorship = authorship.replace(")", "");
			authorship = authorship.replace("（", "");
			authorship = authorship.replace("）", "");
			authorship = authorship.trim();
		}
		// species表中没有查询到完整引证,到ref表中查询
		if (CommUtils.isStrEmpty(citationstr)) {
			String sql2 = "select s.taxaId, p.* from Species_Paper  s   left join Papers p  on p.id = s.PaperID  where s.Is_First = 'true'  and TaxaID = '"
					+ taxaId + "'";
			ResultSet rs2 = this.query(sql2);
			while (rs2.next()) {
				String author = rs2.getString("Author");
				String years = rs2.getString("Years");
				String title = rs2.getString("Title");
//				String journal = rs2.getString("Journal");
//				String remark = rs2.getString("Remark");
				String source = rs2.getString("Source");
				if (CommUtils.isStrNotEmpty(source)) {
					if (!source.contains(title)) {
						source = title +"."+source;
					}
					if (!source.contains(years)) {
						source = years +"."+source;
					}
					if (!source.contains(author)) {
						source = author +"."+source;
					}
					if (!source.contains(sciName)) {
						source = sciName +","+source;
					}
					if(CommUtils.isStrNotEmpty(typeLocation)) {
						source = source + "(" + typeLocation + ")";
					}
					citationstr = source;
				}
			}
			if (rs2 != null) {
				rs2.close();
			}
		}
		mapColum.put("authorship", authorship);
		mapColum.put("citationstr", citationstr);
		return mapColum;
	}

	/**
	 * 读取excel 文件
	 * 
	 * @param path
	 * @return
	 */
	private List<BirdListComparisonExcelVO> readExcel(String path) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<BirdListComparisonExcelVO> list = ExcelImportUtil.importExcel(new File(path),
				BirdListComparisonExcelVO.class, params);
		System.out.println("读取excel所消耗时间：" + (new Date().getTime() - start));
		System.out.println("数量：" + list.size());
		System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
		System.out.println("读取excel完成");
		return list;
	}

	public ResultSet query(String sql) throws Exception {
		Connection connDB = ConnDB.getConnDB(null);
		PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = prepareStatement.executeQuery();
		return rs;
	}

}
//导出excel There is no getter for property named 'OrderNameL' in 'null'
