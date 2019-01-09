package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.big.repository.OccurrenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccurrenceServiceImpl implements OccurrenceService {
	@Autowired
	private OccurrenceRepository occurrenceRepository;

	
	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String occurrenceId = request.getParameter("occurrenceId");
		if (StringUtils.isNotBlank(occurrenceId)) {
			if (null != this.occurrenceRepository.findOneById(occurrenceId)) {
				this.occurrenceRepository.deleteOneById(occurrenceId);
			}
			return true;
		}
		return false;
	}

}
