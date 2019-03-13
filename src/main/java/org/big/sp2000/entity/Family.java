package org.big.sp2000.entity;

import java.io.Serializable;


/**
 * The persistent class for the families database table.
 * 
 */

public class Family implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String recordId;

	private String class_;

	private String classC;

	private String classCPy;

	private String databaseId;

	private String databaseName;

	private String family;

	private String familyC;

	private String familyCPy;

	private String familyCode;

	private String familyCommonName;

	private String hierarchyCode;

	private int isAcceptedName;

	private String kingdom;

	private String kingdomC;

	private String kingdomCPy;

	private String order;

	private String orderC;

	private String orderCPy;

	private String phylum;

	private String phylumC;

	private String phylumCPy;

	private String superfamily;

	private String superfamilyC;

	private String superfamilyCPy;

	public Family() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public String getClassC() {
		return this.classC;
	}

	public void setClassC(String classC) {
		this.classC = classC;
	}

	public String getClassCPy() {
		return this.classCPy;
	}

	public void setClassCPy(String classCPy) {
		this.classCPy = classCPy;
	}

	public String getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getFamily() {
		return this.family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFamilyC() {
		return this.familyC;
	}

	public void setFamilyC(String familyC) {
		this.familyC = familyC;
	}

	public String getFamilyCPy() {
		return this.familyCPy;
	}

	public void setFamilyCPy(String familyCPy) {
		this.familyCPy = familyCPy;
	}

	public String getFamilyCode() {
		return this.familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	public String getFamilyCommonName() {
		return this.familyCommonName;
	}

	public void setFamilyCommonName(String familyCommonName) {
		this.familyCommonName = familyCommonName;
	}

	public String getHierarchyCode() {
		return this.hierarchyCode;
	}

	public void setHierarchyCode(String hierarchyCode) {
		this.hierarchyCode = hierarchyCode;
	}

	public int getIsAcceptedName() {
		return this.isAcceptedName;
	}

	public void setIsAcceptedName(int isAcceptedName) {
		this.isAcceptedName = isAcceptedName;
	}

	public String getKingdom() {
		return this.kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getKingdomC() {
		return this.kingdomC;
	}

	public void setKingdomC(String kingdomC) {
		this.kingdomC = kingdomC;
	}

	public String getKingdomCPy() {
		return this.kingdomCPy;
	}

	public void setKingdomCPy(String kingdomCPy) {
		this.kingdomCPy = kingdomCPy;
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderC() {
		return this.orderC;
	}

	public void setOrderC(String orderC) {
		this.orderC = orderC;
	}

	public String getOrderCPy() {
		return this.orderCPy;
	}

	public void setOrderCPy(String orderCPy) {
		this.orderCPy = orderCPy;
	}

	public String getPhylum() {
		return this.phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String getPhylumC() {
		return this.phylumC;
	}

	public void setPhylumC(String phylumC) {
		this.phylumC = phylumC;
	}

	public String getPhylumCPy() {
		return this.phylumCPy;
	}

	public void setPhylumCPy(String phylumCPy) {
		this.phylumCPy = phylumCPy;
	}

	public String getSuperfamily() {
		return this.superfamily;
	}

	public void setSuperfamily(String superfamily) {
		this.superfamily = superfamily;
	}

	public String getSuperfamilyC() {
		return this.superfamilyC;
	}

	public void setSuperfamilyC(String superfamilyC) {
		this.superfamilyC = superfamilyC;
	}

	public String getSuperfamilyCPy() {
		return this.superfamilyCPy;
	}

	public void setSuperfamilyCPy(String superfamilyCPy) {
		this.superfamilyCPy = superfamilyCPy;
	}

}