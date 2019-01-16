package org.big.service;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface CodeFactoryService {

	public List<String> findAllTable ();

	public String batchExecuteCode(String[] tableNames);

	public Connection connDB(HttpServletRequest requst) throws Exception;

	public List<String> findAllTable(HttpServletRequest requst) throws Exception;
}