package org.big.sp2000.entity;

import java.io.Serializable;


/**
 * The persistent class for the references database table.
 * 
 */
public class Reference implements Serializable {
	private static final long serialVersionUID = 1L;

	private String recordId;

	private String author;

	private String authorC;

	private String databaseId;

	private String referenceCode;

	private String source;

	private String sourceC;

	private String title;

	private String titleC;

	private String year;

	public Reference() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorC() {
		return this.authorC;
	}

	public void setAuthorC(String authorC) {
		this.authorC = authorC;
	}

	public String getDatabaseId() {
		return this.databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

	public String getReferenceCode() {
		return this.referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceC() {
		return this.sourceC;
	}

	public void setSourceC(String sourceC) {
		this.sourceC = sourceC;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleC() {
		return this.titleC;
	}

	public void setTitleC(String titleC) {
		this.titleC = titleC;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}