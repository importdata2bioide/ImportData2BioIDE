package org.big.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.common.QueryTool;
import org.big.entity.Traitproperty;
import org.big.repository.TraitpropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *<p><b>Traitproperty的Service类</b></p>
 *<p> Traitproperty的Service类，与Traitproperty有关的业务逻辑方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class TraitpropertyServiceImpl implements TraitpropertyService {
	@Autowired
	private TraitpropertyRepository traitpropertyRepository;
	
	@Override
	public JSON findBySelect(HttpServletRequest request) {
		String findText = request.getParameter("find");
		if (findText == null || findText.length() <= 0) {
			findText = "";
		}
		int findPage = 1;
		try {
			findPage = Integer.valueOf(request.getParameter("page"));
		} catch (Exception e) {
		}
		int limit_serch = 30;
		int offset_serch = (findPage - 1) * 30;
		String sort = "cnterm";
		String order = "asc";
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Traitproperty> thisList = new ArrayList<>();
		Page<Traitproperty> thisPage = this.traitpropertyRepository.searchByTraitpropertyInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getCnterm());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}
	
	@Override
	public Traitproperty findOneById(String id) {
		return this.traitpropertyRepository.findOneById(id);
	}
}
