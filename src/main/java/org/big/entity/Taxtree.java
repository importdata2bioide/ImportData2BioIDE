package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxtree的Entity类</b></p>
 *<p> Taxtree的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="taxtree", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Taxtree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	@Type(type = "StringJsonUserType")
	private String refsjson;

	@Type(type = "StringJsonUserType")
	private String sourcejson;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String taxtreecol;

	private String treeinfo;

	private String treename;

	//bi-directional many-to-many association to Taxon
	@ManyToMany
	@JoinTable(
		name="taxon_has_taxtree"
		, joinColumns={
			@JoinColumn(name="taxtree_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="taxon_id")
			}
		)
	private List<Taxon> taxons;

	//bi-directional many-to-one association to Dataset
	@ManyToOne
	private Dataset dataset;

	public Taxtree() {
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

	public String getRefsjson() {
		return this.refsjson;
	}

	public void setRefsjson(String refsjson) {
		this.refsjson = refsjson;
	}

	public String getSourcejson() {
		return this.sourcejson;
	}

	public void setSourcejson(String sourcejson) {
		this.sourcejson = sourcejson;
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

	public String getTaxtreecol() {
		return this.taxtreecol;
	}

	public void setTaxtreecol(String taxtreecol) {
		this.taxtreecol = taxtreecol;
	}

	public String getTreeinfo() {
		return this.treeinfo;
	}

	public void setTreeinfo(String treeinfo) {
		this.treeinfo = treeinfo;
	}

	public String getTreename() {
		return this.treename;
	}

	public void setTreename(String treename) {
		this.treename = treename;
	}

	public List<Taxon> getTaxons() {
		return this.taxons;
	}

	public void setTaxons(List<Taxon> taxons) {
		this.taxons = taxons;
	}

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((refsjson == null) ? 0 : refsjson.hashCode());
		result = prime * result + ((sourcejson == null) ? 0 : sourcejson.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxons == null) ? 0 : taxons.hashCode());
		result = prime * result + ((taxtreecol == null) ? 0 : taxtreecol.hashCode());
		result = prime * result + ((treeinfo == null) ? 0 : treeinfo.hashCode());
		result = prime * result + ((treename == null) ? 0 : treename.hashCode());
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
		Taxtree other = (Taxtree) obj;
		if (dataset == null) {
			if (other.dataset != null)
				return false;
		} else if (!dataset.equals(other.dataset))
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
		if (refsjson == null) {
			if (other.refsjson != null)
				return false;
		} else if (!refsjson.equals(other.refsjson))
			return false;
		if (sourcejson == null) {
			if (other.sourcejson != null)
				return false;
		} else if (!sourcejson.equals(other.sourcejson))
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
		if (taxons == null) {
			if (other.taxons != null)
				return false;
		} else if (!taxons.equals(other.taxons))
			return false;
		if (taxtreecol == null) {
			if (other.taxtreecol != null)
				return false;
		} else if (!taxtreecol.equals(other.taxtreecol))
			return false;
		if (treeinfo == null) {
			if (other.treeinfo != null)
				return false;
		} else if (!treeinfo.equals(other.treeinfo))
			return false;
		if (treename == null) {
			if (other.treename != null)
				return false;
		} else if (!treename.equals(other.treename))
			return false;
		return true;
	}

}