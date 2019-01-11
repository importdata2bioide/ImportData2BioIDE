package org.big.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	private XSSFWorkbook wb = null;
	private XSSFSheet sheet = null;

	/**
	 * @param wb
	 * @param sheet
	 */
	public ExcelUtil(XSSFWorkbook wb, XSSFSheet sheet) {
		this.wb = wb;
		this.sheet = sheet;
	}
	


	/**
	 * 合并单元格后给合并后的单元格加边框
	 * 
	 * @param region
	 * @param cs
	 */
	public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs) {
		int toprowNum = region.getFirstRow();
		for (int i = toprowNum; i <= region.getLastRow(); i++) {
			XSSFRow row = sheet.getRow(i);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				XSSFCell cell = row.getCell(j);// XSSFCellUtil.getCell(row, (short) j);
				cell.setCellStyle(cs);
			}
		}
	}

	/**
	 * 设置表头的单元格样式
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public XSSFCellStyle getHeadStyle() {
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格的背景颜色为淡蓝色
		cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 设置表体的单元格样式
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public XSSFCellStyle getBodyStyle() {
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 
	 * title: ExcelUtil.java
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 * @author ZXY
	 */
	public static List<List<String>> readXlsxOrXls(String filePath,String[] notReadSheetNames) throws IOException {
		if (filePath.contains("~$")) {
			// 临时文件，目的是为了防止文档信息丢失
			// 意外断电，也会造成那些文档不自动消失，也会像正常文件一样始终保存在电脑中
			// 此类文件不处理
			return null;
		} else if (filePath.endsWith(".xlsx")) {
			return readXlsx(filePath,notReadSheetNames);
		} else if (filePath.endsWith(".xls")) {
			return readXls(filePath,notReadSheetNames);
		} else {
			return null;
		}

	}
	
	public static XSSFWorkbook getXlsxWorkBook(String path) throws IOException {
		InputStream input = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		return workbook;
	}
	
	public static HSSFWorkbook getXlsWorkBook(String path) throws IOException {
		InputStream input = new FileInputStream(path);
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		return workbook;
	}

	// 导入excel
	public static List<List<String>> readXlsx(String path,String[] notReadSheetNames) throws IOException {
//		System.out.println(path);
		InputStream input = new FileInputStream(path);
		return readXlsx(input,notReadSheetNames);
	}

	public static List<List<String>> readXls(String path,String[] notReadSheetNames) throws IOException {
		InputStream input = new FileInputStream(path);
		return readXls(input,notReadSheetNames);
	}

	public static List<List<String>> readXlsx(InputStream input,String[] notReadSheetNames) throws IOException {
		List<List<String>> result = new ArrayList<List<String>>();
		/*
		 * HSSFWorkbook workbook = new HSSFWorkbook(input); for (int numSheet = 0;
		 * numSheet < workbook.getNumberOfSheets(); numSheet++) { HSSFSheet sheet =
		 * workbook.getSheetAt(numSheet);
		 */
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = workbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			//notReadSheetNames中的数据跳过，不读取
			for (String notReadSheetName : notReadSheetNames) {
				if(xssfSheet.getSheetName().trim().equals(notReadSheetName)) {
					continue;
				}
			}
//			System.out.println("sheetNum:"+xssfSheet.getSheetName());
			// 从第1行开始读取
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow row = xssfSheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				// 获取在某行第一个单元格的下标
				int minCellNum = row.getFirstCellNum();
				// 获取在某行的列数
				int maxCellNum = row.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int i = minCellNum; i < maxCellNum; i++) {
					XSSFCell cell = row.getCell(i);
					try {
						String oneCell = cell.toString();

						if (CommUtils.isStrNotEmpty(oneCell) && !"*".equals(oneCell.trim())) {
							rowList.add(oneCell.replaceAll("\\s*", " "));//replace special space
						}
					} catch (Exception e) {
//						continue;
//						rowList.add("NULL");
					}

				}
				if (rowList.size() > 0) {
					result.add(rowList);
				}
			}
		}
		
		input.close();
		return result;
	}

	public static List<List<String>> readXls(InputStream input,String[] notReadSheetNames) throws IOException {
		List<List<String>> result = new ArrayList<List<String>>();
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet sheet = workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			//notReadSheetNames中的数据跳过，不读取
			for (String notReadSheetName : notReadSheetNames) {
				if(sheet.getSheetName().trim().equals(notReadSheetName)) {
					continue;
				}
			}
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				HSSFRow row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				int minCellNum = row.getFirstCellNum();
				int maxCellNum = row.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				for (int i = minCellNum; i < maxCellNum; i++) {
					HSSFCell cell = row.getCell(i);
					try {
						String oneCell = getStringVal(cell);
						if (CommUtils.isStrNotEmpty(oneCell) && !"*".equals(oneCell.trim())) {
							rowList.add(oneCell.replaceAll("\\s*", " "));//replace special space
						}
					} catch (Exception e) {
//						continue;
//						rowList.add("NULL");
					}

				}
				if (rowList.size() > 0) {
					result.add(rowList);
				}
			}
		}
		input.close();
		return result;
	}

	@SuppressWarnings("deprecation")
	private static String getStringVal(HSSFCell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}
	
	
	
}