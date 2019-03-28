package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;
import org.big.common.StringJsonUserType;
import org.big.entityVO.RankEnum;
import org.hibernate.annotations.TypeDef;

import cn.afterturn.easypoi.excel.annotation.Excel;

/*import cn.afterturn.easypoi.excel.annotation.Excel;*/

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxon的Entity类</b></p>
 *<p> Taxon的Entity类</p>
 * @author BINZI
 *<p>Created date: 2018/4/8 17:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "taxon", schema = "biodata")
@TypeDef(name= "StringJsonUserType", typeClass = StringJsonUserType.class)
public class Taxon implements Serializable {
	private static final long serialVersionUID = 1L;


	@Id
	private String id;
	@Excel(name="学名", orderNum = "0")
	private String scientificname;
	@Excel(name="中文名", orderNum = "1")
	private String chname;
	@Excel(name="命名信息", orderNum = "2")
	private String authorstr;
	@Excel(name="rank", orderNum = "3")
	private int rankid;
	@Excel(name="备注", orderNum = "4", width = 50.00)
	private String remark;
	@Excel(name="种加词",  groupName = "亚种加词", orderNum = "5")
	private String epithet;
	@Excel(name="录入时间",  orderNum = "6")
	@Temporal(TemporalType.TIMESTAMP)
	private Date inputtime;
	
	private String expert; // Taxon审核人

	
	private String inputer;
	
	private int orderNum;//按照insert顺序在分类单元集内递增

	
	
	

	

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}


	/*@Excel(name="命名法规", orderNum = "4")*/
	private String nomencode;
	
	
	private String refjson;

	
	private String sourcesid;

	private int status;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchdate;

	private int synchstatus;

	private String tci;
	
	private String taxonExamine; // Taxon审核人
	
	private int taxonCondition;	// Taxon审核状态 0 - 未提交审核， 1 - 已提交审核， -1 - 审核不通过， 2 - 审核通过

	//bi-directional many-to-one association to Citation
	@OneToMany(mappedBy="taxon")
	private List<Citation> citations;

	//bi-directional many-to-one association to Description
	@OneToMany(mappedBy="taxon")
	private List<Description> descriptions;

	//bi-directional many-to-one association to Distributiondata
	@OneToMany(mappedBy="taxon")
	private List<Distributiondata> distributiondata;

	//bi-directional many-to-one association to Molecular
	@OneToMany(mappedBy="taxon")
	private List<Molecular> moleculars;

	//bi-directional many-to-one association to Multimedia
	@OneToMany(mappedBy="taxon")
	private List<Multimedia> multimedias;

	//bi-directional many-to-one association to Occurrence
	@OneToMany(mappedBy="taxon")
	private List<Occurrence> occurrences;

	//bi-directional many-to-one association to Protection
	@OneToMany(mappedBy="taxon")
	private List<Protection> protections;

//	//bi-directional many-to-one association to Specimendata
//	@OneToMany(mappedBy="taxon")
//	private List<Specimendata> specimendata;

	//bi-directional many-to-one association to Taxkey
	@OneToMany(mappedBy="taxon")
	private List<Taxkey> taxkeys;

	@OneToMany(mappedBy="taxon")
	private List<Commonname> commonnames;
	//bi-directional many-to-one association to Rank
	@ManyToOne
	private Rank rank;

	//bi-directional many-to-one association to Taxaset
	@ManyToOne
	private Taxaset taxaset;

	//bi-directional many-to-many association to Taxtree
	@ManyToMany(mappedBy="taxons")
	private List<Taxtree> taxtrees;

	//bi-directional many-to-one association to Traitdata
	@OneToMany(mappedBy="taxon")
	private List<Traitdata> traitdata;
	
	public Taxon() {
	}
	
	
	
	public Taxon(String id) {
		super();
		this.id = id;
	}

	public Taxon(String scientificname, String chname, String authorstr, String epithet, String nomencode, 
			String remark) {
		super();
		this.authorstr = authorstr;
		this.epithet = epithet;
		this.nomencode = nomencode;
		this.remark = remark;
		this.scientificname = scientificname;
		this.chname = chname;
	}


	public Taxon(String id,String rankId) {
		this.id = id;
		this.rankid = Integer.parseInt(rankId);
		Rank r = new Rank();
		r.setId(rankId);
		this.rank = r;
		
	}

	public String getExpert() {
		return expert;
	}

	public void setExpert(String expert) {
		this.expert = expert;
	}

	public List<Commonname> getCommonnames() {
		return commonnames;
	}

	public void setCommonnames(List<Commonname> commonnames) {
		this.commonnames = commonnames;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChname() {
		return chname;
	}

	public void setChname(String chname) {
		if(StringUtils.isNotEmpty(chname)) {
			chname = chname.trim();
		}
		this.chname = chname;
	}

	public String getAuthorstr() {
		return this.authorstr;
	}

	public void setAuthorstr(String authorstr) {
		if(StringUtils.isNotEmpty(authorstr)) {
			authorstr = authorstr.trim();
		}
		this.authorstr = authorstr;
	}

	public String getEpithet() {
		return this.epithet;
	}

	public void setEpithet(String epithet) {
		if(StringUtils.isNotEmpty(epithet)) {
			epithet = epithet.trim();
		}
		this.epithet = epithet;
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

	public void setInputtime(Timestamp inputtime) {
		this.inputtime = inputtime;
	}

	public String getNomencode() {
		return this.nomencode;
	}

	public void setNomencode(String nomencode) {
		this.nomencode = nomencode;
	}

	
	public int getRankid() {
		return rankid;
	}

	public void setRankid(int rankid) {
		scinameAndRankToEpithet(rankid,this.scientificname);
		this.rankid = rankid;
	}
	
	private void scinameAndRankToEpithet(int rankid,String sciname) {
		if(rankid!=0 && StringUtils.isNotEmpty(sciname)) {
			if(rankid == RankEnum.species.getIndex()) {
				this.epithet = sciname.substring(sciname.lastIndexOf(" ")+1).trim();
			}else if(rankid == RankEnum.subsp.getIndex()) {
				this.epithet = sciname.substring(sciname.lastIndexOf(" ")+1).trim();
			}
		}
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public void setSynchdate(Date synchdate) {
		this.synchdate = synchdate;
	}

	public String getRefjson() {
		return this.refjson;
	}

	public void setRefjson(String refjson) {
		this.refjson = refjson;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getScientificname() {
		return this.scientificname;
	}

	public void setScientificname(String scientificname) {
		if(StringUtils.isNotEmpty(scientificname)) {
			scientificname = scientificname.trim();
		}
		scinameAndRankToEpithet(this.rankid,scientificname);
		this.scientificname = scientificname;
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

	public void setSynchdate(Timestamp synchdate) {
		this.synchdate = synchdate;
	}

	public int getSynchstatus() {
		return this.synchstatus;
	}

	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}

	public String getTci() {
		return this.tci;
	}

	public void setTci(String tci) {
		this.tci = tci;
	}

	public List<Citation> getCitations() {
		return this.citations;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	public Citation addCitation(Citation citation) {
		getCitations().add(citation);
		citation.setTaxon(this);

		return citation;
	}

	public Citation removeCitation(Citation citation) {
		getCitations().remove(citation);
		citation.setTaxon(null);

		return citation;
	}

	public List<Description> getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}

	public Description addDescription(Description description) {
		getDescriptions().add(description);
		description.setTaxon(this);

		return description;
	}

	public Description removeDescription(Description description) {
		getDescriptions().remove(description);
		description.setTaxon(null);

		return description;
	}

	public List<Distributiondata> getDistributiondata() {
		return this.distributiondata;
	}

	public void setDistributiondata(List<Distributiondata> distributiondata) {
		this.distributiondata = distributiondata;
	}

	public Distributiondata addDistributiondata(Distributiondata distributiondata) {
		getDistributiondata().add(distributiondata);
		distributiondata.setTaxon(this);

		return distributiondata;
	}

	public Distributiondata removeDistributiondata(Distributiondata distributiondata) {
		getDistributiondata().remove(distributiondata);
		distributiondata.setTaxon(null);

		return distributiondata;
	}

	public List<Molecular> getMoleculars() {
		return this.moleculars;
	}

	public void setMoleculars(List<Molecular> moleculars) {
		this.moleculars = moleculars;
	}

	public Molecular addMolecular(Molecular molecular) {
		getMoleculars().add(molecular);
		molecular.setTaxon(this);

		return molecular;
	}

	public Molecular removeMolecular(Molecular molecular) {
		getMoleculars().remove(molecular);
		molecular.setTaxon(null);

		return molecular;
	}

	public List<Multimedia> getMultimedias() {
		return this.multimedias;
	}

	public void setMultimedias(List<Multimedia> multimedias) {
		this.multimedias = multimedias;
	}

	public Multimedia addMultimedia(Multimedia multimedia) {
		getMultimedias().add(multimedia);
		multimedia.setTaxon(this);

		return multimedia;
	}

	public Multimedia removeMultimedia(Multimedia multimedia) {
		getMultimedias().remove(multimedia);
		multimedia.setTaxon(null);

		return multimedia;
	}

	public List<Occurrence> getOccurrences() {
		return this.occurrences;
	}

	public void setOccurrences(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}

	public Occurrence addOccurrence(Occurrence occurrence) {
		getOccurrences().add(occurrence);
		occurrence.setTaxon(this);

		return occurrence;
	}

	public Occurrence removeOccurrence(Occurrence occurrence) {
		getOccurrences().remove(occurrence);
		occurrence.setTaxon(null);

		return occurrence;
	}

	public List<Protection> getProtections() {
		return this.protections;
	}

	public void setProtections(List<Protection> protections) {
		this.protections = protections;
	}

	public Protection addProtection(Protection protection) {
		getProtections().add(protection);
		protection.setTaxon(this);

		return protection;
	}

	public Protection removeProtection(Protection protection) {
		getProtections().remove(protection);
		protection.setTaxon(null);

		return protection;
	}

//	public List<Specimendata> getSpecimendata() {
//		return this.specimendata;
//	}
//
//	public void setSpecimendata(List<Specimendata> specimendata) {
//		this.specimendata = specimendata;
//	}

//	public Specimendata addSpecimendata(Specimendata specimendata) {
//		getSpecimendata().add(specimendata);
//
//		return specimendata;
//	}
//
//	public Specimendata removeSpecimendata(Specimendata specimendata) {
//		getSpecimendata().remove(specimendata);
//		return specimendata;
//	}

	public List<Taxkey> getTaxkeys() {
		return this.taxkeys;
	}

	public void setTaxkeys(List<Taxkey> taxkeys) {
		this.taxkeys = taxkeys;
	}

	public Taxkey addTaxkey(Taxkey taxkey) {
		getTaxkeys().add(taxkey);
		taxkey.setTaxon(this);

		return taxkey;
	}

	public Taxkey removeTaxkey(Taxkey taxkey) {
		getTaxkeys().remove(taxkey);
		taxkey.setTaxon(null);

		return taxkey;
	}

	public Rank getRank() {
		return this.rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public Taxaset getTaxaset() {
		return this.taxaset;
	}

	public void setTaxaset(Taxaset taxaset) {
		this.taxaset = taxaset;
	}

	public List<Taxtree> getTaxtrees() {
		return this.taxtrees;
	}

	public void setTaxtrees(List<Taxtree> taxtrees) {
		this.taxtrees = taxtrees;
	}

	public List<Traitdata> getTraitdata() {
		return this.traitdata;
	}

	public void setTraitdata(List<Traitdata> traitdata) {
		this.traitdata = traitdata;
	}

	public Traitdata addTraitdata(Traitdata traitdata) {
		getTraitdata().add(traitdata);
		traitdata.setTaxon(this);

		return traitdata;
	}

	public Traitdata removeTraitdata(Traitdata traitdata) {
		getTraitdata().remove(traitdata);
		traitdata.setTaxon(null);

		return traitdata;
	}


	public String getTaxonExamine() {
		return taxonExamine;
	}

	public void setTaxonExamine(String taxonExamine) {
		this.taxonExamine = taxonExamine;
	}

	public int getTaxonCondition() {
		return taxonCondition;
	}

	public void setTaxonCondition(int taxonCondition) {
		this.taxonCondition = taxonCondition;
	}

	
	@Override
	public String toString() {
		return "Taxon [authorstr=" + authorstr + ", epithet=" + epithet + ", nomencode=" + nomencode + ", remark="
				+ remark + ", scientificname=" + scientificname + ", chname=" + chname + "]";
	}

}