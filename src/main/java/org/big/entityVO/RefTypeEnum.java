package org.big.entityVO;

public enum RefTypeEnum {
	synonym("异名引证", 1),
	other("其他", 0);
	
	// 成员变量
		private String name;
		private int index;

		// 构造方法
		private RefTypeEnum(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 普通方法
		public static String getName(int index) {
			for (RefTypeEnum c : RefTypeEnum.values()) {
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
