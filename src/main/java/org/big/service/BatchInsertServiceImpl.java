package org.big.service;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.big.common.CommUtils;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BatchInsertServiceImpl implements BatchInsertService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("unused")
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@PersistenceContext
	EntityManager em;

	/**
	 * Taxon
	 * 
	 * @param records
	 * @throws SQLException 
	 */
	public void batchInsertTaxon(List<Taxon> records, String inputtimeStr) throws SQLException {
		String insertSql = " INSERT INTO taxon (id,scientificname,authorstr,epithet,rankid,nomencode,remark,"
				+ "sourcesid,tci,refjson,status,inputer,inputtime,synchstatus,synchdate,"
				+ "taxaset_id,rank_id,chname,taxon_condition,ref_class_sys,expert,taxon_examine,order_num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		//jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstmt, int i) throws SQLException {
				Taxon taxon = records.get(i);
				pstmt.setString(1, taxon.getId());
				pstmt.setString(2, taxon.getScientificname());
				pstmt.setString(3, taxon.getAuthorstr());
				pstmt.setString(4, taxon.getEpithet());
				pstmt.setInt(5, taxon.getRankid());
				pstmt.setString(6, taxon.getNomencode());
				pstmt.setString(7, taxon.getRemark());
				pstmt.setString(8, taxon.getSourcesid());
				pstmt.setString(9, taxon.getTci());
				pstmt.setString(10, taxon.getRefjson());
				pstmt.setInt(11, taxon.getStatus());
				pstmt.setString(12, taxon.getInputer());
				pstmt.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(14, taxon.getSynchstatus());
				pstmt.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(16, taxon.getTaxaset().getId());//taxaset_id
				pstmt.setInt(17, taxon.getRankid());
				pstmt.setString(18, taxon.getChname());
				pstmt.setInt(19, taxon.getTaxonCondition());
				pstmt.setString(20, null);//参考分类体系
				pstmt.setString(21, taxon.getExpert());
				pstmt.setString(22, taxon.getTaxonExamine());
				pstmt.setInt(23, taxon.getOrderNum());
			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	/**
	 * Citation 引证
	 * 
	 * @param records
	 */
	public void batchInsertCitation(List<Citation> records, String inputtimeStr) {
		String insertSql = "INSERT INTO citation (id, sciname, authorship, nametype, citationstr, shortrefs, refjson, "
				+ "sourcesid, status, inputer, inputtime, synchstatus, synchdate, taxon_id, sourcesid_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Citation citation = records.get(i);
				ps.setString(1, citation.getId());
				ps.setString(2, citation.getSciname());
				ps.setString(3, citation.getAuthorship());
				ps.setInt(4, citation.getNametype());
				ps.setString(5, citation.getCitationstr());// 引证原文
				ps.setString(6, citation.getShortrefs());
				ps.setString(7, citation.getRefjson());
				ps.setString(8, citation.getSourcesid());
				ps.setInt(9, citation.getStatus());
				ps.setString(10, citation.getInputer());
				try {
					ps.setDate(11, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ps.setInt(12, citation.getSynchstatus());
				try {
					ps.setDate(13, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ps.setString(14, citation.getTaxon().getId());
				ps.setString(15, citation.getSourcesid());
			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	/**
	 * Description 描述
	 * 
	 * @param records
	 */
	public void batchInsertDescription(List<Description> records, String inputtimeStr) {
		String insertSql = "INSERT INTO description (id, describer, desdate, destitle, descontent, destypeid, "
				+ "rightsholder, licenseid, remark, `language`, relation, sourcesid, refjson, status, "
				+ "inputer, inputtime, synchstatus, synchdate, descriptiontype_id, "
				+ "taxon_id, license_id, traitdata_id, sourcesid_id, relation_des, relation_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Description description = records.get(i);
				ps.setString(1, description.getId());
				ps.setString(2, description.getDescriber());
				ps.setString(3, description.getDesdate());
				ps.setString(4, description.getDestitle());
				ps.setString(5, description.getDescontent());
				ps.setString(6, description.getDestypeid());
				ps.setString(7, description.getRightsholder());
				ps.setString(8, description.getLicenseid());
				ps.setString(9, description.getRemark());
				ps.setString(10, description.getLanguage());
				ps.setString(11, null);// Json格式
				ps.setString(12, description.getSourcesid());
				ps.setString(13, description.getRefjson());
				ps.setInt(14, description.getStatus());
				ps.setString(15, description.getInputer());
				try {
					ps.setDate(16, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ps.setInt(17, description.getSynchstatus());
				try {
					ps.setDate(18, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				ps.setString(19, description.getDescriptiontype().getId());
				ps.setString(20, description.getTaxon().getId());
				ps.setString(21, description.getLicenseid());
				ps.setString(22, null);// traitdata_id
				ps.setString(23, description.getSourcesid());// sourcesid_id
				ps.setString(24, description.getRelationDes());// relation_des
				ps.setString(25, description.getRelationId());// relation_id

			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	/**
	 * Distributiondata 分布
	 * 
	 * @param records
	 */
	public void batchInsertDistributiondata(List<Distributiondata> records, String inputtimeStr) throws Exception {
		String insertSql = "INSERT INTO distributiondata (id, taxonid, lng, lat, refjson, sourcesid, status, inputer, inputtime, "
				+ "synchstatus, synchdate, geojson, taxon_id, discontent) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Distributiondata distributiondata = records.get(i);
				ps.setString(1, distributiondata.getId());
				ps.setString(2, distributiondata.getTaxon().getId());
				ps.setDouble(3, distributiondata.getLng());
				ps.setDouble(4, distributiondata.getLat());
				ps.setString(5, distributiondata.getRefjson());
				ps.setString(6, distributiondata.getSourcesid());
				ps.setInt(7, distributiondata.getStatus());
				ps.setString(8, distributiondata.getInputer());
				try {
					ps.setDate(9, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
				}
				ps.setInt(10, distributiondata.getSynchstatus());
				try {
					ps.setDate(11, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
				}
				ps.setString(12, distributiondata.getGeojson());
				ps.setString(13, distributiondata.getTaxon().getId());
				ps.setString(14, distributiondata.getDiscontent());
			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	/**
	 * Commonname 俗名
	 * 
	 * @param records
	 */
	public void batchInsertCommonname(List<Commonname> records) {
		String insertSql = "INSERT INTO commonname (id,commonname,inputer,inputtime,language,refjson,sourcesid,status,synchdate,synchstatus,"
				+ "taxon_id,taxon,expert,remark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstmt, int i) throws SQLException {
				Commonname commonname = records.get(i);
				pstmt.setString(1, commonname.getId());
				pstmt.setString(2, commonname.getCommonname());
				pstmt.setString(3, commonname.getInputer());
				pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(5, commonname.getLanguage());
				pstmt.setString(6, commonname.getRefjson());
				pstmt.setString(7, commonname.getSourcesid());
				pstmt.setInt(8, commonname.getStatus());
				pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(10, commonname.getSynchstatus());
				pstmt.setString(11, commonname.getTaxon().getId());
				pstmt.setBytes(12, null);
				pstmt.setString(13, commonname.getExpert());
				pstmt.setString(14, commonname.getRemark());

			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	public void batchInsertTaxonHasTaxtree(List<TaxonHasTaxtree> records) {
		String insertSql = "INSERT INTO taxon_has_taxtree (taxon_id, taxtree_id, pid, prev_taxon) VALUES ( ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TaxonHasTaxtree taxonHasTaxtree = records.get(i);
				ps.setString(1, taxonHasTaxtree.getTaxonId());
				ps.setString(2, taxonHasTaxtree.getTaxtreeId());
				ps.setString(3, taxonHasTaxtree.getPid());
				ps.setString(4, taxonHasTaxtree.getPrevTaxon());
			}

			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}

	@Override
	public void batchUpdateTaxonOrderNumById(List<Taxon> records) {
        String sql = "update taxon set order_num=? where id=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return records.size();
                //这个方法设定更新记录数，通常List里面存放的都是我们要更新的，所以返回list.size();
            }
            public void setValues(PreparedStatement ps, int i)throws SQLException {
            	Taxon taxon = records.get(i);
                ps.setInt(1, taxon.getOrderNum());
                ps.setString(2, taxon.getId());
               
            }
        });
		
	}

}
