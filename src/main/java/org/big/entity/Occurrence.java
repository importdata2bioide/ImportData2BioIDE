package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;


/**
 *<p><b>Occurrence的Entity类</b></p>
 *<p> Occurrence的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "occurrence", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Occurrence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String city;

	private String country;

	private String county;

	private String eventdate;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private double lat;

	private double lng;

	private String locality;

	private String location;

	private String province;

	private String refjson;

	private String sourcesid;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	@Column(name="taxon_rank_id")
	private String taxonRankId;

	@Column(name="taxon_taxaset_id")
	private String taxonTaxasetId;

	private String taxonid;

	private String type;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;
	
	private String title;

	public Occurrence() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getEventdate() {
		return this.eventdate;
	}

	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}
	
	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return this.lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return this.synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getTaxonRankId() {
		return taxonRankId;
	}

	public void setTaxonRankId(String taxonRankId) {
		this.taxonRankId = taxonRankId;
	}

	public String getTaxonTaxasetId() {
		return this.taxonTaxasetId;
	}

	public void setTaxonTaxasetId(String taxonTaxasetId) {
		this.taxonTaxasetId = taxonTaxasetId;
	}

	public String getTaxonid() {
		return this.taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + ((eventdate == null) ? 0 : eventdate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((locality == null) ? 0 : locality.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		result = prime * result + ((taxonRankId == null) ? 0 : taxonRankId.hashCode());
		result = prime * result + ((taxonTaxasetId == null) ? 0 : taxonTaxasetId.hashCode());
		result = prime * result + ((taxonid == null) ? 0 : taxonid.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Occurrence other = (Occurrence) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (eventdate == null) {
			if (other.eventdate != null)
				return false;
		} else if (!eventdate.equals(other.eventdate))
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
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		if (locality == null) {
			if (other.locality != null)
				return false;
		} else if (!locality.equals(other.locality))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
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
		if (taxonRankId == null) {
			if (other.taxonRankId != null)
				return false;
		} else if (!taxonRankId.equals(other.taxonRankId))
			return false;
		if (taxonTaxasetId == null) {
			if (other.taxonTaxasetId != null)
				return false;
		} else if (!taxonTaxasetId.equals(other.taxonTaxasetId))
			return false;
		if (taxonid == null) {
			if (other.taxonid != null)
				return false;
		} else if (!taxonid.equals(other.taxonid))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Occurrence [id=" + id + ", refjson=" + refjson + ", sourcesid=" + sourcesid + ", title=" + title + "]";
	}

}