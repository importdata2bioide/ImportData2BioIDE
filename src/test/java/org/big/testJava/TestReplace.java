package org.big.testJava;

public class TestReplace {

	public static void main(String[] args) {
//		String authorship = "ssfdf.dsfdsfdf.dfdsfsdf.sfdfswrew.ewe.Unknown.dsfsffdsff.";
//		 authorship = authorship.replace("Unknown.", "");
//		 System.out.println(authorship);
		 String str="分布   大幅:减少使对方电话，地方:(都是),sdhsj,sdhk.sdajsd.sfsdkas;asdhasd；";
		 String trim  = str.replaceAll( ":. " , "、");
//		String line = "分布	大幅减少使对方电话，地方(都是),sdhsj,sdhk.sdajsd.sfsdkas;asdhasd；";
//		line =  line.replaceAll( "[，,.。；;：:]" , "、").trim();
		 System.out.println(trim);

	}

}
