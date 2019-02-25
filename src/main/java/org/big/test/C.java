package org.big.test;

public class C {

	public static void main(String[] args) {
		String s = "不能（收到就好）snasdss石家庄";
		for (int index = 0; index <= s.length() - 1; index++) {
			// 将字符串拆开成单个的字符
			String w = s.substring(index, index + 1);
			if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
				System.out.println("第一个中文的索引位置:" + index + ",值是：" + w);
				
			}else {
				break;
			}
		}

	}

}
