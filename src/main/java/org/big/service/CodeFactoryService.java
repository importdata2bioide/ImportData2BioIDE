package org.big.service;

import java.util.List;

public interface CodeFactoryService {

	public List<String> findAllTable ();

	public String batchExecuteCode(String[] tableNames);
}