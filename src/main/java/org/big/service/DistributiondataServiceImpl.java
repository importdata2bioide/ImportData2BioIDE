package org.big.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.PartOfSpeech;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.sp2000.entity.Distribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class DistributiondataServiceImpl implements DistributiondataService {
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;

	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String distributiondataId = request.getParameter("distributiondataId");
		if (StringUtils.isNotBlank(distributiondataId)) {
			if (null != this.distributiondataRepository.findOneById(distributiondataId)) {
				this.distributiondataRepository.deleteOneById(distributiondataId);
			}
			return true;
		}
		return false;
	}

	@Override
	public Distributiondata saveOne(Distributiondata record) {
		String id = record.getId();
		if (StringUtils.isEmpty(id)) {
			record.setId(UUIDUtils.getUUID32());
		}
		record.setInputtime(new Date());
		record.setSynchdate(new Date());
		record.setStatus(1);
		record.setSynchstatus(0);
		distributiondataRepository.save(record);
		return record;
	}

	@Override
	public List<Distribution> getDistributionByTaxaset(String taxasetId) {
		List<Object[]> list = distributiondataRepository.findDistributionByTaxaset(taxasetId);
		List<Distribution> resultlist = new ArrayList<>();
		for (Object[] obj : list) {
			Distribution entity = new Distribution();
			entity.setRecordId(obj[0].toString());
			entity.setNameCode(obj[1].toString());
			String geojson = obj[2].toString();
			JSONObject jsonObject = CommUtils.strToJSONObject(geojson);
			String[] geos = jsonObject.get("geoIds").toString().split("&");
			StringBuffer str = new StringBuffer();
			for (String geoId : geos) {
				Geoobject geoobject = geoobjectRepository.findOneById(geoId);
				if (geoobject == null) {
					throw new ValidationException("找不到分布地，Geoobject.id=" + geoId);
				}
				str.append(geoobject.getCngeoname());
				str.append(",");
			}
			String distributionC = str.toString().substring(0, str.toString().length() - 1);// 去掉最后的逗号
			entity.setDistributionC(distributionC);
			entity.setDistributionBack(obj[3].toString());
			resultlist.add(entity);
		}
		return resultlist;
	}

	@Override
	public String getDisJsonByLine(String descontent) {
		// 空字符串，返回
		if (StringUtils.isEmpty(descontent)) {
			return null;
		}
		Map<String, String> geojsonValue = new HashMap<>();// 使用map,过滤重复数据
		List<Word> words = WordSegmenter.segWithStopWords(descontent);// 分词，不移除停用词
		PartOfSpeechTagging.process(words);// 词性标注
		for (Word word : words) {
			PartOfSpeech speech = word.getPartOfSpeech();
			String des = speech.getDes();// 词性
			String text = word.getText();
			if (des.equals("地名") || des.equals("未知")) {
				// 查询
				Geoobject obj = geoobjectRepository.findOneByCngeoname(text);// 精确查询
				if (obj == null) {
					obj = getSpecialProvinces(text);// 特殊的
					if (obj == null) {
						obj = findWithAddSheng(text);// 添加省字查询
					}
					if (obj == null) {
						obj = fuzzyQuery(text);// 模糊查询
					}
				}
				// 处理查询结果，为空则放入map,不为空则append
				if (obj != null) {
					geojsonValue.put(obj.getId(), text);
				}
			}
		} // end for
		String keyString = CommUtils.getKeyString(geojsonValue);
		if (StringUtils.isEmpty(keyString)) {
			return null;
		} 
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("geoIds", keyString);
		return String.valueOf(jsonObject);
	}

	private Geoobject getSpecialProvinces(String text) {
		String queryText = null;
		if (text.contains("宁夏")) {
			queryText = "宁夏回族自治区";
		} else if (text.contains("香港")) {
			queryText = "香港特别行政区";
		} else if (text.contains("澳门")) {
			queryText = "澳门特别行政区";
		} else if (text.contains("台湾")) {
			queryText = "台湾省";
		} else if (text.contains("西藏")) {
			queryText = "西藏自治区";
		} else if (text.contains("内蒙古")) {
			queryText = "内蒙古自治区";
		} else if (text.contains("新疆")) {
			queryText = "新疆维吾尔自治区";
		} else if (text.contains("广西")) {
			queryText = "广西壮族自治区";
		} else if (text.contains("北京")) {
			queryText = "北京市";
		} else if (text.contains("重庆")) {
			queryText = "重庆市";
		} else if (text.contains("上海")) {
			queryText = "上海市";
		} else if (text.contains("天津")) {
			queryText = "天津市";
		}
		if (StringUtils.isNotEmpty(queryText)) {
			return geoobjectRepository.findOneByCngeoname(queryText);
		}
		return null;
	}

	private Geoobject findWithAddSheng(String text) {
		text = text + "省";
		return geoobjectRepository.findOneByCngeoname(text);
	}

	/**
	 * 
	 * @Description 模糊查询
	 * @param text
	 * @return
	 * @author ZXY
	 */
	private Geoobject fuzzyQuery(String text) {
		Geoobject object = null;
		List<Geoobject> list = geoobjectRepository.findByLikeCngeoname(text);
		if (list.size() > 0) {
			object = list.get(0);
		}
		if (object == null) {
			List<Geoobject> list2 = geoobjectRepository.findByLikeRemark(text);
			if (list2.size() > 0) {
				object = list2.get(0);
			}
		}
		return object;
	}

}
