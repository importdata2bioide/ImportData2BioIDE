package org.big.service;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>UserTeam的Service类接口</b></p>
 *<p> UserTeam的Service类接口，与UserTeam有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface UserTeamService {

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param userId User的id
     * @param teamId Team的id
     * @return void
     */
    void saveOne(String userId,String teamId);
    
    /**
     *<b>权限转让通知邮件</b>
     *<p> 权限转让通知邮件</p>
     * @author WangTianshan (王天山)
     * @param userId User的id
     * @param teamId Team的id
     * @return void
     */
    String SendEmailTransPermissionAdvice(HttpServletRequest request);
}
