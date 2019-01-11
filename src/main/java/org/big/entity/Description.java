package org.big.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.big.common.StringJsonUserType;
import org.hibernate.annotations.TypeDef;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *<p><b>Description的Entity类</b></p>
 *<p> Description的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "description", schema = "biodata")
@TypeDef( name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Description implements Serializable {

	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = 1275405641637293147L;
	@Id
	private String id;
	private String destitle;
	private String desdate;
	private String describer;
	private String language;
	private String destypeid;
	@Lob
	private String descontent;
	private String remark;
	private String refjson;
	private String sourcesid;
	private String expert;
	private String rightsholder;
	private String dCopyright;		//版权声明
	private String licenseid;
	
	private String relationDes;	// 其他描述的Id
	private String relationId;	// 关系Id
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;

	//bi-directional many-to-one association to Descriptiontype
	@ManyToOne
	private Descriptiontype descriptiontype;

	//bi-directional many-to-one association to Taxon
	@ManyToOne
	@JSONField(serialize=false)
	@JsonIgnore
	private Taxon taxon;
	
	public Description() {
	}
	
	public Description(String desdate, String describer, String language, String destypeid,
			String descontent, String remark, String refjson, String sourcesid, String expert, String rightsholder,
			String dCopyright, String licenseid, Taxon taxon) {
		super();
		this.desdate = desdate;
		this.describer = describer;
		this.language = language;
		this.destypeid = destypeid;
		this.descontent = descontent;
		this.remark = remark;
		this.refjson = refjson;
		this.sourcesid = sourcesid;
		this.expert = expert;
		this.rightsholder = rightsholder;
		this.dCopyright = dCopyright;
		this.licenseid = licenseid;
		this.taxon = taxon;
	}

	public String getdCopyright() {
		return dCopyright;
	}

	public void setdCopyright(String dCopyright) {
		this.dCopyright = dCopyright;
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescontent() {
		return this.descontent;
	}

	public void setDescontent(String descontent) {
		this.descontent = descontent;
	}

	public String getDescriber() {
		return this.describer;
	}

	public void setDescriber(String describer) {
		this.describer = describer;
	}

	public String getDesdate() {
		return this.desdate;
	}

	public void setDesdate(String desdate) {
		this.desdate = desdate;
	}

	public String getDestitle() {
		return this.destitle;
	}

	public void setDestitle(String destitle) {
		this.destitle = destitle;
	}

	public String getDestypeid() {
		return destypeid;
	}

	public void setDestypeid(String destypeid) {
		this.destypeid = destypeid;
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

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLicenseid() {
		return this.licenseid;
	}

	public void setLicenseid(String licenseid) {
		this.licenseid = licenseid;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getRelationDes() {
		return relationDes;
	}

	public void setRelationDes(String relationDes) {
		this.relationDes = relationDes;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRightsholder() {
		return this.rightsholder;
	}

	public void setRightsholder(String rightsholder) {
		this.rightsholder = rightsholder;
	}

	public String getSourcesid() {
		return sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
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

	public Descriptiontype getDescriptiontype() {
		return this.descriptiontype;
	}

	public void setDescriptiontype(Descriptiontype descriptiontype) {
		this.descriptiontype = descriptiontype;
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
		result = prime * result + ((descontent == null) ? 0 : descontent.hashCode());
		result = prime * result + ((describer == null) ? 0 : describer.hashCode());
		result = prime * result + ((descriptiontype == null) ? 0 : descriptiontype.hashCode());
		result = prime * result + ((desdate == null) ? 0 : desdate.hashCode());
		result = prime * result + ((destitle == null) ? 0 : destitle.hashCode());
		result = prime * result + ((destypeid == null) ? 0 : destypeid.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((licenseid == null) ? 0 : licenseid.hashCode());
		result = prime * result + ((refjson == null) ? 0 : refjson.hashCode());
		result = prime * result + ((relationDes == null) ? 0 : relationDes.hashCode());
		result = prime * result + ((relationId == null) ? 0 : relationId.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((rightsholder == null) ? 0 : rightsholder.hashCode());
		result = prime * result + ((sourcesid == null) ? 0 : sourcesid.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
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
		Description other = (Description) obj;
		if (descontent == null) {
			if (other.descontent != null)
				return false;
		} else if (!descontent.equals(other.descontent))
			return false;
		if (describer == null) {
			if (other.describer != null)
				return false;
		} else if (!describer.equals(other.describer))
			return false;
		if (descriptiontype == null) {
			if (other.descriptiontype != null)
				return false;
		} else if (!descriptiontype.equals(other.descriptiontype))
			return false;
		if (desdate == null) {
			if (other.desdate != null)
				return false;
		} else if (!desdate.equals(other.desdate))
			return false;
		if (destitle == null) {
			if (other.destitle != null)
				return false;
		} else if (!destitle.equals(other.destitle))
			return false;
		if (destypeid == null) {
			if (other.destypeid != null)
				return false;
		} else if (!destypeid.equals(other.destypeid))
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
		if (licenseid == null) {
			if (other.licenseid != null)
				return false;
		} else if (!licenseid.equals(other.licenseid))
			return false;
		if (refjson == null) {
			if (other.refjson != null)
				return false;
		} else if (!refjson.equals(other.refjson))
			return false;
		if (relationDes == null) {
			if (other.relationDes != null)
				return false;
		} else if (!relationDes.equals(other.relationDes))
			return false;
		if (relationId == null) {
			if (other.relationId != null)
				return false;
		} else if (!relationId.equals(other.relationId))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (rightsholder == null) {
			if (other.rightsholder != null)
				return false;
		} else if (!rightsholder.equals(other.rightsholder))
			return false;
		if (sourcesid == null) {
			if (other.sourcesid != null)
				return false;
		} else if (!sourcesid.equals(other.sourcesid))
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
		if (taxon == null) {
			if (other.taxon != null)
				return false;
		} else if (!taxon.equals(other.taxon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Description [id=" + id + ", desdate=" + desdate + ", describer=" + describer + ", language=" + language
				+ ", destypeid=" + destypeid + ", descontent=" + descontent + ", refjson=" + refjson + ", sourcesid="
				+ sourcesid + ", licenseid=" + licenseid + ", rightsholder=" + rightsholder + ", remark=" + remark
				+ "]";
	}
}