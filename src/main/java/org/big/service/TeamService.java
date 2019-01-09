package org.big.service;


import com.alibaba.fastjson.JSON;

import org.big.entity.Team;
import javax.servlet.http.HttpServletRequest;
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

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findbyInfo(HttpServletRequest request);

    /**
     *<b>带分页排序的条件查询(仅显示所属的团队)</b>
     *<p> 根据用户id查询Team列表，带分页排序的条件查询功能</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findbyUser(HttpServletRequest request);

    /**
     *<b>根据name查找一个实体</b>
     *<p> 据name查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param name 实体的name
     * @return org.big.entity.Team
     */
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
     *<b>根据TeamId与UserIds删除成员</b>
     *<p> 根据TeamId与UserIds删除成员</p>
     * @author WangTianshan (王天山)
     * @param teamId Team的ID
     * @param userId User的ID
     * @return int
     */
    boolean removeMembersByTeamIdAndUserId(HttpServletRequest request);
    
    /**
     *<b>根据TeamId与UserIds完成团队负责人权限转换</b>
     *<p> 根据TeamId与UserIds完成团队负责人权限转换</p>
     * @author BINZI (王天山)
     * @param teamId Team的ID
     * @param userId User的ID
     * @return int
     */
    void updateTeamInfoByLeader(HttpServletRequest request);
    
    /**
     *<b>存储一个新的Team实体</b>
     *<p> 存储一个新的Team实体</p>
     * @author BINZI
     * @param thisTeam 实体
     * @return com.alibaba.fastjson.JSON
     */
	JSON newOne(Team thisTeam, HttpServletRequest request);
	
	/**
	 *<b>保存修改Team实体</b>
	 *<p> 保存修改Team实体</p>
	 * @author BINZI
	 * @param thisTeam 实体
	 * @return
	 */
	void saveForUpdate(Team thisTeam);

    /**
     *<b>根据userId查找它所属的所有Team信息</b>
     *<p> 根据userId查找它所属的所有Team信息</p>
     * @param request
     * @return
     */
    JSON findTeamListByUserId(String userId, HttpServletRequest request);
}
