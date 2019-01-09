package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;

/**
 *<p><b>Rank的Entity类</b></p>
 *<p> Rank的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "rank", schema = "biodata")
public class Rank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String sort;
	
	private String chname;

	private String enname;

	//bi-directional many-to-one association to Taxon
	@OneToMany(mappedBy="rank")
	private List<Taxon> taxons;

	public Rank() {
	}
	
	public Rank(String chname) {
		super();
		this.chname = chname;
	}
	
	public Rank(String chname, String enname) {
		super();
		this.chname = chname;
		this.enname = enname;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getChname() {
		return this.chname;
	}

	public void setChname(String chname) {
		this.chname = chname;
	}

	public String getEnname() {
		return this.enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public List<Taxon> getTaxons() {
		return this.taxons;
	}

	public void setTaxons(List<Taxon> taxons) {
		this.taxons = taxons;
	}

	public Taxon addTaxon(Taxon taxon) {
		getTaxons().add(taxon);
		taxon.setRank(this);

		return taxon;
	}

	public Taxon removeTaxon(Taxon taxon) {
		getTaxons().remove(taxon);
		taxon.setRank(null);

		return taxon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chname == null) ? 0 : chname.hashCode());
		result = prime * result + ((enname == null) ? 0 : enname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((taxons == null) ? 0 : taxons.hashCode());
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
		Rank other = (Rank) obj;
		if (chname == null) {
			if (other.chname != null)
				return false;
		} else if (!chname.equals(other.chname))
			return false;
		if (enname == null) {
			if (other.enname != null)
				return false;
		} else if (!enname.equals(other.enname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		if (taxons == null) {
			if (other.taxons != null)
				return false;
		} else if (!taxons.equals(other.taxons))
			return false;
		return true;
	}

}