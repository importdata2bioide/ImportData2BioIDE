package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 
 * @author zxy
 *
 */
public class PlantEncyclopediaExcelVO implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	//fixedIndex	int	-1	对应excel的列,忽略名字
	@Excel(fixedIndex = 0, width=40.00, name = "")
	private String colA;
	
	@Excel(fixedIndex = 1, width=40.00, name = "")
	private String colB;
	
	@Excel(fixedIndex = 2, width=40.00, name = "")
	private String colC;
	
	@Excel(fixedIndex = 3, width=40.00, name = "")
	private String colD;
	
	@Excel(fixedIndex = 4, width=40.00, name = "")
	private String colE;
	
	@Excel(fixedIndex = 5, width=40.00, name = "")
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
