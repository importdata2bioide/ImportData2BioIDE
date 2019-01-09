package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.Date;

/**
 *<p><b>Molecular的Entity类</b></p>
 *<p> Molecular的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "molecular", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Molecular implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String authorinstitution;

	private String authorname;

	private String city;

	private String country;

	private String county;

	private String fastaurl;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private double lat;

	private double lng;

	private String locality;

	private String location;

	private String ncbiid;
	@Type(type = "StringJsonUserType")
	private String otherinfo;

	private String province;

	@Type(type = "StringJsonUserType")
	private String refjson;

	@Lob
	private String sequence;

	@Type(type = "StringJsonUserType")
	private String sourcesjson;

	@Column(name="spe_id")
	private String speId;

	@Column(name="spe_location")
	private String speLocation;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String title;

	private String type;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	private Taxon taxon;

	public Molecular() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorinstitution() {
		return this.authorinstitution;
	}

	public void setAuthorinstitution(String authorinstitution) {
		this.authorinstitution = authorinstitution;
	}

	public String getAuthorname() {
		return this.authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
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

	public String getFastaurl() {
		return this.fastaurl;
	}

	public void setFastaurl(String fastaurl) {
		this.fastaurl = fastaurl;
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

	public String getNcbiid() {
		return this.ncbiid;
	}

	public void setNcbiid(String ncbiid) {
		this.ncbiid = ncbiid;
	}

	public String getOtherinfo() {
		return this.otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
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

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSourcesjson() {
		return this.sourcesjson;
	}

	public void setSourcesjson(String sourcesjson) {
		this.sourcesjson = sourcesjson;
	}

	public String getSpeId() {
		return this.speId;
	}

	public void setSpeId(String speId) {
		this.speId = speId;
	}

	public String getSpeLocation() {
		return this.speLocation;
	}

	public void setSpeLocation(String speLocation) {
		this.speLocation = speLocation;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		result = prime * result + ((authorinstitution == null) ? 0 : authorinstitution.hashCode());
		result = prime * result + ((authorname == null) ? 0 : authorname.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + ((fastaurl == null) ? 0 : fastaurl.hashCode());
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
		result = prime * result + ((ncbiid == null) ? 0 : ncbiid.hashCode());
		result = prime * result + ((otherinfo == null) ? 0 : otherinfo.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((sourcesjson == null) ? 0 : sourcesjson.hashCode());
		result = prime * result + ((speId == null) ? 0 : speId.hashCode());
		result = prime * result + ((speLocation == null) ? 0 : speLocation.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
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
		Molecular other = (Molecular) obj;
		if (authorinstitution == null) {
			if (other.authorinstitution != null)
				return false;
		} else if (!authorinstitution.equals(other.authorinstitution))
			return false;
		if (authorname == null) {
			if (other.authorname != null)
				return false;
		} else if (!authorname.equals(other.authorname))
			return false;
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
		if (fastaurl == null) {
			if (other.fastaurl != null)
				return false;
		} else if (!fastaurl.equals(other.fastaurl))
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
		if (ncbiid == null) {
			if (other.ncbiid != null)
				return false;
		} else if (!ncbiid.equals(other.ncbiid))
			return false;
		if (otherinfo == null) {
			if (other.otherinfo != null)
				return false;
		} else if (!otherinfo.equals(other.otherinfo))
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
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (sourcesjson == null) {
			if (other.sourcesjson != null)
				return false;
		} else if (!sourcesjson.equals(other.sourcesjson))
			return false;
		if (speId == null) {
			if (other.speId != null)
				return false;
		} else if (!speId.equals(other.speId))
			return false;
		if (speLocation == null) {
			if (other.speLocation != null)
				return false;
		} else if (!speLocation.equals(other.speLocation))
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

}