package org.big.sp2000.entity;

import java.io.Serializable;


/**
 * The persistent class for the distribution database table.
 * 
 */
public class Distribution implements Serializable {
	private static final long serialVersionUID = 1L;

	private String recordId;

	private String distribution;

	private String distributionBack;

	private String distributionC;

	private String level4;

	private String nameCode;

	private String sea;

	public Distribution() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getDistribution() {
		return this.distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getDistributionBack() {
		return this.distributionBack;
	}

	public void setDistributionBack(String distributionBack) {
		this.distributionBack = distributionBack;
	}

	public String getDistributionC() {
		return this.distributionC;
	}

	public void setDistributionC(String distributionC) {
		this.distributionC = distributionC;
	}

	public String getLevel4() {
		return this.level4;
	}

	public void setLevel4(String level4) {
		this.level4 = level4;
	}

	public String getNameCode() {
		return this.nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public String getSea() {
		return this.sea;
	}

	public void setSea(String sea) {
		this.sea = sea;
	}

}