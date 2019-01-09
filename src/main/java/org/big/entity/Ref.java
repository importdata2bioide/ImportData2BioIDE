package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import java.util.Date;

/**
 * <p>
 * <b>Ref的Entity类</b>
 * </p>
 * <p>
 * Ref的Entity类
 * </p>
 * 
 * @author BINZI
 *         <p>
 *         Created date: 2018/4/8 17:35
 *         </p>
 *         <p>
 *         Copyright: The Research Group of Biodiversity Informatics
 *         (BiodInfoGroup) - 中国科学院动物研究所生物多样性信息学研究组
 *         </p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "refs", schema = "biodata")
public class Ref implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String ptype; // 文献类型
	private String refstr; // 完整题录
	private String author; // 作者
	private String pyear; // 发表年代
	private String title; // 题目
	private String journal; // 期刊|专著|论文集
	private String rVolume; // 卷*
	private String rPeriod; // 期*
	private String refs; // 起始页
	private String refe; // 终止页
	private String isbn; // 文献标识*
	private String place; // 出版地
	private String press; // 出版社
	private String translator; // 编译者
	private String keywords; // 关键字
	private String tpage; // 总页数
	private String tchar; // 总字数
	private String languages; // 文献语言
	private String olang; // 原始语言
	private String version; // 版本*
	@Lob
	private String remark; // 备注
	private String leftstr;
	private String rLink;

	private String inputer;
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	private int status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;
	private int synchstatus;
	@Transient
	private double SimilarScoreWithCitation;

	@Transient
	private String mayBeRefStr;

	public String getMayBeRefStr() {
		return mayBeRefStr;
	}

	public void setMayBeRefStr(String mayBeRefStr) {
		this.mayBeRefStr = mayBeRefStr;
	}

	public double getSimilarScoreWithCitation() {
		return SimilarScoreWithCitation;
	}

	public void setSimilarScoreWithCitation(double similarScoreWithCitation) {
		SimilarScoreWithCitation = similarScoreWithCitation;
	}

	public Ref() {
	}

	public Ref(String id, String ptype, String refstr, String author, String pyear, String title, String journal,
			String rVolume, String rPeriod, String refs, String refe, String isbn, String place, String press,
			String translator, String keywords, String tpage, String tchar, String languages, String olang,
			String version, String remark) {
		super();
		this.id = id;
		this.ptype = ptype;
		this.refstr = refstr;
		this.author = author;
		this.pyear = pyear;
		this.title = title;
		this.journal = journal;
		this.rVolume = rVolume;
		this.rPeriod = rPeriod;
		this.refs = refs;
		this.refe = refe;
		this.isbn = isbn;
		this.place = place;
		this.press = press;
		this.translator = translator;
		this.keywords = keywords;
		this.tpage = tpage;
		this.tchar = tchar;
		this.languages = languages;
		this.olang = olang;
		this.version = version;
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getRefstr() {
		return refstr;
	}

	public void setRefstr(String refstr) {
		this.refstr = refstr;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPyear() {
		return pyear;
	}

	public void setPyear(String pyear) {
		this.pyear = pyear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getrVolume() {
		return rVolume;
	}

	public void setrVolume(String rVolume) {
		this.rVolume = rVolume;
	}

	public String getrPeriod() {
		return rPeriod;
	}

	public void setrPeriod(String rPeriod) {
		this.rPeriod = rPeriod;
	}

	public String getRefs() {
		return refs;
	}

	public void setRefs(String refs) {
		this.refs = refs;
	}

	public String getRefe() {
		return refe;
	}

	public void setRefe(String refe) {
		this.refe = refe;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTpage() {
		return tpage;
	}

	public void setTpage(String tpage) {
		this.tpage = tpage;
	}

	public String getTchar() {
		return tchar;
	}

	public void setTchar(String tchar) {
		this.tchar = tchar;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getOlang() {
		return olang;
	}

	public void setOlang(String olang) {
		this.olang = olang;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public int getSynchstatus() {
		return synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getLeftstr() {
		return leftstr;
	}

	public void setLeftstr(String leftstr) {
		this.leftstr = leftstr;
	}

	public String getrLink() {
		return rLink;
	}

	public void setrLink(String rLink) {
		this.rLink = rLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputer == null) ? 0 : inputer.hashCode());
		result = prime * result + ((inputtime == null) ? 0 : inputtime.hashCode());
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		result = prime * result + ((journal == null) ? 0 : journal.hashCode());
		result = prime * result + ((keywords == null) ? 0 : keywords.hashCode());
		result = prime * result + ((languages == null) ? 0 : languages.hashCode());
		result = prime * result + ((leftstr == null) ? 0 : leftstr.hashCode());
		result = prime * result + ((olang == null) ? 0 : olang.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + ((press == null) ? 0 : press.hashCode());
		result = prime * result + ((ptype == null) ? 0 : ptype.hashCode());
		result = prime * result + ((pyear == null) ? 0 : pyear.hashCode());
		result = prime * result + ((rLink == null) ? 0 : rLink.hashCode());
		result = prime * result + ((rPeriod == null) ? 0 : rPeriod.hashCode());
		result = prime * result + ((rVolume == null) ? 0 : rVolume.hashCode());
		result = prime * result + ((refe == null) ? 0 : refe.hashCode());
		result = prime * result + ((refs == null) ? 0 : refs.hashCode());
		result = prime * result + ((refstr == null) ? 0 : refstr.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + status;
		result = prime * result + ((synchdate == null) ? 0 : synchdate.hashCode());
		result = prime * result + synchstatus;
		result = prime * result + ((tchar == null) ? 0 : tchar.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((tpage == null) ? 0 : tpage.hashCode());
		result = prime * result + ((translator == null) ? 0 : translator.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		Ref other = (Ref) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
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
		if (isbn == null) {
			if (other.isbn != null)
				return false;
		} else if (!isbn.equals(other.isbn))
			return false;
		if (journal == null) {
			if (other.journal != null)
				return false;
		} else if (!journal.equals(other.journal))
			return false;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (languages == null) {
			if (other.languages != null)
				return false;
		} else if (!languages.equals(other.languages))
			return false;
		if (leftstr == null) {
			if (other.leftstr != null)
				return false;
		} else if (!leftstr.equals(other.leftstr))
			return false;
		if (olang == null) {
			if (other.olang != null)
				return false;
		} else if (!olang.equals(other.olang))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (press == null) {
			if (other.press != null)
				return false;
		} else if (!press.equals(other.press))
			return false;
		if (ptype == null) {
			if (other.ptype != null)
				return false;
		} else if (!ptype.equals(other.ptype))
			return false;
		if (pyear == null) {
			if (other.pyear != null)
				return false;
		} else if (!pyear.equals(other.pyear))
			return false;
		if (rLink == null) {
			if (other.rLink != null)
				return false;
		} else if (!rLink.equals(other.rLink))
			return false;
		if (rPeriod == null) {
			if (other.rPeriod != null)
				return false;
		} else if (!rPeriod.equals(other.rPeriod))
			return false;
		if (rVolume == null) {
			if (other.rVolume != null)
				return false;
		} else if (!rVolume.equals(other.rVolume))
			return false;
		if (refe == null) {
			if (other.refe != null)
				return false;
		} else if (!refe.equals(other.refe))
			return false;
		if (refs == null) {
			if (other.refs != null)
				return false;
		} else if (!refs.equals(other.refs))
			return false;
		if (refstr == null) {
			if (other.refstr != null)
				return false;
		} else if (!refstr.equals(other.refstr))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
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
		if (tchar == null) {
			if (other.tchar != null)
				return false;
		} else if (!tchar.equals(other.tchar))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (tpage == null) {
			if (other.tpage != null)
				return false;
		} else if (!tpage.equals(other.tpage))
			return false;
		if (translator == null) {
			if (other.translator != null)
				return false;
		} else if (!translator.equals(other.translator))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ref [id=" + id + ", ptype=" + ptype + ", refstr=" + refstr + ", author=" + author + ", pyear=" + pyear
				+ ", title=" + title + ", journal=" + journal + ", rVolume=" + rVolume + ", rPeriod=" + rPeriod
				+ ", refs=" + refs + ", refe=" + refe + ", isbn=" + isbn + ", place=" + place + ", press=" + press
				+ ", translator=" + translator + ", keywords=" + keywords + ", tpage=" + tpage + ", tchar=" + tchar
				+ ", languages=" + languages + ", olang=" + olang + ", version=" + version + ", remark=" + remark
				+ ", status=" + status + ", inputer=" + inputer + ", inputtime=" + inputtime + ", synchdate="
				+ synchdate + ", synchstatus=" + synchstatus + ", leftstr=" + leftstr + ", rLink=" + rLink + "]";
	}

}