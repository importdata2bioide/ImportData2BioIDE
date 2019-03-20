package org.big.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.common.UUIDUtils;
import org.big.entity.Citation;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.ExcelUntilB;
import org.big.entityVO.ExcelUntilK;
import org.big.entityVO.ExcelUntilP;
import org.big.entityVO.NametypeEnum;
import org.big.repository.RefRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class BirdAddDataImpl implements BirdAddData {
	@Autowired
	private RefRepository refRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private BatchInsertService batchInsertService;

	private String userId_wangtianshan = "95c24cdc24794909bd140664e2ee9c3b";
	private String datasetId_2019bird = "7da1c0ac-18c6-4710-addd-c9d49e8a2532";
	private String sourcesId_2019bird = "428700b4-449e-4284-a082-b2fe347682ff";

	@Override
	public void importByExcel() throws Exception {
		String folderPath = "E:\\003采集系统\\0012鸟类名录\\";
		String reffilePath = folderPath + "参考文献汇总_0308.xlsx";
		String UnPasseriformesCitationPath = folderPath + "非雀形目.xlsx";
		String passeriformesCitationPath = folderPath + "雀形目（整理顺序）.xlsx";
		String otherCitationPath = folderPath + "其他-引证信息.xlsx";
		boolean save = false;
		// 1、读取参考文献，并保存到map中
		Map<String, String> refMap = readRefs(reffilePath);
		// 2、导入非雀形目引证信息
//		importUnPasseriformesCitation(refMap, UnPasseriformesCitationPath, save);
		// 3、导入雀形目引证信息
//		importUnPasseriformesCitation(refMap, passeriformesCitationPath, save);
		// 4、导入接受名引证信息
		otherCitationPath(refMap, otherCitationPath, save);
	}

	/**
	 * 
	 * @Description 6、 导入引证信息“其他-引证信息.xlsx”，
	 * @Description 这表都是接受名引证，请用“author”“year”两个字段更新命名信息“authorship”字段，
	 * @Description 如果原来“authorhsip”字段带括号，请为新的authorship添加括号
	 * @Description
	 * @param refMap
	 * @param otherCitationPath
	 * @param save
	 * @author ZXY
	 */
	private void otherCitationPath(Map<String, String> refMap, String filePath, boolean save) {
		List<ExcelUntilP> list = readExcel(filePath, ExcelUntilP.class);
		for (ExcelUntilP row : list) {
			String acceptName = row.getColF().trim();
			List<Object[]> objlist = taxonRepository.findByDatasetAndSciName(datasetId_2019bird, acceptName);
			Taxon taxon = turnObjToTaxon(objlist, acceptName);
		}

	}

	/**
	 * 
	 * @Description 3、 导入引证信息“非雀形目.xlsx”：
	 * @Description 判断名称命名信息是否完整，进行补充
	 * @Description 如果备注写“新组合”，给接受名的命名信息加上括号
	 * @param refMap
	 * @author ZXY
	 * @throws Exception
	 */
	private void importUnPasseriformesCitation(Map<String, String> refMap, String filePath, boolean save)
			throws Exception {
		List<ExcelUntilK> list = readExcel(filePath, ExcelUntilK.class);
		List<Citation> resultlist = new ArrayList<>();
		List<Taxon> taxonlist = new ArrayList<>();
		// 查询taxon
		for (ExcelUntilK row : list) {
			String acceptName = row.getColJ().trim();
			List<Object[]> objlist = taxonRepository.findByDatasetAndSciName(datasetId_2019bird, acceptName);
			Taxon taxon = turnObjToTaxon(objlist, acceptName);
			String sciname = row.getColE().trim();
			String author = row.getColF();// 可能空
			String nametypeStr = row.getColG();// 可能空值
			int nametype = -1;
			String refType = "";
			switch (nametypeStr) {
			case "accepted name":
				nametype = NametypeEnum.acceptedName.getIndex();
				refType = "0";
				break;
			case "synonym":
				nametype = NametypeEnum.synonym.getIndex();
				refType = "1";
				break;
			default:
				throw new ValidationException("未定义的nameType:" + nametypeStr);
			}
			String citationstr = row.getColH();// 可能空值
			String ref = row.getColI();
			JSONArray jsonArray = new JSONArray();
			if (StringUtils.isNotEmpty(ref)) {
				ref = ref.replace(",", ";");
				String[] refSeqs = ref.split(";");
				for (String seq : refSeqs) {
					seq = seq.trim();
					String refId = refMap.get(seq);
					if (StringUtils.isEmpty(refId)) {
						throw new ValidationException("未找到的参考文献序号：" + seq);
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("refE", " 0");
					jsonObject.put("refS", " 0");
					jsonObject.put("refId", "" + refId);
					jsonObject.put("refType", refType);
					jsonArray.add(jsonObject);
				}

			}
			String remark = row.getColK();
			if (StringUtils.isNotEmpty(author) && remark.contains("新组合")
					&& nametype == NametypeEnum.acceptedName.getIndex()) {
				author = "(" + author + ")";
			}
			// 新建一条引证信息
			Citation record = new Citation();
			record.setId(UUIDUtils.getUUID32());
			record.setSciname(sciname);
			record.setAuthorship(author);
			record.setNametype(nametype);
			record.setCitationstr(citationstr);
			if (jsonArray.size() > 0) {
				record.setRefjson(jsonArray.toJSONString());
			}
			record.setSourcesid(sourcesId_2019bird);
			record.setSourcesidId(sourcesId_2019bird);
			record.setInputer(userId_wangtianshan);
			record.setTaxon(taxon);
			record.setRemark(remark);
			record.setStatus(1);
			resultlist.add(record);
			// 更新taxon的authorstr字段
			taxon.setAuthorstr(author);
			if (StringUtils.isNotEmpty(author)) {
				taxonlist.add(taxon);
			}

		}
		if (save) {
			batchSubmitService.saveAll(resultlist);
			batchInsertService.batchUpdateTaxonAuthorstrById(taxonlist);
		}

	}

	private Taxon turnObjToTaxon(List<Object[]> objlist, String acceptName) {
		Taxon taxon = new Taxon();
		if (objlist.size() != 1) {
			throw new ValidationException("error 从数据库里查询出多条或0条数据, scientificname=" + acceptName);
		} else {
			Object[] obj = objlist.get(0);
			taxon.setId(obj[0].toString());
			taxon.setScientificname(obj[1].toString());
			taxon.setAuthorstr(obj[2] == null ? null : obj[2].toString());
			taxon.setEpithet(obj[3] == null ? null : obj[3].toString());
			taxon.setChname(obj[4] == null ? null : obj[4].toString());
			String rankid = obj[5].toString();
			taxon.setRankid(Integer.parseInt(rankid));
			Rank rank = new Rank();
			rank.setId(rankid);
			taxon.setRank(rank);
		}

		return taxon;
	}

	/**
	 * 
	 * @Description
	 * @param filePath
	 * @return
	 * @author ZXY
	 */
	private Map<String, String> readRefs(String filePath) {
		List<ExcelUntilB> list = readExcel(filePath, ExcelUntilB.class);
		System.out.println("从excel读取参考文献条数：" + list.size());
		Map<String, String> refMaP = new HashMap<>(list.size() + 10);
		for (ExcelUntilB excelUntilB : list) {
			String refstr = excelUntilB.getColB().trim();
			String seq = excelUntilB.getColA().trim();

//			Ref ref = refRepository.findByRefstrAndInputer("戴波, 付义强, 王家才等. 灰腹地莺在四川的分布. 动物学杂志(已接收).", userId_wangtianshan);
			Ref ref = refRepository.findByRefstrAndInputer(refstr, userId_wangtianshan);
			if (ref == null) {
				throw new ValidationException(seq + ",数据库中没有找到完整题录=" + refstr);
			} else {
				refMaP.put(seq, ref.getId());
			}
		}
		System.out.println("excel文件行数：" + list.size() + ",map大小：" + refMaP.size());
		return refMaP;
	}

	private <T> List<T> readExcel(String path, Class<T> t) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<T> list = ExcelImportUtil.importExcel(new File(path), t, params);
		System.out.println("读取excel所消耗时间：" + (new Date().getTime() - start));
		System.out.println(path + ",excel行数：" + list.size());
		System.out.println("打印第一行表格内容：" + ReflectionToStringBuilder.toString(list.get(0)));
		System.out.println("读取excel完成");
		return list;
	}

}
