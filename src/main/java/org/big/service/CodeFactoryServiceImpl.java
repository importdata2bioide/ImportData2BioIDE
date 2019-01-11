package org.big.service;

import java.util.List;

import org.big.repository.TaxonHasTaxtreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 * @author ZXY
 *
 */
@Service
public class CodeFactoryServiceImpl implements CodeFactoryService {
	 @Autowired
	 private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	 
	 final String  tableSchema = "biodata";
	 
	 public List<String> findAllTable () {
		 return taxonHasTaxtreeRepository.findAllTable(tableSchema);
	 }

	@Override
	public String batchExecuteCode(String[] tableNames) {
		StringBuffer codeStr = new StringBuffer();
		for (String tableName : tableNames) {
			List<Object[]> list = taxonHasTaxtreeRepository.findColumnByTable(tableName, tableSchema);
			String codes = createMethod(list);
			codeStr.append(codes);
		}
		return codeStr.toString();
	}

	private String createMethod(List<Object[]> list) {
//		for (Object[] obj : list) {
			//TABLE_NAME,COLUMN_NAME,DATA_TYPE,IS_NULLABLE,COLUMN_COMMENT
//			Object tableName = obj[0];
//			Object columnName = obj[1];
//			Object dataType = obj[2];
//			Object isNullable = obj[3];
//			Object columnComment = obj[4];
//		}
		return null;
	}

	
}
