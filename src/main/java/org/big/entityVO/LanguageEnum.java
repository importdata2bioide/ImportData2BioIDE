package org.big.entityVO;

public enum LanguageEnum {
	//'语言 1|中文/2|英文/3|法语/4|俄罗斯/5|西班牙/6|其他'
	chinese("1", 1),English("2",2),French("3",3),Russia("4",4),Spain("5",5),others("6",6);
	
	// 成员变量
		private String name;
		private int index;

		// 构造方法
		private LanguageEnum(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 普通方法
		public static String getName(int index) {
			for (LanguageEnum c : LanguageEnum.values()) {
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
