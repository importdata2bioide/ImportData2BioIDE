package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 *<p><b>Taxkey的Entity类</b></p>
 *<p> Taxkey的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "taxkey", schema = "biodata")
public class Taxkey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Lob
	private String abstraction;

	private String keytitle;
	
	private String keytype;
	//bi-directional many-to-one association to Keyitem
	@OneToMany(mappedBy="taxkey")
	private List<Keyitem> keyitems;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;

	public Taxkey() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAbstraction() {
		return this.abstraction;
	}

	public void setAbstraction(String abstraction) {
		this.abstraction = abstraction;
	}

	public String getKeytitle() {
		return this.keytitle;
	}

	public void setKeytitle(String keytitle) {
		this.keytitle = keytitle;
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public List<Keyitem> getKeyitems() {
		return this.keyitems;
	}

	public void setKeyitems(List<Keyitem> keyitems) {
		this.keyitems = keyitems;
	}

	public Keyitem addKeyitem(Keyitem keyitem) {
		getKeyitems().add(keyitem);
		keyitem.setTaxkey(this);

		return keyitem;
	}

	public Keyitem removeKeyitem(Keyitem keyitem) {
		getKeyitems().remove(keyitem);
		keyitem.setTaxkey(null);

		return keyitem;
	}

	public Taxon getTaxon() {
		return this.taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abstraction == null) ? 0 : abstraction.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((keyitems == null) ? 0 : keyitems.hashCode());
		result = prime * result + ((keytitle == null) ? 0 : keytitle.hashCode());
		result = prime * result + ((keytype == null) ? 0 : keytype.hashCode());
		result = prime * result + ((taxon == null) ? 0 : taxon.hashCode());
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
		Taxkey other = (Taxkey) obj;
		if (abstraction == null) {
			if (other.abstraction != null)
				return false;
		} else if (!abstraction.equals(other.abstraction))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyitems == null) {
			if (other.keyitems != null)
				return false;
		} else if (!keyitems.equals(other.keyitems))
			return false;
		if (keytitle == null) {
			if (other.keytitle != null)
				return false;
		} else if (!keytitle.equals(other.keytitle))
			return false;
		if (keytype == null) {
			if (other.keytype != null)
				return false;
		} else if (!keytype.equals(other.keytype))
			return false;
		if (taxon == null) {
			if (other.taxon != null)
				return false;
		} else if (!taxon.equals(other.taxon))
			return false;
		return true;
	}
}