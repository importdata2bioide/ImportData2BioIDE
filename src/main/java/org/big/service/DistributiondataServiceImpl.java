package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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


}
