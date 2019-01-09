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
 *<p><b>Traitdata的Entity类</b></p>
 *<p> Traitdata的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Table(name="traitdata", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Traitdata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String traitjson;	// 术语、属性、属性值、单位、测量依据
	private String refjson;
	private String sourcesId;
	private String expert;

	private String desid;
	private int status;
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String taxonid;

	private String trainsetid;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Traitdata() {
	}
	
	public Traitdata(String traitjson, String refjson, String sourcesId, String expert, Taxon taxon) {
		super();
		this.traitjson = traitjson;
		this.refjson = refjson;
		this.sourcesId = sourcesId;
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

	public String getDesid() {
		return this.desid;
	}

	public void setDesid(String desid) {
		this.desid = desid;
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

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getSourcesId() {
		return this.sourcesId;
	}

	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
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

	public String getTaxonid() {
		return this.taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}

	public String getTrainsetid() {
		return this.trainsetid;
	}

	public void setTrainsetid(String trainsetid) {
		this.trainsetid = trainsetid;
	}

	public String getTraitjson() {
		return this.traitjson;
	}

	public void setTraitjson(String traitjson) {
		this.traitjson = traitjson;
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
		result = prime * result + ((desid == null) ? 0 : desid.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sourcesId == null) ? 0 : sourcesId.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		result = prime * result + ((taxonid == null) ? 0 : taxonid.hashCode());
		result = prime * result + ((trainsetid == null) ? 0 : trainsetid.hashCode());
		result = prime * result + ((traitjson == null) ? 0 : traitjson.hashCode());
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
		Traitdata other = (Traitdata) obj;
		if (desid == null) {
			if (other.desid != null)
				return false;
		} else if (!desid.equals(other.desid))
			return false;
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
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (sourcesId == null) {
			if (other.sourcesId != null)
				return false;
		} else if (!sourcesId.equals(other.sourcesId))
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
		if (taxonid == null) {
			if (other.taxonid != null)
				return false;
		} else if (!taxonid.equals(other.taxonid))
			return false;
		if (trainsetid == null) {
			if (other.trainsetid != null)
				return false;
		} else if (!trainsetid.equals(other.trainsetid))
			return false;
		if (traitjson == null) {
			if (other.traitjson != null)
				return false;
		} else if (!traitjson.equals(other.traitjson))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Traitdata [id=" + id + ", desid=" + desid + ", inputer=" + inputer + ", inputtime=" + inputtime
				+ ", refjson=" + refjson + ", sourcesId=" + sourcesId + ", status=" + status + ", synchdate="
				+ synchdate + ", synchstatus=" + synchstatus + ", taxonid=" + taxonid + ", trainsetid=" + trainsetid
				+ ", traitjson=" + traitjson + "]";
	}

}