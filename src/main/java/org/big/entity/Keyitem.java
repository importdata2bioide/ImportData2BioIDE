package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>Keyitem的Entity类</b></p>
 *<p> Keyitem的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "keyitem", schema = "biodata")
public class Keyitem implements Serializable {

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = -8432384438553470096L;

	@Id
	private String id;

	private int branchid;

	private String featureimgjson;

	private int innerorder;

	private String item;

	private int orderid;

	private String pid;

	private String taxonid;

	private String keytype;
	
	//bi-directional many-to-one association to Taxkey
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxkey taxkey;

	public Keyitem() {
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBranchid() {
		return this.branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public String getFeatureimgjson() {
		return this.featureimgjson;
	}

	public void setFeatureimgjson(String featureimgjson) {
		this.featureimgjson = featureimgjson;
	}

	public int getInnerorder() {
		return this.innerorder;
	}

	public void setInnerorder(int innerorder) {
		this.innerorder = innerorder;
	}

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getOrderid() {
		return this.orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTaxonid() {
		return this.taxonid;
	}

	public void setTaxonid(String taxonid) {
		this.taxonid = taxonid;
	}

	public Taxkey getTaxkey() {
		return this.taxkey;
	}

	public void setTaxkey(Taxkey taxkey) {
		this.taxkey = taxkey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + branchid;
		result = prime * result + ((featureimgjson == null) ? 0 : featureimgjson.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + innerorder;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + orderid;
		result = prime * result + ((pid == null) ? 0 : pid.hashCode());
		result = prime * result + ((taxkey == null) ? 0 : taxkey.hashCode());
		result = prime * result + ((taxonid == null) ? 0 : taxonid.hashCode());
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
		Keyitem other = (Keyitem) obj;
		if (branchid != other.branchid)
			return false;
		if (featureimgjson == null) {
			if (other.featureimgjson != null)
				return false;
		} else if (!featureimgjson.equals(other.featureimgjson))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (innerorder != other.innerorder)
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (orderid != other.orderid)
			return false;
		if (pid == null) {
			if (other.pid != null)
				return false;
		} else if (!pid.equals(other.pid))
			return false;
		if (taxkey == null) {
			if (other.taxkey != null)
				return false;
		} else if (!taxkey.equals(other.taxkey))
			return false;
		if (taxonid == null) {
			if (other.taxonid != null)
				return false;
		} else if (!taxonid.equals(other.taxonid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Keyitem [id=" + id + ", branchid=" + branchid + ", featureimgjson=" + featureimgjson + ", innerorder="
				+ innerorder + ", item=" + item + ", orderid=" + orderid + ", pid=" + pid + ", taxonid=" + taxonid
				+ ", taxkey=" + taxkey + "]";
	}
	

}