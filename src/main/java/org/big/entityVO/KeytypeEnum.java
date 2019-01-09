package org.big.entityVO;

public enum KeytypeEnum {
	//1 - 双向式，2 - 单项式
	DoubleTerm("双项式", 1),Monomial("单项式",2);

	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private KeytypeEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (KeytypeEnum c : KeytypeEnum.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
