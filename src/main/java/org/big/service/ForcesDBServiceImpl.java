package org.big.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.Configuration;
import org.big.common.ConnDB;
import org.big.common.OtherConnDB;
import org.big.common.ReadxmlByDom;
import org.big.common.ReturnCode;
import org.big.common.UUIDUtils;
import org.big.controller.RefsController;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Dataset;
import org.big.entity.Datasource;
import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Distributiondata;
import org.big.entity.ForcesTreeVO;
import org.big.entity.Geoobject;
import org.big.entity.Keyitem;
import org.big.entity.License;
import org.big.entity.Multimedia;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Resource;
import org.big.entity.Specimendata;
import org.big.entity.Taxaset;
import org.big.entity.Taxkey;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.entityVO.KeytypeEnum;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.PtypeEnum;
import org.big.entityVO.XmlParamsVO;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DatasetRepository;
import org.big.repository.DatasourceRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.DescriptiontypeRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.KeyitemRepository;
import org.big.repository.LicenseRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.RankRepository;
import org.big.repository.RefRepository;
import org.big.repository.SpecimendataRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxkeyRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.big.repository.TeamRepository;
import org.big.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * spring默认所有的bean都是singleton的
 * 
 * @author BIGIOZ
 *
 */
@Service
public class ForcesDBServiceImpl implements ForcesDBService {
	private final static Logger logger = LoggerFactory.getLogger(ForcesDBServiceImpl.class);
	Configuration configuration = null;

	// 项目启动后执行
	@PostConstruct
	public void initConfiguration() {
		if (configuration == null) {
			configuration = new Configuration();
		}
		logger.info("初始化旧采集系统连接参数");
		configuration.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		configuration.setUsername("sa");
		configuration.setPassword("123456");
		configuration.setUrl("jdbc:sqlserver://localhost:1433;DatabaseName=forcsdb");
	}

	private String TreeID = "40F60C0F-C2E8-479B-84C8-1C4E59331D59";
	private String loginUser = "";// 3a29945023d04ef8a134f0f017d316f0
	private String taxasetId = "00fe280f56b94beb9bea301bf52454a9";
	private String sourcesid = "665a87846a4a4720b19f65f1d60c1812";
	private String taxtreeId = "d7930c1de010482a964289b755061e59";
	String IMAGEPATH = "E:\\all of the data\\csdbimges20181022145131\\";
	static String inputtimeStr = "2018-11-22 00:02:33";

	@Autowired
	private KeyitemService keyitemService;
	@Autowired
	private AQueryForcesDB aQueryForcesDB;
	@Autowired
	private BatchInsertService batchInsertService;

	@Autowired
	private BatchSubmitService batchSubmitService;

	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private RankRepository rankRepository;
	@Autowired
	private RefRepository refRepository;
	@Autowired
	private CommonnameRepository commonnameRepository;
	@Autowired
	private DescriptiontypeRepository descriptiontypeRepository;
	@Autowired
	private DescriptionRepository descriptionRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private DatasetRepository datasetRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DatasourceRepository datasourceRepository;
	@SuppressWarnings("unused")
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private TaxkeyRepository taxkeyRepository;
	@Autowired
	private KeyitemRepository keyitemRepository;
	@Autowired
	private SpecimendataRepository specimendataRepository;

	StringBuffer refjson = new StringBuffer();
	private String uploadPath = "upload/";// 图片在新采集系统的保存路径

	@Override
	public String selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1.查询数据
		Connection connDB = null;
		ResultSet rs = null;
		List<ForcesTreeVO> treelist = new ArrayList<>(100);
		connDB = OtherConnDB.getConnDB(configuration);
		String sql = "select * from tree where TreeName not in  ('test1')";
		PreparedStatement prepareStatement = connDB.prepareStatement(sql);
		rs = prepareStatement.executeQuery();
		while (rs.next()) {
			ForcesTreeVO f = new ForcesTreeVO();
			f.setId(rs.getString("ID"));
			f.setTreeName(rs.getString("TreeName"));
			treelist.add(f);
		}
		rs.close();
//		prepareStatement.close();
		logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "查询数据集完毕，数量：" + treelist.size());
		int i = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		Document document = db.newDocument();
		// 不显示standalone="no"
		document.setXmlStandalone(true);
		Element root = document.createElement("root");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = df.parse("2018-11-22 00:00:00");

		for (ForcesTreeVO f : treelist) {
			i++;
			String name = f.getTreeName();
			f.getId();
			System.out.println("20190411打印=" + name);
			Taxaset taxaset = taxasetRepository.findOneByTsname(name);// 分类单元集
			Datasource datasource = datasourceRepository.findOneByTitle(name);

			Taxtree taxtree = taxtreeRepository.findOneByTreenameAndInfo(name, name);
			logger.info(i + ".-------------------" + name);
			logger.info("private String loginUser = \"3a29945023d04ef8a134f0f017d316f0\";");// 新采集系统的用户user.id
			logger.info("private String taxasetId = \"" + taxaset.getId() + "\";");// 新采集系统的taxaset.id（分类单元集）
			logger.info("private String sourcesid = \"" + datasource.getId() + "\";");// 新采集系统的datasources.id（数据源）
			logger.info("private String taxtreeId = \"" + taxtree.getId() + "\";");// 新采集系统的taxtree.id（分类树）
			logger.info("String IMAGEPATH = \"E:\\\\003采集系统\\\\0001旧采集系统\\\\csdbimges20181022145131\\\\\"");
			logger.info("static String inputtimeStr = \"2018-11-05 01:00:00\";");
			Element params = document.createElement("Params");
			// 为params添加子节点
			Element treeID = document.createElement("TreeID");
			treeID.setTextContent(f.getId());
			params.appendChild(treeID);

			Element loginUser = document.createElement("loginUser");
			loginUser.setTextContent("3a29945023d04ef8a134f0f017d316f0");
			params.appendChild(loginUser);

			Element taxasetId = document.createElement("taxasetId");
			taxasetId.setTextContent(taxaset.getId());
			params.appendChild(taxasetId);

			Element sourcesid = document.createElement("sourcesid");
			sourcesid.setTextContent(datasource.getId());
			params.appendChild(sourcesid);

			Element taxtreeId = document.createElement("taxtreeId");
			taxtreeId.setTextContent(taxtree.getId());
			params.appendChild(taxtreeId);

			Element IMAGEPATH = document.createElement("IMAGEPATH");
			IMAGEPATH.setTextContent("E:\\003采集系统\\0001旧采集系统\\csdbimges20181022145131\\");
			params.appendChild(IMAGEPATH);

			date.setTime(date.getTime() + i * 1000);
			Element inputtimeStr = document.createElement("inputtimeStr");
			inputtimeStr.setTextContent(df.format(date));
			params.appendChild(inputtimeStr);
			// 为params节点添加属性
			params.setAttribute("seq", String.valueOf(i));
			params.setAttribute("name", name);
			// 将params节点添加到bookstore根节点中
			root.appendChild(params);

		}
		document.appendChild(root);
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.transform(new DOMSource(document), new StreamResult(new File("E:\\book1.xml")));
		logger.info("生成book1.xml成功");
		return IMAGEPATH;

	}

	@Override
	public String createAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String currentTeamId = "04fd60c66610416083f7d988c1f9bbfb";
		String currentUserId = "3a29945023d04ef8a134f0f017d316f0";
		String sql = "select * from tree where TreeName  not in ('动物志','鸟纲-郑-雷-sp2000','中国鱼类拉汉名称')";
		// 团队
		Team team = teamRepository.findOneById(currentTeamId);
		String id = team.getId();
		if (CommUtils.isStrEmpty(id)) {
			throw new ValidationException("Team 数据不规范，id值为空！无法继续...");
		} else {
			logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "所选团队是：" + team.getName());
		}
		// 用户
		User currentUser = userRepository.findOneById(currentUserId);
		// 1.在旧采集系统中查询要录入的数据
		Connection connDB = null;
		ResultSet rs = null;
		List<ForcesTreeVO> treelist = new ArrayList<>(100);
		connDB = OtherConnDB.getConnDB(configuration);
		PreparedStatement prepareStatement = connDB.prepareStatement(sql);
		rs = prepareStatement.executeQuery();
		while (rs.next()) {
			ForcesTreeVO f = new ForcesTreeVO();
			f.setId(rs.getString("ID"));
			f.setTreeName(rs.getString("TreeName"));
			treelist.add(f);
		}
		rs.close();
//		prepareStatement.close();
		logger.info("旧采集系统：查询数据集完毕，数量：" + treelist.size());
		// 2.检验数据库中是否已经存在该数据源和分类单元集
		for (ForcesTreeVO forcesTreeVO : treelist) {
			String treeName = forcesTreeVO.getTreeName().trim();
			logger.info(treeName + " " + currentTeamId);
			// 2.1检验数据集是否存在，不存在则创建
			Dataset dataset = datasetRepository.findOneByDsnameAndTeam(treeName, currentTeamId);
			if (dataset == null || CommUtils.isStrNotEmpty(dataset.getId())) {
				logger.info("创建一个数据集");
				// 创建一个数据集
				Dataset ds = new Dataset();
				ds.setId(UUIDUtils.getUUID32());
				ds.setDsname(treeName);
				ds.setDsabstract(treeName);
				ds.setStatus(1);
				ds.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				ds.setTeam(team);
				ds.setCreatedDate(CommUtils.getTimestamp(inputtimeStr));
				ds.setCreator(currentUser);
				Dataset dsSave = datasetRepository.save(ds);
				logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "创建一个分类单元集");
				// 创建一个分类单元集
				Taxaset taxaset = new Taxaset();
				taxaset.setId(UUIDUtils.getUUID32());
				taxaset.setTsname(treeName);
				taxaset.setTsinfo(treeName);
				taxaset.setStatus(1);
				taxaset.setSynchstatus(0);
				taxaset.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				taxaset.setDataset(dsSave);
				taxaset.setCreatedDate(CommUtils.getTimestamp(inputtimeStr));
				taxaset.setDataset(dsSave);
				taxasetRepository.save(taxaset);
				// 分类树
				logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "创建一个分类树");
				Taxtree taxtree = new Taxtree();
				taxtree.setId(UUIDUtils.getUUID32());
				taxtree.setTreename(treeName);
				taxtree.setTreeinfo(treeName);
				taxtree.setStatus(1);
				taxtree.setSynchstatus(0);
				taxtree.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				taxtree.setDataset(dsSave);
				taxtree.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				taxtreeRepository.save(taxtree);
			} else {
				logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "数据集(" + treeName + ") 已存在,跳过");
			}
			Datasource datasource = datasourceRepository.findOneByTitle(treeName);
			if (datasource == null || CommUtils.isStrEmpty(datasource.getId())) {
				Datasource entity = new Datasource();
				entity.setId(UUIDUtils.getUUID32());
				entity.setTitle(treeName);
				entity.setInfo(treeName);
				entity.setVersions("1.0");
				entity.setStatus(1);
				entity.setInputer(currentUserId);
				entity.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				entity.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				entity.setStatus(0);
				datasourceRepository.save(entity);
			} else {
				logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + " " + "数据源(" + treeName + ") 已存在,跳过");
			}
		}

		return "login";

	}

	@Override
	public String insertDSAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		initData(request);
		logger.info("1、基础信息" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		this.insertTaxon(request);// taxon
		try {
			logger.info("2、引证" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertCitation(request);// 引证
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐导入引证出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("3、描述" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertDesc(request);// 描述
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐⭐导入描述出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("4、分布" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertDistribution(request);// 分布
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入分布出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("5、检索表" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertKeyitem(request);// 检索表
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入检索表出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("6、俗名" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertCommName(request);// 俗名
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入俗名出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("7、多媒体" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertMultimedia(request, response);// 多媒体
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入多媒体出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("8、标本" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertSpecimen(request);
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入标本出错，错误信息：" + e.getMessage());
		}
		try {
			logger.info("9、分类树" + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			this.insertTree(request);
		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
			logger.error("⭐error⭐⭐⭐⭐⭐⭐⭐⭐导入分类树出错，错误信息：" + e.getMessage());
		}
		logger.info("~~~~~~~~~~~~~~~~~~~~~FINISH（完成一个数据集）~~~~~~~~~~~~~~~~~" + " "
				+ CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

		return "finish";
	}

	@SuppressWarnings("static-access")
	@Override
	public String insertDSAllByXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<XmlParamsVO> list = ReadxmlByDom.Readxml("book1.xml");
		logger.info("从xml中一共读取了: " + list.size() + " " + CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		for (XmlParamsVO paramsVO : list) {
			// 54-60（包含）（完成）
			// 62-65（包含）（61是动物志，以前导过了）（完成）
			// 66-70（完成）
			// 71-75（除75引证外都完成了）75引证错误信息：系统断定检查已失败。有关详细信息，请查看 SQL Server
			// 错误日志。通常，断定失败是由软件错误或数据损坏导致的。若要检查数据库是否已损坏，请考虑运行 DBCC CHECKDB。如果您同意在安装过程中将转储发送到
			// Microsoft，则将向 Microsoft 发送微型转储。更新可能在 Microsoft 的最新 Service Pack
			// 或技术支持部门的修补程序中提供。
			// 76-80（完成）
			// 81-85（完成）
			// 86-90（完成）
			// 91-98（完成）
			if (Integer.parseInt(paramsVO.getSeq()) < 91 || Integer.parseInt(paramsVO.getSeq()) > 98) {
				continue;
			}
			logger.info("录入一条数据：" + paramsVO.toString());

			logger.info("seq：" + paramsVO.getSeq());
			this.TreeID = paramsVO.getTreeID();
			this.loginUser = paramsVO.getLoginUser();
			this.taxasetId = paramsVO.getTaxasetId();
			this.sourcesid = paramsVO.getSourcesid();
			this.taxtreeId = paramsVO.getTaxtreeId();
			this.IMAGEPATH = paramsVO.getImagePath();
			this.inputtimeStr = paramsVO.getInputtimeStr();
			// 存入数据库
			this.insertDSAll(request, response);

		}
		logger.info("（完成所有数据集）insertDSAllByXml execute finish");
		return "finish";

	}

	public String insertCitation(HttpServletRequest request) throws Exception {
		initData(request);
		Connection connDB = null;
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		List<Citation> records = new ArrayList<>(6000);
		try {
			logger.info("开始录入Citation(ID随机生成)");
			// 在旧系统查询引证数据
			connDB = OtherConnDB.getConnDB(configuration);
			String sql = "select st.name as StatusName,sy.id as sy_id,sy.Inputer as sy_Inputer,sy.Latin_Name as sy_Latin_Name,sy.Chinese_Name as sy_Chinese_Name,sy.fullname as sy_fullname,sc.* from taxa sc left join taxa sy on sy.SynonymOf = sc.id  left join Status st on st.id = sy.StatusID where sc.StatusID = (select id from Status where name = 'accepted name') and sc.treeid = ?";
			prepareStatement = connDB.prepareStatement(sql);
			prepareStatement.setString(1, TreeID);
			rs = prepareStatement.executeQuery();
			long startTime = System.currentTimeMillis(); // 获取开始时间
			int i = 0;
			while (rs.next()) {
				i++;
				logger.info("进度报告Begin：" + i);
				Citation c = new Citation();
				// ID
				c.setId(UUIDUtils.getUUID32());
				// sciname
				String scientificname1 = rs.getString("sy_fullname");
				String scientificname2 = rs.getString("sy_Latin_Name");
				String Chinese_Name = rs.getString("sy_Chinese_Name");
				if (isStrNotEmpty(scientificname1)) {
					c.setSciname(scientificname1);
				} else if (isStrNotEmpty(scientificname2)) {
					c.setSciname(scientificname2);
				} else if (isStrNotEmpty(Chinese_Name)) {
					c.setSciname(Chinese_Name);
				} else {
					continue;
				}
				// nametype
				String statusName = rs.getString("StatusName").trim();
				int nameType = 0;
				if ("misapplied name".equals(statusName)) {
					nameType = NametypeEnum.misappliedName.getIndex();
				} else if ("uncertain name".equals(statusName)) {
					nameType = NametypeEnum.uncertainName.getIndex();
				} else if ("synonym".equals(statusName)) {
					nameType = NametypeEnum.synonym.getIndex();
				} else if ("provisionally accepted name".equals(statusName)) {
					nameType = NametypeEnum.provisionallyAcceptedName.getIndex();
				} else if ("ambiguous synonym".equals(statusName)) {
					nameType = NametypeEnum.ambiguousSynonym.getIndex();
				} else if ("accepted name".equals(statusName)) {
					nameType = NametypeEnum.acceptedName.getIndex();
				}
				c.setNametype(nameType);
				c.setSourcesid(sourcesid);

				c.setStatus(1);
				c.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				c.setSynchstatus(0);
				c.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				// inputer
				String Inputer = rs.getString("sy_Inputer");
				c.setInputer(aQueryForcesDB.getUserId(loginUser, Inputer));
				// taxon_id
				Taxon taxon = taxonRepository.findOneById(rs.getString("ID").trim());
				if (taxon == null || CommUtils.isStrEmpty(taxon.getId())) {
					continue;
				} else if ((nameType != NametypeEnum.acceptedName.getIndex())
						&& taxon.getScientificname().trim().equals(c.getSciname())) {
					continue;
				}
				c.setTaxon(taxon);
				// authorship（命名信息）
				c.setAuthorship(getAuthorship(rs.getString("sy_id")));
				// refjson(参考文献)
				String citationRefjson = getCitationRefjson(rs.getString("sy_id"), c.getNametype());
				if (citationRefjson != null) {
					c.setRefjson(citationRefjson);
				}
				// Shortrefs 引证原文
				c.setCitationstr(getCitationstr(rs.getString("sy_id"), taxon, nameType));
				records.add(c);
				// 测试
//				batchSubmitService.saveAll(records);

				int size = records.size();
				if (size % 100 == 0) {
					logger.info(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss") + "进度报告：" + size);
				}
				if (records.size() == 3000) {
					logger.info("程序运行时间开始保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min");
					logger.info(CommUtils.getCurrentDate() + "向数据库中保存:" + records.size());
//					batchInsertService.batchInsertCitation(records, inputtimeStr);
//					citationRepository.saveAll(records);
					batchSubmitService.saveAll(records);
					logger.info(CommUtils.getCurrentDate() + "保存:" + records.size());
					records.clear();
					logger.info(CommUtils.getCurrentDate() + "清空数组:" + records.size());
					logger.info("程序运行时间结束保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min");
				}

			}

//			batchInsertService.batchInsertCitation(records, inputtimeStr);
			batchSubmitService.saveAll(records);
			logger.info("执行时间： " + (System.currentTimeMillis() - startTime) / 60000 + "min");

			logger.info("保存完成");
		} finally {
			if (rs != null) {
				rs.close();
			}

			logger.info("------------------------Citation信息 执行结束-------------------------");
		}

		return "引证信息录入完成";
	}

	@Cacheable(value = "getCitationstr")
	private String getCitationstr(String taxaId, Taxon taxon, int nametype) throws Exception {
		String sciName = taxon.getScientificname();
		String citationstr = "";
		String typeLocation = "";
		String authorship = "";
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
		if (CommUtils.isStrEmpty(citationstr) && nametype == NametypeEnum.acceptedName.getIndex()) {
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
						source = title + "." + source;
					}
					if (!source.contains(years)) {
						source = years + "." + source;
					}
					if (!source.contains(author)) {
						source = author + "." + source;
					}
					if (!source.contains(sciName)) {
						source = sciName + "," + source;
					}
					if (CommUtils.isStrNotEmpty(typeLocation)) {
						source = source + "(" + typeLocation + ")";
					}
					citationstr = source;
				}
			}
			if (rs2 != null) {
				rs2.close();
			}
		}
		return citationstr;
	}

	@Cacheable(value = "getCitationRefjson")
	private String getCitationRefjson(String taxonId, int nameType) throws Exception {
		JSONArray jsonArray = new JSONArray();
		Connection connDB = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		PreparedStatement prepareStatement = null;
		PreparedStatement pst2 = null;
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String Namesql = "select PaperID from Species_Paper where TaxaID = ?";
			prepareStatement = connDB.prepareStatement(Namesql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			prepareStatement.setString(1, taxonId);
			rs = prepareStatement.executeQuery();
			while (rs.next()) {

				String PaperID = rs.getString("PaperID");
				// 从新采集系统查询文献id
				try {
					if (refmap.get(PaperID) == null) {
						Ref refid = refRepository.findOneById(PaperID);
						String id = refid.getId();
						refmap.put(PaperID, id);
					}
				} catch (Exception e) {
					logger.info(
							"错误信息：" + e.getMessage() + "未查询到此参考文献：PaperID = " + PaperID + "    taxonId = " + taxonId);
					continue;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("refE", " 0");
				jsonObject.put("refS", " 0");
				jsonObject.put("refId", "" + PaperID);
				jsonObject.put("refType", "0");
				jsonArray.add(jsonObject);

			}
			rs.close();

			if (nameType == NametypeEnum.synonym.getIndex()) {// 异名引证
				// 在旧系统Synonym_Paper中查询数据
				connDB = OtherConnDB.getConnDB(configuration);
				String synonymsql = "select PaperID from Synonym_Paper where SynID = ?";
				pst2 = connDB.prepareStatement(synonymsql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pst2.setString(1, taxonId);
				rs2 = pst2.executeQuery();
				while (rs2.next()) {
					String PaperID = rs2.getString("PaperID");
					// 从新采集系统查询文献id
					try {
						if (refmap.get(PaperID) == null) {
							Ref refid = refRepository.findOneById(PaperID);
							String id = refid.getId();
							refmap.put(PaperID, id);
						}
					} catch (Exception e) {
						logger.error("错误信息：" + e.getMessage() + "未查询到此参考文献：PaperID = " + PaperID + "    taxonId = "
								+ taxonId);
						continue;
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("refE", " 0");
					jsonObject.put("refS", " 0");
					jsonObject.put("refId", "" + PaperID);
					jsonObject.put("refType", "1");
					jsonArray.add(jsonObject);

				}
				rs.close();

			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		if (jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();

	}

	private String getTaxonRefjson(String taxonId) throws Exception {
		Connection connDB = null;
		JSONArray jsonArray = new JSONArray();
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String Namesql = "select PaperID from Species_Paper where TaxaID = ?";
			prepareStatement = connDB.prepareStatement(Namesql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			prepareStatement.setString(1, taxonId);
			rs = prepareStatement.executeQuery();
			while (rs.next()) {
				String PaperID = rs.getString("PaperID");
				// 从新采集系统查询文献id
				if (refmap.get(PaperID) == null) {
					Ref refid = null;
					try {
						refid = refRepository.findOneById(PaperID);
						String id = refid.getId();
						refmap.put(PaperID, id);
					} catch (Exception e) {
						logger.error("错误信息 " + e.getMessage() + "未查询到此参考文献，PaperID = " + PaperID + "    taxonId = "
								+ taxonId);
						continue;
					}
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("refE", " 0");
				jsonObject.put("refS", " 0");
				jsonObject.put("refId", PaperID);
				jsonArray.add(jsonObject);

			}
		} finally {
			if (rs != null) {
				rs.close();
			}
//			if (prepareStatement != null) {
//				prepareStatement.close();
//			}
		}
		if (jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();

	}

	/**
	 * Authorship (命名信息)
	 * 
	 * @return
	 * @throws Exception
	 */

	private String getAuthorship(String taxonId) throws Exception {
		Connection connDB = null;
		ResultSet rs = null;
		String authorstr = "";
		PreparedStatement prepareStatement = null;
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String Namesql = "select Named_Person as person ,Named_Date as date from species where TaxaID = ?";
			prepareStatement = connDB.prepareStatement(Namesql);
			prepareStatement.setString(1, taxonId);
			rs = prepareStatement.executeQuery();
			while (rs.next()) {
				String Named_Person = rs.getString("person");
				String Named_Date = rs.getString("date");
				// 命名人和年代
				if (!isStrNotEmpty(Named_Date)) {// Named_Date为空
					authorstr = Named_Person;
				} else if (isStrNotEmpty(Named_Date) && Named_Person.contains(Named_Person)) {// Named_Date非空且Named_Person包含Named_Date
					authorstr = Named_Person;
				} else {
					authorstr = Named_Person + "," + Named_Date;
				}
				break;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}

		}
		if (isStrNotEmpty(authorstr)) {
			authorstr = authorstr.replace("(", "");
			authorstr = authorstr.replace(")", "");
			authorstr = authorstr.replace("（", "");
			authorstr = authorstr.replace("）", "");
			authorstr = authorstr.trim();
		}
		return authorstr;

	}

	/**
	 * Keyitem 检索表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	public String insertKeyitem(HttpServletRequest request) throws Exception {
		logger.info("------------------------开始录入 检索表（id和旧系统保持一致）-------------------------");
		initData(request);
		Connection connDB = null;
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		List<String> taxkeyIds = new ArrayList<>();
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			// 查询检索表名称
			String Namesql = "select * from taxonKeyInfo where id in (select taxonKeyID from taxonKey where treeID = ? group by taxonKeyID )";
			prepareStatement = connDB.prepareStatement(Namesql);
			prepareStatement.setString(1, TreeID);
			rs = prepareStatement.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				taxkeyIds.add(id);
				String keyname = rs.getString("keyname");
				String startTaxonID = rs.getString("startTaxonID");
				String remark = rs.getString("remark");
				String source = rs.getString("source");
				Taxon taxon = taxonRepository.findOneById(startTaxonID);
				if (taxon == null) {
					continue;
				}
				JSONObject js = new JSONObject();
				js.put("remark", remark);
				js.put("source", source);
				Taxkey taxkey = new Taxkey();
				taxkey.setId(id);
				taxkey.setKeytitle(keyname);
				taxkey.setAbstraction(String.valueOf(js));
				taxkey.setTaxon(taxon);
				taxkey.setKeytype(String.valueOf(KeytypeEnum.DoubleTerm.getIndex()));
				// 保存taxkey
				taxkeyRepository.save(taxkey);
				// 保存keyItem
				saveKeyItem(taxkey);
				// 保存特征图片
				saveResource(taxkey, request);

			}

		} catch (Exception e) {
			logger.error("------------------------录入 检索表发生错误，删除此次录入的数据-------------------------" + "错误信息："
					+ e.getMessage() + taxkeyIds.size());
//			logger.error(e.getMessage(), e);
			if (taxkeyIds.size() > 0) {
				// delete keyitem
				keyitemRepository.deleteBytaxkeyId(taxkeyIds);
				// delete taxkey
				taxkeyRepository.deleteByIds(taxkeyIds);
				// delete resource
				keyitemService.deleteBytaxkeyId(taxkeyIds);

			}

		} finally {

			if (rs != null) {
				rs.close();
			}
			logger.info("------------------------ 检索表 执行完成-------------------------" + taxkeyIds.size());
		}

		return null;
	}

	/**
	 * save Resource 保存检索表特征图片
	 * 
	 * @param taxkey
	 * @param request
	 * @throws Exception
	 * @throws IOException
	 */
	private void saveResource(Taxkey taxkey, HttpServletRequest request) throws Exception, IOException {
		Connection connDB = null;
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String itemsql = "select f.* from taxonKey t   inner join featureImage f on t.id = f.keyItemID where t.taxonKeyID = ?";
			prepareStatement = connDB.prepareStatement(itemsql);
			prepareStatement.setString(1, taxkey.getId());
			rs = prepareStatement.executeQuery();
			while (rs.next()) {
				Resource r = new Resource();
				String id = rs.getString("id");
				String keyItemID = rs.getString("keyItemID");
				String url = IMAGEPATH + rs.getString("url");
				rs.getString("caption");
				r.setId(id);
				r.setKeyitemId(keyItemID);

				keyitemService.batchResourceImages(r, url, uploadPath + "images/", request);

			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

	}

	private void saveKeyItem(Taxkey taxkey) throws Exception {
		Connection connDB = null;
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String itemsql = "select * from taxonKey where taxonKeyID = ? order by parentid ASC";
			prepareStatement = connDB.prepareStatement(itemsql);
			prepareStatement.setString(1, taxkey.getId());
			rs = prepareStatement.executeQuery();
			List<Keyitem> Keyitems = new ArrayList<>();
			while (rs.next()) {
				Keyitem keyitem = new Keyitem();
				String id = rs.getString("id");
				String parentid = rs.getString("parentid");
				rs.getString("startTaxonID");
				String orderNumber = rs.getString("orderNumber");
				int branchNumber = rs.getInt("branchNumber");
				String targetTaxonID = rs.getString("targetTaxonID");
				String featureItem = rs.getString("featureItem");
				keyitem.setId(id);
				keyitem.setItem(featureItem);
				keyitem.setOrderid(Integer.parseInt(orderNumber));
				if (branchNumber != 0) {
					keyitem.setBranchid(branchNumber);
				} else {
					keyitem.setTaxonid(targetTaxonID);
				}
				keyitem.setPid(parentid);
				keyitem.setTaxkey(taxkey);
				keyitem.setKeytype(String.valueOf(KeytypeEnum.DoubleTerm.getIndex()));// 双项式
				Keyitems.add(keyitem);
			}
			// 使用parentId转换innerorder
			int i = 1;
			for (Keyitem keyitem : Keyitems) {
				if (keyitem.getPid().equals("00000000-0000-0000-0000-000000000000")) {
					keyitem.setInnerorder(1);
					String pid = keyitem.getId();

					while (findChildren(pid, Keyitems).size() > 0) {
						i = i + 1;
						List<Keyitem> children = findChildren(pid, Keyitems);
						for (Keyitem child : children) {
							// 更新children 的 innerorder
							for (Keyitem keyitem2 : Keyitems) {
								if (child.getId().equals(keyitem2.getId())) {
									keyitem2.setInnerorder(i);
								}
							}
							if (findChildren(child.getPid(), Keyitems).size() > 0) {
								pid = child.getId();
							}
						}

					}
				}
			}
			for (Keyitem keyitem : Keyitems) {
				if (keyitem.getInnerorder() == 0) {
					keyitem.setInnerorder(i + 1);
				}
			}
			// 保存全部
			keyitemRepository.saveAll(Keyitems);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	public List<Keyitem> findChildren(String pid, List<Keyitem> records) {
		List<Keyitem> children = new ArrayList<>();
		for (Keyitem keyitem : records) {
			if (keyitem.getPid().equals(pid)) {
				children.add(keyitem);
			}
		}
		return children;
	}

	public String insertTaxon(HttpServletRequest request) throws Exception, ParseException {
		logger.info("insertTaxon：开始录入taxon（id和旧系统保持一致）");
		List<Taxon> entities = null;
		try {
			initData(request);
			taxonRepository.deleteByInputtimeAndSourcesidAndTsId(CommUtils.getUtilDate(inputtimeStr), sourcesid,
					taxasetId);
			Connection connDB = null;
			ResultSet rs2 = null;
			ResultSet rs = null;
			try {
				connDB = OtherConnDB.getConnDB(configuration);

				String sql = "select t.id as id,t.Inputer as Inputer,t.id as TaxaID,t.ParentID as ParentID, t.fullname as scientificname1,t.Latin_Name as scientificname2,r.RankEN as RankEN,t.remark as remark,t.Chinese_Name as  chname,p.fullname as parent1,p.Latin_Name as parent2"
						+ " from taxa t left join Rank_List r on  r.id = t.RankID  left join taxa p on t.ParentID = p.id left join Status st on st.id = t.StatusID"
						+ " where t.TreeID = ?  and st.Name like '%accepted name%'  ";
//						+ "and t.id in ('42EA147F-08B4-49CB-88A7-00057A1C4DBE','1B155170-CD00-48BD-9960-669EFD71D68E','7DCCEDBD-AD4B-40BB-9BE7-81BE76F099F7','0D1E010E-04A0-45B2-AC10-21824DDEB757')";

				// s.Named_Person,s.Named_Date,
				// left join species s on s.taxaid = t.id
				PreparedStatement prepareStatement = connDB.prepareStatement(sql);
				prepareStatement.setString(1, TreeID);
				rs = prepareStatement.executeQuery();
				entities = new ArrayList<>();
				List<Rank> ranklist = rankRepository.findAll();
				Map<String, String> rankMap = new HashMap<>();
				for (Rank rank : ranklist) {
					String enname = rank.getEnname().toUpperCase();
					String id = rank.getId();
					rankMap.put(enname, id);
				}
				logger.info("insertTaxon：查询完毕，开始整合数据");
				int i = 0;
				while (rs.next()) {
					i++;
					if (i % 1000 == 0) {
						System.out.println("打印（无意义）：taxon 已经执行了：" + i);
					}
					Taxon t = new Taxon();
					t.setId(rs.getString("id"));

					rs.getString("ParentID");
					String TaxaID = rs.getString("TaxaID");
					String scientificname1 = rs.getString("scientificname1");
					String scientificname2 = rs.getString("scientificname2");
					String RankEN = rs.getString("RankEN");
					rs.getString("remark");
					String chname = rs.getString("chname");

					String parent1 = rs.getString("parent1");
					String parent2 = rs.getString("parent2");
					// scientificname
					if (isStrNotEmpty(scientificname1)) {
						t.setScientificname(scientificname1);
					} else if (isStrNotEmpty(scientificname2)) {
						t.setScientificname(scientificname2);
					}
					// rank_id
					String rankid = rankMap.get(RankEN.toUpperCase());
					if (RankEN.toUpperCase().equals("SUBSP.")) {
						rankid = rankMap.get("subspecies".toUpperCase());
					}
					Rank rank = new Rank();
					rank.setId(rankid);
					t.setRank(rank);

					// epithet种加词/亚种加词
					String[] array = { "subsp.", "species" };
					boolean flag = Arrays.asList(array).contains(RankEN.toLowerCase());
					if (flag) {
						String scientificname = t.getScientificname();
						String[] split = scientificname.split(" ");
						String epithet = split[split.length - 1];
						t.setEpithet(epithet);
					}
					// inputtime
					t.setInputtime(CommUtils.getTimestamp(inputtimeStr));
					// remark
					String remarkStr = "";
					if (isStrNotEmpty(parent1)) {
						remarkStr = "{\"parent\" : \"" + parent1 + "\"}";
					} else if (isStrNotEmpty(parent2)) {
						remarkStr = "{\"parent\" : \"" + parent2 + "\"}";
					}
					String remark = rs.getString("remark");
					if (isStrNotEmpty(remark)) {
						remarkStr = remarkStr + ",{\"remark\" : \"" + remark + "\"}";
					}
					t.setRemark(remarkStr.trim());
					// sourcesid
					t.setSourcesid(sourcesid);

					t.setStatus(1);
					t.setChname(chname);
					t.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
					t.setSynchstatus(0);
					Taxaset taxaset = new Taxaset();
					taxaset.setId(taxasetId);
					t.setTaxaset(taxaset);
					t.setTaxonCondition(1);
					// 录入人
					String Inputer = rs.getString("Inputer");
					t.setInputer(aQueryForcesDB.getUserId(loginUser, Inputer));
					// 参考文献
					// 访问数据库查询
					String taxonRefjson = getTaxonRefjson(rs.getString("id"));
					if (taxonRefjson != null) {
						t.setRefjson(taxonRefjson);
					}
					// 命名信息
					// 访问数据库查询
					t.setAuthorstr(getAuthorship(rs.getString("id")));
					entities.add(t);
					// 逐条保存
					try {
						taxonRepository.save(t);
					} catch (Exception e) {
//						logger.error(e.getMessage(), e);
						logger.error("保存失败01，尝试删除中文名重新保存：id = " + TaxaID + "    Chname =" + t.getChname() + "\t错误信息："
								+ e.getMessage());
						t.setChname(null);
						try {
							taxonRepository.save(t);
						} catch (Exception e1) {
							logger.error("保存失败03，" + e.getMessage() + "错误信息如下：id = " + TaxaID + "    Chname ="
									+ t.getChname());
						}
					}
				}
				rs.close();

//				logger.info("taxon开始存储..." + entities.size());
//				taxonRepository.saveAll(entities);

				logger.info("taxon存储完成，数量" + entities.size());
			} finally {
				if (rs2 != null) {
					rs2.close();
				}
				if (rs != null) {
					rs.close();
				}

			}
		} catch (Exception e) {
			logger.error("录入Taxon数据失败，错误信息如下，不删除数据" + e.getMessage());
		} finally {
			logger.info("录入Taxon数据,执行完毕");
		}
		return "birdTaxonInsert finish ,count " + entities.size();
	}

	// 多媒体
	public void insertMultimedia(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("insertMultimedia: （ID随机生成）开始录入多媒体，");
		initData(request);
		ResultSet rs = null;
		Connection connDB = null;
		int i = 0;
		try {
			// 在旧采集系统查询数据
			connDB = OtherConnDB.getConnDB(configuration);
			String sql = "select ty.Type as MediaType,t.id as taxaid,t.Latin_Name,t.fullname,m.* from MultiMedia m left join taxa t on t.id = m.Belong_ID left join MultiMedia_Type ty on  ty.id = m.DescType where t.TreeID = ?";
			PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			prepareStatement.setString(1, TreeID);
			rs = prepareStatement.executeQuery();
			logger.info("查询完成，开始处理并导入数据");
			while (rs.next()) {
				++i;
				Multimedia m = new Multimedia();
				String MediaType = rs.getString("MediaType");
				m.setMediatype(MediaType);
				String oldTaxaID = rs.getString("taxaid");
//				System.out.println(oldTaxaID);
				Taxon taxon = null;
				try {
					taxon = taxonRepository.findByTaxonOldId(oldTaxaID, sourcesid);
				} catch (Exception e1) {
					logger.info("查询TAXON出错,在旧采集系统的ID是：" + oldTaxaID + "错误信息：" + e1.getMessage());
				}
				if (taxon == null || taxon.getId() == null) {
					logger.info("找不到TAXON,在旧采集系统的ID是：" + oldTaxaID + "跳过此条数据继续执行");
					continue;
				}
				m.setTaxon(taxon);
				String Latin_Name = rs.getString("Latin_Name");
				String fullname = rs.getString("fullname");
				// MultiMedia表
				String FileContent = rs.getString("FileContent");
				m.setInfo(FileContent);
				rs.getString("FileSize");
				String URL = rs.getString("URL");
				rs.getString("FileName");
				String rightsholder = rs.getString("rightsholder");
				String PicDescription = rs.getString("PicDescription");
				String FileType = rs.getString("FileType");
				String FileSource = rs.getString("FileSource");
				String FileAuthor = rs.getString("FileAuthor");

				String FileUrl = rs.getString("FileUrl");

				m.setTitle(isStrNotEmpty(fullname) ? fullname : Latin_Name);// 标题
				if (true) {
					m.setMediatype("1");// 媒体类型
				}
				m.setSourcesid(sourcesid);// 数据源
				m.setRightsholder(FileAuthor);// 版权人
				m.setLisenceid("3");// 共享协议
				m.setOldPath(FileUrl);// 原始路径
				m.setContext(FileSource + "	" + FileType + "	" + PicDescription + "	" + rightsholder);
				m.setContext(m.getContext().trim());// 图注
				m.setCountry(null);// 国家
				double Lon = rs.getDouble("Lon");
				if (Lon != 0) {
					m.setLng(Lon);// 经度
				}
				double Lat = rs.getDouble("Lat");
				if (Lat != 0) {
					m.setLat(Lat);// 纬度
				}
				File localFile = new File(IMAGEPATH + URL);

				try {
					this.batchImages(taxon.getId(), request, m, localFile, uploadPath + "images/");
					request.getSession().setAttribute("teamId", teamId);
					request.getSession().setAttribute("datasetId", datasetId);
				} catch (Exception e) {
					logger.error("保存图片出错，跳过" + e.getMessage());
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		logger.info("多媒体 保存完成，数量：" + i);

	}

	// 分类树
	public String insertTree(HttpServletRequest request) throws Exception {
		logger.info("------------------------开始录入分类树-------------------------");
		initData(request);
		ResultSet rs = null;
		Connection connDB = null;
		List<TaxonHasTaxtree> entities = null;
		// 从旧采集系统查询数据
		try {
			connDB = OtherConnDB.getConnDB(configuration);
			String sql = "select id,Latin_Name,Chinese_Name,fullname,ParentID from taxa where TreeID = ?";
			PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			prepareStatement.setString(1, TreeID);
			rs = prepareStatement.executeQuery();
			TaxonHasTaxtree t = null;
			entities = new ArrayList<>(6100);
			logger.info("查询分类树完毕");
			while (rs.next()) {
				String id = rs.getString("id");
				rs.getString("Latin_Name");
				rs.getString("fullname");
				rs.getString("Chinese_Name");
				String ParentID = rs.getString("ParentID");
				Taxon taxon = taxonRepository.findByTaxonOldId(id, sourcesid);
				if (taxon == null || !isStrNotEmpty(taxon.getId())) {
//					logger.info("找不到oldid为：" + id + " ,sourcesid = " + sourcesid + "的记录");
					continue;
				}

				Taxon parentTaxon = taxonRepository.findByTaxonOldId(ParentID, sourcesid);

				if (ParentID.equals("00000000-0000-0000-0000-000000000000")) {
					parentTaxon = new Taxon();
					parentTaxon.setId(taxtreeId);
				}
				if (parentTaxon == null || CommUtils.isStrEmpty(parentTaxon.getId())) {
//					logger.info("找不到ParentID为：" + ParentID + " ,sourcesid = " + sourcesid + "的记录");
					continue;
				}
				if (!isStrNotEmpty(taxtreeId)) {
					throw new ValidationException("taxtreeId值为空，请初始化此值...");
				}
				t = new TaxonHasTaxtree();
				t.setTaxonId(taxon.getId());
				t.setPid(parentTaxon.getId());
				t.setTaxtreeId(taxtreeId);
				entities.add(t);

			}
			logger.info("开始向新采集系统录入数据:" + entities.size());
			// 向新采集系统录入数据
			taxonHasTaxtreeRepository.saveAll(entities);
			logger.info("向新采集系统录入数据完毕:" + entities.size());
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		logger.info("------------------------录入分类树结束-------------------------");
		return "insert  tree  data  finish ... count: " + entities.size();

	}

	// 分布
	public String insertDistribution(HttpServletRequest request) throws Exception {
		try {
			logger.info("开始录入分布数据（ID随机生成）------------------------");
			initData(request);
			List<Taxon> taxonlist = taxonRepository.findBySourcesidAll(sourcesid);
			List<Distributiondata> entities = new ArrayList<>();
			long startTime = System.currentTimeMillis(); // 获取开始时间
			int i = 0;
			for (Taxon taxon : taxonlist) {
//				String zxyoldid = taxon.getId();
				String taxonid = taxon.getId();
				// 根据旧采集系统的taxonid查询所有分布地
				Map<String, String> map = getGeojsonaAndDiscontent(taxonid);
				String geojson = map.get("geoIds");
				if (!geojson.equals("{\"geoIds\":\"\"}")) {
				} else {
					continue;
				}
				Distributiondata d = new Distributiondata();
				d.setId(UUIDUtils.getUUID32());
				d.setTaxonid(taxonid);
				d.setTaxon(taxon);
				d.setLng(0.0);
				d.setLat(0.0);
				d.setSourcesid(sourcesid);
				d.setStatus(1);
				d.setInputer(loginUser);
				d.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				d.setSynchstatus(0);
				d.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				d.setGeojson(geojson);
				d.setDiscontent(map.get("Description_Content"));
				entities.add(d);
				if (entities.size() == 3000) {
					i = i + entities.size();
					logger.info("（部分）分布——开始保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min");
					logger.info(CommUtils.getCurrentDate() + "分布——向数据库中保存:" + entities.size());
//					batchInsertService.batchInsertDistributiondata(entities, inputtimeStr);
//					distributiondataRepository.saveAll(entities);
					batchSubmitService.saveAll(entities);
					entities.clear();
					logger.info(CommUtils.getCurrentDate() + "分布——清空数组:" + entities.size());
					logger.info("（部分）分布——结束保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min, 已经存入数据库记录数："
							+ i);
				}
			}
			logger.info("entities begin save:" + entities.size());
			distributiondataRepository.saveAll(entities);
			logger.info("entities finish save:" + entities.size());
		} finally {
			logger.info("分布信息 执行结束");
		}

		return "insertDistribution (分布) finish";

	}

	@SuppressWarnings("static-access")
	private void getXmlParams(HttpServletRequest request) {
		String seq = request.getParameter("seqBegin");
		int parseIntSeq = 0;
		if (CommUtils.isStrEmpty(seq)) {
			return;
		} else {
			try {
				parseIntSeq = Integer.parseInt(seq);
			} catch (NumberFormatException e) {
				logger.error("warn 参数的参数seq应该是大于0的整数，默认使用全局变量参数...");
				return;
			}
		}
		// 读取xml文件
		List<XmlParamsVO> list = ReadxmlByDom.Readxml("book1.xml");
		boolean find = false;
		for (XmlParamsVO paramsVO : list) {
			if (Integer.parseInt(paramsVO.getSeq()) == parseIntSeq) {
				find = true;
				logger.info("向分类单元集中录入数据，：" + paramsVO.toString());
				logger.info("seq：" + paramsVO.getSeq());
				this.TreeID = paramsVO.getTreeID();
				this.loginUser = paramsVO.getLoginUser();
				this.taxasetId = paramsVO.getTaxasetId();
				this.sourcesid = paramsVO.getSourcesid();
				this.taxtreeId = paramsVO.getTaxtreeId();
				this.IMAGEPATH = paramsVO.getImagePath();
				this.inputtimeStr = paramsVO.getInputtimeStr();
				break;
			} else {
				continue;
			}
		}
		if (!find) {
			logger.error("warn 在xml文件中没有找到参数 == " + seq + "的信息，默认使用全局变量参数...");
		}
	}

	// 俗名
	public String insertCommName(HttpServletRequest request) throws Exception {
		try {
			logger.info("开始录入俗名（ID随机生成）");
			initData(request);
			Connection connDB = null;
			try {
				String sql = "select t.*,CN.Inputer AS Inputer,CN.ID AS commid,cn.ComName as commonname,cn.Lang as language from (select id as TaxaID from taxa where TreeID = ? ) t left join CommonName cn on  cn.TaxaID = t.TaxaID where cn.ComName is not null order by cn.ComName";
				connDB = OtherConnDB.getConnDB(configuration);

				PreparedStatement prepareStatement = connDB.prepareStatement(sql);
				prepareStatement.setString(1, TreeID);
				ResultSet rs = prepareStatement.executeQuery();
				List<Commonname> entities = new ArrayList<>();
				while (rs.next()) {
					Commonname d = new Commonname();
					d.setRefjson(getCommonNameRefjson(rs.getString("commid")));
					d.setCommonname(rs.getString("commonname"));
					String language = rs.getString("language");
					if ("en".equals(language)) {
						language = "2";
					} else if ("zh-CN".equals(language) || "zh-TW".equals(language)) {
						language = "1";
					} else {
						language = "6";
					}
					d.setLanguage(language);
//				Taxon taxon = taxonRepository.findByTaxonOldId(rs.getString("TaxaID"));
					d.setTaxon(taxonRepository.findByTaxonOldId(rs.getString("TaxaID"), sourcesid));
					// 一些必要的
					d.setId(UUIDUtils.getUUID32());
					String Inputer = rs.getString("Inputer");
					d.setInputer(aQueryForcesDB.getUserId(loginUser, Inputer));
					d.setInputtime(CommUtils.getTimestamp(inputtimeStr));
					d.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
					d.setSynchstatus(0);
					d.setStatus(1);
					d.setSourcesid(sourcesid);
					entities.add(d);
				}
				logger.info("birdCommNameInsert save begin..." + entities.size());
				List<Commonname> list = commonnameRepository.saveAll(entities);
				for (Commonname c : list) {
					logger.info(c.getId());
					break;
				}
				logger.info("birdCommNameInsert save finish..." + entities.size());

			} finally {
				logger.info("birdCommNameInsert finish...");
			}
		} finally {
			logger.info("俗名信息 执行结束");
		}

		return "birdCommNameInsert insert finish";

	}

	public String insertRefs() throws Exception, ParseException {
		ThreadLocal<List<Ref>> connectionHolder = new ThreadLocal<List<Ref>>();
		Connection connDB = null;
		int count = 0;// 保存的总数量
		try {
			String sql = "select id,remark,Author as author ,Years as pyear ,Title as title,Journal as journal,"
					+ "Volume  as r_volume,Begins as refs ,Ends as refe ,Press as press ,"
					+ "Place as place ,Editor  as editor ,ISBN as isbn,Tpage as Tpage,Tchar  as tchar ,"
					+ "Version  as version ,Translator as translator ,Languages as languages ,"
					+ "Inputer as inputer,Keywords,old_languages as olang,Type as ptype,Source,Numbers from papers";
			connDB = OtherConnDB.getConnDB(configuration);
			PreparedStatement prepareStatement = connDB.prepareStatement(sql);
			ResultSet rs = prepareStatement.executeQuery();
			List<Ref> entities = new ArrayList<>(1050);
			logger.info("查询完成");
			while (rs.next()) {
				Ref r = new Ref();
				// 新采集系统 1 - 期刊；2 - 专著； 3 - 论文集. 4-其他
				String ptype = rs.getString("ptype");
				if (CommUtils.isStrEmpty(ptype)) {
					r.setPtype(String.valueOf(PtypeEnum.other.getIndex()));
				} else if (ptype.contains("论文")) {
					r.setPtype(String.valueOf(PtypeEnum.LWJ.getIndex()));
				} else if (ptype.contains("专著")) {
					r.setPtype(String.valueOf(PtypeEnum.ZZ.getIndex()));
				} else if (ptype.contains("期刊")) {
					r.setPtype(String.valueOf(PtypeEnum.QK.getIndex()));
				} else {
					r.setPtype(String.valueOf(PtypeEnum.other.getIndex()));
				}
				r.setMayBeRefStr(rs.getString("remark"));
				r.setId(rs.getString("id").trim());
				r.setRemark("旧采集系统");
				r.setAuthor(rs.getString("author"));// 作者
				r.setTranslator(rs.getString("editor"));// 编者/译者
				r.setPyear(rs.getString("pyear"));// 年代
				r.setTitle(rs.getString("title"));// 标题
				r.setJournal(rs.getString("journal"));// 期刊/专著的名字
				r.setrVolume(rs.getString("r_volume"));// 卷
				r.setrPeriod(rs.getString("Numbers"));// 期
				r.setRefs(rs.getString("refs"));// 起始页
				r.setRefe(rs.getString("refe"));// 终止页
				r.setPress(rs.getString("press"));// 出版社
				r.setPlace(rs.getString("place"));// 出版地
				r.setIsbn(rs.getString("isbn"));
				r.setTpage(rs.getString("Tpage"));
				r.setTchar(rs.getString("tchar"));
				r.setInputer(rs.getString("inputer"));
				r.setKeywords(rs.getString("Keywords"));
				// 一些必要的
				r.setInputer(loginUser);
				r.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				r.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				r.setSynchstatus(0);
				r.setStatus(1);
				// 完整题录
				String source = rs.getString("Source");
				// update refs set title = journal where title like '%不详%' and remark = '旧采集系统'
				// and journal is not null
				// select * from refs where remark = '旧采集系统' and refstr is null or refstr = ''
				// order by refstr,author
				// delete from refs where refstr is null or refstr = ''
				getRefstrByType(r, source);

				entities.add(r);
				if (entities.size() == 1000) {
					count = count + entities.size();
					List<Ref> copylist = new ArrayList<>();
					copylist.addAll(entities);
					CommUtils.executor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								connectionHolder.set(copylist);// 实际上此处使用不到，目的在于解决多线程共享变量问题
								System.out.println(Thread.currentThread().getName() + ":" + copylist.size());
								System.out.println("begin 线程池中线程数目：" + CommUtils.executor.getPoolSize()
										+ "，队列中等待执行的任务数目：" + CommUtils.executor.getQueue().size() + "，已执行完的任务数目："
										+ CommUtils.executor.getCompletedTaskCount());
								for (Ref ref : copylist) {
									refRepository.save(ref);
								}
								System.out.println("end 线程池中线程数目：" + CommUtils.executor.getPoolSize() + "，队列中等待执行的任务数目："
										+ CommUtils.executor.getQueue().size() + "，已执行完的任务数目："
										+ CommUtils.executor.getCompletedTaskCount());

							} catch (Exception e) {
								System.out.println("error IA001 ：" + e.getMessage());
							}
						}
					});
					entities.clear();// 清空
				}
			}
			if (entities != null && entities.size() > 0) {
				count = count + entities.size();
				logger.info("即将保存完成" + entities.size());
				refRepository.saveAll(entities);
			}
			CommUtils.executor.shutdown();// 关闭线程池,不能再提交新任务，等待执行的任务不受影响
			logger.info("数量" + count);

		} finally {
			logger.info("查询完毕，正在保存中...");
		}
		while (true) {
			if (CommUtils.executor.isTerminated()) {
				logger.info("保存旧系统参考文献到新数据库，执行完了");
				break;
			}
		}
		return "保存完成，数量" + count;

	}

	private void getRefstrByType(Ref r, String source) {
		String refstr = "";// 完整题录
		String author = r.getAuthor();// 作者
		String editor = r.getTranslator();// 编者或译者
		String pyear = r.getPyear();// 年代
		String title = r.getTitle();// 标题
		String journal = r.getJournal();// '期刊/专著/论文集名称
		String r_volume = r.getrVolume();// 卷
		String period = r.getrPeriod();// 期
		String ptype = r.getPtype();
		String refs = r.getRefs();
		String refe = r.getRefe();
		String place = r.getPlace();// 出版地
		String press = r.getPress();// 出版社
		String mayBeRefStr = r.getMayBeRefStr();

		if (CommUtils.isStrNotEmpty(source)) {// 旧采集系统source字段空
			if (CommUtils.isStrNotEmpty(title) && source.contains(title)) {
				refstr = source;
			} else if (CommUtils.isStrNotEmpty(title) && "不详".equals(title.trim())) {
				// 重新拼接title
				refstr = source;
				r.setRefstr(refstr);
				RefsController rc = new RefsController();
				String[] split = source.split("；");
				String parAuthor = "";
				String partYear = "";
				String partTitle = "";
				for (String str : split) {
					try {
						Map<String, String> map = rc.parseLineChineseOrEng(null, str);
						String paresAuthor = map.get("author");
						if (CommUtils.isStrNotEmpty(paresAuthor)) {
							parAuthor = parAuthor + ";" + paresAuthor;
						}
						String parseYear = map.get("year");
						if (CommUtils.isStrNotEmpty(parseYear)) {
							partYear = partYear + ";" + parseYear;
						}
						String parseTitle = map.get("title");
						if (CommUtils.isStrNotEmpty(parseTitle)) {
							partTitle = partTitle + ";" + parseTitle;
						}
					} catch (Exception e) {
					}
				}

				if (CommUtils.isStrNotEmpty(parAuthor)) {
					if (parAuthor.trim().startsWith(";")) {
						parAuthor = parAuthor.substring(1, parAuthor.length());
					}
					r.setAuthor(parAuthor);
				}
				if (CommUtils.isStrNotEmpty(partYear)) {
					if (partYear.trim().startsWith(";")) {
						partYear = partYear.substring(1, partYear.length());
					}
					r.setPyear(partYear);
				}
				if (CommUtils.isStrNotEmpty(partTitle)) {
					if (partTitle.trim().startsWith(";")) {
						partTitle = partTitle.substring(1, partTitle.length());
					}
					r.setTitle(partTitle);
				}
//				System.out.println(r.toString());
//				logger.info("---------" + r.getTitle());
			} else {
				refstr = source;
			}
		} else if (CommUtils.isStrNotEmpty(mayBeRefStr)) {// 旧采集系统remark 字段非空
			// 作者
			if (isStrNotEmpty(author) && !mayBeRefStr.contains(author)) {
				refstr = refstr + "." + author;
			}
			// 年代
			if (isStrNotEmpty(pyear) && !mayBeRefStr.contains(pyear)) {
				refstr = refstr + "." + pyear;
			}
			// 标题
			if (isStrNotEmpty(title) && !mayBeRefStr.contains(title)) {
				refstr = refstr + "." + title;
			}
			refstr = refstr + "." + mayBeRefStr;
		} else if (String.valueOf(PtypeEnum.QK.getIndex()).equals(ptype)) {// 文献类型是期刊
			// 【格式】作者.出版年份.篇名[J].刊名，卷号（期号）：起止页码.
			// 【举例】王海粟.2004.浅议会计信息披露模式.财政研究,21(1)：56-58.
			// 作者
			if (isStrNotEmpty(author)) {
				refstr = refstr + "." + author;
			} else if (isStrNotEmpty(editor)) {
				refstr = refstr + "." + editor;
			}
			// 出版年份
			if (isStrNotEmpty(pyear)) {
				refstr = refstr + "." + pyear;
			}
			// 篇名
			if (isStrNotEmpty(title)) {
				refstr = refstr + "." + title;
			}
			// 刊名
			if (isStrNotEmpty(journal)) {
				refstr = refstr + "." + journal;
			}
			// 卷号
			if (isStrNotEmpty(r_volume)) {
				refstr = refstr + "," + r_volume;
			}
			// （期号）
			if (isStrNotEmpty(period)) {
				refstr = refstr + "(" + period + ")";
			}
			// 起始页码
			if (isStrNotEmpty(refs) && isStrNotEmpty(refe)) {
				refstr = refstr + ":" + refs + "-" + refe;
			} else if (isStrNotEmpty(refs)) {
				refstr = refstr + ":" + refs;
			} else if (isStrNotEmpty(refe)) {
				refstr = refstr + ":" + refe;
			}

		} else if (String.valueOf(PtypeEnum.ZZ.getIndex()).equals(ptype)) {// 专著
			// 【格式】作者.出版年份.书名[M].出版地：出版社.起止页码.
			// 作者
			if (isStrNotEmpty(author)) {
				refstr = refstr + "." + author;
			} else if (isStrNotEmpty(editor)) {
				refstr = refstr + "." + editor;
			}
			// 出版年份
			if (isStrNotEmpty(pyear)) {
				refstr = refstr + "." + pyear;
			}
			// 书名
			if (isStrNotEmpty(title)) {
				refstr = refstr + "." + title;
			}
			// 出版地
			if (isStrNotEmpty(place)) {
				refstr = refstr + "." + place;
			}
			// 出版社
			if (isStrNotEmpty(press)) {
				refstr = refstr + ":" + press;
			}
			// 起始页码
			if (isStrNotEmpty(refs) && isStrNotEmpty(refe)) {
				refstr = refstr + "," + refs + "-" + refe;
			} else if (isStrNotEmpty(refs)) {
				refstr = refstr + "." + refs;
			} else if (isStrNotEmpty(refe)) {
				refstr = refstr + "." + refe;
			}

		} else if (String.valueOf(PtypeEnum.LWJ.getIndex()).equals(ptype)) {// 论文集
			// 【格式】作者.出版年份.篇名.出版地：出版者,起始页码.
			// 作者
			if (isStrNotEmpty(author)) {
				refstr = refstr + "." + author;
			} else if (isStrNotEmpty(editor)) {
				refstr = refstr + "." + editor;
			}
			// 出版年份
			if (isStrNotEmpty(pyear)) {
				refstr = refstr + "." + pyear;
			}
			// 篇名
			if (isStrNotEmpty(title)) {
				refstr = refstr + "." + title;
			}
			// 出版地
			if (isStrNotEmpty(place)) {
				refstr = refstr + "." + place;
			}
			// 出版社
			if (isStrNotEmpty(press)) {
				refstr = refstr + ":" + press;
			}
			// 起始页码
			if (isStrNotEmpty(refs) && isStrNotEmpty(refe)) {
				refstr = refstr + "," + refs + "-" + refe;
			} else if (isStrNotEmpty(refs)) {
				refstr = refstr + "." + refs;
			} else if (isStrNotEmpty(refe)) {
				refstr = refstr + "." + refe;
			}

		} else if (String.valueOf(PtypeEnum.other.getIndex()).equals(ptype)) {// 其他
			// 作者
			if (isStrNotEmpty(author)) {
				refstr = refstr + "." + author;
			} else if (isStrNotEmpty(editor)) {
				refstr = refstr + "." + editor;
			}
			// 出版年份
			if (isStrNotEmpty(pyear)) {
				refstr = refstr + "." + pyear;
			}
			// 标题
			if (isStrNotEmpty(title) && title.contains(author.replaceAll(".,，", ""))
					&& title.contains(pyear.replaceAll(".,，", ""))) {
				refstr = title;
			} else if (isStrNotEmpty(title)) {
				refstr = refstr + "." + title;
			}
			// 可能是期刊
			if (isStrNotEmpty(journal)) {
				// 刊名
				if (isStrNotEmpty(journal)) {
					refstr = refstr + "." + journal;
				}
				// 卷号
				if (isStrNotEmpty(r_volume) && !refstr.contains(r_volume)) {
					refstr = refstr + "," + r_volume;
				}
				// （期号）
				if (isStrNotEmpty(period) && !refstr.contains(period)) {
					refstr = refstr + "(" + period + ")";
				}
			} else if (isStrNotEmpty(place)) {// 可能是专著或论文集
				// 出版地
				if (isStrNotEmpty(place)) {
					refstr = refstr + "." + place;
				}
				// 出版社
				if (isStrNotEmpty(press)) {
					refstr = refstr + ":" + press;
				}
			}

			// 起始页码
			if (isStrNotEmpty(refs) && isStrNotEmpty(refe) && !refstr.contains(refs) && !refstr.contains(refe)) {
				refstr = refstr + "," + refs + "-" + refe;
			} else if (isStrNotEmpty(refs) && !refstr.contains(refs)) {
				refstr = refstr + "." + refs;
			} else if (isStrNotEmpty(refe) && !refstr.contains(refe)) {
				refstr = refstr + "." + refe;
			}
		}

		// 最后所有的完整题录都判断一下是否包含作者和年代,
		String finalAuthor = r.getAuthor();
		String finalPyear = r.getPyear();
		// 先年代
		if (!refstr.contains(finalPyear) && !finalPyear.contains(";")) {
			refstr = finalPyear + "." + refstr;
		}
		// 后作者
		if (!refstr.contains(finalAuthor) && !finalPyear.contains(";")) {
			refstr = finalAuthor + "." + refstr;
		}

		refstr = refstr.replace("..", ".");
		refstr = refstr.replace("不详", "");
		refstr = refstr.replace("unknown", "");
		refstr = refstr.replace("Unknown", "");
		refstr = refstr.replace(",,", ",");
		refstr = refstr.replace("，，", "，");
		refstr = refstr.replace("（ ）", "");
		refstr = refstr.replace("..", ".");

		refstr = refstr.trim();

		if (refstr.trim().startsWith(".")) {
			refstr = refstr.substring(1, refstr.length()).trim();
		}
		r.setRefstr(refstr);

	}

	// 描述信息
	public String insertDesc(HttpServletRequest request) throws Exception {
		ResultSet rs = null;
		logger.info("开始录入描述信息（id和旧系统保持一致）（");
		initData(request);
		Connection connDB = null;
		connDB = OtherConnDB.getConnDB(configuration);
		try {
			StringBuffer querySql = new StringBuffer();
			querySql.append("select des.Inputer as desInputer,");
			querySql.append(
					"des.Relation_Desc,des.id as id ,''  as destitle, des.Description_Person as describer,des.Description_Time as desdate,des.Description_Content as descontent, ");
			querySql.append("desType.Description_Type as destype, ");
			querySql.append("des.rightsholder as rightsholder,'' as desclicense,des.Lang as desclanguage,");
			querySql.append(
					"ta.fullname as scientific_name1,ta.Latin_Name as scientific_name2,ta.Chinese_Name as chinese_name,'' as t_group,");
			querySql.append("tre.id as datasources_id,tre.TreeName as stitle,ta.id as taxon_id ");
			querySql.append(
					"from Description_Species des left join taxa  ta on des.TaxaID = ta.id left join  Description_Type desType  on desType.id =  ");
			querySql.append("des.Description_Type_ID left join tree tre on tre.id = ta.TreeID ");
			querySql.append("where tre.id = ? ");
			PreparedStatement prepareStatement = connDB.prepareStatement(querySql.toString());
			prepareStatement.setString(1, TreeID);
			rs = prepareStatement.executeQuery();
			List<Description> entities = new ArrayList<>();
			logger.info("查询描述信息完毕，开始处理...");
			long startTime = System.currentTimeMillis(); // 获取开始时间
			int i = 0;
			while (rs.next()) {
				String scientific_name1 = rs.getString("scientific_name1");
				String scientific_name2 = rs.getString("scientific_name2");
				String name = "";
				if (isStrNotEmpty(scientific_name1)) {
					name = scientific_name1;
				} else if (isStrNotEmpty(scientific_name2)) {
					name = scientific_name2;
				}
				Description d = new Description();
				d.setRefjson(getDescRefjson(rs.getString("id")));
				Taxon taxon = taxonRepository.findByTaxonOldId(rs.getString("taxon_id"), sourcesid);
				if (taxon == null || !isStrNotEmpty(taxon.getId())) {
					logger.info("查询taxon为空，跳过这条数据继续执行" + rs.getString("taxon_id") + "\n rs.getString(\"id\")=="
							+ rs.getString("id"));
					continue;
				}
				d.setTaxon(taxon);
				d.setDescriber(rs.getString("describer"));
				d.setDesdate(rs.getString("desdate"));
				d.setDestitle(name + "的" + rs.getString("destype"));// destitle和destype一致
				String descontent = rs.getString("descontent");
				descontent = descontent.replaceAll("&nbsp;", "");
				d.setDescontent(descontent.trim());
				// 描述类型
				String destype = rs.getString("destype");
				List<Descriptiontype> typelist = descriptiontypeRepository.findByName(destype);
				Descriptiontype descriptiontype = new Descriptiontype();
				if (destype.equals("全球种数估计")) {
					descriptiontype.setId("453");
				} else if (destype.equals("生物学描述")) {
					descriptiontype.setId("101");
				} else if (destype.equals("国内种数估计")) {
					descriptiontype.setId("452");
				} else if (destype.equals("生境与习性（生态）")) {
					descriptiontype.setId("152");
				} else if (destype.equals("保护现状")) {
					descriptiontype.setId("301");
				} else if (typelist != null && typelist.size() > 0) {
					descriptiontype.setId(typelist.get(0).getId());
				} else {
					// 创建一条描述类型数据
					Descriptiontype entity = new Descriptiontype();
					entity.setName(destype);
					entity.setPid("0");
					entity.setStyle(destype);
					descriptiontype = descriptiontypeRepository.save(entity);
				}
				d.setDestypeid(String.valueOf(descriptiontype.getId()));
				d.setDescriptiontype(descriptiontype);

				d.setRightsholder(rs.getString("rightsholder"));
				String desclanguage = rs.getString("desclanguage");
				if (isStrNotEmpty(desclanguage)) {
					if ("en".trim().equals(desclanguage)) {
						d.setLanguage("2");// 英文
					} else if ("zh".trim().equals(desclanguage)) {
						d.setLanguage("1");// 中文
					}
				}

				String Relation_Desc = rs.getString("Relation_Desc");
				d.setRelationId(Relation_Desc);

				// 一些必要的
				d.setId(rs.getString("id"));
				String Inputer = rs.getString("desInputer");
				d.setInputer(aQueryForcesDB.getUserId(loginUser, Inputer));
				d.setInputtime(CommUtils.getTimestamp(inputtimeStr));
				d.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
				d.setSynchstatus(0);
				d.setStatus(1);
				d.setSourcesid(sourcesid);

				entities.add(d);
				if (entities.size() == 3000) {
					i = i + entities.size();
					logger.info("(部分)描述——开始保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min");
					logger.info(CommUtils.getCurrentDate() + "描述——向数据库中保存:" + entities.size());
//					batchInsertService.batchInsertDescription(entities, inputtimeStr);
//					descriptionRepository.saveAll(entities);
					batchSubmitService.saveAll(entities);
					entities.clear();
					logger.info(CommUtils.getCurrentDate() + "(部分)描述——清空数组:" + entities.size());
					logger.info("(部分)描述——结束保存： " + (System.currentTimeMillis() - startTime) / 60000 + "min, 已经存入数据库记录数："
							+ i);
				}

			}
			logger.info("处理完毕，保存剩余描述" + entities.size());
			descriptionRepository.saveAll(entities);
			logger.info("全部保存完毕" + entities.size());

		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		return "description insert finish";

	}

	private boolean isStrNotEmpty(String str) {
		if (str != null && str.length() != 0 && !str.equals("") && !str.equals(" ")) {
			return true;
		}
		return false;
	}

	public void saveMultimedia(String taxonId, Multimedia thisMultimedia) {
		Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
		thisMultimedia.setInputer(loginUser);
		thisMultimedia.setInputtime(CommUtils.getTimestamp(inputtimeStr));
		thisMultimedia.setSynchdate(CommUtils.getTimestamp(inputtimeStr));
		thisMultimedia.setTaxon(thisTaxon);
		thisMultimedia.setStatus(1);
		thisMultimedia.setSynchstatus(0);
		this.multimediaRepository.save(thisMultimedia);
	}

	private String teamId = null;// 新采集系统的team.id（团队）
	private String datasetId = null;// 新采集系统的dataset.id（数据集）

	public void initData(HttpServletRequest request) {
		getXmlParams(request);
		if (CommUtils.isStrEmpty(loginUser)) {
			throw new ValidationException("loginUser值为空！请初始化，无法继续...");
		}
		if (!isStrNotEmpty(taxasetId)) {
			throw new ValidationException("taxasetId值为空！请初始化，无法继续...");
		}
		// 通过分类单元集ID获取数据集ID和团队ID
		Taxaset taxaset = taxasetRepository.findOneById(taxasetId);
		String datasetId = taxaset.getDataset().getId();
		if (!isStrNotEmpty(datasetId)) {
			throw new ValidationException("Taxaset 数据不规范，datasetId值为空！无法继续...");
		}
		Dataset dataset = datasetRepository.findOneById(datasetId);
		String teamId = dataset.getTeam().getId();
		if (!isStrNotEmpty(teamId)) {
			throw new ValidationException("Dataset 数据不规范，teamId值为空！无法继续...");
		}
		Team team = teamRepository.findOneById(teamId);
		String id = team.getId();
		if (!isStrNotEmpty(id)) {
			throw new ValidationException("Team 数据不规范，id值为空！无法继续...");
		}
		Taxtree taxtree = taxtreeRepository.findOneById(taxtreeId);

		if (!isStrNotEmpty(taxtree.getTreename())) {
			throw new ValidationException("Taxtree 数据不规范，Treename值为空！无法继续...");
		}
		// 通过分类单元集ID获取数据集ID和团队ID
		this.datasetId = datasetId;
		this.teamId = teamId;
		logger.info(" 团队:" + team.getName());
		logger.info(" 数据集:" + dataset.getDsname());
		logger.info(" 分类单元集:" + taxaset.getTsname());
		logger.info(" 分类树:" + taxtree.getTreename());
	}

	private Map<String, String> getGeojsonaAndDiscontent(String taxonId) throws Exception {
		Map<String, String> map = new HashMap<>();
		Connection connDB = null;
		ResultSet rs = null;
		JSONObject geo = new JSONObject();
		String Description_Content = "";
		try {
			String sql = "select lp.CName, lsd.* ,ds.Description_Content "
					+ " from Location_Species_Detail lsd left join Taxa taxa on taxa.id = lsd.TaxaID left join Location_province lp on  lp.id = lsd.ProvinceID "
					+ " left join Description_Species ds on ds.id = lsd.DescriptionID"
					+ " where TreeID = ? and  lsd.TaxaID = ?";
			connDB = OtherConnDB.getConnDB(configuration);
			PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			prepareStatement.setString(1, TreeID);
			prepareStatement.setString(2, taxonId);
			rs = prepareStatement.executeQuery();
			String geoIds = "";
			while (rs.next()) {
				String geoId = "";
				String cngeoname = rs.getString("CName");
				List<Geoobject> list = null;
				if ("全中国".equals(cngeoname.trim())) {
					list = geoobjectRepository.findAll();
				}
				if ("东北".equals(cngeoname.trim())) {
					continue;
				} else {
					list = geoobjectRepository.findByCngeoname(cngeoname);
				}
				if (list != null && list.size() != 0) {
					geoId = list.get(0).getId();
				} else {
//					logger.info("在新的采集系统中找不到分布地：" + cngeoname);
					continue;
				}
				Description_Content = rs.getString("Description_Content");
				if (rs.next()) {
					rs.previous();
					geoIds = geoIds + geoId + "&";
				} else {
					rs.previous();
					geoIds = geoIds + geoId;
				}

			}
			geo.put("geoIds", geoIds);

		} finally {
			if (rs != null)
				rs.close();
		}
		if (geo.get("geoIds") == null) {
			map.put("geoIds", "");
		} else {
			map.put("geoIds", geo.toJSONString());
		}
		map.put("Description_Content", Description_Content);
		return map;
	}

	Map<String, String> refmap = new HashMap<>();

	private String getCommonNameRefjson(String ComID) throws Exception {
		refjson.setLength(0);
		refjson.append("[");
		Connection connDB = null;
		// 从旧采集系统查询关联关系
		connDB = OtherConnDB.getConnDB(configuration);
		String sql = "select PaperID from CommonName_Paper where ComID = ?";
		// ResultSet.TYPE_SCROLL_INSENSITIVE 结果集的游标可以上下移动，当数据库变化时，当前结果集不变。
//		ResultSet.CONCUR_READ_ONLY 不能用结果集更新数据库中的表。

		PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		prepareStatement.setString(1, ComID);
		ResultSet rs = prepareStatement.executeQuery();
		boolean hasRef = false;
		while (rs.next()) {
			hasRef = true;
			String PaperID = rs.getString("PaperID");
			// 从新采集系统查询文献id
			try {
				if (refmap.get(PaperID) == null) {
					Ref refid = refRepository.findOneById(PaperID);
					String id = refid.getId();
					refmap.put(PaperID, id);
				}
			} catch (Exception e) {
				logger.error("查找文献失败：PaperID = " + PaperID);
				continue;
			}
			// 拼接字符串
			if (rs.next()) {
				rs.previous();
				refjson.append("{\"refS\":\"0\",\"refE\":\"0\",\"refId\":\"" + PaperID + "\"},");
			} else {
				rs.previous();
				refjson.append("{\"refS\":\"0\",\"refE\":\"0\",\"refId\":\"" + PaperID + "\"}");
			}

		}
		refjson.append("]");
		rs.close();
		if (!hasRef) {
			refjson.setLength(0);
		}
		return refjson.toString();
	}

	private String getDescRefjson(String descId) throws Exception {
		refjson.setLength(0);
		refjson.append("[");
		Connection connDB = null;
		// 从旧采集系统查询关联关系
		connDB = OtherConnDB.getConnDB(configuration);
		String sql = "select PaperID from Description_Paper where DescriptionID = ?";
		PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		prepareStatement.setString(1, descId);
		ResultSet rs = prepareStatement.executeQuery();
		boolean hasRef = false;
		while (rs.next()) {
			hasRef = true;
			String PaperID = rs.getString("PaperID");
			// 从新采集系统查询文献id
			if (refmap.get(PaperID) == null) {
				Ref refid = refRepository.findOneById(PaperID);
				String id = refid.getId();
				refmap.put(PaperID, id);
			}
			// 拼接字符串
			if (rs.next()) {
				rs.previous();
				refjson.append("{\"refS\":\"0\",\"refE\":\"0\",\"refId\":\"" + PaperID + "\"},");
			} else {
				rs.previous();
				refjson.append("{\"refS\":\"0\",\"refE\":\"0\",\"refId\":\"" + PaperID + "\"}");
			}

		}
		refjson.append("]");
		rs.close();
		if (!hasRef) {
			refjson.setLength(0);
			return null;
		}
		return refjson.toString();
	}

	@Autowired
	private TaxtreeService taxtreeService;

	/**
	 * 删除分类树
	 * 
	 * @param request
	 * @return
	 */
	public boolean removeNodeAndAllChindren(HttpServletRequest request) {
		// 处理节点顺序
		// 获取当前节点
		TaxonHasTaxtree thisNode = this.taxtreeService.findOneTaxonHasTaxtreeByIds(request.getParameter("taxonId"),
				request.getParameter("taxtreeId"));
		// 获得后一个节点
		TaxonHasTaxtree nextNode = this.taxtreeService.findNextNode(request.getParameter("taxonId"),
				request.getParameter("taxtreeId"));
		// 判断是否有后节点
		if (nextNode != null) {
			// 判断是否有是首节点
			if (thisNode.getPrevTaxon() != null) {
				// 获取前一个节点
				TaxonHasTaxtree prevNode = this.taxtreeService.findOneTaxonHasTaxtreeByIds(thisNode.getPrevTaxon(),
						request.getParameter("taxtreeId"));
				nextNode.setPrevTaxon(prevNode.getTaxonId());
			} else
				nextNode.setPrevTaxon(null);
			this.taxtreeService.saveOneTaxonHasTaxtree(nextNode);
		}
		return this.taxtreeService.removeNodeAndAllChindren(request.getParameter("taxonId"),
				request.getParameter("taxtreeId"));
	}

	public JSON batchImages(String taxonId, HttpServletRequest req, Multimedia m, File localFile, String uploadPath)
			throws Exception {
		JSONObject thisResult = new JSONObject();
		// 转 MultipartFile
		FileInputStream in_file = new FileInputStream(localFile);
		MultipartFile file = new MockMultipartFile(localFile.getName(), in_file);
		if (null == file || file.isEmpty()) {
			logger.info("没有上传文件");
			return thisResult;
		} else {
			// 初始化媒体
			Multimedia thisMultimedia;
			try {
				thisMultimedia = new Multimedia();
				if (file.getSize() > 100 * 1024 * 1024) {
					thisResult.put("code", ReturnCode.FAILURE.getCode());
					thisResult.put("message", ReturnCode.FAILURE.getMessage_zh());
					thisResult.put("data", "");
				}
				String name = localFile.getName();
				String[] split = name.split("\\.");
				String suffix = split[1]; // 后缀名
				String newFileName = UUIDUtils.getUUID32() + "." + suffix; // 文件名
				String realPath = req.getSession().getServletContext().getRealPath(uploadPath); // 本地存储路径
				// 构造路径 -- Team/Dataset/Taxon/文件
				realPath = realPath + teamId + "/" + datasetId + "/" + taxonId + "/";
				try {
					// 先把文件保存到本地
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
				} catch (IOException e1) {
					logger.error("文件保存到本地发生异常:" + e1.getMessage() + ",跳过继续执行");
					return thisResult;
				}
				// 图片经纬度
				if (m.getLat() != 0.0) {
					thisMultimedia.setLat(m.getLat());
				}
				double lng = m.getLng();
				if (lng != 0.0) {
					thisMultimedia.setLng(lng);
				}
				thisMultimedia.setId(UUIDUtils.getUUID32());// 主键
				thisMultimedia.setSourcesid(m.getSourcesid());
				thisMultimedia.setTitle(m.getTitle().trim());

				thisMultimedia.setContext(m.getContext().trim());
				thisMultimedia.setRightsholder(m.getRightsholder());
				thisMultimedia.setOldPath(m.getOldPath());
				String licenseid = m.getLisenceid();
				if (StringUtils.isNotBlank(licenseid)) {
					License thisLicense = this.licenseRepository.findOneById(licenseid);
					thisMultimedia.setLisenceid(thisLicense.getId());
					thisMultimedia.setLicense(thisLicense);
				}
				thisMultimedia.setCity(m.getCity());
				thisMultimedia.setCountry(m.getCountry());
				thisMultimedia.setCounty(m.getCounty());
				thisMultimedia.setLocality(m.getLocality());
				thisMultimedia.setLocation(m.getLocation());
				thisMultimedia.setProvince(m.getProvince());
				thisMultimedia.setMediatype(m.getMediatype());
				thisMultimedia.setPath(teamId + "/" + datasetId + "/" + taxonId + "/" + newFileName);
				thisMultimedia.setSuffix(suffix);
				thisMultimedia.setOldPath(m.getOldPath());
				// 参考文献
				@SuppressWarnings("unused")
				int countMultimediaReferences = 0; // 参考文献总数量(无用)
				String multimediaReferences = null; // 参考文献内容

				if (StringUtils.isNotBlank(multimediaReferences)) {
					@SuppressWarnings("null")
					String[] multimediaReference = multimediaReferences.split(",");
					JSONArray jsonArray = new JSONArray();
					String jsonStr = null;
					for (int i = 0; i < multimediaReference.length; i++) {
						String[] context = multimediaReference[i].split("&");
						jsonStr = "{" + "\"refId\"" + ":\"" + context[0] + "\"," + "\"refS\"" + ":\"" + context[1]
								+ "\"," + "\"refE\"" + ":\"" + context[2] + "\"" + "}";
						JSONObject jsonText = JSON.parseObject(jsonStr);
						jsonArray.add(jsonText);
					}
					thisMultimedia.setRefjson(jsonArray.toJSONString());
				}
			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
				logger.error("保存一张多媒体出错，错误信息：" + e.getMessage());
				return thisResult;
			}
			// 保存数据
			this.saveMultimedia(taxonId, thisMultimedia);
			// 构建返回结果
			thisResult.put("data", "");
		}
		return thisResult;
	}

	public String insertSpecimen(HttpServletRequest request) throws Exception {
		logger.info("录入标本，（id和旧系统一致）");
		Connection connDB = OtherConnDB.getConnDB(configuration);
		StringBuffer sql = new StringBuffer();
		sql.append("select *  from Typespecimen s left join taxa t on t.id = s.TaxaID where t.TreeID = ?");
		PreparedStatement prepareStatement = connDB.prepareStatement(sql.toString());
		prepareStatement.setString(1, TreeID);
		ResultSet rs = prepareStatement.executeQuery();
		int i = 0;
		while (rs.next()) {
			i++;
			Specimendata s = new Specimendata();
			s.setId(rs.getString("ID"));
			s.setTaxonId(rs.getString("TaxaID"));
			s.setCollector(rs.getString("CollectionPerson"));
			s.setCollectdate(rs.getString("CollectionDate"));
			s.setLocality(rs.getString("CollectionPlace"));
			s.setIdenby(rs.getString("Judger"));
			s.setIdendate(rs.getString("JudgerDate"));
			s.setSpecimenno(rs.getString("SpecimenNO"));
			s.setSpecimentype(rs.getString("SpecimenType"));
			s.setSex(rs.getString("Gender"));
			s.setInputer(aQueryForcesDB.getUserId(loginUser, rs.getString("Inputer")));
			s.setConserveStatus(rs.getString("ConserveStatus"));
			s.setAltitude(rs.getString("Altitude"));
			try {
				s.setLat(rs.getDouble("Latitude"));
			} catch (Exception e) {

			}
			try {
				s.setLng(rs.getDouble("Longitude"));
			} catch (Exception e) {

			}
			s.setDeep(rs.getString("Deep"));
			s.setHost(rs.getString("Host"));
			s.setDescNote(rs.getString("Description"));
			s.setSpecimenStatus(rs.getString("SpecimenStatus"));
			s.setStoredin(rs.getString("txtunits"));
			String refjson = getSpecimenRef(s.getId());
			if (CommUtils.isStrNotEmpty(refjson)) {
				s.setRefjson(refjson);
			}
			// 通用的
			s.setInputtime(CommUtils.getUtilDate(inputtimeStr));
			s.setSynchdate(CommUtils.getUtilDate(inputtimeStr));
			s.setState(1);// '状态（默认1、可用；0、不可用）
			s.setSynchstatus(0);// DEFAULT 0 COMMENT '同步状态，即是否与服务器进行同步
			specimendataRepository.save(s);
			if (i % 500 == 0) {
				logger.info("保存条数：" + i);
			}

		}
		logger.info("insertSpecimen 完成：total " + i);
		return "insertSpecimen ： total " + i;
	}

	/**
	 * Specimen 标本参考文献
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private String getSpecimenRef(String SpecimenID) throws Exception {
		JSONArray jsonArray = new JSONArray();
		Connection connDB = OtherConnDB.getConnDB(configuration);
		StringBuffer sql = new StringBuffer();
		sql.append("select * from Specimen_Paper where SpecimenID = ?");
		PreparedStatement prepareStatement = connDB.prepareStatement(sql.toString());
		prepareStatement.setString(1, SpecimenID);
		ResultSet rs = prepareStatement.executeQuery();
		while (rs.next()) {
			String paperID = rs.getString("PaperID");
			// 从新采集系统查询文献id
			try {
				Ref refid = refRepository.findOneById(paperID);
				refid.getId();
			} catch (Exception e) {
				logger.error(
						"错误信息：" + e.getMessage() + "未查询到此参考文献：PaperID = " + paperID + "    SpecimenID = " + SpecimenID);
				continue;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("refE", " 0");
			jsonObject.put("refS", " 0");
			jsonObject.put("refId", "" + paperID);
			jsonObject.put("refType", "0");
			jsonArray.add(jsonObject);
		}
		if (rs != null) {
			rs.close();
		}
		if (jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();
	}

	public ResultSet query(String sql) throws Exception {
		Connection connDB = OtherConnDB.getConnDB(configuration);
		PreparedStatement prepareStatement = connDB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = prepareStatement.executeQuery();
		return rs;
	}

	@Override
	public boolean getCitationFromForcesDB(String scientificname, String sciname, String TreeId, Citation citation,
			String nameTypeId) throws Exception {
		boolean update = false;
		// 已知数据源、异名引证名称，查询引证命名信息和引证原文
		String sql = "select s.* from Species s left join taxa t on t.id  = s.TaxaID where  t.treeId = '" + TreeId
				+ "' and (t.fullname = '" + sciname + "' or t.latin_name = '" + sciname
				+ "') and t.StatusID = 'BEDBB69A-139D-45A3-8CD9-CC7D55BF6E7E'";
//		logger.info(sql);
		ResultSet rs = this.query(sql);
		while (rs.next()) {
			String authorstr = "";
			String Named_Person = rs.getString("Named_Person");
			String Named_Date = rs.getString("Named_Date");
			// 命名人和年代
			if (!isStrNotEmpty(Named_Date)) {// Named_Date为空
				authorstr = Named_Person;
			} else if (isStrNotEmpty(Named_Date) && Named_Person.contains(Named_Person)) {// Named_Date非空且Named_Person包含Named_Date
				authorstr = Named_Person;
			} else {
				authorstr = Named_Person + "," + Named_Date;
			}
			if (isStrNotEmpty(authorstr)) {
				authorstr = authorstr.replace("(", "");
				authorstr = authorstr.replace(")", "");
				authorstr = authorstr.replace("（", "");
				authorstr = authorstr.replace("）", "");
				authorstr = authorstr.trim();
			}
			if (StringUtils.isNotEmpty(authorstr)) {
				citation.setAuthorship(authorstr);
			}
			// 引证原文
			String citationstr = "";
			String remark = rs.getString("Remark");
			String description = rs.getString("Description");
			String typeLocation = rs.getString("Type_Location");
			if (StringUtils.isNotEmpty(remark)) {
				citationstr = remark;
			} else if (StringUtils.isNotEmpty(description)) {
				citationstr = description;
			}
			if (StringUtils.isNotEmpty(typeLocation)) {
				citationstr = citationstr + " " + typeLocation;
			}
			if (StringUtils.isNotEmpty(citationstr)) {
				if (!citationstr.contains(sciname)) {
					citationstr = sciname + " " + citationstr;
				}
				update = true;
				citation.setCitationstr(citationstr);
			}
			break;
		}
		return update;
	}

	@Override
	public List<Citation> getAcceptCitationByParams(Taxon taxon, String forcesDB_Tree_Id,String sourcesid,String inputer) throws Exception {
		ResultSet rs = null;
		List<Citation> resultlist = new ArrayList<>();
		try {
			String scientificname = taxon.getScientificname();
			String sql = "select * from Species where taxaid = (select id from Taxa where treeId = '" + forcesDB_Tree_Id
					+ "' and (fullname = '" + scientificname + "' or latin_name = '" + scientificname
					+ "') and StatusID = '67280F4A-D8D6-4CAD-BCDA-843866010852')";
			rs = this.query(sql);
			while (rs.next()) {
				Citation record = new Citation();
				record.setId(UUIDUtils.getUUID32());
				record.setSciname(scientificname);
				record.setNametype(NametypeEnum.acceptedName.getIndex());
				record.setStatus(1);
				record.setSourcesid(sourcesid);
				record.setInputer(inputer);
				record.setTaxon(taxon);
				String authorstr = "";
				String Named_Person = rs.getString("Named_Person");
				String Named_Date = rs.getString("Named_Date");
				// 命名人和年代
				if (!isStrNotEmpty(Named_Date)) {// Named_Date为空
					authorstr = Named_Person;
				} else if (isStrNotEmpty(Named_Date) && Named_Person.contains(Named_Person)) {// Named_Date非空且Named_Person包含Named_Date
					authorstr = Named_Person;
				} else {
					authorstr = Named_Person + "," + Named_Date;
				}
				if (isStrNotEmpty(authorstr)) {
					authorstr = authorstr.replace("(", "");
					authorstr = authorstr.replace(")", "");
					authorstr = authorstr.replace("（", "");
					authorstr = authorstr.replace("）", "");
					authorstr = authorstr.trim();
				}
				if (StringUtils.isNotEmpty(authorstr)) {
					record.setAuthorship(authorstr);
				}
				// 引证原文
				String citationstr = "";
				String remark = rs.getString("Remark");
				String description = rs.getString("Description");
				String typeLocation = rs.getString("Type_Location");
				if (StringUtils.isNotEmpty(remark)) {
					citationstr = remark;
				} else if (StringUtils.isNotEmpty(description)) {
					citationstr = description;
				}
				if (StringUtils.isNotEmpty(typeLocation)) {
					citationstr = citationstr + " " + typeLocation;
				}
				if (StringUtils.isNotEmpty(citationstr)) {
					if (!citationstr.contains(scientificname)) {
						citationstr = scientificname + " " + citationstr;
					}
					record.setCitationstr(citationstr);
				}
				
				resultlist.add(record);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return resultlist;
	}

}
