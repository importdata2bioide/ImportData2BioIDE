package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 *<p><b>Multimedia的Entity类</b></p>
 *<p> Multimedia的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "multimedia", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Multimedia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String mediatype;	//媒体类型
	private String context;		//图注
	private String title;		//标签
	private String oldPath;		//文件地址
	private String sourcesid;	//数据源	
	private String expert;		//审核专家
	private String rightsholder;//版权所有者
	private String copyright;	//版权声明
	private String lisenceid;	//共享协议
	private String path;		//原始链接
	private Date createtime;	//创建时间
	private String creator;		//创建人
	private String publisher;	//发布者
	private String contributor;	//贡献者
	private String country;		//国家
	private String province;	//省
	private String city;		//市
	private String county;		//县
	private String locality;	//小地点
	private double lng;			//经度
	private double lat;			//维度
	private String refjson;		//参考文献
	
	private String location;
	private String desid;
	@Column(name="dis_id")
	private String disId;
	private String info;
	private String suffix;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;		//录入时间
	private String inputer;		//录入者
	//bi-directional many-to-one association to License
	@ManyToOne
	private License license;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Multimedia() {
	}

	public Multimedia(String mediatype, String context, String title, String oldPath, String sourcesid, String expert,
			String rightsholder, String copyright, String lisenceid, String path, Date createtime, String creator,
			String publisher, String contributor, String country, String province, String city, String county,
			String locality, double lng, double lat, String refjson, Taxon taxon) {
		super();
		this.mediatype = mediatype;
		this.context = context;
		this.title = title;
		this.oldPath = oldPath;
		this.sourcesid = sourcesid;
		this.expert = expert;
		this.rightsholder = rightsholder;
		this.copyright = copyright;
		this.lisenceid = lisenceid;
		this.path = path;
		this.createtime = createtime;
		this.creator = creator;
		this.publisher = publisher;
		this.contributor = contributor;
		this.country = country;
		this.province = province;
		this.city = city;
		this.county = county;
		this.locality = locality;
		this.lng = lng;
		this.lat = lat;
		this.refjson = refjson;
		this.taxon = taxon;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getMediatype() {
		return mediatype;
	}

	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
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

	public String getDesid() {
		return this.desid;
	}

	public void setDesid(String desid) {
		this.desid = desid;
	}

	public String getDisId() {
		return this.disId;
	}

	public void setDisId(String disId) {
		this.disId = disId;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public String getLisenceid() {
		return this.lisenceid;
	}

	public void setLisenceid(String lisenceid) {
		this.lisenceid = lisenceid;
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

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public String getRightsholder() {
		return this.rightsholder;
	}

	public void setRightsholder(String rightsholder) {
		this.rightsholder = rightsholder;
	}

	public String getSourcesid() {
		return this.sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
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

	public License getLicense() {
		return this.license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result + ((desid == null) ? 0 : desid.hashCode());
		result = prime * result + ((disId == null) ? 0 : disId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((license == null) ? 0 : license.hashCode());
		result = prime * result + ((lisenceid == null) ? 0 : lisenceid.hashCode());
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((locality == null) ? 0 : locality.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((mediatype == null) ? 0 : mediatype.hashCode());
		result = prime * result + ((oldPath == null) ? 0 : oldPath.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((rightsholder == null) ? 0 : rightsholder.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Multimedia other = (Multimedia) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
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
		if (desid == null) {
			if (other.desid != null)
				return false;
		} else if (!desid.equals(other.desid))
			return false;
		if (disId == null) {
			if (other.disId != null)
				return false;
		} else if (!disId.equals(other.disId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
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
		if (license == null) {
			if (other.license != null)
				return false;
		} else if (!license.equals(other.license))
			return false;
		if (lisenceid == null) {
			if (other.lisenceid != null)
				return false;
		} else if (!lisenceid.equals(other.lisenceid))
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
		if (mediatype == null) {
			if (other.mediatype != null)
				return false;
		} else if (!mediatype.equals(other.mediatype))
			return false;
		if (oldPath == null) {
			if (other.oldPath != null)
				return false;
		} else if (!oldPath.equals(other.oldPath))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
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
		if (rightsholder == null) {
			if (other.rightsholder != null)
				return false;
		} else if (!rightsholder.equals(other.rightsholder))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
			return false;
		if (status != other.status)
			return false;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
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
		return true;
	}

}