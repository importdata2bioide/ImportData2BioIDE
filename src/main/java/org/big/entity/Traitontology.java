package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Traitontology的Entity类</b></p>
 *<p> Traitontology的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="traitontology", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Traitontology implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	private String id;

	private String catalog1;

	private String catalog2;

	private String catalog3;

	private String cnterm;

	@Lob
	private String definition;

	private String enterm;

	private String group;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	private String sourcesjson;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String synonymys;

	//bi-directional many-to-one association to TraitsetHasTraitontology
	@OneToMany(mappedBy="traitontology")
	private List<TraitsetHasTraitontology> traitsetHasTraitontologies;

	public Traitontology() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatalog1() {
		return this.catalog1;
	}

	public void setCatalog1(String catalog1) {
		this.catalog1 = catalog1;
	}

	public String getCatalog2() {
		return this.catalog2;
	}

	public void setCatalog2(String catalog2) {
		this.catalog2 = catalog2;
	}

	public String getCatalog3() {
		return this.catalog3;
	}

	public void setCatalog3(String catalog3) {
		this.catalog3 = catalog3;
	}

	public String getCnterm() {
		return this.cnterm;
	}

	public void setCnterm(String cnterm) {
		this.cnterm = cnterm;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getEnterm() {
		return this.enterm;
	}

	public void setEnterm(String enterm) {
		this.enterm = enterm;
	}

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
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

	public String getSourcesjson() {
		return this.sourcesjson;
	}

	public void setSourcesjson(String sourcesjson) {
		this.sourcesjson = sourcesjson;
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

	public String getSynonymys() {
		return this.synonymys;
	}

	public void setSynonymys(String synonymys) {
		this.synonymys = synonymys;
	}

	public List<TraitsetHasTraitontology> getTraitsetHasTraitontologies() {
		return this.traitsetHasTraitontologies;
	}

	public void setTraitsetHasTraitontologies(List<TraitsetHasTraitontology> traitsetHasTraitontologies) {
		this.traitsetHasTraitontologies = traitsetHasTraitontologies;
	}

	public TraitsetHasTraitontology addTraitsetHasTraitontology(TraitsetHasTraitontology traitsetHasTraitontology) {
		getTraitsetHasTraitontologies().add(traitsetHasTraitontology);
		traitsetHasTraitontology.setTraitontology(this);

		return traitsetHasTraitontology;
	}

	public TraitsetHasTraitontology removeTraitsetHasTraitontology(TraitsetHasTraitontology traitsetHasTraitontology) {
		getTraitsetHasTraitontologies().remove(traitsetHasTraitontology);
		traitsetHasTraitontology.setTraitontology(null);

		return traitsetHasTraitontology;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((catalog1 == null) ? 0 : catalog1.hashCode());
		result = prime * result + ((catalog2 == null) ? 0 : catalog2.hashCode());
		result = prime * result + ((catalog3 == null) ? 0 : catalog3.hashCode());
		result = prime * result + ((cnterm == null) ? 0 : cnterm.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((enterm == null) ? 0 : enterm.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((sourcesjson == null) ? 0 : sourcesjson.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((synonymys == null) ? 0 : synonymys.hashCode());
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
		Traitontology other = (Traitontology) obj;
		if (catalog1 == null) {
			if (other.catalog1 != null)
				return false;
		} else if (!catalog1.equals(other.catalog1))
			return false;
		if (catalog2 == null) {
			if (other.catalog2 != null)
				return false;
		} else if (!catalog2.equals(other.catalog2))
			return false;
		if (catalog3 == null) {
			if (other.catalog3 != null)
				return false;
		} else if (!catalog3.equals(other.catalog3))
			return false;
		if (cnterm == null) {
			if (other.cnterm != null)
				return false;
		} else if (!cnterm.equals(other.cnterm))
			return false;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (enterm == null) {
			if (other.enterm != null)
				return false;
		} else if (!enterm.equals(other.enterm))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
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
		if (sourcesjson == null) {
			if (other.sourcesjson != null)
				return false;
		} else if (!sourcesjson.equals(other.sourcesjson))
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
		if (synonymys == null) {
			if (other.synonymys != null)
				return false;
		} else if (!synonymys.equals(other.synonymys))
			return false;
		if (traitsetHasTraitontologies == null) {
			if (other.traitsetHasTraitontologies != null)
				return false;
		} else if (!traitsetHasTraitontologies.equals(other.traitsetHasTraitontologies))
			return false;
		return true;
	}

}