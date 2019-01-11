package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;
/**
 * 
 * @Description 国家保护动物名录导出excel模板
 * @author ZXY
 */
public class NationalListOfProtectedAnimalsVO implements java.io.Serializable{
	
	private static final long serialVersionUID = 4313298744831622702L;
		@Excel(width = 40.00, name = "纲中文名")
		private String colA;

		@Excel( width = 40.00, name = "纲拉丁名")
		private String colB;

		@Excel(width = 40.00, name = "目中文名")
		private String colC;

		@Excel( width = 40.00, name = "目拉丁名")
		private String colD;
		
		@Excel(width = 40.00, name = "科中文名")
		private String colE;

		@Excel( width = 40.00, name = "科拉丁名")
		private String colF;

		@Excel(width = 40.00, name = "种中文名")
		private String colG;

		@Excel( width = 40.00, name = "种拉丁名")
		private String colH;
		
		@Excel( width = 40.00, name = "国家保护等级")
		private String colI;

		public String getColA() {
			return colA;
		}

		public void setColA(String colA) {
			this.colA = colA;
		}

		public String getColB() {
			return colB;
		}

		public void setColB(String colB) {
			this.colB = colB;
		}

		public String getColC() {
			return colC;
		}

		public void setColC(String colC) {
			this.colC = colC;
		}

		public String getColD() {
			return colD;
		}

		public void setColD(String colD) {
			this.colD = colD;
		}

		public String getColE() {
			return colE;
		}

		public void setColE(String colE) {
			this.colE = colE;
		}

		public String getColF() {
			return colF;
		}

		public void setColF(String colF) {
			this.colF = colF;
		}

		public String getColG() {
			return colG;
		}

		public void setColG(String colG) {
			this.colG = colG;
		}

		public String getColH() {
			return colH;
		}

		public void setColH(String colH) {
			this.colH = colH;
		}

		public String getColI() {
			return colI;
		}

		public void setColI(String colI) {
			this.colI = colI;
		}
		
		

}
