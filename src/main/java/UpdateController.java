

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.entity.Taxon;
import org.big.entityVO.ExcelVO;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Controller
public class UpdateController {

	@Autowired
	private TaxonRepository taxonRepository;

	@RequestMapping(value = "/updateTaxonChname")
	@ResponseBody
	public String selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<ExcelVO> list = ExcelImportUtil.importExcel(new File("E:\\采集系统\\t.xlsx"), ExcelVO.class, params);
		System.out.println(new Date().getTime() - start);
		System.out.println(list.size());
		System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
		System.out.println(list.get(0).getId());
		System.out.println(list.get(1).getId());
		System.out.println(list.get(2).getId());
		System.out.println(list.get(3).getId());
		System.out.println(list.get(4).getId());
		System.out.println("读取完成");

		for (ExcelVO e : list) {
			String chname = e.getChname();
			String id = e.getId();
			if (chname == null || "".equals(chname) || "null".equals(chname)) {
				continue;
			}
			if (id == null || "".equals(id) || "null".equals(id)) {
				continue;
			}
			Taxon taxon = taxonRepository.findOneById(id);
			if (taxon == null || taxon.getId() == null) {
				continue;
			}
			// 更新操作
			taxonRepository.updateByCnameAndId(chname, id);
			System.out.println("更新完成..." + id + "    " + chname);

		}
		System.out.println("更新完成...");
		return "finish";

	}

}
