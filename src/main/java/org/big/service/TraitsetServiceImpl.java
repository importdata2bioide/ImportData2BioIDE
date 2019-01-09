package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Traitset;
import org.big.entity.UserDetail;
import org.big.repository.TraitsetRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *<p><b>Traitset的Service类</b></p>
 *<p> Traitset的Service类，与Traitset有关的业务逻辑方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class TraitsetServiceImpl implements TraitsetService {
	@Autowired
	private TraitsetRepository traitsetRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public JSON findTraitsetList(HttpServletRequest request) {
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
		List<Traitset> thisList = new ArrayList<>();
		Page<Traitset> thisPage = this.traitsetRepository.searchInfo(searchText,
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
			row.put("name", "<a href=\"console/taxaset/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getName() + "</a>");
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
		String sort = "name";
		String order = "desc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Traitset> thisList = new ArrayList<>();
		Page<Traitset> thisPage = this.traitsetRepository.searchByTraitsetInfo(findText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage)
			incompleteResulte = false;
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		/*if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个数据集");
			items.add(row);
		}*/
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getName());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public Traitset findOneById(String id) {
		return this.traitsetRepository.findOneById(id);
	}

	@Override
	public void updateOneById(@Valid Traitset thisTraitset) {
		thisTraitset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.traitsetRepository.save(thisTraitset);
	}

	@Override
	public void saveOne(@Valid Traitset thisTraitset) {
		thisTraitset.setId(UUIDUtils.getUUID32());
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisTraitset.setInputer(thisUser.getId());
		thisTraitset.setStatus(1);
		thisTraitset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisTraitset.setSynchstatus(0);
		this.traitsetRepository.save(thisTraitset);
	}
	
	@Override
	public void removeOne(String Id) {
		this.traitsetRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Traitset thisTraitset = this.traitsetRepository.findOneById(id);
		if (null != thisTraitset && 1 == thisTraitset.getStatus()) {
			thisTraitset.setStatus(0);
			this.traitsetRepository.save(thisTraitset);
			return true;
		}
		return false;
	}

}
