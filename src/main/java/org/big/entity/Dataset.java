package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;
import java.util.Date;

/**
 *<p><b>Dataset的Entity类</b></p>
 *<p> Dataset的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "dataset", schema = "biodata")
public class Dataset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@ManyToOne
	private User creator;

	private String dsabstract;

	private String dsname;

	private String lisenceid;

	private String mark;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	// bi-directional many-to-one association to Team
	@ManyToOne
	private Team team;

	public Dataset() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getDsabstract() {
		return dsabstract;
	}

	public void setDsabstract(String dsabstract) {
		this.dsabstract = dsabstract;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getLisenceid() {
		return lisenceid;
	}

	public void setLisenceid(String lisenceid) {
		this.lisenceid = lisenceid;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((dsabstract == null) ? 0 : dsabstract.hashCode());
		result = prime * result + ((dsname == null) ? 0 : dsname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lisenceid == null) ? 0 : lisenceid.hashCode());
		result = prime * result + ((mark == null) ? 0 : mark.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((team == null) ? 0 : team.hashCode());
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
		Dataset other = (Dataset) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (dsabstract == null) {
			if (other.dsabstract != null)
				return false;
		} else if (!dsabstract.equals(other.dsabstract))
			return false;
		if (dsname == null) {
			if (other.dsname != null)
				return false;
		} else if (!dsname.equals(other.dsname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lisenceid == null) {
			if (other.lisenceid != null)
				return false;
		} else if (!lisenceid.equals(other.lisenceid))
			return false;
		if (mark == null) {
			if (other.mark != null)
				return false;
		} else if (!mark.equals(other.mark))
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
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}

}