package org.big.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.util.Date;
import java.util.List;

/**
 *<p><b>License的Entity类</b></p>
 *<p> License的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "license", schema = "biodata")
public class License implements Serializable {

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = -2319420528084529741L;

	@Id
	private String id;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	private int status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	@Lob
	private String text;

	private String title;

	private String imageurl;
	//bi-directional many-to-one association to Multimedia
	@OneToMany(mappedBy="license")
	@JSONField(serialize=false)
	@JsonIgnore
	private List<Multimedia> multimedias;
	
	public License() {
	}
	
	public License(String title) {
		super();
		this.title = title;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInputer() {
		return this.inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return this.synchdate;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return this.synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Multimedia> getMultimedias() {
		return this.multimedias;
	}

	public void setMultimedias(List<Multimedia> multimedias) {
		this.multimedias = multimedias;
	}

	public Multimedia addMultimedia(Multimedia multimedia) {
		getMultimedias().add(multimedia);
		multimedia.setLicense(this);

		return multimedia;
	}

	public Multimedia removeMultimedia(Multimedia multimedia) {
		getMultimedias().remove(multimedia);
		multimedia.setLicense(null);

		return multimedia;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageurl == null) ? 0 : imageurl.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((multimedias == null) ? 0 : multimedias.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		License other = (License) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageurl == null) {
			if (other.imageurl != null)
				return false;
		} else if (!imageurl.equals(other.imageurl))
			return false;
		if (inputer == null) {
			if (other.inputer != null)
				return false;
		} else if (!inputer.equals(other.inputer))
			return false;
		if (inputtime == null) {
			if (other.inputtime != null)
				return false;
		} else if (!inputtime.equals(other.inputtime))
			return false;
		if (multimedias == null) {
			if (other.multimedias != null)
				return false;
		} else if (!multimedias.equals(other.multimedias))
			return false;
		if (status != other.status)
			return false;
		if (synchdate == null) {
			if (other.synchdate != null)
				return false;
		} else if (!synchdate.equals(other.synchdate))
			return false;
		if (synchstatus != other.synchstatus)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}