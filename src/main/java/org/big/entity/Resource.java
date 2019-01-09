package org.big.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *<p><b>KeyitemResource的Entity类</b></p>
 *<p> KeyitemResource的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/8/23 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Table(name = "resource", schema = "biodata")
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String resourceName;
	private String keyitemId;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getKeyitemId() {
		return keyitemId;
	}
	public void setKeyitemId(String keyitemId) {
		this.keyitemId = keyitemId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((keyitemId == null) ? 0 : keyitemId.hashCode());
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
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
		Resource other = (Resource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyitemId == null) {
			if (other.keyitemId != null)
				return false;
		} else if (!keyitemId.equals(other.keyitemId))
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
