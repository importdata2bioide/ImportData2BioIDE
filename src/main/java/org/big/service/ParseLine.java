package org.big.service;

import java.util.List;
import java.util.Map;

import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;

public interface ParseLine {

	Taxon parseClass(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseOrder(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseFamily(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseSubfamily(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseGenus(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus, Map<String, String> genusMap);

	Taxon parseSubgenus(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus,
			Map<String, String> genusMap);

	Taxon parseSpecies(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus,
			Map<String, String> speciesMap);

	Taxon parseSubspecies(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus);

	Citation parseCitation(String line, Taxon preTaxon,BaseParamsForm baseParamsForm);
	
	String handCitationLine(String line);

	List<Commonname> parseCommonName(String line, Taxon preTaxon, BaseParamsForm baseParamsForm);

	Description parseDesc(String line, Taxon preTaxon, BaseParamsForm baseParamsForm, String string);

	Distributiondata parseDistribution(String line, Taxon preTaxon, BaseParamsForm baseParamsForm, Description desc);

	List<Ref> parseRefs(String line, Taxon preTaxon, BaseParamsForm baseParamsForm);
}
