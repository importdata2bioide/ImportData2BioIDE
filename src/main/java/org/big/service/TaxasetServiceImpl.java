package org.big.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Taxaset;
import org.big.repository.TaxasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class TaxasetServiceImpl implements TaxasetService {
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private DatasetService datasetService;
	
	@Override
	public void saveOne(Taxaset thisTaxaset) {
		thisTaxaset.setId(UUIDUtils.getUUID32());
		thisTaxaset.setStatus(1);
		thisTaxaset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisTaxaset.setSynchstatus(0);
		this.taxasetRepository.save(thisTaxaset);
	}

	@Override
	public void removeOne(String Id) {
		this.taxasetRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Taxaset thisTaxaset = this.taxasetRepository.findOneById(id);
		if (null != thisTaxaset && 1 == thisTaxaset.getStatus()) {
			thisTaxaset.setStatus(0);
			this.taxasetRepository.save(thisTaxaset);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateOneById(Taxaset thisTaxaset) {
		thisTaxaset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.taxasetRepository.save(thisTaxaset);
	}

	@Override
	public Taxaset findOneById(String Id) {
		return this.taxasetRepository.findOneById(Id);
	}

	@Override
	public Taxaset findOneByTsname(String tsname) {
		return this.taxasetRepository.findOneByTsname(tsname);
	}

	@Override
	public JSON findTaxasetList(HttpServletRequest request) {
		String dsId = (String) request.getSession().getAttribute("datasetID");
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "synchdate";
		String order = "desc";
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Taxaset> thisList = new ArrayList<>();
		Page<Taxaset> thisPage = this.taxasetRepository.searchInfo(searchText,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order), dsId);
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
			row.put("tsname", "<a href=\"console/taxaset/show/" + thisList.get(i).getId() + "\">" + thisList.get(i).getTsname() + "</a>");
			row.put("tsinfo", thisList.get(i).getTsinfo());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String addTime = "";
			String editTime = "";
			try {
				addTime = formatter.format(thisList.get(i).getCreatedDate());
				editTime = formatter.format(thisList.get(i).getSynchdate());
			} catch (Exception e) {
			}
			row.put("createdDate", addTime);
			row.put("synchdate", editTime);
			row.put("edit", thisEdit);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
    }
		
	@Override
	public List<Taxaset> findTaxasetsByDatasetId(String dsId) {
		return this.taxasetRepository.findTaxasetsByDatasetId(dsId);
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
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxaset> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxaset
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxaset> thisPage = this.taxasetRepository.searchByTsname(findText, dsId,
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
			row.put("text", thisList.get(i).getTsname());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON findBySelectAndNew(HttpServletRequest request) {
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
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxaset> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxaset
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxaset> thisPage = this.taxasetRepository.searchByTsname(findText, dsId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisSelect.put("total_count", thisPage.getTotalElements());
		Boolean incompleteResulte = true;
		if ((thisPage.getTotalElements() / 30) > findPage) {
			incompleteResulte = false;
		}
		thisSelect.put("incompleteResulte", incompleteResulte);
		thisList = thisPage.getContent();
		if (findPage == 1) {
			JSONObject row = new JSONObject();
			row.put("id", "addNew");
			row.put("text", "新建一个分类单元集");
			items.add(row);
		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getTsname());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON newOne(Taxaset thisTaxaset, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			thisTaxaset.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			thisTaxaset.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTaxaset.setStatus(1);
			String id = UUIDUtils.getUUID32();
			thisTaxaset.setId(id);
			thisTaxaset.setSynchstatus(0);
			// 获取当前选中Dataset
			String dsid = (String) request.getSession().getAttribute("datasetID");
			thisTaxaset.setDataset(datasetService.findbyID(dsid));
			this.taxasetRepository.save(thisTaxaset);

			thisResult.put("result", true);
			thisResult.put("newId", this.taxasetRepository.findOneById(id).getId());
			thisResult.put("newTsname", this.taxasetRepository.findOneById(id).getTsname());
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}
}
