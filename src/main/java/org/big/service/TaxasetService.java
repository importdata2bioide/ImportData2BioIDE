package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Taxaset;

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
public interface TaxasetService {
	// 自定义Taxaset 增 删 改 查 (一方)
    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisTaxaset 实体
     * @return void
     */
    void saveOne(Taxaset thisTaxaset);	// 新增 | 修改 -- 是否有ID
    
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String Id);
    
    /**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
    boolean logicRemove(String id);
    
    /**
     *<b>修改一个实体</b>
     *<p> 修改一个实体</p>
     * @author BINZI
     * @param thisTaxaset 实体
     * @return void
     */
    void updateOneById(Taxaset thisTaxaSet);
    
    /**
     *<b>根据TaxasetId查找一个Taxaset实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Taxaset
     */
    Taxaset findOneById(String Id);

    /**
     *<b>根据tsName查找一个实体</b>
     *<p> 据tsName查找一个实体</p>
     * @author BINZI
     * @param name 实体的name
     * @return org.big.entity.Taxaset
     */
    Taxaset findOneByTsname(String tsname);
    
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findTaxasetList(HttpServletRequest request);
    
    /**
     *<b>查找指定Dataset下的所有Taxaset</b>
     *<p> 查找指定Dataset下的所有Taxaset</p>
     * @author BINZI
     * @param id
     * @return java.util.List 
     */
    List<Taxaset> findTaxasetsByDatasetId(String id);

    /**
     *<b>Taxaset的select列表</b>
     *<p> 当前Dataset下的Taxaset的select检索列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findBySelect(HttpServletRequest request);

    /**
     *<b>Taxaset的select列表(含新建)</b>
     *<p> 当前Dataset下的Taxaset的select检索列表(含新建)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findBySelectAndNew(HttpServletRequest request);

    /**
     *<b>存储一个新的Taxaset实体</b>
     *<p> 存储一个新的Taxaset实体</p>
     * @author BINZI
     * @param thisTaxaset 实体
     * @return com.alibaba.fastjson.JSON
     */
	JSON newOne(Taxaset thisTaxaset, HttpServletRequest request);

}