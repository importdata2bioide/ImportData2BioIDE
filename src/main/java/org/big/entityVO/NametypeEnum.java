package org.big.entityVO;

public enum NametypeEnum {
	acceptedName("accepted name", 1), ambiguousSynonym("ambiguous synonym", 2), misappliedName("misapplied name", 3),
	provisionallyAcceptedName("provisionally accepted name", 4), synonym("synonym", 5),
	uncertainName("uncertain name", 6);

	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private NametypeEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (NametypeEnum c : NametypeEnum.values()) {
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
