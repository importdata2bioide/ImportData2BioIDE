package org.big.service;

import org.big.entity.Geoobject;
import org.big.repository.GeoobjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoobjectServiceImpl implements GeoobjectService{

	@Autowired
	private GeoobjectRepository geoobjectRepository;
	
	
	@Override
	public Geoobject findOneById(String geoobjectId) {
		return this.geoobjectRepository.findOneById(geoobjectId);
	}

}
