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

	private String groups;

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

	

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
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

	
}