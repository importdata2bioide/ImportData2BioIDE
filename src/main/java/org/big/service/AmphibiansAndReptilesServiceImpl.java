package org.big.service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.collections4.map.HashedMap;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.common.UUIDUtils;
import org.big.entity.Dataset;
import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.entity.License;
import org.big.entity.Multimedia;
import org.big.entity.Rank;
import org.big.entity.Specimendata;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entity.Taxtree;
import org.big.entity.Team;
import org.big.entityVO.AmphibiansAndReptilesExcelVO;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.LicenseEnum;
import org.big.entityVO.RankEnum;
import org.big.repository.DatasetRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.DescriptiontypeRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.SpecimendataRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class AmphibiansAndReptilesServiceImpl implements AmphibiansAndReptilesService {

	final static Logger logger = LoggerFactory.getLogger(AmphibiansAndReptilesServiceImpl.class);
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private DatasetRepository datasetRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private SpecimendataRepository specimendataRepository;
	@Autowired
	private DescriptiontypeRepository descriptiontypeRepository;
	@Autowired
	private TaxtreeService taxtreeService;
	
	private String COPYRIGHT = "本站（（http://www.amphibiachina.org/））系本网编辑转载，转载目的在于传递更多信息，并不代表本网赞同其观点和对其真实性负责。如涉及作品内容、版权和其它问题，请在30日内与本网联系，我们将在第一时间删除内容！[声明]本站文章版权归原作者所有 内容为作者个人观点 本站只提供参考并不构成任何投资及应用建议。本站拥有对此声明的最终解释权";
	private String RIGHTSHOLDER = "中国两栖类（http://www.amphibiachina.org/）";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.big.service.AmphibiansAndReptilesService#handleAndInsertExcel()
	 */
	@Override
	public String handleAndInsertExcel(BaseParamsForm amphibiansAndReptilesForm, HttpServletRequest request) {
		// 验证
		validataBaseParamsForm(amphibiansAndReptilesForm);
		// 解析excel文件
		insertAmphibiansAndReptiles(amphibiansAndReptilesForm, request);
		logger.info(RIGHTSHOLDER+"    解析并保存已完成。");
		return "OK";

	}

	/**
	 * 
	 * title: AmphibiansAndReptilesServiceImpl.java
	 * 
	 * @param amphibiansAndReptilesForm
	 * @author ZXY
	 */
	private void insertAmphibiansAndReptiles(BaseParamsForm amphibiansAndReptilesForm, HttpServletRequest request) {
		String filePath = null;
		List<AmphibiansAndReptilesExcelVO> list = null;
		if (CommUtils.isStrNotEmpty(amphibiansAndReptilesForm.getFilePath())) {
			filePath = amphibiansAndReptilesForm.getFilePath();
		} else {
			filePath = amphibiansAndReptilesForm.getMultipartFile().getOriginalFilename();
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		System.out.println(filePath);
		list = ExcelImportUtil.importExcel(new File(filePath), AmphibiansAndReptilesExcelVO.class, params);
		if (list == null) {
			// excel中数据为空，返回
			return;
		}
		List<Taxon> taxonFamilylist = new LinkedList<>();
		List<Taxon> taxonGenuslist = new LinkedList<>();
		List<Taxon> taxonSpecieslist = new LinkedList<>();
		List<Distributiondata> distributionlist = new LinkedList<>();
		List<Description> descriptionlist = new LinkedList<>();
		List<Specimendata> specimenlist = new LinkedList<>();
		List<Multimedia> multimedialist = new LinkedList<>();
		Map<String,String> imagePathMap = new HashedMap<>();
		int count = list.size();
		logger.info("数据条数：" + list.size());
		logger.info("正在解析...");
		// 逐行解析
		int i = 0;
		for (AmphibiansAndReptilesExcelVO e : list) {
			i++;
			handleRow(e, taxonFamilylist, taxonGenuslist, taxonSpecieslist, distributionlist, descriptionlist,
					specimenlist, multimedialist, amphibiansAndReptilesForm, request,imagePathMap);
			if (i % 20 == 0) {
				logger.info("已经解析..."+i+"......"+(i*100/count)+"%");
			}
		}
		logger.info("解析完毕...");
		// 保存至数据库
		if (amphibiansAndReptilesForm.isInsert()) {
			logger.info("开始保存至数据库...");
			taxonRepository.saveAll(taxonFamilylist);
			taxonRepository.saveAll(taxonGenuslist);
			taxonRepository.saveAll(taxonSpecieslist);
			if (distributionlist.size() > 0)
				distributiondataRepository.saveAll(distributionlist);
			if (descriptionlist.size() > 0)
				descriptionRepository.saveAll(descriptionlist);
			if (specimenlist.size() > 0)
				specimendataRepository.saveAll(specimenlist);
			if (multimedialist.size() > 0) {
				multimediaRepository.saveAll(multimedialist);
			}
		}

	}

	private void handleRow(AmphibiansAndReptilesExcelVO e, List<Taxon> taxonFamilylist, List<Taxon> taxonGenuslist,
			List<Taxon> taxonSpecieslist, List<Distributiondata> distributionlist, List<Description> descriptionlist,
			List<Specimendata> specimenlist, List<Multimedia> multimedialist, BaseParamsForm amphibiansAndReptilesForm,
			HttpServletRequest request,Map<String,String> imagePathMap) {
		String province = e.getProvince();
		String familyInfo = e.getFamilyInfo();
		String genus = e.getGenus();
		String speciesChinese = e.getSpeciesChinese().trim();
		String speciesEn = e.getSpeciesEn().trim();
		String imagePath = e.getImagePath();
		String familyTaxonId = null;
		String genusTaxonId = null;
		String speciesTaxonId = null;
		// 科
		if (CommUtils.isStrNotEmpty(familyInfo)) {
			String familyChinese = CommUtils.cutChinese(familyInfo).trim();
			String familyEn = CommUtils.cutByStrAfter(familyInfo, familyChinese).trim();
			familyTaxonId = existTaxon(familyChinese, familyEn, taxonFamilylist);
			if (CommUtils.isStrEmpty(familyTaxonId)) {// 不存在
				familyTaxonId = UUIDUtils.getUUID32();
				Taxon record = new Taxon();
				record.setChname(familyChinese);
				record.setScientificname(familyEn);
				record.setId(familyTaxonId);
				Rank r = new Rank();
				r.setId(String.valueOf(RankEnum.family.getIndex()));
				record.setRank(r);
				record.setRankid(String.valueOf(RankEnum.family.getIndex()));
				EntityInit.initTaxon(record, amphibiansAndReptilesForm);
				taxonFamilylist.add(record);

			}
		}
		// 属
		if (CommUtils.isStrNotEmpty(genus)) {
			String genusChinese = genus.trim();
			String genusEn = speciesEn.split(" ")[0].trim();
			genusTaxonId = existTaxon(genusChinese, genusEn, taxonGenuslist);
			if (CommUtils.isStrEmpty(genusTaxonId)) {// 不存在
				genusTaxonId = UUIDUtils.getUUID32();
				Taxon record = new Taxon();
				record.setChname(genusChinese);
				record.setScientificname(genusEn);
				record.setId(genusTaxonId);
				Rank r = new Rank();
				r.setId(String.valueOf(RankEnum.genus.getIndex()));
				record.setRank(r);
				record.setRankid(String.valueOf(RankEnum.genus.getIndex()));
				EntityInit.initTaxon(record, amphibiansAndReptilesForm);
				JSONObject js = new JSONObject();
				js.put("parentId", familyTaxonId);
				js.put("parentName", familyInfo);
				record.setRemark(String.valueOf(js));
				taxonGenuslist.add(record);
			}
		}
		// 种
		speciesTaxonId = existTaxon(speciesChinese, speciesEn, taxonSpecieslist);
		if (CommUtils.isStrEmpty(speciesTaxonId)) {// 不存在
			speciesTaxonId = UUIDUtils.getUUID32();
			Taxon record = new Taxon();
			record.setChname(speciesChinese);
			record.setScientificname(speciesEn);
			record.setId(speciesTaxonId);
			Rank r = new Rank();
			r.setId(String.valueOf(RankEnum.species.getIndex()));
			record.setRank(r);
			record.setRankid(String.valueOf(RankEnum.species.getIndex()));
			EntityInit.initTaxon(record, amphibiansAndReptilesForm);
			JSONObject js = new JSONObject();
			js.put("parentId", genusTaxonId);
			js.put("parentName", genus);
			record.setRemark(String.valueOf(js));
			taxonSpecieslist.add(record);
			// desc  描述和标本信息(species存在，描述信息也已经存在)
			handleDesc(amphibiansAndReptilesForm, e, descriptionlist, specimenlist, record);
		}
		// 地理分布
		handleDisTributionByTaxon(speciesTaxonId, province, distributionlist, amphibiansAndReptilesForm);
		// 多媒体
		if(CommUtils.isStrEmpty(imagePathMap.get(imagePath))) {
			imagePathMap.put(imagePath, imagePath);
			handleMultimediaByTaxon(speciesTaxonId, imagePath, multimedialist, amphibiansAndReptilesForm, request,
					province);
		}
	}

	private void handleMultimediaByTaxon(String taxonId, String imagePath, List<Multimedia> list, BaseParamsForm params,
			HttpServletRequest req, String province) {
		if (CommUtils.isStrEmpty(imagePath)) {
			return;
		}
		List<String> files = CommUtils.getAllFiles(imagePath, null);
		if (files == null || files.size() == 0) {
			return;
		}
		// 项目路径/CommUtils.imageUploadPath/Teamid/Datasetid/Taxonid/文件名称
		String taxasetId = params.getmTaxasetId();
		Taxaset taxaset = taxasetRepository.findOneById(taxasetId);
		String datasetId = taxaset.getDataset().getId();
		if (CommUtils.isStrEmpty(datasetId)) {
			throw new ValidationException("Taxaset 数据不规范，datasetId值为空！无法继续...");
		}
		Dataset dataset = datasetRepository.findOneById(datasetId);
		String teamId = dataset.getTeam().getId();
		if (CommUtils.isStrEmpty(teamId)) {
			throw new ValidationException("Dataset 数据不规范，teamId值为空！无法继续...");
		}
		Team team = teamRepository.findOneById(teamId);
		String id = team.getId();
		if (CommUtils.isStrEmpty(id)) {
			throw new ValidationException("Team 数据不规范，id值为空！无法继续...");
		}
		String realPath = "E:/003采集系统/可放入服务器的图片/upload/images/"; // 本地存储路径
//		String realPath = "D:\\eclipse-workspace\\ImportData2BioIDE\\ImportData2BioIDE\\src\\main\\webapp\\upload\\images\\"; // 本地存储路径

		realPath = realPath + team.getId() + "/" + dataset.getId() + "/" + taxonId + "/";
		for (String filePath : files) {
			String saveImageName = CommUtils.saveImage(new File(filePath), realPath);
			if (CommUtils.isStrNotEmpty(saveImageName)) {
				// create a new record
				Multimedia record = new Multimedia();
				record.setId(UUIDUtils.getUUID32());
				record.setTitle(filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf(".")));
				record.setLisenceid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				License license = new License();
				license.setId(record.getLisenceid());
				record.setLicense(license);
				record.setRightsholder(RIGHTSHOLDER);
				record.setPath(team.getId() + "/" + dataset.getId() + "/" + taxonId + "/" + saveImageName);
				record.setProvince(province);
				record.setCountry("中国");
				Taxon taxon = new Taxon();
				taxon.setId(taxonId);
				record.setTaxon(taxon);
				record.setSuffix(saveImageName.substring(saveImageName.lastIndexOf(".") + 1));
				record.setContext("中国两栖类（http://www.amphibiachina.org/class）");
				record.setCopyright(COPYRIGHT);
				record.setMediatype("1");
				EntityInit.initMultimedia(record, params);//填充必要的参数
				list.add(record);
			}
		}

	}

	private void handleDesc(BaseParamsForm params, AmphibiansAndReptilesExcelVO e,
			List<Description> descriptionlist, List<Specimendata> specimenlist, Taxon taxon) {
		String desc = e.getDesc();//鉴别特征
		String bodyLength = e.getBodyLength();//形态特征
		String habitat = e.getHabitat();// 生物学信息
		String distribution = e.getDistribution();//地理分布
		String typeSpecimen = e.getTypeSpecimen();//模式标本
		String typeLocality = e.getTypeLocality();//模式产地
		// add desc
		if (CommUtils.isStrNotEmpty(desc) && !desc.trim().equals("无")) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("鉴别特征");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的鉴别特征");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(desc);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
				
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：生物学");
			}
		}
		// add bodyLength
		if (CommUtils.isStrNotEmpty(bodyLength) && !bodyLength.trim().equals("无")) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("形态描述");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的形态特征");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(bodyLength);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：形态描述");
			}
		}
		// add habitat
		if (CommUtils.isStrNotEmpty(habitat)  && !habitat.trim().equals("无")) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("生物学");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的生物学信息");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(habitat);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：生物学");
			}
		}
		// add distribution
		if (CommUtils.isStrNotEmpty(distribution) && !distribution.trim().equals("无") ) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("分布信息");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的分布信息");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(distribution);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：分布信息");
			}
		}
		// add typeSpecimen
		if (CommUtils.isStrNotEmpty(typeSpecimen) && !typeSpecimen.trim().equals("无")) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("其他信息");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的其他信息");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(typeSpecimen);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：其他信息");
			}
		}
		// add typeLocality
		if (CommUtils.isStrNotEmpty(typeLocality) && !typeLocality.trim().equals("无")) {
			Descriptiontype type = descriptiontypeRepository.findOneByName("其他信息");
			if (type != null) {
				Description d = new Description();
				d.setTaxon(taxon);
				d.setDestypeid(String.valueOf(type.getId()));
				d.setDescriptiontype(type);
				d.setId(UUIDUtils.getUUID32());
				d.setDestitle(taxon.getScientificname()+"的其他信息");
				d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
				d.setDescontent(typeLocality);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				d.setdCopyright(COPYRIGHT);
				d.setRightsholder(RIGHTSHOLDER);
				d.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
				EntityInit.initDescription(d, params);
				descriptionlist.add(d);
			} else {
				logger.info("在 descriptiontype 表中 没有找到描述类型：其他信息");
			}
		}

	}

	private void handleDisTributionByTaxon(String speciesTaxonId, String province,
			List<Distributiondata> distributionlist, BaseParamsForm params) {
		// {"geoIds": "03265241BC7B474DAF05A1146755FD4C&5A1146755F"
		// 是否已经存在taxon的分布信息
		String discontent = province;// 原文
		province = province.substring(0, 2);
		if (province.equals("台湾")) {
			province = "台湾省";
		} else if (province.equals("北京")) {
			province = "北京市";
		}
		List<Geoobject> list = geoobjectRepository.findByLikeCngeoname(province);
		boolean exist = false;
		for (Distributiondata distribution : distributionlist) {
			if (distribution.getTaxonid().equals(speciesTaxonId)) {
				String geojson = distribution.getGeojson();
				JSONObject jsonObject = CommUtils.strToJSONObject(geojson);
				String values = jsonObject.get("geoIds").toString();
				if (list.size() > 0) {
					String id = list.get(0).getId();
					if (values.contains(id)) {
						return;
					} else {
						values = values + "&" + id;
					}
				} else {
					logger.info("找不到分布地：" + province);
				}
				if (values.endsWith("&")) {
					values = values.substring(0, values.length() - 1);
				}
				jsonObject.put("geoIds", values);
				distribution.setGeojson(String.valueOf(jsonObject));
				distribution.setDiscontent(distribution.getDiscontent() + "、" + discontent);
				exist = true;
				break;
			}
		}
		// 不存在，创建一个新的
		if (!exist) {
			JSONObject jsonObject = new JSONObject();
			if (list.size() > 0) {
				jsonObject.put("geoIds", list.get(0).getId());
			} else {
				logger.info("找不到分布地：" + province);
			}
			Distributiondata d = new Distributiondata();
			d.setId(UUIDUtils.getUUID32());
			Taxon taxon = new Taxon();
			taxon.setId(speciesTaxonId);
			d.setTaxon(taxon);
			d.setTaxonid(speciesTaxonId);
			d.setGeojson(String.valueOf(jsonObject));
			d.setDiscontent(discontent);// 分布原文
			EntityInit.initDistributiondata(d, params);
			distributionlist.add(d);
		}
	}

	private String existTaxon(String familyChinese, String familyEn, List<Taxon> taxonFamilylist) {
		for (Taxon taxon : taxonFamilylist) {
			if (taxon.getChname().equals(familyChinese) && taxon.getScientificname().equals(familyEn)) {
				return taxon.getId();
			}
		}
		return null;
	}

	private void validataBaseParamsForm(BaseParamsForm amphibiansAndReptilesForm) {
		JSONObject jsonObject = new JSONObject();
		MultipartFile multipartFile = amphibiansAndReptilesForm.getMultipartFile();
		if (multipartFile != null && CommUtils.isStrNotEmpty(multipartFile.getOriginalFilename())) {
			String fileName = multipartFile.getOriginalFilename();// 文件原名称
			System.out.println("fileName = " + fileName);
			String type = fileName.indexOf(".") != -1
					? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
					: null;// 文件后缀名
			if (type != null) {// 判断文件类型是否为空
				if (!"xlsx".equals(type.toUpperCase())) {
					jsonObject.put("fileType", "不是我想要的文件类型,请按要求重新上传");
				}
			} else {
				jsonObject.put("fileType", "无法识别的文件类型");
			}
		} else if (CommUtils.isStrNotEmpty(amphibiansAndReptilesForm.getFilePath())) {
			if (!new File(amphibiansAndReptilesForm.getFilePath()).exists()) {
				jsonObject.put("fileType", "目标文件不存在");
			}
		} else {
			jsonObject.put("MultipartFile or filePath", "不能为空");
		}
		if (CommUtils.isStrEmpty(amphibiansAndReptilesForm.getmLoginUser())) {
			jsonObject.put("录入人id", "不能为空");
		}
		if (CommUtils.isStrEmpty(amphibiansAndReptilesForm.getmSourcesid())) {
			jsonObject.put("数据源id", "不能为空");
		}
		if (CommUtils.isStrEmpty(amphibiansAndReptilesForm.getmInputtimeStr())) {
			jsonObject.put("录入时间（字符串）", "不能为空");
		}
		if (CommUtils.isStrEmpty(amphibiansAndReptilesForm.getmTaxasetId())) {
			jsonObject.put("分类单元集id", "不能为空");
		}
		if (CommUtils.isStrEmpty(amphibiansAndReptilesForm.getmTaxtreeId())) {
			jsonObject.put("分类树ID", "不能为空");
		}
		if (jsonObject.size() > 0) {
			throw new ValidationException(jsonObject.toJSONString());
		}

	}

	@Override
	public String insertTree(BaseParamsForm baseParamsForm) {
		String mTaxasetId = baseParamsForm.getmTaxasetId();
		String mTaxtreeId = baseParamsForm.getmTaxtreeId();
		validateParams(mTaxasetId,mTaxtreeId);
		List<Taxon> taxonlist = taxonRepository.findByTaxaset(mTaxasetId);
		logger.info("从分类单元集下，查询到taxon个数："+taxonlist.size());
		taxtreeService.saveTreeByJsonRemark(taxonlist, mTaxtreeId);
		return "OK";
	}

	private void validateParams(String mTaxasetId, String mTaxtreeId) {
		JSONObject jsonObject = new JSONObject();
		if (CommUtils.isStrEmpty(mTaxasetId)) {
			jsonObject.put("分类单元集ID", "不能为空");
		}else {
			Taxaset taxaset = taxasetRepository.findOneById(mTaxasetId);
			if(taxaset == null) {
				jsonObject.put("找不到该分类单元集", "[id = "+mTaxasetId+"]");
			}
		}
		if (CommUtils.isStrEmpty(mTaxtreeId)) {
			jsonObject.put("分类树ID", "不能为空");
		}else {
			Taxtree taxtree = taxtreeService.findOneById(mTaxtreeId);
			if(taxtree == null) {
				jsonObject.put("找不到该分类树", "[id = "+mTaxtreeId+"]");
			}
		}
		if (jsonObject.size() > 0) {
			throw new ValidationException(jsonObject.toJSONString());
		}
		
	}

}
