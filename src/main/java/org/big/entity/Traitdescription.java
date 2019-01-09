package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Traitdescription的Entity类</b></p>
 *<p> Traitdescription的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="traitdescription", schema = "biodata")
public class Traitdescription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Lob
	private String definition;

	private int propertyid;

	private int traitontologyid;

	public Traitdescription() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getPropertyid() {
		return this.propertyid;
	}

	public void setPropertyid(int propertyid) {
		this.propertyid = propertyid;
	}

	public int getTraitontologyid() {
		return this.traitontologyid;
	}

	public void setTraitontologyid(int traitontologyid) {
		this.traitontologyid = traitontologyid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + id;
		result = prime * result + propertyid;
		result = prime * result + traitontologyid;
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
		Traitdescription other = (Traitdescription) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (id != other.id)
			return false;
		if (propertyid != other.propertyid)
			return false;
		if (traitontologyid != other.traitontologyid)
			return false;
		return true;
	}

}