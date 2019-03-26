package org.big.service;

import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;

public interface ParseLine {

	Taxon parseClass(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseOrder(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseFamily(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

	Taxon parseSubfamily(String line, BaseParamsForm baseParamsForm,LineStatus thisLineStatus);

}
