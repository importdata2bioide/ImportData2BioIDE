package org.big.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

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
public class Commonname implements Serializable, Cloneable {
   
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
    	if(StringUtils.isNotEmpty(commonname)) {
    		commonname = commonname.trim();
    	}
        return commonname;
    }

    public void setCommonname(String commonname) {
    	if(StringUtils.isNotEmpty(commonname)) {
    		commonname = commonname.trim();
    	}
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

  //实现Cloneable接口，重写clone方法
  	@Override 
      public Object clone() { 
  		Commonname copyRecord = null; 
          try{ 
          	copyRecord = (Commonname)super.clone(); 
          }catch(CloneNotSupportedException e) { 
              e.printStackTrace(); 
          } 
          return copyRecord; 
      } 
  	
}
