package org.big.repository;

import org.big.entity.User;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>User的DAO类接口</b></p>
 *<p> User的DAO类接口，与User有关的持久化操作方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface UserRepository extends BaseRepository<User, String> {
	
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    @Query(value = "select u from User u" +
                    " where (" +
                    "u.userName like %?1% " +
                    "or u.email like %?1% " +
                    "or u.nickname like %?1% " +
                    "or u.phone like %?1%)"
    )
    
    Page<User> searchInfo(
        String findText,
        Pageable pageable
    );
    /**
     *<b>根据User的userID查询一个符合条件的User</b>
     *<p> 根据User的userID查询一个符合条件的User</p>
     * @author BINZI
     * @param userId 用户ID
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    
    User findOneById(String userId);
    
    /**
     *<b>根据User的UserName查询一个符合条件的User</b>
     *<p> 根据User的UserName查询一个符合条件的User</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    
    User findOneByUserName(String name);
    /**
     *<b>根据User的UserName查询一个符合条件的User</b>
     *<p> 根据User的UserName查询一个符合条件的User</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    
    User findOneByNickname(String name);

    /**
     *<b>带分页排序的条件查询的Team成员列表</b>
     *<p> 带分页排序的条件查询的当前用户所能查看权限的Team成员列表</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    @Query(value = "select u from User u " +
            "LEFT JOIN UserTeam ut "+
            "ON ut.teamId=?1"+
            " where " +
            "ut.userId=u.id AND"+
            "(" +
            "u.userName like %?2% " +
            "or u.email like %?2% " +
            "or u.nickname like %?2%)"
    )
    Page<User> searchByTeamId(
            String teamId,
            String findText,
            Pageable pageable
    );
    
    /**
     *<b>分页条件查询根据User的UserName & Email查询一个符合条件的User</b>
     *<p> 分页条件查询根据User的UserName & Email查询一个符合条件的User</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    @Query(value = "select u.nickname, u.email from User u" +
            " where (u.nickname like %?1% or u.email like %?1%) and u.email != ?2")
    Page<Object> findUserInfoByEmail(String findText, String email, Pageable pageable);
    
    /**
     * 根据邮箱查user对象
     * @param email
     * @return
     */
	User findOneByEmail(String email);
}
