package org.big.service;

import org.big.entity.Rank;
import org.big.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private RankRepository rankRepository;

	@Override
	public void insertTransonalTest() {
		Rank entity = new Rank();
		entity.setId("100");
		entity.setChname("测试1");
		entity.setEnname("测试1");
		rankRepository.save(entity );
		
		Rank entity2 = new Rank();
		entity2.setId("101");
		entity2.setChname("测试2");
		entity2.setEnname("测试2");
		rankRepository.save(entity2 );
		
	}

}
