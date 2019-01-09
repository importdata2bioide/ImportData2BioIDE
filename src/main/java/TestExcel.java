

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.entityVO.BirdListComparisonExcelVO;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

public class TestExcel {
	
	public static void main(String[] args) throws FileNotFoundException,
	   IOException {
		ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        long start = new Date().getTime();
        List<BirdListComparisonExcelVO> list = ExcelImportUtil.importExcel(
           new File("E:\\采集系统\\新版比对旧版结果.xlsx"),
           BirdListComparisonExcelVO.class, params);
        System.out.println("时间"+(new Date().getTime() - start));
        System.out.println("数量："+list.size());
        System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
        System.out.println(list.get(0).getNewGenus());
        System.out.println(list.get(1).getNewGenus());
        System.out.println(list.get(2).getNewGenus());
        System.out.println(list.get(3).getNewGenus());
        System.out.println(list.get(4).getNewGenus());
        System.out.println("读取完成");
	 }

}
