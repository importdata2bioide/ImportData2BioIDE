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
 *<p><b>Taxaset的Entity类</b></p>
 *<p> Taxaset的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "taxaset", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Taxaset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Type(type = "StringJsonUserType")
	private String refsjson;

	@Type(type = "StringJsonUserType")
	private String sourcejson;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String tsinfo;

	private String tsname;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	private String creator;
	//bi-directional many-to-one association to Dataset
	@ManyToOne
	private Dataset dataset;

	//bi-directional many-to-one association to Taxon
	@OneToMany(mappedBy="taxaset")
	private List<Taxon> taxons;

	public Taxaset() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTsinfo() {
		return this.tsinfo;
	}

	public void setTsinfo(String tsinfo) {
		this.tsinfo = tsinfo;
	}

	public String getTsname() {
		return this.tsname;
	}

	public void setTsname(String tsname) {
		this.tsname = tsname;
	}

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public List<Taxon> getTaxons() {
		return this.taxons;
	}

	public void setTaxons(List<Taxon> taxons) {
		this.taxons = taxons;
	}

	public Taxon addTaxon(Taxon taxon) {
		getTaxons().add(taxon);
		taxon.setTaxaset(this);

		return taxon;
	}

	public Taxon removeTaxon(Taxon taxon) {
		getTaxons().remove(taxon);
		taxon.setTaxaset(null);

		return taxon;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((refsjson == null) ? 0 : refsjson.hashCode());
		result = prime * result + ((sourcejson == null) ? 0 : sourcejson.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxons == null) ? 0 : taxons.hashCode());
		result = prime * result + ((tsinfo == null) ? 0 : tsinfo.hashCode());
		result = prime * result + ((tsname == null) ? 0 : tsname.hashCode());
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
		Taxaset other = (Taxaset) obj;
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
		if (tsinfo == null) {
			if (other.tsinfo != null)
				return false;
		} else if (!tsinfo.equals(other.tsinfo))
			return false;
		if (tsname == null) {
			if (other.tsname != null)
				return false;
		} else if (!tsname.equals(other.tsname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Taxaset [id=" + id + ", refsjson=" + refsjson + ", tsname=" + tsname + "]";
	}

}