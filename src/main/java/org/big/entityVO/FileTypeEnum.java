package org.big.entityVO;

public enum FileTypeEnum {

	Expert("专家", 1), Ref("数据源", 2), DataSource("参考文献", 3), 
	Taxon("基础信息", 4), Citation("引证", 5), Description("描述", 6),
	Commname("俗名", 7), Distribution("分布", 8), 
	Features("特征", 9), Protect("保护", 10), MultiMedia("多媒体", 11);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private FileTypeEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (FileTypeEnum c : FileTypeEnum.values()) {
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
