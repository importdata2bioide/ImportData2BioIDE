package org.big.entity;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p><b>Team的Entity类</b></p>
 * <p>Team的Entity类</p>
 * @author WangTianshan (王天山)
 * <p>Created date: 2017/9/19 21:35 </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "team", schema = "biodata")
public class Team {
	private String id;
	private String name;
	private String leader;
	private Timestamp adddate;
	private String note;
	private String mark;
	private String status;
	/** 保留默认无参构造 */
	public Team() {
	}

	public Team(Team team) {
		this.id = team.getId();
		this.name = team.getName();
		this.leader = team.getLeader();
		this.adddate = team.getAdddate();
		this.note = team.getNote();
	}

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "leader")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Basic
	@Column(name = "adddate")
	public Timestamp getAdddate() {
		return adddate;
	}

	public void setAdddate(Timestamp adddate) {
		this.adddate = adddate;
	}

	@Basic
	@Column(name = "note")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adddate == null) ? 0 : adddate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((leader == null) ? 0 : leader.hashCode());
		result = prime * result + ((mark == null) ? 0 : mark.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
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
		Team other = (Team) obj;
		if (adddate == null) {
			if (other.adddate != null)
				return false;
		} else if (!adddate.equals(other.adddate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (leader == null) {
			if (other.leader != null)
				return false;
		} else if (!leader.equals(other.leader))
			return false;
		if (mark == null) {
			if (other.mark != null)
				return false;
		} else if (!mark.equals(other.mark))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		return true;
	}


}
