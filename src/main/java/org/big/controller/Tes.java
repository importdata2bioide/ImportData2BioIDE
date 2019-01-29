package org.big.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.entityVO.RankEnum;
import org.big.service.PlantAsyncServiceImpl;

public class Tes {

	public static void main(String[] args) {

		System.out.println(CommUtils.ToPinyin("石家庄"));

	}

	public void insertUser() throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		int entitiesSize = 0;
		try {
			// 连接数据库,字段个数20
			connection = null;
			String insertSql = " INSERT INTO user (id,user_name,password,email,phone,role,adddate,avatar,dtime,idnum,level,mark,mobile,nickname,profile_picture,resetmark,resettime,score,status,uploadnum) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = connection.prepareStatement(insertSql);
			for (int i = 0; i < entitiesSize; i++) {
				pstmt.setString(1, null);
				pstmt.setString(2, null);
				pstmt.setString(3, null);
				pstmt.setString(4, null);
				pstmt.setString(5, null);
				pstmt.setString(6, null);
				pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(8, null);
				pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(10, 1);
				pstmt.setInt(11, 1);
				pstmt.setString(12, null);
				pstmt.setString(13, null);
				pstmt.setString(14, null);
				pstmt.setString(15, null);
				pstmt.setString(16, null);
				pstmt.setTimestamp(17, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(18, 1);
				pstmt.setInt(19, 1);
				pstmt.setInt(20, 1);
				pstmt.addBatch();// 添加到同一个批处理中;
			} // for end
			pstmt.executeBatch();// 执行批处理
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			// do something
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

}
