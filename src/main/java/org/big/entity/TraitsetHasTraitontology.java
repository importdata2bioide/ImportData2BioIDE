package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>TraitsetHasTraitontology的Entity类</b></p>
 *<p> TraitsetHasTraitontology的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name="traitset_has_traitontology")
public class TraitsetHasTraitontology implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="p_traitontology_id")
	private String pTraitontologyId;

	//bi-directional many-to-one association to Traitontology
	@ManyToOne
	private Traitontology traitontology;

	//bi-directional many-to-one association to Traitset
	@ManyToOne
	private Traitset traitset;

	public TraitsetHasTraitontology() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPTraitontologyId() {
		return this.pTraitontologyId;
	}

	public void setPTraitontologyId(String pTraitontologyId) {
		this.pTraitontologyId = pTraitontologyId;
	}

	public Traitontology getTraitontology() {
		return this.traitontology;
	}

	public void setTraitontology(Traitontology traitontology) {
		this.traitontology = traitontology;
	}

	public Traitset getTraitset() {
		return this.traitset;
	}

	public void setTraitset(Traitset traitset) {
		this.traitset = traitset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pTraitontologyId == null) ? 0 : pTraitontologyId.hashCode());
		result = prime * result + ((traitontology == null) ? 0 : traitontology.hashCode());
		result = prime * result + ((traitset == null) ? 0 : traitset.hashCode());
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
		TraitsetHasTraitontology other = (TraitsetHasTraitontology) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pTraitontologyId == null) {
			if (other.pTraitontologyId != null)
				return false;
		} else if (!pTraitontologyId.equals(other.pTraitontologyId))
			return false;
		if (traitontology == null) {
			if (other.traitontology != null)
				return false;
		} else if (!traitontology.equals(other.traitontology))
			return false;
		if (traitset == null) {
			if (other.traitset != null)
				return false;
		} else if (!traitset.equals(other.traitset))
			return false;
		return true;
	}

}