package org.big.service;

import org.big.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {
	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public boolean delOne(String id) {
		if (null != this.resourceRepository.findOneById(id)) {
			this.resourceRepository.deleteOneById(id);
			return true;
		}
		return false;
	}
	
}
