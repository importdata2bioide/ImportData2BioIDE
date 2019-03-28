package org.big.entityVO;

import org.big.entity.Taxon;

/**
 * LineStatus 临时状态 purpose 中国生物物种名录
 * 
 * @author BIGIOZ
 *
 */
public class LineStatus {
	private Taxon class_;// 纲
	private Taxon order;// 目
	private Taxon superfamily;// 超科
	private Taxon family;// 科
	private Taxon subfamily;// 亚科
	private Taxon tribe;// 族
	private Taxon subtribe;// 亚族
	private Taxon genus;// 属
	private Taxon subgenus;// 亚属
	private Taxon species;// 种
	private Taxon subspecies;// 亚种

	private SpeciesCatalogueEnum lastLineType;
	
	public Taxon getParentOfSubspecies() {
		if (species != null) {
			return species;
		}
		if (subgenus != null) {
			return subgenus;
		}
		if (genus != null) {
			return genus;
		}
		return null;
	}
	public Taxon getParentOfSpecies() {
		if (subgenus != null) {
			return subgenus;
		}
		if (genus != null) {
			return genus;
		}
		return null;
	}
	public Taxon getParentOfSubgenus() {
		if (genus != null) {
			return genus;
		}
		return null;
	}
	public Taxon getParentOfGenus() {
		if (subtribe != null) {
			return subtribe;
		}
		if (tribe != null) {
			return tribe;
		}
		if (subfamily != null) {
			return subfamily;
		}
		if (family != null) {
			return family;
		}
		return null;
	}
	public Taxon getParentOfSubTribe() {
		if (tribe != null) {
			return tribe;
		}
		if (subfamily != null) {
			return subfamily;
		}
		if (family != null) {
			return family;
		}
		return null;
	}
	public Taxon getParentOfTribe() {
		if (subfamily != null) {
			return subfamily;
		}
		if (family != null) {
			return family;
		}
		return null;
	}
	
	public Taxon getParentOfSubFamily() {
		if (family != null) {
			return family;
		}
		return null;
	}
	
	
	public Taxon getParentOfFamily() {
		if (superfamily != null) {
			return superfamily;
		}
		if (order != null) {
			return order;
		}
		return null;
	}

	/**
	 * 
	 * @Description superfamily的上一级
	 * @return
	 * @author ZXY
	 */
	public Taxon getParentOfSuperfamily() {
		if (order != null) {
			return order;
		}
		return null;
	}

	public Taxon getParentOfOrder() {
		if (class_ != null) {
			return class_;
		}
		return null;
	}
	public Taxon getParentOfClass() {
		return null;
	}

	// 纲
	public void clearUnderClass() {
		this.order = null;
		clearUnderOrder();
	}

	// 目
	public void clearUnderOrder() {
		this.superfamily = null;
		clearUnderSuperfamily();
	}

	// 超科
	public void clearUnderSuperfamily() {
		this.family = null;
		clearUnderFamily();
	}

	// 科
	public void clearUnderFamily() {
		this.subfamily = null;
		clearUnderSubfamily();
	}

	// 亚科
	public void clearUnderSubfamily() {
		this.tribe = null;
		clearUnderTribe();
	}

	// 族
	public void clearUnderTribe() {
		this.subtribe = null;
		clearUnderSubtribe();
	}

	// 亚族
	public void clearUnderSubtribe() {
		this.genus = null;
		clearUnderGenus();
	}

	// 属
	public void clearUnderGenus() {
		this.subgenus = null;
		clearUnderSubgenus();
	}

	// 亚属
	public void clearUnderSubgenus() {
		this.species = null;
		clearUnderSpecies();
	}

	// 种
	public void clearUnderSpecies() {
		this.subspecies = null;
	}

	public Taxon getClass_() {
		return class_;
	}

	public void setClass_(Taxon class_) {
		this.class_ = class_;
	}

	public Taxon getOrder() {
		return order;
	}

	public void setOrder(Taxon order) {
		this.order = order;
	}

	public Taxon getSuperfamily() {
		return superfamily;
	}

	public void setSuperfamily(Taxon superfamily) {
		this.superfamily = superfamily;
	}

	public Taxon getFamily() {
		return family;
	}

	public void setFamily(Taxon family) {
		this.family = family;
	}

	public Taxon getSubfamily() {
		return subfamily;
	}

	public void setSubfamily(Taxon subfamily) {
		this.subfamily = subfamily;
	}

	public Taxon getGenus() {
		return genus;
	}

	public void setGenus(Taxon genus) {
		this.genus = genus;
	}

	public SpeciesCatalogueEnum getLastLineType() {
		return lastLineType;
	}

	public void setLastLineType(SpeciesCatalogueEnum lastLineType) {
		this.lastLineType = lastLineType;
	}

	public Taxon getSubgenus() {
		return subgenus;
	}

	public void setSubgenus(Taxon subgenus) {
		this.subgenus = subgenus;
	}

	public Taxon getTribe() {
		return tribe;
	}

	public void setTribe(Taxon tribe) {
		this.tribe = tribe;
	}

	public Taxon getSubtribe() {
		return subtribe;
	}

	public void setSubtribe(Taxon subtribe) {
		this.subtribe = subtribe;
	}

	public Taxon getSpecies() {
		return species;
	}

	public void setSpecies(Taxon species) {
		this.species = species;
	}

	public Taxon getSubspecies() {
		return subspecies;
	}

	public void setSubspecies(Taxon subspecies) {
		this.subspecies = subspecies;
	}

}
