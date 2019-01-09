package org.big.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.big.common.CommUtils;
import org.big.common.ConnDB;
import org.big.entity.User;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.Species;
import org.big.entityVO.SpeciesPaper;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class AQueryForcesDBImpl implements AQueryForcesDB {

	@Autowired
	private UserRepository userRepository;
	// 新系统所有用户
	List<User> users = new ArrayList<>();

	// 旧系统命名参考文献
	List<SpeciesPaper> SpeciesPaperlist = new ArrayList<>(400000);
	// 旧系统命名信息
	List<Species> Specieslist = new ArrayList<>(400000);
	
	//旧系统异名参考文献
	List<SpeciesPaper> SynonymPaperList = new ArrayList<>(130000);

//	@Override
	public String getUserId(String currenUserId, String UserName) {
		if (users.size() <= 0) {
			users = userRepository.findAll();
		}
		for (User user : users) {
			if (user.getUserName().equals(UserName)) {
				return user.getId();
			}
		}
		return currenUserId;

	}

	/**
	 * 数据放在内存中遍历查询
	 */
	public String getTaxonRefjson(String taxonId) throws SQLException {
		JSONArray jsonArray = new JSONArray();
		initSpeciesPaperlist();
		// 查找数据
		for (SpeciesPaper speciesPaper : SpeciesPaperlist) {
			if (speciesPaper.getTaxaID().equals(taxonId)) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("refE", " 0");
				jsonObject.put("refS", " 0");
				jsonObject.put("refId", speciesPaper.getPaperID());
				jsonArray.add(jsonObject);
			}
		}
		// 返回查找结果
		if (jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();
	}

	public String getAuthorship(String taxonId) throws SQLException {
		initSpecieslist();
		// 查询数据
		String authorstr = "";
		for (Species species : Specieslist) {
			if (species.getTaxaID().equals(taxonId)) {
				String Named_Date = species.getNamedDate();
				String Named_Person = species.getNamedPerson();
				if (!CommUtils.isStrNotEmpty(Named_Date)) {// Named_Date为空
					authorstr = Named_Person;
				} else if (CommUtils.isStrNotEmpty(Named_Date) && Named_Person.contains(Named_Person)) {// Named_Date非空且Named_Person包含Named_Date
					authorstr = Named_Person;
				} else {
					authorstr = Named_Person + "," + Named_Date;
				}
				break;
			}
		}
		// 处理结果
		if (CommUtils.isStrNotEmpty(authorstr)) {
			authorstr = authorstr.replace("(", "");
			authorstr = authorstr.replace(")", "");
			authorstr = authorstr.replace("（", "");
			authorstr = authorstr.replace("）", "");
			authorstr = authorstr.trim();
		}
		return authorstr;
	}

	public String getCitationRefjson(String taxonId, int nameType) throws SQLException {
		JSONArray jsonArray = new JSONArray();
		initSpeciesPaperlist();
		initSynonymPaper();
		// SpeciesPaper查找数据
		for (SpeciesPaper speciesPaper : SpeciesPaperlist) {
			if (speciesPaper.getTaxaID().equals(taxonId)) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("refE", " 0");
				jsonObject.put("refS", " 0");
				jsonObject.put("refId", "" + speciesPaper.getPaperID());
				jsonObject.put("refType", "0");
				jsonArray.add(jsonObject);
			}
		}
		// 初始化
		initSynonymPaper();
		if (nameType == NametypeEnum.synonym.getIndex()) {// 异名引证
			for (SpeciesPaper speciesPaper : SynonymPaperList) {
				if (speciesPaper.getTaxaID().equals(taxonId)) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("refE"," 0");
					jsonObject.put("refS"," 0");
					jsonObject.put("refId",speciesPaper.getPaperID());
					jsonObject.put("refType","1");
					jsonArray.add(jsonObject);
				}
			}

		}
		//返回数据
		if(jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();
	}

	private void initSynonymPaper() throws SQLException {
		if (SynonymPaperList.size() <= 0) {
			//在旧系统Synonym_Paper中查询数据
			Connection connDB = ConnDB.getConnDB();
			String synonymsql = "select PaperID,SynID as TaxaID from Synonym_Paper";
			PreparedStatement pst2 = connDB.prepareStatement(synonymsql);
			ResultSet rs = pst2.executeQuery();
			while (rs.next()) {
				SpeciesPaper s = new SpeciesPaper();
				s.setPaperID(rs.getString("PaperID"));
				s.setTaxaID(rs.getString("TaxaID"));
				SynonymPaperList.add(s);
			}
			rs.close();
		}
		
	}

	private  void initSpeciesPaperlist() throws SQLException {
		// 初始化数据
		if (SpeciesPaperlist.size() <= 0) {
			Connection connDB = ConnDB.getConnDB();
			String Namesql = "select PaperID,TaxaID from Species_Paper order by  TaxaID desc";
			PreparedStatement prepareStatement = connDB.prepareStatement(Namesql);
			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next()) {
				SpeciesPaper s = new SpeciesPaper();
				s.setPaperID(rs.getString("PaperID"));
				s.setTaxaID(rs.getString("TaxaID"));
				SpeciesPaperlist.add(s);
			}
			rs.close();
		}
	}
	
	
	private void initSpecieslist() throws SQLException {
		// 初始化数据
		if (Specieslist.size() <= 0) {
			Connection connDB = null;
			ResultSet rs = null;
			PreparedStatement prepareStatement = null;
			try {
				connDB = ConnDB.getConnDB();
				String Namesql = "select Named_Person as person ,Named_Date as date,TaxaID from species order by TaxaID";
				prepareStatement = connDB.prepareStatement(Namesql);
				rs = prepareStatement.executeQuery();
				while (rs.next()) {
					Species e = new Species();
					e.setNamedPerson(rs.getString("person"));
					e.setNamedDate(rs.getString("date"));
					e.setTaxaID(rs.getString("TaxaID"));
					Specieslist.add(e);
				}
			} finally {
				if (rs != null) {
					rs.close();
				}
			}
		}
	}

}
