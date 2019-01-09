package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 *<p><b>Distributiondata的Entity类</b></p>
 *<p> Distributiondata的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "distributiondata", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Distributiondata implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String geojson;
	
	private String dismark;			// 地理标识
	private String dismapstandard;	// 地图类型
	private String distype ;		// 分布类型
	
	private String province;
	private String city;
	private String county;
	private String locality;
	
	private double lat;
	private double lng;
	
	private String sourcesid;
	private String refjson;
	private String expert;			// 审核专家
	
	private String discontent;
	private String taxonid;
	
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private Integer synchstatus;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDiscontent() {
		return discontent;
	}

	public void setDiscontent(String discontent) {
		this.discontent = discontent;
	}
	
/*	//bi-directional many-to-one association to TraitsetHasTraitontology
		@OneToMany(mappedBy="distributiondata")
		private List<DistributiondataHasGeoobject> distributiondataHasGeoobjects;*/

	public String getDismark() {
		return dismark;
	}

	public void setDismark(String dismark) {
		this.dismark = dismark;
	}

	public String getDismapstandard() {
		return dismapstandard;
	}

	public void setDismapstandard(String dismapstandard) {
		this.dismapstandard = dismapstandard;
	}

	public String getDistype() {
		return distype;
	}

	public void setDistype(String distype) {
		this.distype = distype;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getGeojson() {
		return geojson;
	}

	public void setGeojson(String geojson) {
		this.geojson = geojson;
	}

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Distributiondata() {
	}
	
	/*分布模板1*/
	public Distributiondata(String geojson, String dismark, String dismapstandard, String distype, String sourcesid,
			String refjson, String expert, Taxon taxon) {
		super();
		this.geojson = geojson;
		this.dismark = dismark;
		this.dismapstandard = dismapstandard;
		this.distype = distype;
		this.sourcesid = sourcesid;
		this.refjson = refjson;
		this.expert = expert;
		this.taxon = taxon;
	}
	
	/*分布模板2*/
	public Distributiondata(String province, String city, String county, String locality, double lat,
			double lng, String sourcesid, String refjson, String expert, Taxon taxon) {
		super();
		this.province = province;
		this.city = city;
		this.county = county;
		this.locality = locality;
		this.lat = lat;
		this.lng = lng;
		this.sourcesid = sourcesid;
		this.refjson = refjson;
		this.expert = expert;
		this.taxon = taxon;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getRefjson() {
		return refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getSourcesid() {
		return sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
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

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getTaxonid() {
		return taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}


	public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discontent == null) ? 0 : discontent.hashCode());
		result = prime * result + ((geojson == null) ? 0 : geojson.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		result = prime * result + ((taxonid == null) ? 0 : taxonid.hashCode());
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
		Distributiondata other = (Distributiondata) obj;
		if (discontent == null) {
			if (other.discontent != null)
				return false;
		} else if (!discontent.equals(other.discontent))
			return false;
		if (geojson == null) {
			if (other.geojson != null)
				return false;
		} else if (!geojson.equals(other.geojson))
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
		if (taxonid == null) {
			if (other.taxonid != null)
				return false;
		} else if (!taxonid.equals(other.taxonid))
			return false;
		return true;
	}
}