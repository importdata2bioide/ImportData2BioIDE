package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import java.util.Date;

/**
 *<p><b>Traitproperty的Entity类</b></p>
 *<p> Traitproperty的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="traitproperty", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Traitproperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@GenericGenerator(name="idGenerator", strategy="uuid")
	private String id;
	private String cnterm;
	
	private String enterm;
	@Lob
	private String definition;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	private String sourcesjson;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	public Traitproperty() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnterm() {
		return cnterm;
	}

	public void setCnterm(String cnterm) {
		this.cnterm = cnterm;
	}

	public String getEnterm() {
		return enterm;
	}

	public void setEnterm(String enterm) {
		this.enterm = enterm;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Timestamp inputtime) {
		this.inputtime = inputtime;
	}

	public String getSourcesjson() {
		return sourcesjson;
	}

	public void setSourcesjson(String sourcesjson) {
		this.sourcesjson = sourcesjson;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Timestamp synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnterm == null) ? 0 : cnterm.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((enterm == null) ? 0 : enterm.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((sourcesjson == null) ? 0 : sourcesjson.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
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
		Traitproperty other = (Traitproperty) obj;
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
		return true;
	}

}