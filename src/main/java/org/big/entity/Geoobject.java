package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.Date;

/**
 *<p><b>Geoobject的Entity类</b></p>
 *<p> Geoobject的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "geoobject", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Geoobject implements Serializable {

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = -5489515952143398869L;

	@Id
	private String id;

	private double centerx;

	private double centery;

	private String cngeoname;

	private String engeoname;
	@Type(type = "StringJsonUserType")
	private String geodata;

	private String geotype;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private String pid;

	@Type(type = "StringJsonUserType")
	private String relation;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String version;

	private String adcode;

	private String citycode;

/*	//bi-directional many-to-one association to TraitsetHasTraitontology
	@OneToMany(mappedBy="geoobject")
	private List<DistributiondataHasGeoobject> distributiondataHasGeoobjects;
*/
	//bi-directional many-to-one association to Geogroup
	
	private String geogroupId;
	
	private String remark;
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Geoobject() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCenterx() {
		return centerx;
	}

	public void setCenterx(double centerx) {
		this.centerx = centerx;
	}

	public double getCentery() {
		return centery;
	}

	public void setCentery(double centery) {
		this.centery = centery;
	}

	public String getCngeoname() {
		return cngeoname;
	}

	public void setCngeoname(String cngeoname) {
		this.cngeoname = cngeoname;
	}

	public String getEngeoname() {
		return engeoname;
	}

	public void setEngeoname(String engeoname) {
		this.engeoname = engeoname;
	}

	public String getGeodata() {
		return geodata;
	}

	public void setGeodata(String geodata) {
		this.geodata = geodata;
	}

	public String getGeotype() {
		return geotype;
	}

	public void setGeotype(String geotype) {
		this.geotype = geotype;
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	

	public String getGeogroupId() {
		return geogroupId;
	}

	public void setGeogroupId(String geogroupId) {
		this.geogroupId = geogroupId;
	}


}