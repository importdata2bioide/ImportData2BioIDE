package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExcelUntilF implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// fixedIndex int -1 对应excel的列,忽略名字
	@Excel(fixedIndex = 0, width = 40.00, name = "目（中文）")
	private String colA;

	@Excel(fixedIndex = 1, width = 40.00, name = "接受名（拉丁）（引证名称的接受名）")
	private String colB;

	@Excel(fixedIndex = 2, width = 40.00, name = "接受名（中文）（引证名称的接受名）")
	private String colC;

	@Excel(fixedIndex = 3, width = 40.00, name = "引证名称")
	private String colD;
	
	@Excel(fixedIndex = 4, width = 40.00, name = "命名信息")
	private String colE;
	
	@Excel(fixedIndex = 5, width = 40.00, name = "完整引证")
	private String colF;


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
	
	

	
	

}
