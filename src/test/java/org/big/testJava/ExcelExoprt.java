package org.big.testJava;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.big.common.FilesUtils;
import org.big.entity.Citation;

public class ExcelExoprt {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		List<Citation> citationlist = new ArrayList<>();
		Citation e = new Citation();
		e.setSciname("123");
		citationlist.add(e );
		FilesUtils.exportExcel(citationlist, "引证", "引证", Citation.class, URLEncoder.encode("Citation.xls", "UTF-8"),null);
	}

}
