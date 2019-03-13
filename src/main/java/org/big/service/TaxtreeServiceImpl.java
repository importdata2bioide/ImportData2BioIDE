package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TaxtreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxtreeServiceImpl implements TaxtreeService {
	
	final static Logger logger = LoggerFactory.getLogger(TaxtreeServiceImpl.class);
	@Autowired
	private TaxtreeRepository taxtreeRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DatasetService datasetService;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;

	@Override
	public void saveOne(Taxtree thisTaxtree) {
		thisTaxtree.setId(UUIDUtils.getUUID32());
		thisTaxtree.setStatus(1);
		thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisTaxtree.setSynchstatus(0);
		this.taxtreeRepository.save(thisTaxtree);
	}

	@Override
	public void removeOne(String Id) {
		this.taxtreeRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Taxtree thisTaxtree = this.taxtreeRepository.findOneById(id);
		if (null != thisTaxtree && 1 == thisTaxtree.getStatus()) {
			thisTaxtree.setStatus(0);
			this.taxtreeRepository.save(thisTaxtree);
			return true;
		}
		return false;
	}

	@Override
	public void updateOneById(Taxtree thisTaxtree) {
		thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.taxtreeRepository.save(thisTaxtree);
	}

	@Override
	public Taxtree findOneById(String Id) {
		return this.taxtreeRepository.findOneById(Id);
	}

	@Override
	public JSON findTaxtreeList(HttpServletRequest request) {
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
		List<Taxtree> thisList = new ArrayList<>();
		Page<Taxtree> thisPage = this.taxtreeRepository.searchInfo(searchText, dsId,
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		String thisSelect = "";
		String thisEdit = "";
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			thisSelect = "<input type='checkbox' name='checkbox' id='sel_" + thisList.get(i).getId() + "' />";
			thisEdit = "<a class=\"wts-table-edit-icon\" onclick=\"editThisObject('" + thisList.get(i).getId()
					+ "','taxtree')\" >" + "<span class=\"glyphicon glyphicon-edit\"></span>"
					+ "</a> &nbsp;&nbsp;&nbsp;" + "<a class=\"wts-table-edit-icon\" onclick=\"removeThisObject('"
					+ thisList.get(i).getId() + "','taxtree')\" >"
					+ "<span class=\"glyphicon glyphicon-remove\"></span>" + "</a>";
			row.put("select", thisSelect);
			row.put("treename", "<a href=\"javascript:manageTaxtree('" + thisList.get(i).getId() + "')\">"
					+ thisList.get(i).getTreename() + "</a>");
			// row.put("treename", "<a href=\"console/taxtree/show/" +
			// thisList.get(i).getId() + "\">" + thisList.get(i).getTreename() + "</a>");
			row.put("treeinfo", thisList.get(i).getTreeinfo());
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
		String sort = "treename";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxtree> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxtree
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxtree> thisPage = this.taxtreeRepository.searchByTreename(findText, dsId,
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
			row.put("text", thisList.get(i).getTreename());
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
		String sort = "treename";
		String order = "asc";
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Taxtree> thisList = new ArrayList<>();
		// 获取当前选中Dataset下的Taxtree
		String dsId = (String) request.getSession().getAttribute("datasetID");
		Page<Taxtree> thisPage = this.taxtreeRepository.searchByTreename(findText, dsId,
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
			row.put("text", "新建一个分类树");
			items.add(row);
		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			row.put("id", thisList.get(i).getId());
			row.put("text", thisList.get(i).getTreename());
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public JSON newOne(Taxtree thisTaxtree, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		try {
			thisTaxtree.setInputtime(new Timestamp(System.currentTimeMillis()));
			thisTaxtree.setSynchdate(new Timestamp(System.currentTimeMillis()));
			thisTaxtree.setStatus(1);
			String id = UUIDUtils.getUUID32();
			thisTaxtree.setId(id);
			thisTaxtree.setSynchstatus(0);
			// 获取当前选中Dataset
			String dsid = (String) request.getSession().getAttribute("datasetID");
			thisTaxtree.setDataset(datasetService.findbyID(dsid));
			this.taxtreeRepository.save(thisTaxtree);

			thisResult.put("result", true);
			thisResult.put("newId", this.taxtreeRepository.findOneById(id).getId());
			thisResult.put("newTreename", this.taxtreeRepository.findOneById(id).getTreename());
		} catch (Exception e) {
			thisResult.put("result", false);
		}
		return thisResult;
	}

	@Override
	public void saveOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		this.taxonHasTaxtreeRepository.save(thisTaxonHasTaxtree);
	}

	@Override
	public TaxonHasTaxtree findOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		return this.taxonHasTaxtreeRepository.findOneByIds(thisTaxonHasTaxtree.getTaxonId(),
				thisTaxonHasTaxtree.getTaxtreeId());
	}

	@Override
	public boolean hasTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree) {
		if (this.taxonHasTaxtreeRepository.findOneByIds(thisTaxonHasTaxtree.getTaxonId(),
				thisTaxonHasTaxtree.getTaxtreeId()) != null)
			return true;
		else
			return false;
	}

	@Override
	public JSONArray showChildren(String taxonId, String taxtreeId) {
		JSONArray thisArray = new JSONArray();
		List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository
				.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId, taxtreeId);
		try {
			List<TaxonHasTaxtree> sortList = this.sortTaxonHasTaxtree(thisList);
			if (sortList.size() == thisList.size())
				thisList = sortList;
		} catch (Exception e) {
			System.out.println("排序失败");
		}
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject thisTaxonHasTaxtree = new JSONObject();
			Taxon thisTaxon = this.taxonRepository.findOneById(thisList.get(i).getTaxonId());
			thisTaxonHasTaxtree.put("id", thisTaxon.getId());
			thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname());
			if (StringUtils.isBlank(thisTaxon.getChname()))
				thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname());
			else
				thisTaxonHasTaxtree.put("name", thisTaxon.getScientificname() + " " + thisTaxon.getChname());
			thisTaxonHasTaxtree.put("title", thisTaxon.getRemark());
			if (this.taxonHasTaxtreeRepository.countTaxonHasTaxtreesByPidAndTaxtreeId(thisTaxon.getId(), taxtreeId) > 0)
				thisTaxonHasTaxtree.put("isParent", true);
			else
				thisTaxonHasTaxtree.put("isParent", false);
			thisTaxonHasTaxtree.put("url", null);
			thisTaxonHasTaxtree.put("click", "showTaxon('" + thisTaxon.getId() + "')");
			thisArray.add(thisTaxonHasTaxtree);
		}
		return thisArray;
	}

	@Override
	public List<TaxonHasTaxtree> findChildren(String taxonId, String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId, taxtreeId);
		// return this.taxonHasTaxtreeRepository.findChildren(taxonId,taxtreeId);
	}

	@Override
	public boolean removeOneNode(String taxonId, String taxtreeId) {
		try {
			this.taxonHasTaxtreeRepository.removeByTaxonIdAndTaxtreeId(taxonId, taxtreeId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean removeChindren(String taxonId, String taxtreeId) {
		try {
			this.taxonHasTaxtreeRepository.removeByPidAndTaxtreeId(taxonId, taxtreeId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean removeNodeAndChindren(String taxonId, String taxtreeId) {
		try {
			this.taxonHasTaxtreeRepository.removeByTaxonIdAndTaxtreeId(taxonId, taxtreeId);
			this.taxonHasTaxtreeRepository.removeByPidAndTaxtreeId(taxonId, taxtreeId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean removeNodeAndAllChindren(String taxonId, String taxtreeId) {
		try {
			// 查所有子节点
			List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository
					.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId, taxtreeId);
			// 根据id删除自己和所有一级子节点
			removeNodeAndChindren(taxonId, taxtreeId);
			for (int i = 0; i < thisList.size(); i++) {
				removeNodeAndAllChindren(thisList.get(i).getTaxonId(), taxtreeId);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean addNodeAndAllChindren(String taxonId, String taxtreeId, String origTaxtreeId) {
		try {
			// 查所有子节点
			List<TaxonHasTaxtree> thisList = this.taxonHasTaxtreeRepository
					.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId, origTaxtreeId);
			// 保存自己
			TaxonHasTaxtree origTaxonHasTaxtree = this.taxonHasTaxtreeRepository.findOneByIds(taxonId, origTaxtreeId);
			TaxonHasTaxtree thisTaxonHasTaxtree = new TaxonHasTaxtree();
			thisTaxonHasTaxtree.setTaxtreeId(taxtreeId);
			// thisTaxonHasTaxtree.setTreeSort(origTaxonHasTaxtree.getTreeSort());
			thisTaxonHasTaxtree.setPid(origTaxonHasTaxtree.getPid());
			thisTaxonHasTaxtree.setTaxonId(origTaxonHasTaxtree.getTaxonId());
			thisTaxonHasTaxtree.setPrevTaxon(origTaxonHasTaxtree.getPrevTaxon());
			this.taxonHasTaxtreeRepository.save(thisTaxonHasTaxtree);
			for (int i = 0; i < thisList.size(); i++) {
				addNodeAndAllChindren(thisList.get(i).getTaxonId(), taxtreeId, thisList.get(i).getTaxtreeId());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public TaxonHasTaxtree findOneTaxonHasTaxtreeByIds(String taxonId, String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findOneByIds(taxonId, taxtreeId);
	}

	@Override
	public List<TaxonHasTaxtree> sortTaxonHasTaxtree(List<TaxonHasTaxtree> nodeList) {
		List<TaxonHasTaxtree> sortList = new ArrayList<>();
		if (nodeList.size() > 0) {
			// 获取第一个节点
			TaxonHasTaxtree headNode = this.findHeadNode(nodeList);
			sortList.add(headNode);
			String thisNodeId = headNode.getTaxonId();
			for (int i = 0; i < nodeList.size() - 1; i++) {
				TaxonHasTaxtree nextNode = this.findNextNode(nodeList, thisNodeId);
				if (nextNode.getTaxonId() != null) {
					thisNodeId = nextNode.getTaxonId();
					sortList.add(nextNode);
				}
			}
		} else
			sortList = nodeList;
		return sortList;
	}

	@Override
	public TaxonHasTaxtree findNextNode(List<TaxonHasTaxtree> nodeList, String thisNodeId) {
		TaxonHasTaxtree nextNode = new TaxonHasTaxtree();
		for (int i = 0; i < nodeList.size(); i++) {
			if (thisNodeId.equals(nodeList.get(i).getPrevTaxon())) {
				nextNode = nodeList.get(i);
				break;
			}
		}
		return nextNode;
	}

	@Override
	public TaxonHasTaxtree findHeadNode(List<TaxonHasTaxtree> nodeList) {
		TaxonHasTaxtree nextNode = new TaxonHasTaxtree();
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getPrevTaxon() == null) {
				nextNode = nodeList.get(i);
				break;
			}
		}
		return nextNode;
	}

	@Override
	public TaxonHasTaxtree findLastNode(List<TaxonHasTaxtree> nodeList) {
		List<TaxonHasTaxtree> sortList = this.sortTaxonHasTaxtree(nodeList);
		if (sortList.size() > 0)
			return sortList.get(sortList.size() - 1);
		else
			return null;
	}

	@Override
	public int countChindren(String taxonId, String taxtreeId) {
		return this.taxonHasTaxtreeRepository.countTaxonHasTaxtreesByPidAndTaxtreeId(taxonId, taxtreeId);
	}

	@Override
	public TaxonHasTaxtree findNextNode(String taxonId, String taxtreeId) {
		return this.taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPrevTaxonAndTaxtreeId(taxonId, taxtreeId);
	}

	/**
	 * 废弃，使用getChildNodes()替代
	 */
	@Override
	public List<TaxonHasTaxtree> findNodeAndAllChildren(String taxonId, String taxtreeId) {
		List<TaxonHasTaxtree> resultList = new ArrayList<>();
		// 查询当前node
		TaxonHasTaxtree rootNode = taxonHasTaxtreeRepository.findTaxonHasTaxtreesByTaxonIdAndTaxtreeId(taxonId,
				taxtreeId);
		String pid = rootNode.getTaxonId();
		List<TaxonHasTaxtree> firstLevelchildrenNode = taxonHasTaxtreeRepository
				.findTaxonHasTaxtreesByPidAndAndTaxtreeId(pid, taxtreeId);
		if (firstLevelchildrenNode.size() != 0) {
			resultList.addAll(firstLevelchildrenNode);
		}
		// 一级的所有子节点
		for (TaxonHasTaxtree taxonHasTaxtree : firstLevelchildrenNode) {
			List<TaxonHasTaxtree> secondLevelchildrenNode = taxonHasTaxtreeRepository
					.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonHasTaxtree.getTaxonId(), taxtreeId);
			if (secondLevelchildrenNode.size() != 0) {
				resultList.addAll(secondLevelchildrenNode);
			}
			// 二级的所有子节点
			for (TaxonHasTaxtree second : secondLevelchildrenNode) {
				List<TaxonHasTaxtree> thirdLevelchildrenNode = taxonHasTaxtreeRepository
						.findTaxonHasTaxtreesByPidAndAndTaxtreeId(second.getTaxonId(), taxtreeId);
				if (thirdLevelchildrenNode.size() != 0) {
					resultList.addAll(thirdLevelchildrenNode);
				}
				// 三级的所有子节点
				for (TaxonHasTaxtree third : thirdLevelchildrenNode) {
					List<TaxonHasTaxtree> forthLevelchildrenNode = taxonHasTaxtreeRepository
							.findTaxonHasTaxtreesByPidAndAndTaxtreeId(third.getTaxonId(), taxtreeId);
					if (forthLevelchildrenNode.size() != 0) {
						resultList.addAll(forthLevelchildrenNode);
					}
					// 四级的所有子节点
					for (TaxonHasTaxtree forth : forthLevelchildrenNode) {
						List<TaxonHasTaxtree> fifthLevelchildrenNode = taxonHasTaxtreeRepository
								.findTaxonHasTaxtreesByPidAndAndTaxtreeId(forth.getTaxonId(), taxtreeId);
						if (fifthLevelchildrenNode.size() != 0) {
							resultList.addAll(fifthLevelchildrenNode);
						}
					}
				}
			}
		}
		return resultList;
	}

	@Override
	public int deleteByTaxtreeId(String taxtreeId) {
		List<TaxonHasTaxtree> childNodes = new ArrayList<>();
		// 查询所有的孩子节点
		List<TaxonHasTaxtree> firstLevelchildrenNode = taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxtreeId, taxtreeId);
		childNodes.addAll(firstLevelchildrenNode);
		for (TaxonHasTaxtree taxonHasTaxtree : firstLevelchildrenNode) {
			childNodes.addAll(getChildNodes(null,taxonHasTaxtree.getTaxonId(),taxtreeId));
		}
		System.out.println("查询到孩子节点"+childNodes.size());
		// 删除
		taxonHasTaxtreeRepository.deleteInBatch(childNodes);
		return childNodes.size();

	}


	
	public List<TaxonHasTaxtree> getChildNodes(List<TaxonHasTaxtree> resultList,String taxonId, String taxtreeId){
		if(resultList == null) {
			resultList = new ArrayList<>();
		}
		//查询孩子节点
		List<TaxonHasTaxtree> childrenNode = taxonHasTaxtreeRepository.findTaxonHasTaxtreesByPidAndAndTaxtreeId(taxonId, taxtreeId);
		if (childrenNode.size() != 0) {
			resultList.addAll(childrenNode);
		}
		for (TaxonHasTaxtree taxonHasTaxtree : childrenNode) {
			getChildNodes(resultList,taxonHasTaxtree.getTaxonId(),taxtreeId);
		}
		return resultList;
	}

	@Override
	public List<Taxtree> findTaxtreeByDataset(String datasetId) {
		return taxtreeRepository.findByDatasetId(datasetId);
	}

	@Override
	public int saveTreeByJsonRemark(List<Taxon> taxonlist,String taxtreeId) {
		List<TaxonHasTaxtree> records = new ArrayList<>();
		for (Taxon taxon : taxonlist) {
			TaxonHasTaxtree t = new TaxonHasTaxtree();
			t.setTaxtreeId(taxtreeId);
			t.setTaxonId(taxon.getId());
			//通过remark获取一棵树
			String remark = taxon.getRemark();
			if(CommUtils.isStrNotEmpty(remark)) {//tree node
				JSONObject jsonObject = CommUtils.strToJSONObject(remark);
				String parentId = String.valueOf(jsonObject.get("parentId"));
				if(CommUtils.isStrEmpty(parentId)) {
					t.setPid(taxtreeId);
				}else if(taxonRepository.findOneById(parentId) == null) {
					System.out.println("找不到 [id = "+parentId+" ] 的taxon ,请查看 id = ["+taxon.getId()+"] 的remark字段 ");
				}else {
					t.setPid(parentId);
				}
			}else {//tree root
				t.setPid(taxtreeId);
			}
			records.add(t);
		}
		logger.info("分类树[ id = "+taxtreeId+" ]的节点个数为："+records.size());
		//save 
		taxonHasTaxtreeRepository.saveAll(records);
		
		return records.size();
	}

	@Override
	public List<TaxonHasTaxtree> findByTaxtreeId(String taxtreeId) {
		return taxonHasTaxtreeRepository.findByTaxtreeId(taxtreeId);

	}
}
