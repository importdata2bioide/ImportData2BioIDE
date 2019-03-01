package org.big.service;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Datasource;
import org.big.repository.DatasourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatasourceServiceImpl implements DatasourceService {
	@Autowired
	private DatasourceRepository datasourceRepository;	 

	@Override
	public Datasource findOneByTitleAndInputer(String title, String inputer) {
		return datasourceRepository.findOneByTitleAndInputer(title, inputer);
	}

	
	@Override
	public Datasource insertDSIfNotExist(String title, String inputerId, String expertId) {
		Datasource datasource = datasourceRepository.findOneByTitleAndInputer(title, inputerId);
		if(datasource == null || StringUtils.isBlank(datasource.getId())) {
			System.out.println("insert数据源");
			datasource = new Datasource();
			datasource.setId(UUIDUtils.getUUID32());
			datasource.setTitle(title);
			datasource.setCreater("系统");
			datasource.setVersions("version 1.0");
			datasource.setdAbstract("-");
			datasource.setdType("4");
			datasource.setInputer(inputerId);
			datasource.setdVerifier(expertId);
			datasource.setStatus(1);
			datasource.setSynchstatus(0);
			Timestamp timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
			datasource.setInputtime(timestamp);
			datasource.setSynchdate(timestamp);
			datasourceRepository.save(datasource);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datasource;
	}

	@Override
	public Datasource findOneById(String id) {
		return datasourceRepository.findOneById(id);
	}

}
