package org.big.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.common.CommUtils;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Expert;
import org.big.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@Service
public class ExpertServiceImpl implements ExpertService {
	@Autowired
	private ExpertRepository expertRepository;

	@Override
	public JSON findExpertList(HttpServletRequest request) {
        String searchText=request.getParameter("search");
        if(searchText==null || searchText.length()<=0){
            searchText="";
        }
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
        String sort = "cnName";
		String order = "desc";
		
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<Expert> thisList=new ArrayList<>();

        Page<Expert> thisPage = this.expertRepository.findExpertList(searchText, QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
        thisTable.put("total",thisPage.getTotalElements());
        thisList=thisPage.getContent();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            /*
            String thisSelect="";
            String thisEdit="";
            thisSelect="<input type='checkbox' name='checkbox' id='sel_"+thisList.get(i).getId()+"' />";
            thisEdit=
                    "<a class=\"table-edit-icon\" href=\"javascript:void(0)\" >" +
                    	"<span class=\"glyphicon glyphicon-edit\"></span>" + "</a>&nbsp;&nbsp;&nbsp;"+
                     "<a class=\"table-edit-icon\" href=\"javascript:void(0)\" >" +
                     	"<span class=\"glyphicon glyphicon-remove\"></span>" +
                     "</a>";*/
            
            row.put("cnName","<a href=\"" + thisList.get(i).getCnHomePage() + "\">" + thisList.get(i).getCnName() + "</a>");
            row.put("enName",thisList.get(i).getEnName());
            row.put("expEmail",thisList.get(i).getExpEmail());
            row.put("cnCompany",thisList.get(i).getCnCompany());
            row.put("enCompany",thisList.get(i).getEnCompany());
            row.put("cnAddress",thisList.get(i).getCnAddress());
            row.put("enAddress",thisList.get(i).getEnAddress());
            row.put("cnHomePage","<a href=\"" + thisList.get(i).getCnHomePage() + "\">" + thisList.get(i).getCnHomePage() + "</a>");
            row.put("enHomePage","<a href=\"" + thisList.get(i).getEnHomePage() + "\">" + thisList.get(i).getEnHomePage() + "</a>");
            row.put("expInfo",thisList.get(i).getExpInfo());
            /*row.put("edit",thisEdit);*/
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        return thisTable;
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
		String sort = "expEmail";
		String order = "desc";
		
		JSONObject thisSelect = new JSONObject();
		JSONArray items = new JSONArray();
		List<Expert> thisList = new ArrayList<>();
		Page<Expert> thisPage = this.expertRepository.searchByInfo(findText, 
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
			row.put("text", thisList.get(i).getCnName() + "<" + thisList.get(i).getExpEmail() + ">");
			items.add(row);
		}
		thisSelect.put("items", items);
		return thisSelect;
	}

	@Override
	public List<Expert> export(HttpServletResponse response) {
		List<Expert> expertList = new ArrayList<>();
		//模拟从数据库获取需要导出的数据
        List<Expert> thisList = this.expertRepository.findAll();
        for (int i = 0; i < thisList.size(); i++) {
        	Expert thisExpert = new Expert(
        			thisList.get(i).getId(),
        			thisList.get(i).getCnName(), 
        			thisList.get(i).getEnName(), 
        			thisList.get(i).getCnCompany(), 
        			thisList.get(i).getEnCompany(), 
        			thisList.get(i).getCnAddress(), 
        			thisList.get(i).getEnAddress(), 
        			thisList.get(i).getCnHomePage(), 
        			thisList.get(i).getEnHomePage(), 
        			thisList.get(i).getExpEmail(),
        			thisList.get(i).getExpInfo());
        	expertList.add(thisExpert);
		}
		return expertList;
	}

	@Override
	public Expert findOneById(String id) {
		return this.expertRepository.findById(id);
	}

	@Override
	public Expert findOrCreateByNameAndInputer(String expertNames,String inputerId) {
		String UNKNOWN  = "未知";
		Expert expert = expertRepository.findOneByCnNameAndInputer(expertNames, inputerId);
		if(expert == null) {
			expert = new Expert();
			expert.setId(UUIDUtils.getUUID32());
			expert.setCnAddress(UNKNOWN);
			expert.setCnCompany(UNKNOWN);
			expert.setCnName(expertNames);
			expert.setExpEmail(UNKNOWN);
			expert.setInputer(inputerId);
			Timestamp timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
			expert.setInputtime(timestamp);
			expert.setSynchdate(timestamp);
			expert.setStatus(1);
			expert.setSynchstatus(0);
			expertRepository.save(expert);
		}
		return expert;
	}

}
