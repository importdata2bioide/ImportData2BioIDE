package org.big.entityVO;

public enum SpeciesCatalogueEnum {
	unknown("未知", 0),
	superfamily("总科", 40), 
	family("科", 2), 
	subfamily("亚科", 3),
	genus("属", 4), 
	species("种", 5),
	subspecies("亚种", 11),
	citation("引证", 6),
	distributiondata("分布", 7),
	subtribe("亚族",8),
	tribe("族",9),
	subgenus("亚属", 10);

	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private SpeciesCatalogueEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (SpeciesCatalogueEnum c : SpeciesCatalogueEnum.values()) {
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
