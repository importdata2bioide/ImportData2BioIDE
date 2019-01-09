package org.big.entityVO;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class AmphibiansAndReptilesExcelVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Excel(name="分布地")
	private String province;
	
	@Excel(name="科中文+拉丁")
	private String familyInfo;
	
	@Excel(name="属中文")
	private String genus;
	
	@Excel(name="种中文")
	private String speciesChinese;
	
	@Excel(name="种拉丁")
	private String speciesEn;
	
	@Excel(name="鉴别特征")
	private String desc;
	
	@Excel(name="形态特征")
	private String bodyLength;
	
	@Excel(name="生物学信息")
	private String habitat;
	
	@Excel(name="地理分布")
	private String distribution;
	
	@Excel(name="模式标本")
	private String typeSpecimen;
	
	@Excel(name="模式产地")
	private String typeLocality;
	
	@Excel(name="图片位置")
	private String imagePath;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getFamilyInfo() {
		return familyInfo;
	}

	public void setFamilyInfo(String familyInfo) {
		this.familyInfo = familyInfo;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getSpeciesChinese() {
		return speciesChinese;
	}

	public void setSpeciesChinese(String speciesChinese) {
		this.speciesChinese = speciesChinese;
	}

	public String getSpeciesEn() {
		return speciesEn;
	}

	public void setSpeciesEn(String speciesEn) {
		this.speciesEn = speciesEn;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(String bodyLength) {
		this.bodyLength = bodyLength;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getTypeSpecimen() {
		return typeSpecimen;
	}

	public void setTypeSpecimen(String typeSpecimen) {
		this.typeSpecimen = typeSpecimen;
	}

	public String getTypeLocality() {
		return typeLocality;
	}

	public void setTypeLocality(String typeLocality) {
		this.typeLocality = typeLocality;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	

}
