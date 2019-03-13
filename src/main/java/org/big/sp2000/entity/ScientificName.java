package org.big.sp2000.entity;

import java.io.Serializable;


/**
 * The persistent class for the scientific_names database table.
 * 
 */
public class ScientificName implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nameCode;

	private String acceptedNameCode;

	private String author;

	private String author2;

	private String canonicalName;

	private String comment;

	private String comments;

	private String databaseId;

	private String familyCode;

	private String familyId;

	private String genus;

	private String genusC;

	private String genusCPy;

	private String genuswithsubgenus;

	private String infraspecies;

	private String infraspeciesC;

	private String infraspeciesCPy;

	private String infraspeciesCPy2;

	private String infraspeciesC2;

	private String infraspeciesMarker;

	private String infraspeciesMarker2;

	private String infraspecies2;

	private int isAcceptedName;

	private String scrutinyDate;

	private int sp2000StatusId;

	private String specialistCode;

	private String specialistId;

	private String species;

	private String speciesC;

	private String speciesCPy;

	private String webSite;

	public ScientificName() {
	}

	public String getNameCode() {
		return this.nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public String getAcceptedNameCode() {
		return this.acceptedNameCode;
	}

	public void setAcceptedNameCode(String acceptedNameCode) {
		this.acceptedNameCode = acceptedNameCode;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor2() {
		return this.author2;
	}

	public void setAuthor2(String author2) {
		this.author2 = author2;
	}

	public String getCanonicalName() {
		return this.canonicalName;
	}

	public void setCanonicalName(String canonicalName) {
		this.canonicalName = canonicalName;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getFamilyCode() {
		return this.familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	public String getFamilyId() {
		return this.familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getGenus() {
		return this.genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getGenusC() {
		return this.genusC;
	}

	public void setGenusC(String genusC) {
		this.genusC = genusC;
	}

	public String getGenusCPy() {
		return this.genusCPy;
	}

	public void setGenusCPy(String genusCPy) {
		this.genusCPy = genusCPy;
	}

	public String getGenuswithsubgenus() {
		return this.genuswithsubgenus;
	}

	public void setGenuswithsubgenus(String genuswithsubgenus) {
		this.genuswithsubgenus = genuswithsubgenus;
	}

	public String getInfraspecies() {
		return this.infraspecies;
	}

	public void setInfraspecies(String infraspecies) {
		this.infraspecies = infraspecies;
	}

	public String getInfraspeciesC() {
		return this.infraspeciesC;
	}

	public void setInfraspeciesC(String infraspeciesC) {
		this.infraspeciesC = infraspeciesC;
	}

	public String getInfraspeciesCPy() {
		return this.infraspeciesCPy;
	}

	public void setInfraspeciesCPy(String infraspeciesCPy) {
		this.infraspeciesCPy = infraspeciesCPy;
	}

	public String getInfraspeciesCPy2() {
		return this.infraspeciesCPy2;
	}

	public void setInfraspeciesCPy2(String infraspeciesCPy2) {
		this.infraspeciesCPy2 = infraspeciesCPy2;
	}

	public String getInfraspeciesC2() {
		return this.infraspeciesC2;
	}

	public void setInfraspeciesC2(String infraspeciesC2) {
		this.infraspeciesC2 = infraspeciesC2;
	}

	public String getInfraspeciesMarker() {
		return this.infraspeciesMarker;
	}

	public void setInfraspeciesMarker(String infraspeciesMarker) {
		this.infraspeciesMarker = infraspeciesMarker;
	}

	public String getInfraspeciesMarker2() {
		return this.infraspeciesMarker2;
	}

	public void setInfraspeciesMarker2(String infraspeciesMarker2) {
		this.infraspeciesMarker2 = infraspeciesMarker2;
	}

	public String getInfraspecies2() {
		return this.infraspecies2;
	}

	public void setInfraspecies2(String infraspecies2) {
		this.infraspecies2 = infraspecies2;
	}

	public int getIsAcceptedName() {
		return this.isAcceptedName;
	}

	public void setIsAcceptedName(int isAcceptedName) {
		this.isAcceptedName = isAcceptedName;
	}

	public String getScrutinyDate() {
		return this.scrutinyDate;
	}

	public void setScrutinyDate(String scrutinyDate) {
		this.scrutinyDate = scrutinyDate;
	}

	public int getSp2000StatusId() {
		return this.sp2000StatusId;
	}

	public void setSp2000StatusId(int sp2000StatusId) {
		this.sp2000StatusId = sp2000StatusId;
	}

	public String getSpecialistCode() {
		return this.specialistCode;
	}

	public void setSpecialistCode(String specialistCode) {
		this.specialistCode = specialistCode;
	}

	public String getSpecialistId() {
		return this.specialistId;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
	}

	public String getSpecies() {
		return this.species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSpeciesC() {
		return this.speciesC;
	}

	public void setSpeciesC(String speciesC) {
		this.speciesC = speciesC;
	}

	public String getSpeciesCPy() {
		return this.speciesCPy;
	}

	public void setSpeciesCPy(String speciesCPy) {
		this.speciesCPy = speciesCPy;
	}

	public String getWebSite() {
		return this.webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

}