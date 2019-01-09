package org.big.service;



import org.big.entity.Team;
import java.util.List;

/**
 *<p><b>Team的Service类接口</b></p>
 *<p> Team的Service类接口，与Team有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface TeamService {

    /**
     *<b>根据用户id查询Team列表</b>
     *<p> 根据用户id查询Team列表，即根据用户id查找其所属的Team列表</p>
     * @author WangTianshan (王天山)
     * @param user_id 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Team>
     */
    List<Team> selectTeamsByUserId(String uid);

    /**
     *<b>根据teamid查找一个Team实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return org.big.entity.Team
     */
    Team findbyID(String ID);

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param thisTeam 实体
     * @return void
     */
    void saveOne(Team thisTeam);

    /**
     *<b>保存一个实体和用户的关系</b>
     *<p> 保存一个实体，并将当前操作用户设为Team leader，并加入关系表中</p>
     * @author WangTianshan (王天山)
     * @param thisTeam 实体
     * @return void
     */
    void saveOneByUser(Team thisTeam);

    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    Boolean removeOne(String ID);

    /**
     *<b>根据id删除一个实体和用户的关系</b>
     *<p> 据id删除一个实体，并在关系表中删除与之有关的所有记录</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    Boolean removeOneByUser(String ID);

    Team findOneByName(String name);

    /**
     *<b>根据TeamId统计成员数量</b>
     *<p> 根据TeamId统计成员数量</p>
     * @author WangTianshan (王天山)
     * @param ID Team的ID
     * @return int
     */
    int countMembersByTeamId(String ID);

	
	/**
	 *<b>保存修改Team实体</b>
	 *<p> 保存修改Team实体</p>
	 * @author BINZI
	 * @param thisTeam 实体
	 * @return
	 */
	void saveForUpdate(Team thisTeam);

}
