package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import java.util.Date;
/**
 *<p><b>Datasource的Entity类</b></p>
 *<p> Datasource的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Entity
@Table(name="datasources", schema="biodata")
public class Datasource implements Serializable {
	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = 3612843965079723834L;

	@Id
	private String id;
	
	private String title;
	private String dType;			//类型
	private String versions;
	private String creater;
	private String createtime;
	private String dAbstract;		//摘要
	private String dKeyword;		//关键字
	private String dLink;			//链接
	private String dVerifier;		//审核专家
	private String dRightsholder;	//版权所有者
	private String dCopyright;		//版权声明
	@Lob
	private String info;

	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private Integer synchstatus;

	public Datasource() {
	}
	
	public Datasource(String id, String title, String dType, String versions, String creater, String createtime, String dAbstract,
			String dKeyword, String dLink, String dVerifier, String dRightsholder, String dCopyright, String info) {
		super();
		this.id = id;
		this.title = title;
		this.dType = dType;
		this.versions = versions;
		this.creater = creater;
		this.createtime = createtime;
		this.dAbstract = dAbstract;
		this.dKeyword = dKeyword;
		this.dLink = dLink;
		this.dVerifier = dVerifier;
		this.dRightsholder = dRightsholder;
		this.dCopyright = dCopyright;
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getdType() {
		return dType;
	}

	public void setdType(String dType) {
		this.dType = dType;
	}

	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getdAbstract() {
		return dAbstract;
	}

	public void setdAbstract(String dAbstract) {
		this.dAbstract = dAbstract;
	}

	public String getdKeyword() {
		return dKeyword;
	}

	public void setdKeyword(String dKeyword) {
		this.dKeyword = dKeyword;
	}

	public String getdLink() {
		return dLink;
	}

	public void setdLink(String dLink) {
		this.dLink = dLink;
	}

	public String getdVerifier() {
		return dVerifier;
	}

	public void setdVerifier(String dVerifier) {
		this.dVerifier = dVerifier;
	}

	public String getdRightsholder() {
		return dRightsholder;
	}

	public void setdRightsholder(String dRightsholder) {
		this.dRightsholder = dRightsholder;
	}

	public String getdCopyright() {
		return dCopyright;
	}

	public void setdCopyright(String dCopyright) {
		this.dCopyright = dCopyright;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Timestamp inputtime) {
		this.inputtime = inputtime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getSynchdate() {
		return synchdate;
	}

	public void setSynchdate(Timestamp synchdate) {
		this.synchdate = synchdate;
	}

	public Integer getSynchstatus() {
		return synchstatus;
	}

	public void setSynchstatus(Integer synchstatus) {
		this.synchstatus = synchstatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creater == null) ? 0 : creater.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((dAbstract == null) ? 0 : dAbstract.hashCode());
		result = prime * result + ((dCopyright == null) ? 0 : dCopyright.hashCode());
		result = prime * result + ((dKeyword == null) ? 0 : dKeyword.hashCode());
		result = prime * result + ((dLink == null) ? 0 : dLink.hashCode());
		result = prime * result + ((dRightsholder == null) ? 0 : dRightsholder.hashCode());
		result = prime * result + ((dType == null) ? 0 : dType.hashCode());
		result = prime * result + ((dVerifier == null) ? 0 : dVerifier.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + ((synchstatus == null) ? 0 : synchstatus.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((versions == null) ? 0 : versions.hashCode());
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
		Datasource other = (Datasource) obj;
		if (creater == null) {
			if (other.creater != null)
				return false;
		} else if (!creater.equals(other.creater))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (dAbstract == null) {
			if (other.dAbstract != null)
				return false;
		} else if (!dAbstract.equals(other.dAbstract))
			return false;
		if (dCopyright == null) {
			if (other.dCopyright != null)
				return false;
		} else if (!dCopyright.equals(other.dCopyright))
			return false;
		if (dKeyword == null) {
			if (other.dKeyword != null)
				return false;
		} else if (!dKeyword.equals(other.dKeyword))
			return false;
		if (dLink == null) {
			if (other.dLink != null)
				return false;
		} else if (!dLink.equals(other.dLink))
			return false;
		if (dRightsholder == null) {
			if (other.dRightsholder != null)
				return false;
		} else if (!dRightsholder.equals(other.dRightsholder))
			return false;
		if (dType == null) {
			if (other.dType != null)
				return false;
		} else if (!dType.equals(other.dType))
			return false;
		if (dVerifier == null) {
			if (other.dVerifier != null)
				return false;
		} else if (!dVerifier.equals(other.dVerifier))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
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
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (versions == null) {
			if (other.versions != null)
				return false;
		} else if (!versions.equals(other.versions))
			return false;
		return true;
	}


}