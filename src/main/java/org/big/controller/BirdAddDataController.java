package org.big.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.big.common.FilesUtils;
import org.big.constant.DataConsts;
import org.big.entityVO.ExcelUntilE;
import org.big.entityVO.ExcelUntilF;
import org.big.entityVO.NametypeEnum;
import org.big.service.BirdAddData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @Description 2019鸟数据补充
 * @author ZXY
 */
@Controller
public class BirdAddDataController {
	@Autowired
	private BirdAddData birdAddData;
	
	/**
	 * 
	 * @Description 2019鸟类名录数据转换成word文档
	 * @param response
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdAddDataController_exportToWord", method = RequestMethod.GET)
	public String exportToWord(HttpServletResponse response) throws Exception {
		birdAddData.exportToWord();
		return "index";
	}
	
	@RequestMapping(value = "/birdAddDataController_orderByWord", method = RequestMethod.GET)
	public String orderByWord(HttpServletResponse response) throws Exception {
		birdAddData.orderByWord();
		birdAddData.orderTreeByTaxonOrderNum();
//		birdAddData.deleteSameCommname();
		return "index";
	}
	
	/**
	 * 
	 * @Description http://localhost/importByExcel
	 * @param response
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/importByExcel", method = RequestMethod.GET)
	public String importByExcel(HttpServletResponse response) throws Exception {
		birdAddData.importByExcel();
		return "index";
	}

	@RequestMapping(value = "/birdAddDataController_countCitationByTaxon", method = RequestMethod.GET)
	public String countCitationByTaxon(HttpServletResponse response) throws Exception {
		birdAddData.countCitationByTaxon(response);
		return "index";
	}

	/**
	 * 
	 * @Description 更新2019鸟类名录引证：根据引证原文生成参考文献，没有引证的从旧采集系统导入
	 * @param response
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdAddDataController_updateCitationStrBySciName", method = RequestMethod.GET)
	public String updateCitationStrBySciName(HttpServletResponse response) throws Exception {
//		System.out.println("第1步：根据旧采集系统（鸟纲 郑雷）补充引证，根据引证原文补充参考文献");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.updateCitationStrBySciName(response);
//		System.out.println("第2步：验证并补充接受名引证或异名引证（数据来源于采集系统 数据集=鸟纲-郑-雷-sp2000  或  旧系统数据库鸟纲-郑-雷-sp2000 |  或  旧系统数据库动物志）");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.validateCitation();
//		System.out.println("第3步：接受名和异名 名称相同，命名信息相同，删除异名");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.deleteCitationOfSameSciname(DataConsts.Dataset_Id_Bird2019);
//
//		System.out.println("第4步：根据旧采集系统（动物志）补充异名引证的完整引证字段");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.perfectCitationStr(DataConsts.Dataset_Id_Bird2019);
//		System.out.println("第5步：完善的命名信息（有命名人没有命名时间），根据完整引证解析");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.perfectAuthorByCitationStr();
//		System.out.println("第6步：完善的命名信息（有命名人没有命名时间）,20190415林老师提供的excel");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.perfectAuthorFromExcel();
//		System.out.println("第7步：从2018鸟类名录补充俗名到2019鸟类名录");
//		birdAddData.addCommonName();
//		System.out.println("第8步：根据《补充接受名引证20190422.xlsx》补充2019鸟类名录接受名引证");
//		birdAddData.perfectAcceptCitationStr();
//		System.out.println("最后一步：打印没有接受名引证的taxon");
//		birdAddData.countCitationByDs(DataConsts.Dataset_Id_Bird2019);
//		birdAddData.printDontHasAcceptCitationTaxon();
		// 此taxon 查询不到接受名引证：数据集：tsname=雀形目 scientificname=Pericrocotus solaris

		return "index";
	}

	// 导出数据集下所有的异名引证
	@RequestMapping(value = "/birdAddDataController_exportCitationExcelOfDataSet", method = RequestMethod.GET)
	public void exportCitationExcelOfDataSet(HttpServletResponse response) throws Exception {
		List<ExcelUntilF> list = birdAddData.exportCitationExcelOfDataSet(NametypeEnum.acceptedName,
				DataConsts.Dataset_Id_Bird2019);
		FilesUtils.exportExcel(list, "2019Bird_synonymCitation", "2019Bird_synonymCitation", ExcelUntilF.class,
				URLEncoder.encode("2019Bird_synonymCitation.xls", "UTF-8"), response);
	}

}
