package org.big.testJava;

import org.big.entityVO.NametypeEnum;

public class TestEnum {
	
	public static void main(String[] args) {
		String name = NametypeEnum.getName(3);
		System.out.println(NametypeEnum.misappliedName.getIndex()+"||"+name);
	}

}
