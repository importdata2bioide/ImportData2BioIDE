package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExcelUntilB implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// fixedIndex int -1 对应excel的列,忽略名字
	@Excel(fixedIndex = 0, width = 40.00, name = "A")
	private String colA;

	@Excel(fixedIndex = 1, width = 40.00, name = "B")
	private String colB;

	
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

	

	
	

}
