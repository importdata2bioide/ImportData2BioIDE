package org.big.service;

import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;
import org.big.entityVO.RankEnum;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("parseLineFishWord")
public class ParseLineFishWord implements ParseLine {

	@Override
	public Taxon parseClass(String line, BaseParamsForm params,LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfClass();
		Taxon taxon = subWithPointChar(line, params, "纲",parent);
		return taxon;
	}


	@Override
	public Taxon parseOrder(String line, BaseParamsForm params,LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfOrder();
		Taxon taxon = subWithPointChar(line, params, "目",parent);
		return taxon;
	}

	@Override
	public Taxon parseFamily(String line, BaseParamsForm params,LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfFamily();
		Taxon taxon = subWithPointChar(line, params, "科",parent);
		return taxon;
	}

	@Override
	public Taxon parseSubfamily(String line, BaseParamsForm params,LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfSubFamily();
		Taxon taxon = subWithPointChar(line, params, "亚科",parent);
		return taxon;
	}
	
	private Taxon subWithPointChar(String line, BaseParamsForm params, String pointChar,Taxon parent) {
		Taxon record = new Taxon();
		int index = line.indexOf(pointChar) + 1;
		String chname = line.substring(0, index);// 中文名称
		String sciname = line.substring(index);
		record.setChname(chname);
		record.setScientificname(sciname);
		record.setRankid(RankEnum.Class.getIndex());
		JSONObject jsonRemark = new JSONObject();
		jsonRemark.put(CommUtils.yuanwen, line);
		if(parent != null) {
			jsonRemark.put(CommUtils.parentId, parent.getId());
			jsonRemark.put(CommUtils.parentName, parent.getScientificname());
		}
		record.setRemark(String.valueOf(jsonRemark));
		EntityInit.initTaxon(record, params);
		return record;
	}

}
