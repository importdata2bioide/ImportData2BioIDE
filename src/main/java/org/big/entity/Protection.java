package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 *<p><b>Protection的Entity类</b></p>
 *<p> Protection的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "protection", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Protection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String protlevel;
	private String proassessment;
	private String sourcesid;
	private String refjson;
	private String expert;
	
	private int status;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;

	//bi-directional many-to-one association to Protectstandard
	@ManyToOne
	private Protectstandard protectstandard;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Protection() {
	}
	
	public Protection(String protlevel, String proassessment, String sourcesid, String refjson, String expert, Taxon taxon) {
		super();
		this.protlevel = protlevel;
		this.proassessment = proassessment;
		this.sourcesid = sourcesid;
		this.refjson = refjson;
		this.expert = expert;
		this.taxon = taxon;
	}



	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getProassessment() {
		return this.proassessment;
	}

	public void setProassessment(String proassessment) {
		this.proassessment = proassessment;
	}

	public String getProtlevel() {
		return this.protlevel;
	}

	public void setProtlevel(String protlevel) {
		this.protlevel = protlevel;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
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

	public Protectstandard getProtectstandard() {
		return this.protectstandard;
	}

	public void setProtectstandard(Protectstandard protectstandard) {
		this.protectstandard = protectstandard;
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((proassessment == null) ? 0 : proassessment.hashCode());
		result = prime * result + ((protectstandard == null) ? 0 : protectstandard.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Protection other = (Protection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inputer == null) {
			if (other.inputer != null)
				return false;
		} else if (!inputer.equals(other.inputer))
			return false;
		if (inputtime == null) {
			if (other.inputtime != null)
				return false;
		} else if (!inputtime.equals(other.inputtime))
			return false;
		if (proassessment == null) {
			if (other.proassessment != null)
				return false;
		} else if (!proassessment.equals(other.proassessment))
			return false;
		if (protectstandard == null) {
			if (other.protectstandard != null)
				return false;
		} else if (!protectstandard.equals(other.protectstandard))
			return false;
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
			return false;
		if (status != other.status)
			return false;
		if (synchdate == null) {
			if (other.synchdate != null)
				return false;
		} else if (!synchdate.equals(other.synchdate))
			return false;
		if (synchstatus != other.synchstatus)
			return false;
		if (taxon == null) {
			if (other.taxon != null)
				return false;
		} else if (!taxon.equals(other.taxon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Protection [id=" + id + ", proassessment=" + proassessment + ", refjson="
				+ refjson + ", sourcesid=" + sourcesid + ", protectstandard=" + protectstandard + "]";
	}
	
}