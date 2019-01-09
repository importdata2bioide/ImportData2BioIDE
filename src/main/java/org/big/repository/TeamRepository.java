package org.big.repository;

import org.big.entity.Team;
import org.big.repository.base.BaseRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *<p><b>Team的DAO类接口</b></p>
 *<p> Team的DAO类接口，与Team有关的持久化操作方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TeamRepository extends BaseRepository<Team, String> {

    /**
     *<b>根据用户id查询Team列表</b>
     *<p> 根据用户id查询Team列表，即根据用户id查找其所属的Team列表</p>
     * @author WangTianshan (王天山)
     * @param user_id 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Team>
     */			    
	@Query(value ="SELECT t FROM Team AS t LEFT JOIN UserTeam AS ut ON ut.teamId = t.id WHERE ut.userId = ?1")	
    List<Team> selectTeamsByUserId(String uid);

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Team>
     */
    @Query(value = "select t from Team t where (t.name like %?1% or t.leader like %?1%)")
    Page<Team> searchInfo(String findText, Pageable pageable);

    /**
     *<b>带分页排序的条件查询(仅显示所属的团队)</b>
     *<p> 根据用户id查询Team列表，带分页排序的条件查询功能</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param user_id 当前操作用户的id
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<java.lang.Object>
     */
    @Query(value = "SELECT " +
            "t.id AS id," +
            "t.name AS name," +
            "u.userName AS leader," +
            "u.nickname AS leaderName," +
            "t.note AS note," +
            "t.adddate AS adddate " +
            "FROM Team t " +
            "LEFT JOIN UserTeam ut " +
            "ON ut.teamId=t.id " +
            "LEFT JOIN User u " +
            "ON u.id=t.leader " +
            "WHERE " +
            "ut.userId=?2 "+
            "AND (" +
            "t.name LIKE %?1% " +
            "OR u.userName LIKE %?1%)"
    )
    Page<Object> searchInfoByUser(String findText, String user_id, Pageable pageable);

    /**
     *<b>根据Team的name查询一个符合条件的Team</b>
     *<p> 根据Team的name查询一个符合条件的Team</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.Team>
     */
    Team findOneByName(String name);

    /**
     *<b>统计指定Team的成员计数</b>
     *<p> 统计指定Team的成员计数</p>
     * @author WangTianshan (王天山)
     * @param Id Team的id
     * @return void
     */
    @Query(" select count(ut) from UserTeam ut where ut.teamId = ?1")
    Integer countMembersByTeamId(String Id);

    /**
     *<b>统计指定Team的成员计数</b>
     *<p> 统计指定Team的成员计数</p>
     * @author WangTianshan (王天山)
     * @param Id Team的id
     * @return void
     */
    @Query(" select count(d) from Dataset d where d.team.id = ?1")
    Integer countDatasetsByTeamId(String Id);
    /**
     * <b>通过Id查找一个Team实体</b>
     * <p>	通过Id查找一个Team实体</p>
     * @param teamId
     * @return
     */
    @Cacheable(value="findTeamOneById")
    @Query("select t from Team t where t.id = ?1")
	Team findOneById(String teamId);
}
