package org.big.testJava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTimeAdd {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =df.parse("2017-09-18 23:59:58");
		date.setTime(date.getTime() + 602000);
		System.out.println("当前时间      ："+df.format(date));

	}

}
