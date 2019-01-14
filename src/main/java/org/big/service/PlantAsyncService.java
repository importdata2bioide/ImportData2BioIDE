package org.big.service;

import java.util.List;
import java.util.Map;

import org.big.entityVO.BaseParamsForm;

public interface PlantAsyncService {

	void readSomeExcel(BaseParamsForm baseParamsForm, List<String> partFiles, Map<String, String> map)throws Exception;

}
