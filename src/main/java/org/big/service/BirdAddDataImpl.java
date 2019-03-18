package org.big.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.entity.Ref;
import org.big.entityVO.BirdListComparisonExcelVO;
import org.big.entityVO.ExcelUntilB;
import org.big.repository.RefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class BirdAddDataImpl implements BirdAddData {
	@Autowired
	private RefRepository refRepository;
	
	private String userId_wangtianshan = "95c24cdc24794909bd140664e2ee9c3b";

	@Override
	public void importByExcel() throws Exception {
		String reffilePath = "E:\\003采集系统\\0012鸟类名录\\参考文献汇总_0308.xlsx";
		//1、读取参考文献，并保存到map中
		Map<String,String> refMap = readRefs(reffilePath);
		
	}
	/**
	 * 
	 * @Description 
	 * @param filePath
	 * @return
	 * @author ZXY
	 */
	private Map<String, String> readRefs(String filePath) {
		List<ExcelUntilB> list = readExcel(filePath);
		System.out.println("从excel读取参考文献条数："+list.size());
		Map<String,String> refMaP = new HashMap<>(list.size()+10);
		for (ExcelUntilB excelUntilB : list) {
			String refstr = excelUntilB.getColB().trim();
			String seq = excelUntilB.getColA().trim();
			
//			Ref ref = refRepository.findByRefstrAndInputer("戴波, 付义强, 王家才等. 灰腹地莺在四川的分布. 动物学杂志(已接收).", userId_wangtianshan);
			Ref ref = refRepository.findByRefstrAndInputer(refstr, userId_wangtianshan);
			if(ref == null) {
				System.out.println(seq+",数据库中没有找到完整题录="+refstr);
			}
//			refMaP.put(seq, ref.getId());
		}
		System.out.println("excel文件行数："+list.size()+",map大小："+refMaP.size());
		return refMaP;
	}
	
	private List<ExcelUntilB> readExcel(String path) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<ExcelUntilB> list = ExcelImportUtil.importExcel(new File(path),
				ExcelUntilB.class, params);
		System.out.println("读取excel所消耗时间：" + (new Date().getTime() - start));
		System.out.println("excel行数：" + list.size());
		System.out.println("打印第一行表格内容："+ReflectionToStringBuilder.toString(list.get(0)));
		System.out.println("读取excel完成");
		return list;
	}

	

}
