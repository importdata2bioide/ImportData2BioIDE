package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExcelVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Excel(name="id")
	private String id;
	
	@Excel(name="chname")
	private String chname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChname() {
		return chname;
	}
	public void setChname(String chname) {
		this.chname = chname;
	}
	public ExcelVO(String id, String chname) {
		super();
		this.id = id;
		this.chname = chname;
	}
	public ExcelVO() {
		super();
	}
	

}
