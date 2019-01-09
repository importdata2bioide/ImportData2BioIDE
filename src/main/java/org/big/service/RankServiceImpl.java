package org.big.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Rank;
import org.big.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RankServiceImpl implements RankService {
	@Autowired
	private RankRepository rankRepository;
	
	@Override
	public void saveOne(Rank thisRank) {
		this.rankRepository.save(thisRank);
	}

	@Override
	public void removeOne(String Id) {
		this.rankRepository.deleteOneById(Id);
	}

	@Override
	public void updateOneById(Rank thisRank) {
		this.rankRepository.save(thisRank);
	};
	
	@Override
	public Rank findOneById(String Id) {
		return this.rankRepository.findOneById(Id);
	}

	@Override
	public Rank findOneByEnname(String EnName) {
		return this.rankRepository.findOneByEnname(EnName);
	}
	
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
		String sort = "sort";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "sort";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Rank> thisList = new ArrayList<>();
		Page<Rank> thisPage = this.rankRepository.searchByEnname(findText, 
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getEnname() + "<" + thisList.get(i).getChname() + ">");
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}
}
