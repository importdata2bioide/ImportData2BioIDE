package org.big.service;


import org.big.common.UUIDUtils;
import org.big.entity.Descriptiontype;
import org.big.repository.DescriptiontypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptiontypeServiceImpl implements DescriptiontypeService {
	@Autowired
	private DescriptiontypeRepository descriptiontypeRepository;
	
	
	@Override
	public Descriptiontype findOneById(Integer id) {
		return this.descriptiontypeRepository.findOneById(id);
	}

	@Override
	public Descriptiontype findOneByName(String name) {
		return descriptiontypeRepository.findOneByName(name);
	}

	@Override
	public Descriptiontype insertOneDescType(String name, String style) {
		Descriptiontype d = new Descriptiontype();
		d.setId(UUIDUtils.getUUID32());
		d.setName(name);
		d.setStyle(style+""+name);
		d.setPid("0");
		descriptiontypeRepository.save(d);
		return d;
	}
}
