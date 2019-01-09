package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *<p><b>Phytree的Entity类</b></p>
 *<p> Phytree的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "phytree", schema = "biodata")
public class Phytree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;

	//bi-directional many-to-one association to Dataset
	@ManyToOne
	private Dataset dataset;

	public Phytree() {
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

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Phytree other = (Phytree) obj;
		if (dataset == null) {
			if (other.dataset != null)
				return false;
		} else if (!dataset.equals(other.dataset))
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
		return true;
	}

}