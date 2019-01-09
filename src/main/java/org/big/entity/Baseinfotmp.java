package org.big.entity;

import javax.persistence.*;

import java.util.Date;

/**
 * <p><b>数据库参考文献临时表的Entity类</b></p>
 * <p>数据库参考文献临时表的Entity类</p>
 * @author BINZI
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "baseinfotmp", schema = "biodata")
public class Baseinfotmp {
	@Id
	private String id;
	private String refDsId;
	private String serialNum;
	private String filemark;
	private Integer fileType;	// 0 - 参考文献 || 1 - 数据源 || 2 - 审核专家
	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;

	public Baseinfotmp() {
	}
	public Baseinfotmp(String id, String refDsId, String serialNum, String filemark, Integer fileType, String inputer, Date inputtime) {
		super();
		this.id = id;
		this.refDsId = refDsId;
		this.serialNum = serialNum;
		this.filemark = filemark;
		this.fileType = fileType;
		this.inputer = inputer;
		this.inputtime = inputtime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRefDsId() {
		return refDsId;
	}
	public void setRefDsId(String refDsId) {
		this.refDsId = refDsId;
	}

	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getFilemark() {
		return filemark;
	}
	public void setFilemark(String filemark) {
		this.filemark = filemark;
	}
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
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
	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((filemark == null) ? 0 : filemark.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((refDsId == null) ? 0 : refDsId.hashCode());
		result = prime * result + ((serialNum == null) ? 0 : serialNum.hashCode());
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
		Baseinfotmp other = (Baseinfotmp) obj;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (filemark == null) {
			if (other.filemark != null)
				return false;
		} else if (!filemark.equals(other.filemark))
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
		if (refDsId == null) {
			if (other.refDsId != null)
				return false;
		} else if (!refDsId.equals(other.refDsId))
			return false;
		if (serialNum == null) {
			if (other.serialNum != null)
				return false;
		} else if (!serialNum.equals(other.serialNum))
			return false;
		return true;
	}
	
}
