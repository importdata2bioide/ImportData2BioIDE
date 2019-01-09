package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Geogroup的Entity类</b></p>
 *<p> Geogroup的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "geogroup", schema = "biodata")
public class Geogroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;

	@Lob
	private String remark;

	//bi-directional many-to-one association to Geoobject
	@OneToMany(mappedBy="geogroup")
	private List<Geoobject> geoobjects;

	public Geogroup() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Geoobject> getGeoobjects() {
		return this.geoobjects;
	}

	public void setGeoobjects(List<Geoobject> geoobjects) {
		this.geoobjects = geoobjects;
	}

	public Geoobject addGeoobject(Geoobject geoobject) {
		getGeoobjects().add(geoobject);
		geoobject.setGeogroup(this);

		return geoobject;
	}

	public Geoobject removeGeoobject(Geoobject geoobject) {
		getGeoobjects().remove(geoobject);
		geoobject.setGeogroup(null);

		return geoobject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geoobjects == null) ? 0 : geoobjects.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
		Geogroup other = (Geogroup) obj;
		if (geoobjects == null) {
			if (other.geoobjects != null)
				return false;
		} else if (!geoobjects.equals(other.geoobjects))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		return true;
	}

}