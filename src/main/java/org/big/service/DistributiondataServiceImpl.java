package org.big.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Distributiondata;
import org.big.repository.DistributiondataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributiondataServiceImpl implements DistributiondataService {
	@Autowired
	private DistributiondataRepository distributiondataRepository;


	
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


}
