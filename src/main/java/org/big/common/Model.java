package org.big.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.big.entity.Description;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class Model {
	
	@RequestMapping(value = "/birdDescInsert")
	@ResponseBody
	public String insertDesc() throws SQLException {
		Connection connDB = null;
		ResultSet rs = null;
		List<Description> entities = null;
		try {
			String sql = "select id as remark,Author as author ,Years as pyear ,Title as title,Journal as journal,Volume  as r_volume,Numbers as tchar ,Begins as refs ,Ends as refe ,Press as press ,Place as place ,Editor  as editor ,ISBN as isbn,Tpage as Tpage,Tchar  as tchar ,Version  as version ,Translator as translator ,Languages as languages ,Inputer as inputer,Keywords,old_languages as olang,Type as ptype from papers";
			connDB = ConnDB.getConnDB();
			PreparedStatement prepareStatement = connDB.prepareStatement(sql);
			rs = prepareStatement.executeQuery();
			entities = new ArrayList<>();
			while (rs.next()) {
				Description d = new Description();
				rs.getString("XXX");
				entities.add(d);
			}

		} finally {
			System.out.println("finish..."+entities.size());
			rs.close();
		}

		return "description insert finish";

	}

}
