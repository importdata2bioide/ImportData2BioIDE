//package org.big.test;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.big.common.Configuration;
//import org.big.common.ConnDB;
//import org.big.common.UUIDUtils;
//import org.big.entity.Rank;
//import org.big.entity.Taxaset;
//import org.big.entity.Taxon;
//import org.hibernate.mapping.Array;
//
//public class TestBatchCommit {
//
//	public static void main(String[] args) throws Exception {
//		int size = 10000;
//		long startTime = System.currentTimeMillis();
//		try {
//			Configuration configuration = new Configuration();
//			configuration.setDriver("com.mysql.jdbc.Driver");
//			configuration.setUrl("jdbc:mysql://159.226.67.87:3306/biodata?useSSL=false&rewriteBatchedStatements=true");
//			configuration.setUsername("bioide");
//			configuration.setPassword("big@bioide_2017");
//			Connection connDB = ConnDB.getConnDB(configuration);
//
//			List<Taxon> list = new ArrayList<>(size + 10);
//			for (int k = 0; k < size; k++) {
//				Taxon taxon = new Taxon();
//				taxon.setId(UUIDUtils.getUUID32());
//				Rank rank = new Rank();
//				rank.setId("7");
//				taxon.setRank(rank);
//				taxon.setRankid("7");
//				Taxaset taxaset = new Taxaset();
//				taxaset.setId("05313d7bbeb6417b8733cac3f74a830d");
//				taxon.setTaxaset(taxaset);
//				taxon.setRemark("20190306测试");
//				list.add(taxon);
//			}
//			saveAll(connDB, list);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ConnDB.closeConnDB();
//		}
//		System.out.println(size + "  运行时间: " + (System.currentTimeMillis() - startTime) / 60000 + "min, "
//				+ (System.currentTimeMillis() - startTime) / 1000 + "s ( " + (System.currentTimeMillis() - startTime)
//				+ "ms)");
//	}
//
//	private static void saveAll(Connection conn, List<Taxon> records) throws SQLException {
//
//		PreparedStatement pstmt = null;
//		String insertSql = " INSERT INTO taxon (id,scientificname,authorstr,epithet,rankid,nomencode,remark,"
//				+ "sourcesid,tci,refjson,status,inputer,inputtime,synchstatus,synchdate,"
//				+ "taxaset_id,rank_id,chname,taxon_condition,ref_class_sys,expert,taxon_examine,order_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//		try {
//			conn.setAutoCommit(false);
//			pstmt = conn.prepareStatement(insertSql);
//			for (Taxon taxon : records) {
//				pstmt.setString(1, taxon.getId());
//				pstmt.setString(2, taxon.getScientificname());
//				pstmt.setString(3, taxon.getAuthorstr());
//				pstmt.setString(4, taxon.getEpithet());
//				pstmt.setInt(5, Integer.parseInt(taxon.getRankid()));
//				pstmt.setString(6, taxon.getNomencode());
//				pstmt.setString(7, taxon.getRemark());
//				pstmt.setString(8, taxon.getSourcesid());
//				pstmt.setString(9, taxon.getTci());
//				pstmt.setString(10, taxon.getRefjson());
//				pstmt.setInt(11, taxon.getStatus());
//				pstmt.setString(12, taxon.getInputer());
//				pstmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
//				pstmt.setInt(14, taxon.getSynchstatus());
//				pstmt.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
//				pstmt.setString(16, taxon.getTaxaset().getId());// taxaset_id
//				pstmt.setInt(17, Integer.parseInt(taxon.getRankid()));
//				pstmt.setString(18, taxon.getChname());
//				pstmt.setInt(19, taxon.getTaxonCondition());
//				pstmt.setString(20, null);// 参考分类体系
//				pstmt.setString(21, taxon.getExpert());
//				pstmt.setString(22, taxon.getTaxonExamine());
//				pstmt.setInt(23, taxon.getOrderNum());
//				pstmt.addBatch();// 使用batch（）
//			}
//			pstmt.executeBatch();
//			conn.commit();
//			
//		} finally {
//			if (pstmt != null) {
//				pstmt.close();
//			}
//		}
//
//	}
//
//}
