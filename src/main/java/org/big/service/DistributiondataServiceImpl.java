package org.big.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.sp2000.entity.Distribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class DistributiondataServiceImpl implements DistributiondataService {
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;


	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String distributiondataId = request.getParameter("distributiondataId");
		if (StringUtils.isNotBlank(distributiondataId)) {
			if (null != this.distributiondataRepository.findOneById(distributiondataId)) {
				this.distributiondataRepository.deleteOneById(distributiondataId);
			}
			return true;
		}
		return false;
	}



	@Override
	public Distributiondata saveOne(Distributiondata record) {
		String id = record.getId();
		if (StringUtils.isEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		record.setInputtime(new Date());
		record.setSynchdate(new Date());
		record.setStatus(1);
		record.setSynchstatus(0);
		distributiondataRepository.save(record);
		return record;
	}



	@Override
	public List<Distribution> getDistributionByTaxaset(String taxasetId) {
		List<Object[]> list = distributiondataRepository.findDistributionByTaxaset(taxasetId);
		List<Distribution> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			Distribution entity = new Distribution();
			entity.setRecordId(obj[0].toString());
			entity.setNameCode(obj[1].toString());
			String geojson = obj[2].toString();
			JSONObject jsonObject = CommUtils.strToJSONObject(geojson);
			String[] geos = jsonObject.get("geoIds").toString().split("&");
			StringBuffer str = new StringBuffer();
			for (String geoId : geos) {
				Geoobject geoobject = geoobjectRepository.findOneById(geoId);
				if(geoobject == null) {
					throw new ValidationException("找不到分布地，Geoobject.id="+geoId);
				}
				str.append(geoobject.getCngeoname());
				str.append(",");
			}
			String distributionC = str.toString().substring(0,str.toString().length()-1);//去掉最后的逗号
			entity.setDistributionC(distributionC);
			entity.setDistributionBack(obj[3].toString());
			resultlist.add(entity);
		}
		return resultlist;
	}


}
