package org.big;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.big.entityVO.PlantEncyclopediaExcelVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@RunWith(SpringRunner.class)
@SpringBootTest // 开启Web上下文
@WebAppConfiguration //// 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。
public class BioIdeApplicationTests {
	/**
	 * 
	 * @Description National list of protected animals
	 * @author ZXY
	 */
	@Test
	public void test() {
		String filePath = "E:\\003采集系统\\0009国家保护动物名录\\国家保护动物名录 - 源文件.xlsx";
		System.out.println("test");
		ImportParams params = new ImportParams();
		params.setTitleRows(1);
		params.setHeadRows(0);
		long start = new Date().getTime();
		List<PlantEncyclopediaExcelVO> list = ExcelImportUtil.importExcel(new File(filePath), PlantEncyclopediaExcelVO.class,
				params);
		System.out.println(new Date().getTime() - start);
		System.out.println(list.size());
		System.out.println(ReflectionToStringBuilder.toString(list.get(0)));

	}

	@Before
	public void init() {
		System.out.println("开始测试-----------------");
	}

	@After
	public void after() {
		System.out.println("测试结束-----------------");
	}

}