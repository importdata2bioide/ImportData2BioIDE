package org.big.entityVO;

public enum LicenseEnum {
	BYSA("署名 (BY) -相同方式分享 (SA)", 4),;
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private LicenseEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (LicenseEnum c : LicenseEnum.values()) {
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
