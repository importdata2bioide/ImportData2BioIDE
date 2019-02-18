package org.big.service;

import org.big.common.EntityInit;
import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LanguageEnum;
import org.big.repository.DescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptionServiceImpl implements DescriptionService {
	@Autowired
	private DescriptionRepository descriptionRepository;

	@Override
	public Description findOneById(String desid) {
		return this.descriptionRepository.findOneById(desid);
	}

	@Override
	public void insertDescription(Descriptiontype descriptiontype, String descontent, Taxon taxon,
			BaseParamsForm params) {
		Description d = new Description();
		d.setTaxon(taxon);
		d.setExpert(params.getmLoginUser());
		d.setDestitle(descriptiontype.getName());
		d.setDescontent(descontent);
		d.setDescriptiontype(descriptiontype);
		d.setDestypeid(descriptiontype.getId());
		d.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
		d.setRefjson(taxon.getRefjson());
		EntityInit.initDescription(d, params);
		d.setSourcesid(taxon.getSourcesid());
		descriptionRepository.save(d);
	}


	
}