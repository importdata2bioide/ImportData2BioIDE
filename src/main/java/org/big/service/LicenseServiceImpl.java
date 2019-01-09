package org.big.service;

import org.big.entity.License;
import org.big.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService{
	@Autowired
	private LicenseRepository licenseRepository;

	@Override
	public License findOneById(String id) {
		return this.licenseRepository.findOneById(id);
	}
}
