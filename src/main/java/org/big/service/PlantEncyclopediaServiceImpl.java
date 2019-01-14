package org.big.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.RankEnum;
import org.big.repository.TaxasetRepository;
import org.big.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class PlantEncyclopediaServiceImpl implements PlantEncyclopediaService {
	private final static Logger logger = LoggerFactory.getLogger(PlantEncyclopediaServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private PlantAsyncService plantAsyncService;
	@Autowired
	private ExpertService expertService;
	private final int groupSize = 1000;

	@Override
	public String insertPlantEncyclopedia(BaseParamsForm baseParamsForm) throws Exception {

		// validate 必填验证
		validate(baseParamsForm);
		// get 获取所有文件
		List<String> allFiles = CommUtils.getAllFiles(baseParamsForm.getFilePath(), null);
//		countByRank(allFiles);
		// group 拆分为多个数组
		List<List<String>> groupFiles = CommUtils.groupList(allFiles, groupSize);
		logger.info("groupFiles.size():" + groupFiles.size() + ",allFiles.size()：" + allFiles.size());
		Map<String, String> map = new HashedMap<>();
		for (List<String> partFiles : groupFiles) {
			try {
				plantAsyncService.readSomeExcel(baseParamsForm, partFiles, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "OK";

	}

	private void countByRank(List<String> allFiles) {
		int species = 0;
		int other = 0;

		for (String path : allFiles) {
			if(path.contains("~$")) {
				continue;
			}
			String[] splitPath = StringUtils.split(path, "\\");
			
			String excelName = splitPath[splitPath.length-1].replace("．", ".");
			String upperRank = splitPath[splitPath.length-2].replace("．", ".");
			String onlyChUpperRank = CommUtils.cutByStrAfter(upperRank, ".");
			String chineseName = CommUtils.cutByStrAfter(CommUtils.cutByStrBefore(excelName, ".x"), ".").trim();
			
			if (excelName.contains("变种") && !excelName.contains("含")) {
				//变种
				other++;
			} else if (excelName.contains("变种") && excelName.contains("含")) {
				//种
				species++;
			} else if (excelName.contains("亚种")) {
				//亚种
				other++;
			} else {
				if(upperRank.contains("组") || upperRank.contains("属") || upperRank.contains("系")) {
					//种
					species++;
				}else if(onlyChUpperRank.equals(chineseName)){
					//种
					species++;
				}else if(chineseName.contains(onlyChUpperRank)){
					//变种
					other++;
				}else {
					//
					other++;
//					logger.info(onlyChUpperRank+",excelName:"+ chineseName+","+path);
					
				}
				
			}
		}
		logger.info("总计："+allFiles.size());
		logger.info("种："+species);
		logger.info("种下："+other);

	}

	/**
	 * 
	 * title: PlantEncyclopediaServiceImpl.java
	 * 
	 * @param baseParamsForm
	 * @author ZXY
	 */
	private void validate(BaseParamsForm baseParamsForm) {
		JSONObject jsonObject = new JSONObject();
		if (CommUtils.isStrEmpty(baseParamsForm.getmLoginUser())) {
			jsonObject.put("录入人id", "不能为空");
		} else {
			if (userRepository.findOneById(baseParamsForm.getmLoginUser()) == null) {
				jsonObject.put("录入人[id = " + baseParamsForm.getmLoginUser() + "]", "不存在");
			}
		}
		// 审核人
		if (CommUtils.isStrEmpty(baseParamsForm.getmExpert())) {
			jsonObject.put("审核人id", "不能为空");
		} else {
			if (expertService.findOneById(baseParamsForm.getmExpert()) == null) {
				jsonObject.put("审核人[id = " + baseParamsForm.getmExpert() + "]", "不存在");
			}
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getmInputtimeStr())) {
			baseParamsForm.setmInputtimeStr(CommUtils.getCurrentDate());
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getmTaxasetId())) {
			jsonObject.put("分类单元集id", "不能为空");
		} else {
			if (taxasetRepository.findOneById(baseParamsForm.getmTaxasetId()) == null) {
				jsonObject.put("分类单元集" + "[id = " + baseParamsForm.getmTaxasetId() + "]", "不存在");
			}
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getFilePath())) {
			jsonObject.put("文件路径", "不能为空");
		} else if (!new File(baseParamsForm.getFilePath()).exists()) {
			jsonObject.put(baseParamsForm.getFilePath(), "目标文件不存在");
		}

		if (jsonObject.size() > 0) {
			throw new ValidationException(jsonObject.toJSONString());
		}
	}

}
