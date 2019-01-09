package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.Date;

/**
 *<p><b>Specimendata的Entity类</b></p>
 *<p> Specimendata的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "specimendata", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Specimendata implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String conserveStatus;//保存状态
	
	private String altitude;//海拔高度
	
	private String deep;
	private String host;
	private String descNote;
	
	private String specimenStatus;
	
	public String getSpecimenStatus() {
		return specimenStatus;
	}


	public void setSpecimenStatus(String specimenStatus) {
		this.specimenStatus = specimenStatus;
	}


	public String getDeep() {
		return deep;
	}
	

	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}




	public String getDescNote() {
		return descNote;
	}


	public void setDescNote(String descNote) {
		this.descNote = descNote;
	}


	public void setDeep(String deep) {
		this.deep = deep;
	}

	@Id
	private String id;

	private String city;

	private String collectdate;

	private String collector;

	private String country;

	private String county;

	private String idenby;

	private String idendate;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private double lat;

	private double lng;

	private String locality;

	private String location;
	@Type(type = "StringJsonUserType")
	private String mediajson;

	private String province;

	private String refjson;

	private String sex;

	private String sourcesid;

	private String specimenno;

	private String specimentype;

	private int state;

	private String storedin;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

//	private String taxonid;

	//bi-directional many-to-one association to Taxon
//	@ManyToOne
	private String taxonId;

	public Specimendata() {
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
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

	public String getCollectdate() {
		return this.collectdate;
	}

	public void setCollectdate(String collectdate) {
		this.collectdate = collectdate;
	}

	public String getCollector() {
		return this.collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
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

	public String getIdenby() {
		return this.idenby;
	}

	public void setIdenby(String idenby) {
		this.idenby = idenby;
	}

	public String getIdendate() {
		return this.idendate;
	}

	public void setIdendate(String idendate) {
		this.idendate = idendate;
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

	public String getMediajson() {
		return this.mediajson;
	}

	public void setMediajson(String mediajson) {
		this.mediajson = mediajson;
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

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSourcesid() {
		return this.sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public String getSpecimenno() {
		return this.specimenno;
	}

	public void setSpecimenno(String specimenno) {
		this.specimenno = specimenno;
	}

	public String getSpecimentype() {
		return this.specimentype;
	}

	public void setSpecimentype(String specimentype) {
		this.specimentype = specimentype;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStoredin() {
		return this.storedin;
	}

	public void setStoredin(String storedin) {
		this.storedin = storedin;
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




	public String getTaxonId() {
		return taxonId;
	}


	public void setTaxonId(String taxonId) {
		this.taxonId = taxonId;
	}


	public String getConserveStatus() {
		return conserveStatus;
	}

	public void setConserveStatus(String conserveStatus) {
		this.conserveStatus = conserveStatus;
	}
	

}