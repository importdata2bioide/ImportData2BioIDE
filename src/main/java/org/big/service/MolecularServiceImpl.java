package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Molecular;
import org.big.entity.UserDetail;
import org.big.repository.MolecularRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class MolecularServiceImpl implements MolecularService {
	@Autowired
	private MolecularRepository molecularRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaxonRepository taxonRepository;
	
	@Override
	public JSON findMolecularList(HttpServletRequest request) {
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		
		String sort = "synchdate";
		String order = "desc";
		sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "synchdate";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Molecular> thisList = new ArrayList<>();
		Page<Molecular> thisPage = this.molecularRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','molecular')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a>&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','molecular')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("title", thisList.get(i).getTitle());
			row.put("inputer", this.userRepository.findOneById(thisList.get(i).getInputer()).getNickname());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String addTime = "";
			String editTime = "";
			try {
				addTime = formatter.format(thisList.get(i).getInputtime());
				editTime = formatter.format(thisList.get(i).getSynchdate());
			} catch (Exception e) {
			}
			row.put("inputtime", addTime);
			row.put("synchdate", editTime);
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
    }

	@Override
	public JSON addMolecular(@Valid Molecular thisMolecular, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisMolecular.setInputer(thisUser.getId());
			thisMolecular.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisMolecular.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisMolecular.setTaxon(this.taxonRepository.findOneById((String) request.getSession().getAttribute("taxonId")));
			thisMolecular.setStatus(1);
			thisMolecular.setSynchstatus(0);
			this.molecularRepository.save(thisMolecular);
			thisResult.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public boolean logicRemove(String id) {
		Molecular thisMolecular = this.molecularRepository.findOneById(id);
		if (null != thisMolecular && 1 == thisMolecular.getStatus()) {
			thisMolecular.setStatus(0);
			this.molecularRepository.save(thisMolecular);
			return true;
		}
		return false;
	}

}
