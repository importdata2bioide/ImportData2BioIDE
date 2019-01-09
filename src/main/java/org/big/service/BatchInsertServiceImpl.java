package org.big.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
public class BatchInsertServiceImpl implements BatchInsertService{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@SuppressWarnings("unused")
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@PersistenceContext
	EntityManager em;
	

	/**
	 * Taxon
	 * @param records
	 */
	public void batchInsertTaxon(List<Taxon> records,String inputtimeStr) {
		String insertSql = "INSERT INTO taxon (id, scientificname, authorstr, epithet, rankid, nomencode, remark, sourcesid, tci,"
				+ " refjson, status, inputer, inputtime, synchstatus, synchdate, taxaset_id, rank_id, chname, sourcesid_id "
				+ " ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,)";      
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Taxon taxon = records.get(i);
				String id = taxon.getId();
				ps.setString(1, id);
				String scientificname = taxon.getScientificname();
				ps.setString(2, scientificname);
				String authorstr = taxon.getAuthorstr();
				ps.setString(3, authorstr);
				String epithet = taxon.getEpithet();
				ps.setString(4, epithet);
				String id2 = taxon.getRank().getId();
				ps.setString(5, id2);
				String nomencode = taxon.getNomencode();
				ps.setString(6, nomencode);
				String remark = taxon.getRemark();
				ps.setString(7, remark);
				String sourcesid = taxon.getSourcesid();
				ps.setString(8, sourcesid);
				String tci = taxon.getTci();
				ps.setString(9, tci);
				String refjson = taxon.getRefjson();
				ps.setString(10, refjson);
				int status = taxon.getStatus();
				ps.setInt(11, status);
				String inputer = taxon.getInputer();
				ps.setString(12, inputer);
				try {
					ps.setDate(13, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int synchstatus = taxon.getSynchstatus();
				ps.setInt(14, synchstatus);
				try {
					ps.setDate(15, CommUtils.getSqlDate(inputtimeStr));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String id3 = taxon.getTaxaset().getId();
				ps.setString(16, id3);
				String rankid = taxon.getRankid();
				ps.setString(17, rankid);
				String chname = taxon.getChname();
				ps.setString(18, chname);
				String sourcesid2 = taxon.getSourcesid();
				ps.setString(19, sourcesid2);
			}

			@Override
			public int getBatchSize() {
				return records.size();
			}});
	}
	
	/**
	 * Citation 引证
	 * @param records
	 */
	public void batchInsertCitation(List<Citation> records,String inputtimeStr) {
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
				ps.setString(5,citation.getCitationstr());//引证原文
				ps.setString(6, citation.getShortrefs());
				ps.setString(7, citation.getRefjson());
				ps.setString(8, citation.getSourcesid());
				ps.setInt(9,citation.getStatus());
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
	 * @param records
	 */
	public void batchInsertDescription(List<Description> records,String inputtimeStr) {
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
				ps.setString(11, null);//Json格式
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
				ps.setString(22, null);//traitdata_id
				ps.setString(23, description.getSourcesid());//sourcesid_id
				ps.setString(24, description.getRelationDes());//relation_des
				ps.setString(25, description.getRelationId());//relation_id
				
			}
			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}
	
	/**
	 * Distributiondata 分布
	 * @param records
	 */
	public void batchInsertDistributiondata(List<Distributiondata> records, String inputtimeStr ) throws Exception {
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
				ps.setString(12,distributiondata.getGeojson());
				ps.setString(13, distributiondata.getTaxon().getId());
				ps.setString(14,distributiondata.getDiscontent() );
			}
			@Override
			public int getBatchSize() {
				return records.size();
			}
		});
	}
	
	
	/**
	 * Commonname 俗名
	 * @param records
	 */
	public void batchInsertCommonname(List<Commonname> records) {
		String insertSql = "";      
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				records.get(i);
				 
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


}
