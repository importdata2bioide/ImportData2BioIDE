package org.big.service;


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
}
