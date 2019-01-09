package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Rank;

import com.alibaba.fastjson.JSON;

/**
 *<p><b>Rank的Service类接口</b></p>
 *<p> Rank的Service类接口，与Rank有关的业务逻辑方法</p>
 * @author BINZI
 *<p>Created date: 2018/5/18 25:40</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface RankService {
	// 一方
	// 自定义Rank 增 删 改 查
    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisRank 实体
     * @return void
     */
    void saveOne(Rank thisRank);	// 新增 | 修改 -- 是否有ID
    
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String Id);
    
    /**
     *<b>修改一个实体</b>
     *<p> 修改一个实体</p>
     * @author BINZI
     * @param thisRank 实体
     * @return void
     */
    void updateOneById(Rank thisRank);
    /**
     *<b>查询所有Rank实体</b>
     *<p> 查询所有Rank实体</p>
     * @return
     */
//    List<Rank> findAll();
    
    /**
     *<b>根据rankId查找一个Rank实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Rank
     */
    Rank findOneById(String Id);

    /**
     *<b>根据EnName查找一个实体</b>
     *<p> 据EnName查找一个实体</p>
     * @author BINZI
     * @param name 实体的name
     * @return org.big.entity.Rabk
     */
    Rank findOneByEnname(String EnName);
    
    /**
     * <b> 查Rank数据</b>
     * <p> 查Rank数据</P>
     * @param request
     * @return
     */
	JSON findBySelect(HttpServletRequest request);

}
