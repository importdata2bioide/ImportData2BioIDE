package org.big.controller;

import javax.servlet.http.HttpServletRequest;

import org.big.entityVO.BaseParamsForm;
import org.big.service.PlantEncyclopediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * PlantEncyclopedia植物百科
 * @author BIGIOZ
 *
 */
@Controller
@RequestMapping(value = "/guest")
public class PlantEncyclopediaController {
	
	@Autowired
	private PlantEncyclopediaService plantEncyclopediaService;
	/**
	 * 
	 * title: PlantEncyclopediaController.java 
	 * @param baseParamsForm
	 * @param request
	 * @return
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/plantEncyclopediaController_doSave",method = RequestMethod.POST)
	public String doSave(BaseParamsForm baseParamsForm,HttpServletRequest request) {
		try {
			plantEncyclopediaService.insertPlantEncyclopedia(baseParamsForm);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	
	//以下文件打不开
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\41. 胡枝子属 Lespedeza\6. 柔毛胡枝子.xlsx
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\41. 胡枝子属 Lespedeza\7. 大叶胡枝子.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\41. 胡枝子属 Lespedeza\8. 展枝胡枝子.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\41. 胡枝子属 Lespedeza\9. 春花胡枝子.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\42. 鸡眼草属 Kummerowia\1. 鸡眼草.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\42. 鸡眼草属 Kummerowia\2. 长萼鸡眼草.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\45. 紫矿属 Butea\1. 紫矿.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\45. 紫矿属 Butea\2. 绒毛紫矿.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\48. 旋花豆属 Cochlianthus\1. 高山旋花豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\48. 旋花豆属 Cochlianthus\2a. 细茎旋花豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\48. 旋花豆属 Cochlianthus\2b. 短柄旋花豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\50. 豆薯属 Pachyrhizus\1. 豆薯.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\51. 乳豆属 Galactia\1. 琉球乳豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\51. 乳豆属 Galactia\2. 乳豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\51. 乳豆属 Galactia\3. 台湾乳豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\52. 毛蔓豆属 Calopogonium\1. 毛蔓豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\54. 土黄芪属 Nogra\1. 广西土黄芪.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\55. 华扁豆属 Sinodolichos\1. 华扁豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\57. 软荚豆属 Teramnus\1. 软荚豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\58. 琼豆属 Teyleria\1. 琼豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\59. 宿苞豆属 Shuteria\1. 硬毛宿苞豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\59. 宿苞豆属 Shuteria\2a. 宿苞豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\59. 宿苞豆属 Shuteria\2b. 毛宿苞豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\59. 宿苞豆属 Shuteria\2c. 光宿苞豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\61. 两型豆属 Amphicarpaea\1. 两型豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\61. 两型豆属 Amphicarpaea\2. 锈毛两型豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\61. 两型豆属 Amphicarpaea\3. 线苞两型豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\62. 拟大豆属 Ophrestia\1. 羽叶拟大豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\63. 距瓣豆属 Centrosema\1. 距瓣豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\64. 蝶豆属 Clitoria(新模板)\1. 蝶豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\64. 蝶豆属 Clitoria(新模板)\2. 棱荚蝶豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\64. 蝶豆属 Clitoria(新模板)\3. 广东蝶豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\64. 蝶豆属 Clitoria(新模板)\4. 三叶蝶豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\70. 硬皮豆属 Macrotyloma\1. 硬皮豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\72. 大翼豆属 Macroptilium\1. 紫花大翼豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\72. 大翼豆属 Macroptilium\2. 大翼豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\73. 菜豆属 Phaseolus\1. 菜豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\73. 菜豆属 Phaseolus\2. 荷包豆.xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
//	打开文件出错：E:\003采集系统\5-2-1-金效华\汇交专项-植物专题\豆科\中国植物志第41卷豆科(347)\73. 菜豆属 Phaseolus\3. 棉豆 .xlsx，No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
	


}
