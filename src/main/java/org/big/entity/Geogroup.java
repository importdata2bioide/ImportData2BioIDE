package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;


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

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = -5332756687515372112L;
	
	

	@Id
	private String id;

	private String name;

	@Lob
	private String remark;

	
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

	

	



}