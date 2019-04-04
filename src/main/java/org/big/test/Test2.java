package org.big.test;

import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.big.service.ParseLine;
import org.big.service.ParseLineFishWord;

public class Test2 {
	
	private static final String s = "123";
	
	private static final String s2 = "444"+s;
	public static void main(String[] args) {
		ParseLine parseLineFishWord = new ParseLineFishWord();
		parseLineFishWord.initSpeciesRegExlist();
		List<String> list = new ArrayList<>();
		list.add("下盔鲨(黑鳍翅鲨)Hypogaleus hyugaensis (Miyosi, 1939)");
		list.add("灰星鲨Mustelus griseus Pietschmann, 1908");
		list.add("大口尖齿鲨Chaenogaleus macrostoma (Bleeker, 1852)");
		list.add("(1097)长须纹胸Glyptothorax longinema Li");
		list.add("（1）蒲氏黏盲鳗Eptatretus burgeri (Girard, 1855)");
		list.add("陈氏黏盲鳗Eptatretus cheni (Shen & Tao, 1975)");
		int i = 0;
		for (String line : list) {
			i++;
//			System.out.println(i+"__"+list.size());
			boolean species = parseLineFishWord.validateSpecies(line);
			
		}
	}

}
