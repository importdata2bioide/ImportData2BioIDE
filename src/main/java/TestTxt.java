import java.util.List;

import org.big.common.CommUtils;
import org.big.controller.SpeciesCatalogueController;

public class TestTxt {
	
	public static void main(String[] args) {

				List<String> txt = CommUtils.readTxt("E:\\采集系统\\杨定-名录-导入\\双翅目蝇类名录 正文1.txt", "Unicode");
		for (String s : txt) {
			System.out.println(s);
		}
		String line = "w(516) 叶突颊鬃实蝇 Chetostoma mirabilis (Chen, 1948)";
		boolean startWithSeq = CommUtils.isStartWithSeq(line);
		System.out.println(startWithSeq);
		System.out.println("Drosophila ezoana Takada et Okada, 1958.".length());
		new SpeciesCatalogueController();
		
		
		

////	
	}

}
