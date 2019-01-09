package org.big.entityVO;

import org.big.entity.Taxon;

/**
 * LineStatus 临时状态
 * purpose 中国生物物种名录
 * @author BIGIOZ
 *
 */
public class LineStatus {
	
	public Taxon superfamily;
	public Taxon family;
	public Taxon subfamily;
	public Taxon genus;
	
	public SpeciesCatalogueEnum lastLineType;
	public Taxon subgenus;
	public Taxon tribe;
	public Taxon subtribe;
	public Taxon species;
	public Taxon subspecies;
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
