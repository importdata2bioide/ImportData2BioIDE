package org.big.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.big.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 *<p><b>User的Service类接口</b></p>
 *<p> User的Service类接口，与User有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface UserService {

    /**
     *<b>根据id查找一个实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return org.big.entity.User
     */
    User findbyID(String ID);

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param thisUser 实体
     * @return void
     */
    void addUserBySuoer(User thisUser);

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisUser 实体
     * @return void
     */
	void editUserBySuoer(User thisUser);
	
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM User WHERE id = ?1")
    void removeOne(String ID);

  
    /**
     *<b>根据user_name查找一个实体</b>
     *<p> 据user_name查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param user_name 实体的user_name
     * @return org.big.entity.User
     */
    User findOneByName(String user_name);

    /**
     *<b>根据email查找一个实体</b>
     *<p> 据email查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param email 实体的email
     * @return org.big.entity.User
     */
    User findOneByEmail(String email);
    
 
    /**
     *<b>改变实体状态</b>
     *<p> 改变实体状态</p>
     * @author WangTianshan (王天山)
     * @param thisUser 实体
     * @param status 状态代码
     * @return void
     */
    void changeStatus(User thisUser,int status);
    
    /**
     *<b>注册</b>
     *<p> 注册</p>
     * @author WangTianshan (王天山)
     * @param newUser newUser
     * @param request 页面请求
     * @param response 页面响应
     * @return java.lang.String
     */
    String registerNewOne(HttpServletRequest request, User newUser);
    
    /**
     *<b>发送激活邮件</b>
     *<p> 发送激活邮件</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @param response 页面响应
     * @return java.lang.String
     */
    String sendActiveEmail(HttpServletRequest request, HttpServletResponse response);
    
    /**
     *<b>激活用户</b>
     *<p> 激活用户</p>
     * @author WangTianshan (王天山)
     * @param username 用户名
     * @param mark 激活码
     * @param request 页面请求
     * @param response 页面响应
     * @return java.lang.String
     */
    String activeUser(String userName, String mark,HttpServletRequest request);
    
    /**
     *<b>发送密码找回邮件</b>
     *<p> 发送密码找回邮件</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @param response 页面响应
     * @return java.lang.String
     */
    String sendPasswordEmail(HttpServletRequest request, HttpServletResponse response);
    
    /**
     *<b>发送密码找回邮件</b>
     *<p> 发送密码找回邮件</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    Boolean canRestPassword(String username,String mark);

    /**
     *<b>重置密码</b>
     *<p> 重置密码</p>
     * @author WangTianshan (王天山)
     * @return java.lang.String
     */
    Boolean restPassword(String username,String password);
    
  
}
