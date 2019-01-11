package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

/**
 *<p><b>Expert的Entity类</b></p>
 *<p> Expert的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/10/15 10:15</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "expert", schema = "biodata")
public class Expert implements Serializable {
	/**
	 * @Description 
	 * @author ZXY  
	 */
	private static final long serialVersionUID = 6737467966289563181L;
	@Id
	private String id;
	private String cnName;
	private String enName;
	private String cnCompany;
	private String enCompany;
	private String cnAddress;
	private String enAddress;
	private String expEmail;
	private String cnHomePage;
	private String enHomePage;
	private String expInfo;
	
	private Integer status;
	
	private String inputer;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private Integer synchstatus;
	
	public Expert() {
		super();
	}
	
	public Expert(String id, String cnName, String enName, String cnCompany, String enCompany, String cnAddress, String enAddress, 
			 String expEmail, String cnHomePage, String enHomePage, String expInfo) {
		super();
		this.id = id;
		this.cnName = cnName;
		this.enName = enName;
		this.cnCompany = cnCompany;
		this.enCompany = enCompany;
		this.expEmail = expEmail;
		this.cnAddress = cnAddress;
		this.enAddress = enAddress;
		this.cnHomePage = cnHomePage;
		this.enHomePage = enHomePage;
		this.expInfo = expInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getCnCompany() {
		return cnCompany;
	}
	public void setCnCompany(String cnCompany) {
		this.cnCompany = cnCompany;
	}
	public String getEnCompany() {
		return enCompany;
	}
	public void setEnCompany(String enCompany) {
		this.enCompany = enCompany;
	}
	public String getCnAddress() {
		return cnAddress;
	}
	public void setCnAddress(String cnAddress) {
		this.cnAddress = cnAddress;
	}
	public String getEnAddress() {
		return enAddress;
	}
	public void setEnAddress(String enAddress) {
		this.enAddress = enAddress;
	}
	public String getCnHomePage() {
		return cnHomePage;
	}
	public void setCnHomePage(String cnHomePage) {
		this.cnHomePage = cnHomePage;
	}
	public String getEnHomePage() {
		return enHomePage;
	}
	public void setEnHomePage(String enHomePage) {
		this.enHomePage = enHomePage;
	}
	public String getExpEmail() {
		return expEmail;
	}
	public void setExpEmail(String expEmail) {
		this.expEmail = expEmail;
	}
	public String getExpInfo() {
		return expInfo;
	}
	public void setExpInfo(String expInfo) {
		this.expInfo = expInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
	public String toString() {
		return "Expert [id=" + id + ", cnName=" + cnName + ", enName=" + enName + ", cnCompany=" + cnCompany
				+ ", enCompany=" + enCompany + ", cnAddress=" + cnAddress + ", enAddress=" + enAddress + ", expEmail="
				+ expEmail + ", cnHomePage=" + cnHomePage + ", enHomePage=" + enHomePage + ", expInfo=" + expInfo
				+ ", status=" + status + ", inputer=" + inputer + ", inputtime=" + inputtime + ", synchdate="
				+ synchdate + "]";
	}
	
}
