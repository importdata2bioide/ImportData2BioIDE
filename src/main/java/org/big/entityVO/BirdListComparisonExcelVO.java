package org.big.entityVO;


import cn.afterturn.easypoi.excel.annotation.Excel;

public class BirdListComparisonExcelVO implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Excel(name="科(拉丁)", width=40.00)
	private String familyNameL;
	
	@Excel(name="科(中文)", width=40.00)
	private String familyNameC;
	
	@Excel(name="目(拉丁)", width=40.00)
	private String orderNameL;
	
	@Excel(name="目(中文)", width=40.00)
	private String orderNameC;//注意首字母不要大写
	
	@Excel(name="newGenus", width=40.00)
	private String newGenus;

	@Excel(name="newEpithet", width=40.00)
	private String newEpithet;

	@Excel(name="chineseName", width=40.00)
	private String chineseName;

	@Excel(name="oldGenus", width=40.00)
	private String oldGenus;

	@Excel(name="oldEpithet", width=40.00)
	private String oldEpithet;

	@Excel(name="remark", width=40.00)
	private String remark;

	public String getNewGenus() {
		return newGenus;
	}

	public void setNewGenus(String newGenus) {
		this.newGenus = newGenus;
	}

	public String getNewEpithet() {
		return newEpithet;
	}

	public void setNewEpithet(String newEpithet) {
		this.newEpithet = newEpithet;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getOldGenus() {
		return oldGenus;
	}

	public void setOldGenus(String oldGenus) {
		this.oldGenus = oldGenus;
	}

	public String getOldEpithet() {
		return oldEpithet;
	}

	public void setOldEpithet(String oldEpithet) {
		this.oldEpithet = oldEpithet;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFamilyNameL() {
		return familyNameL;
	}

	public void setFamilyNameL(String familyNameL) {
		this.familyNameL = familyNameL;
	}

	public String getFamilyNameC() {
		return familyNameC;
	}

	public void setFamilyNameC(String familyNameC) {
		this.familyNameC = familyNameC;
	}

	public String getOrderNameL() {
		return orderNameL;
	}

	public void setOrderNameL(String orderNameL) {
		this.orderNameL = orderNameL;
	}

	public String getOrderNameC() {
		return orderNameC;
	}

	public void setOrderNameC(String orderNameC) {
		this.orderNameC = orderNameC;
	}
	
	
	

}
