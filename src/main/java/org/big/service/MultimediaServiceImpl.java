package org.big.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.big.entity.Multimedia;
import org.big.entity.Taxon;
import org.big.entity.UserDetail;
import org.big.repository.MultimediaRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class MultimediaServiceImpl implements MultimediaService {
	@Autowired
	private MultimediaRepository multimediaRepository;
	
	
	@Autowired
	private TaxonRepository taxonRepository;
	


	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String multimediaId = request.getParameter("multimediaId");
		if (StringUtils.isNotBlank(multimediaId)) {
			if (null != this.multimediaRepository.findOneById(multimediaId)) {
				this.multimediaRepository.deleteOneById(multimediaId);
			}
			return true;
		}
		return false;
	}

	@Override
	public void saveMultimedia(String taxonId, Multimedia thisMultimedia) {
		Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisMultimedia.setInputer(thisUser.getId());
		thisMultimedia.setInputtime(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setTaxon(thisTaxon);
		thisMultimedia.setStatus(1);
		thisMultimedia.setSynchstatus(0);
		
		this.multimediaRepository.save(thisMultimedia);
	}

	
	

}
