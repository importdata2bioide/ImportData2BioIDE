package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 *<p><b>Citation的Entity类</b></p>
 *<p> Citation的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "citation", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Citation implements Serializable {

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = 1469505469318276985L;
	@Id
	private String id;
	@Excel(name="* 引证名称", width=40.00)
	private String sciname;
	@Excel(name="* 命名信息", width=40.00)
	private String authorship;
	@Excel(name="* 名称状态(类型)", width=40.00, replace = {
			"  _0", 
			"accepted name_1", 
			"ambiguous synonym_2", 
			"misapplied name_3", 
			"provisionally accepted name_4", 
			"synonym_5", 
			"uncertain name_6"})
	private int nametype;
	@Excel(name="* 完整引证", width=40.00)
	private String citationstr;
	
	
	private String sourcesidId;
	
	@Excel(name="* 接受名（引证名称的接受名）", width=40.00)
	@Transient
	private String taxonName;
	
	@Excel(name="科(拉丁)", width=40.00)
	@Transient
	private String familyNameL;
	
	@Excel(name="科(中文)", width=40.00)
	@Transient
	private String familyNameC;
	
	@Excel(name="目(拉丁)", width=40.00)
	@Transient
	private String orderNameL;
	
	@Excel(name="目(中文)", width=40.00)
	@Transient
	private String orderNameC;//注意首字母不要大写
	
	@Excel(name="备注", width=40.00)
	private String remark;
	
	
	@Excel(name="*参考文献")
	private String refjson;
	
//	@Excel(name="*数据源")
	private String sourcesid;
	
//	@Excel(name="*审核专家")
	private String expert;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	

	private String shortrefs;
	
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Citation() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorship() {
		return this.authorship;
	}
	

	public String getSourcesidId() {
		return sourcesidId;
	}

	public void setSourcesidId(String sourcesidId) {
		this.sourcesidId = sourcesidId;
	}

	public void setAuthorship(String authorship) {
		if(StringUtils.isNotEmpty(authorship)) {
			authorship = authorship.trim();
		}
		this.authorship = authorship;
	}

	public String getCitationstr() {
		return this.citationstr;
	}

	public void setCitationstr(String citationstr) {
		if(StringUtils.isNotEmpty(citationstr)) {
			citationstr = citationstr.trim();
		}
		this.citationstr = citationstr;
	}

	public String getInputer() {
		return this.inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Timestamp inputtime) {
		this.inputtime = inputtime;
	}

	public int getNametype() {
		return this.nametype;
	}

	public void setNametype(int nametype) {
		this.nametype = nametype;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getSciname() {
		return this.sciname;
	}

	public void setSciname(String sciname) {
		if(StringUtils.isNotEmpty(sciname)) { 
			sciname = sciname.trim();
		}
		this.sciname = sciname;
	}

	public String getShortrefs() {
		return this.shortrefs;
	}

	public void setShortrefs(String shortrefs) {
		this.shortrefs = shortrefs;
	}

	public String getSourcesid() {
		return this.sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return this.synchdate;
	}

	public void setSynchdate(Timestamp synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return this.synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	

	public String getTaxonName() {
		return taxonName;
	}

	public void setTaxonName(String taxonName) {
		this.taxonName = taxonName;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	

}