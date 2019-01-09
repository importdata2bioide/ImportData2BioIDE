package org.big.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Protectstandard的Entity类</b></p>
 *<p> Protectstandard的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "protectstandard", schema = "biodata")
public class Protectstandard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private String releasedate;

	private String source;

	private String standardinfo;

	private String standardname;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String version;

	private String protlevel;
	
	//bi-directional many-to-one association to Protection
	@OneToMany(mappedBy="protectstandard")
	@JSONField(serialize=false)
	@JsonIgnore
	private List<Protection> protections;

	public Protectstandard() {
	}
	
	public String getProtlevel() {
		return protlevel;
	}

	public void setProtlevel(String protlevel) {
		this.protlevel = protlevel;
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

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getReleasedate() {
		return this.releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStandardinfo() {
		return this.standardinfo;
	}

	public void setStandardinfo(String standardinfo) {
		this.standardinfo = standardinfo;
	}

	public String getStandardname() {
		return this.standardname;
	}

	public void setStandardname(String standardname) {
		this.standardname = standardname;
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

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Protection> getProtections() {
		return this.protections;
	}

	public void setProtections(List<Protection> protections) {
		this.protections = protections;
	}

	public Protection addProtection(Protection protection) {
		getProtections().add(protection);
		protection.setProtectstandard(this);

		return protection;
	}

	public Protection removeProtection(Protection protection) {
		getProtections().remove(protection);
		protection.setProtectstandard(null);

		return protection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((protections == null) ? 0 : protections.hashCode());
		result = prime * result + ((releasedate == null) ? 0 : releasedate.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((standardinfo == null) ? 0 : standardinfo.hashCode());
		result = prime * result + ((standardname == null) ? 0 : standardname.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		Protectstandard other = (Protectstandard) obj;
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
		if (protections == null) {
			if (other.protections != null)
				return false;
		} else if (!protections.equals(other.protections))
			return false;
		if (releasedate == null) {
			if (other.releasedate != null)
				return false;
		} else if (!releasedate.equals(other.releasedate))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (standardinfo == null) {
			if (other.standardinfo != null)
				return false;
		} else if (!standardinfo.equals(other.standardinfo))
			return false;
		if (standardname == null) {
			if (other.standardname != null)
				return false;
		} else if (!standardname.equals(other.standardname))
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
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Protectstandard [id=" + id + ", releasedate=" + releasedate + ", source=" + source + ", standardinfo="
				+ standardinfo + ", standardname=" + standardname + ", version=" + version + ", protlevel=" + protlevel
				+ "]";
	}
	
}