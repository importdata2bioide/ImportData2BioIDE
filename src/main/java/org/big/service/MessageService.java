package org.big.service;

import com.alibaba.fastjson.JSON;
import org.big.entity.Message;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>Message的Service类接口</b></p>
 *<p> Message的Service类接口，与Message有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface MessageService {

    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String ID);


}
