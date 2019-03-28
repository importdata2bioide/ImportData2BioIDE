package org.big.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *<p><b>Commonname的Entity类</b></p>
 *<p> Commonname的Entity类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "commonname")
public class Commonname implements Serializable{
   
	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = 955262543875860259L;
	private String id;
    private String commonname;
    private String language;
    private String refjson;
    private String sourcesid;
    private String expert;
    private String remark;
    
    private Integer status;
    private String inputer;
    private Timestamp inputtime;
    private Integer synchstatus;
    private Timestamp synchdate;

	private Taxon taxon;
    
	public Commonname() {
	}

	public Commonname(String commonname, String language, String refjson, String sourcesid, String expert, String remark, Taxon taxon) {
		super();
		this.commonname = commonname;
		this.language = language;
		this.refjson = refjson;
		this.sourcesid = sourcesid;
		this.expert = expert;
		this.remark = remark;
		this.taxon = taxon;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
    public Taxon getTaxon() {
		return taxon;
	}

	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "commonname")
    public String getCommonname() {
        return commonname;
    }

    public void setCommonname(String commonname) {
        this.commonname = commonname;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "refjson")
    public String getRefjson() {
        return refjson;
    }

    public void setRefjson(String refjson) {
        this.refjson = refjson;
    }

    @Basic
    @Column(name = "sourcesid")
    public String getSourcesid() {
        return sourcesid;
    }

    public void setSourcesid(String sourcesid) {
        this.sourcesid = sourcesid;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	@Basic
    @Column(name = "inputtime")
    public Timestamp getInputtime() {
        return inputtime;
    }

    public void setInputtime(Timestamp inputtime) {
        this.inputtime = inputtime;
    }

    @Basic
    @Column(name = "synchstatus")
    public Integer getSynchstatus() {
        return synchstatus;
    }

    public void setSynchstatus(Integer synchstatus) {
        this.synchstatus = synchstatus;
    }

    @Basic
    @Column(name = "synchdate")
    public Timestamp getSynchdate() {
        return synchdate;
    }

    public void setSynchdate(Timestamp synchdate) {
        this.synchdate = synchdate;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commonname == null) ? 0 : commonname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + ((synchstatus == null) ? 0 : synchstatus.hashCode());
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
		Commonname other = (Commonname) obj;
		if (commonname == null) {
			if (other.commonname != null)
				return false;
		} else if (!commonname.equals(other.commonname))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (synchdate == null) {
			if (other.synchdate != null)
				return false;
		} else if (!synchdate.equals(other.synchdate))
			return false;
		if (synchstatus == null) {
			if (other.synchstatus != null)
				return false;
		} else if (!synchstatus.equals(other.synchstatus))
			return false;
		if (taxon == null) {
			if (other.taxon != null)
				return false;
		} else if (!taxon.equals(other.taxon))
			return false;
		return true;
	}

}
