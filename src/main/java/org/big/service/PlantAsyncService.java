package org.big.service;

import java.util.List;
import java.util.Map;

import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.RankEnum;

public interface PlantAsyncService {

	void insertListExcel(BaseParamsForm baseParamsForm, List<String> partFiles, Map<String, String> map)throws Exception;
	
	RankEnum judgeRankIsWhatByPath(String path);
}
