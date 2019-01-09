package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.entity.Traitontology;
import org.big.entity.UserDetail;
import org.big.repository.TraitontologyRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *<p><b>Traitontology的Service类</b></p>
 *<p> Traitontology的Service类，与Traitontology有关的业务逻辑方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class TraitontologyServiceImpl implements TraitontologyService {
	@Autowired
	private TraitontologyRepository traitontologyRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public JSON findBySelect(HttpServletRequest request, String trainsetid) {
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
		List<Traitontology> thisList = new ArrayList<>();
		Page<Traitontology> thisPage = this.traitontologyRepository.searchByTraitontologyInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), trainsetid);
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();

		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			
			String enterm = thisList.get(i).getEnterm();
			if (StringUtils.isNotBlank(enterm) ) {
				row.put("text", thisList.get(i).getCnterm() + "（" + thisList.get(i).getEnterm() + "）");
			}else {
				row.put("text", thisList.get(i).getCnterm());
			}
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findTraitontologyServiceList(HttpServletRequest request) {
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
		List<Traitontology> thisList = new ArrayList<>();
		Page<Traitontology> thisPage = this.traitontologyRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
	        thisSelect="<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
	        thisEdit=
	        	 "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId() + "','taxaset')\" >" +
	             "<span class=\"glyphicon glyphicon-edit\"></span>" +
	             "</a> &nbsp;&nbsp;&nbsp;" +
	             "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('" + thisList.get(i).getId() + "','taxaset')\" >" +
	             "<span class=\"glyphicon glyphicon-remove\"></span>" +
	             "</a>";
			row.put("select", thisSelect);
			row.put("enterm", "<a href=\"console/taxaset/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getEnterm() + "(" + thisList.get(i).getCnterm() + ")" + "</a>");
			row.put("group", thisList.get(i).getGroup());
			row.put("inputer", userRepository.findOneById(thisList.get(i).getInputer()).getNickname());
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
	public boolean logicRemove(String id) {
		Traitontology thisTraitontology = this.traitontologyRepository.findOneById(id);
		if (null != thisTraitontology && 1 == thisTraitontology.getStatus()) {
			thisTraitontology.setStatus(0);
			this.traitontologyRepository.save(thisTraitontology);
			return true;
		}
		return false;
	}

	@Override
	public void updateOneById(@Valid Traitontology thisTraitontology) {
		thisTraitontology.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.traitontologyRepository.save(thisTraitontology);
	}

	@Override
	public void saveTraitontology(@Valid Traitontology thisTraitontology) {
		try {
			UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			thisTraitontology.setInputer(thisUser.getId());
			thisTraitontology.setStatus(1);
			thisTraitontology.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTraitontology.setSynchstatus(0);
			this.traitontologyRepository.save(thisTraitontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Traitontology findOneById(String id) {
		return this.traitontologyRepository.findOneById(id);
	}
}
