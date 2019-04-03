package org.big.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.common.FilesUtils;
import org.big.common.UUIDUtils;
import org.big.constant.ConfigConsts;
import org.big.entity.Citation;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.BirdListComparisonExcelVO;
import org.big.entityVO.ExcelUntilB;
import org.big.entityVO.ExcelUntilD;
import org.big.entityVO.ExcelUntilK;
import org.big.entityVO.ExcelUntilP;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.RankEnum;
import org.big.repository.CitationRepository;
import org.big.repository.RefRepository;
import org.big.repository.TaxonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class BirdAddDataImpl implements BirdAddData {
	
	final static Logger logger = LoggerFactory.getLogger(BirdAddDataImpl.class);

	@Autowired
	private RefRepository refRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private ToolService toolService;
	@Autowired
	private TaxasetService taxasetService;

	private String userId_wangtianshan = "95c24cdc24794909bd140664e2ee9c3b";
	private String datasetId_2019bird = "7da1c0ac-18c6-4710-addd-c9d49e8a2532";
	private String sourcesId_2019bird = "428700b4-449e-4284-a082-b2fe347682ff";
	Map<String, String> sciNameMap = new HashMap<>();

	@Override
	public void importByExcel() throws Exception {
		String folderPath = "E:\\003采集系统\\0012鸟类名录\\";
		String reffilePath = folderPath + "最终-整合-参考文献.xlsx";
//		String UnPasseriformesCitationPath = folderPath + "非雀形目.xlsx";
//		String passeriformesCitationPath = folderPath + "雀形目（整理顺序）.xlsx";
		String integrationPath = folderPath + "最终-整合-0.xlsx";
		String otherCitationPath = folderPath + "其他-引证信息.xlsx";
		boolean save = false;
		// 1、读取参考文献，并保存到map中
		Map<String, String> refMap = readRefs(reffilePath);
		// 2、导入非雀形目引证信息
//		importCitation(refMap, UnPasseriformesCitationPath, save);
		// 3、导入雀形目引证信息
//		importCitation(refMap, passeriformesCitationPath, save);
		//导入整合引证
		importCitation(refMap, integrationPath, save);
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
	 * @throws Exception
	 */
	private void otherCitationPath(Map<String, String> refMap, String filePath, boolean save) throws Exception {
		List<ExcelUntilP> list = readExcel(filePath, ExcelUntilP.class);
		List<Citation> resultAddlist = new ArrayList<>();
		List<Citation> resultUpdatelist = new ArrayList<>();
		List<Taxon> updateTaxonlist = new ArrayList<>();
		int i = 0;
		for (ExcelUntilP row : list) {
			String acceptName = row.getColF();
			if (StringUtils.isEmpty(acceptName)) {
				continue;
			}
			acceptName = acceptName.trim();
			List<Object[]> objlist = taxonRepository.findByDatasetAndSciName(datasetId_2019bird, acceptName);
			Taxon taxon = null;
			try {
				taxon = turnObjToTaxon(objlist, acceptName);
			} catch (Exception e) {

			}
			if (taxon != null) {
				String author = row.getColK();
				String year = row.getColL();
				String authorAndYear = row.getColH();//excel文件里的命名信息
				String authorship = "";//存储到数据库的命名信息
				if(StringUtils.isEmpty(author) &&StringUtils.isEmpty(year)) {
					continue;
				}
				//命名人和命名年代
				authorship = row.getColK().trim() + "," + row.getColL().trim();
				if(StringUtils.isNotEmpty(authorAndYear) &&(authorAndYear.contains("（")||authorAndYear.contains("("))) {
					authorship = "("+authorship+")";
				}
				String taxonId = taxon.getId();
				Citation citation = citationRepository.findByTaxonIdAndSciname(taxonId, acceptName);
				String page = row.getColM();
				JSONObject remark = null;
				if (StringUtils.isNotEmpty(page)) {
					remark = new JSONObject();
					remark.put("page", page.trim());
				}
				if (citation != null) {
					// 前两个excel已经保存接受名引证，更新命名信息
//					logger.info("前两个excel已经保存接受名引证了:" + acceptName);
					// 更新（引证信息）操作,更新的字段有authorship、citationstr、remark
					citation.setAuthorship(authorship);
					citation.setCitationstr(row.getColN());
					citation.setRemark(String.valueOf(remark));
					resultUpdatelist.add(citation);
				} else {
					// 新增（引证信息）操作
					Citation record = new Citation();
					record.setId(UUIDUtils.getUUID32());
					record.setSciname(acceptName);
					record.setAuthorship(authorship);
					record.setNametype(NametypeEnum.acceptedName.getIndex());
					record.setCitationstr(row.getColN());
					record.setSourcesid(sourcesId_2019bird);
					record.setSourcesidId(sourcesId_2019bird);
					record.setInputer(userId_wangtianshan);
					record.setTaxon(taxon);
					record.setRemark(String.valueOf(remark));
					record.setStatus(1);
					resultAddlist.add(record);
				}
				if(StringUtils.isNotEmpty(authorship)) {
					taxon.setAuthorstr(authorship);
					updateTaxonlist.add(taxon);
				}
			}else {
				i++;
//				logger.info(row.getColA()+"	"+row.getColB()+"	"+row.getColC()+"	"+row.getColD()+"	"+row.getColE()+"	"+row.getColF()+"	"+row.getColG()+"	"+row.getColH()+"	"+row.getColI()+"	"+row.getColJ()+"	"+row.getColK()+"	"+row.getColL()+"	"+row.getColM()+"	"+row.getColN()+"	"+row.getColO()+"	"+row.getColP());
			}
		}
//		for (Citation c : resultUpdatelist) {
//			toolService.printEntity(c);
//		}
		logger.info("更新："+resultUpdatelist.size());
		logger.info("新增："+resultAddlist.size());
		logger.info("没有找到的："+i);
		if (save) {
			batchSubmitService.saveAll(resultAddlist);
			batchInsertService.batchUpdateCitationById(resultUpdatelist);
			batchInsertService.batchUpdateTaxonAuthorstrById(updateTaxonlist);
		}

	}

	private boolean existInCitationExcel(String acceptName) {
		if (sciNameMap.get(acceptName) != null) {
			return true;
		}
		return false;
	}

	private Citation turnObjToCitation(List<Object[]> objlist, String sciname) {
		Citation citation = null;
		if (objlist.size() != 1) {
			throw new ValidationException("error turnObjToCitation 从数据库里查询出多条或0条引证数据, sciname=" + sciname);
		} else {
			citation = new Citation();
			Object[] obj = objlist.get(0);
			citation.setId(obj[0].toString());
			citation.setSciname(obj[1].toString());
			citation.setAuthorship(obj[2] == null ? null : obj[2].toString());
			citation.setNametype(Integer.parseInt(obj[3].toString()));
			citation.setTaxon(new Taxon(obj[4].toString()));
		}
		return citation;
	}

	/**
	 * 
	 * @Description 3、 导入引证信息“非雀形目.xlsx”：
	 * @Description 判断名称命名信息是否完整，进行补充
	 * @Description 如果备注写“属名不同，种加词相同  [新组合]”，给接受名的命名信息加上括号
	 * @param refMap
	 * @author ZXY
	 * @throws Exception
	 */
	private void importCitation(Map<String, String> refMap, String filePath, boolean save) throws Exception {
		List<ExcelUntilK> list = readExcel(filePath, ExcelUntilK.class);
		List<Citation> resultlist = new ArrayList<>();
		List<Taxon> taxonlist = new ArrayList<>();
		// 查询taxon
		for (ExcelUntilK row : list) {
			String acceptName = row.getColJ().trim();
			if (StringUtils.isEmpty(acceptName)) {
				continue;
			}
			sciNameMap.put(acceptName, filePath);
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
				ref = ref.replace("，", ";");
				String[] refSeqs = ref.split(";");
				for (String seq : refSeqs) {
					seq = seq.trim();
					String refId = refMap.get(seq);
					if (StringUtils.isEmpty(refId)) {
						logger.error(ConfigConsts.HANDLE_ERROR+"未找到的参考文献序号：" + seq);
						continue;
//						throw new ValidationException("未找到的参考文献序号：" + seq);
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
			if (nametype == NametypeEnum.acceptedName.getIndex() && StringUtils.isNotEmpty(author) && remark.equals("属名不同，种加词相同  [新组合]")) {
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
//			batchInsertService.batchUpdateTaxonAuthorstrById(taxonlist);
		}

	}

	private Taxon turnObjToTaxon(List<Object[]> objlist, String acceptName) {
		Taxon taxon = null;
		if (objlist.size() != 1) {
			throw new ValidationException("error 从数据库里查询出多条或0条数据, scientificname=" + acceptName);
		} else {
			taxon = new Taxon();
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
		logger.info("从excel读取参考文献条数：" + list.size());
		Map<String, String> refMaP = new HashMap<>(list.size() + 10);
		for (ExcelUntilB excelUntilB : list) {
			String refstr = excelUntilB.getColB().trim();
			String seq = excelUntilB.getColA().trim();
			Ref ref = refRepository.findByRefstrAndInputer(refstr, userId_wangtianshan);
			if (ref == null) {
				
//				logger.info(seq + ",数据库中没有找到完整题录=" + refstr);
				throw new ValidationException(seq + ",数据库中没有找到完整题录=" + refstr);
			} else {
				refMaP.put(seq, ref.getId());
			}
		}
		logger.info("excel文件行数：" + list.size() + ",map大小：" + refMaP.size());
		return refMaP;
	}

	private <T> List<T> readExcel(String path, Class<T> t) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<T> list = ExcelImportUtil.importExcel(new File(path), t, params);
		logger.info("读取excel所消耗时间：" + (new Date().getTime() - start));
		logger.info(path + ",excel行数：" + list.size());
		logger.info("打印第一行表格内容：" + ReflectionToStringBuilder.toString(list.get(0)));
		logger.info("读取excel完成");
		return list;
	}

	@Override
	public void countCitationByTaxon(HttpServletResponse response) {
		List<String> genusRankIds = new ArrayList<>();
		genusRankIds.add(String.valueOf(RankEnum.genus.getIndex()));
		//属接受名引证统计
		List<Object[]> genusAcceptNamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird, genusRankIds, String.valueOf(NametypeEnum.acceptedName.getIndex()));
		//属异名引证统计
		List<Object[]> genusSynamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird, genusRankIds, String.valueOf(NametypeEnum.synonym.getIndex()));
		List<String> speciesRankIds = new ArrayList<>();
		speciesRankIds.add(String.valueOf(RankEnum.species.getIndex()));
		//species接受名引证统计
		List<Object[]> speciesAcceptNamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird, speciesRankIds, String.valueOf(NametypeEnum.acceptedName.getIndex()));
		//species异名引证统计
		List<Object[]> speciesSynamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird, speciesRankIds, String.valueOf(NametypeEnum.synonym.getIndex()));
		List<ExcelUntilD> resultlist = new ArrayList<>(genusAcceptNamelist.size()+speciesAcceptNamelist.size()+10);
//		mergeList(genusAcceptNamelist,genusSynamelist,resultlist);
		mergeList(speciesAcceptNamelist,speciesSynamelist,resultlist);
		//导出文件
		try {
			FilesUtils.exportExcel(resultlist, "引证统计", "引证统计", ExcelUntilD.class, URLEncoder.encode("CitationCount.xls", "UTF-8"),
					response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void mergeList(List<Object[]> genusAcceptNamelist, List<Object[]> genusSynamelist,
			List<ExcelUntilD> resultlist) {
		//转换成map
		Map<String,Object[]> map = new HashMap<>(genusSynamelist.size()+10);
		for (Object[] obj : genusSynamelist) {
			map.put(obj[0].toString(), obj);
		}
		//a.id,a.scientificname,b.tsname,e.sl 
		for (Object[] obj : genusAcceptNamelist) {
			String taxonId = obj[0].toString();
			String acceptName = obj[1].toString();
			String orderCHName = obj[2].toString();
			Object synameCountObj = map.get(taxonId)[3];
			int synameCount = synameCountObj==null?0:Integer.parseInt(synameCountObj.toString());
			int acceptNameCount = obj[3] == null?0:Integer.parseInt(obj[3].toString());
			ExcelUntilD row = new ExcelUntilD();
			row.setColA(orderCHName);
			row.setColB(acceptName);
			row.setColC(String.valueOf(acceptNameCount));
			row.setColD(String.valueOf(synameCount));
			resultlist.add(row);
		}
		
		
	}

	@Override
	public void updateCitationStrBySciName(HttpServletResponse response) {
		//根据数据集查询引证
//		List<Citation> list = citationRepository.findByTaxasetId(datasetId_2019bird);
		
	}

}
