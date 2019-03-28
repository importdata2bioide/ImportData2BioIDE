package org.big.common;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Multimedia;
import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LicenseEnum;
import org.big.entityVO.RankEnum;

public class EntityInit {

	public static void initDistributiondata(Distributiondata record, BaseParamsForm params) {
		String id = record.getId();
		if (CommUtils.isStrEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		record.setInputer(params.getmLoginUser());
		record.setSourcesid(params.getmSourcesid());
		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);
		Taxaset taxaset = new Taxaset();
		taxaset.setId(params.getmTaxasetId());
		record.setStatus(1);
		record.setSynchstatus(0);

	}

	public static void initCitation(Citation record, BaseParamsForm params) {
		String id = record.getId();
		if (CommUtils.isStrEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		record.setInputer(params.getmLoginUser());

		if (CommUtils.isStrNotEmpty(params.getmSourcesid())) {
			record.setSourcesid(params.getmSourcesid());
		}

		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);

		record.setStatus(1);
		record.setSynchstatus(0);

	}

	public static void initCommonname(Commonname record, BaseParamsForm params) {
		String id = record.getId();
		if (CommUtils.isStrEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		record.setInputer(params.getmLoginUser());

		if (CommUtils.isStrNotEmpty(params.getmSourcesid())) {
			record.setSourcesid(params.getmSourcesid());
		}
		if (CommUtils.isStrEmpty(params.getmInputtimeStr())) {
			params.setmInputtimeStr(CommUtils.getCurrentDate());
		}
		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);

		record.setStatus(1);
		record.setSynchstatus(0);
	}

	public static void initTaxon(Taxon record, BaseParamsForm params) {
		//主键
		String id = record.getId();
		if (CommUtils.isStrEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		//录入人
		record.setInputer(params.getmLoginUser());
		//数据源
		if (CommUtils.isStrNotEmpty(params.getmSourcesid())) {
			record.setSourcesid(params.getmSourcesid());
		}
		//录入时间
		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);
		//数据集
		Taxaset taxaset = new Taxaset();
		taxaset.setId(params.getmTaxasetId());
		record.setTaxaset(taxaset);
		//rank 分类等级,默认为species
		if(record.getRankid() == 0 && record.getRank() == null) {
			record.setRankid(RankEnum.species.getIndex());
		}
		if(record.getRankid()!= 0) {
			Rank rank = new Rank();
			rank.setId(String.valueOf(record.getRankid()));
			record.setRank(rank);
		}
		record.setStatus(1);
		record.setSynchstatus(0);
		record.setTaxonCondition(1);//审核状态
	}

	public static void initMultimedia(Multimedia record, BaseParamsForm params) {
		//主键
		if (StringUtils.isEmpty(record.getId())) {
			record.setId(UUIDUtils.getUUID32());
		}
		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		record.setInputer(params.getmLoginUser());
		record.setSourcesid(params.getmSourcesid());

		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);

		record.setStatus(1);
		record.setSynchstatus(0);
	}

	public static void initDescription(Description record, BaseParamsForm params) {
		if (StringUtils.isEmpty(record.getId())) {
			record.setId(UUIDUtils.getUUID32());
		}
		Timestamp timestamp = null;
		if (StringUtils.isEmpty(params.getmInputtimeStr())) {
			timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
		} else {
			timestamp = CommUtils.getTimestamp(params.getmInputtimeStr());
		}
		if(StringUtils.isEmpty(record.getLicenseid())) {
			record.setLicenseid(String.valueOf(LicenseEnum.BYSA.getIndex()));
		}
		record.setInputer(params.getmLoginUser());
		record.setSourcesid(params.getmSourcesid());

		record.setInputtime(timestamp);
		record.setSynchdate(timestamp);

		record.setStatus(1);
		record.setSynchstatus(0);
		if(StringUtils.isEmpty(record.getLicenseid())) {
			record.setLicenseid("4");
		}
	}

}
