package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Traitset的Entity类</b></p>
 *<p> Traitset的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="traitset", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Traitset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private String name;

	@Lob
	private String remark;
	
	private String sourcesid;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	//bi-directional many-to-one association to TraitsetHasTraitontology
	@OneToMany(mappedBy="traitset")
	private List<TraitsetHasTraitontology> traitsetHasTraitontologies;

	public Traitset() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<TraitsetHasTraitontology> getTraitsetHasTraitontologies() {
		return this.traitsetHasTraitontologies;
	}

	public void setTraitsetHasTraitontologies(List<TraitsetHasTraitontology> traitsetHasTraitontologies) {
		this.traitsetHasTraitontologies = traitsetHasTraitontologies;
	}

	public TraitsetHasTraitontology addTraitsetHasTraitontology(TraitsetHasTraitontology traitsetHasTraitontology) {
		getTraitsetHasTraitontologies().add(traitsetHasTraitontology);
		traitsetHasTraitontology.setTraitset(this);

		return traitsetHasTraitontology;
	}

	public TraitsetHasTraitontology removeTraitsetHasTraitontology(TraitsetHasTraitontology traitsetHasTraitontology) {
		getTraitsetHasTraitontologies().remove(traitsetHasTraitontology);
		traitsetHasTraitontology.setTraitset(null);

		return traitsetHasTraitontology;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((traitsetHasTraitontologies == null) ? 0 : traitsetHasTraitontologies.hashCode());
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
		Traitset other = (Traitset) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
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
		if (traitsetHasTraitontologies == null) {
			if (other.traitsetHasTraitontologies != null)
				return false;
		} else if (!traitsetHasTraitontologies.equals(other.traitsetHasTraitontologies))
			return false;
		return true;
	}

}