package org.big.entityVO;

public enum LineAttreEnum {

	Phylum("门", 2),
	Class("纲", 3),
	family("科", 5), 
	genus("属", 6), 
	species("种", 7), 
	var("变种", 31), 
	Forma("变型", 38), 
	subsp("亚种", 42), 
	Distribute("分布", 100),
	chname("中文名",101)
	;
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private LineAttreEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (LineAttreEnum c : LineAttreEnum.values()) {
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
