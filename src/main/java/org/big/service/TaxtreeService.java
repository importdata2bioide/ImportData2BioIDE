package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *<p><b>Rank的Service类接口</b></p>
 *<p> Rank的Service类接口，与Rank有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2018/8/2 23:48</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface TaxtreeService {
	List<TaxonHasTaxtree> findByTaxtreeId(String taxtreeId);

	
	int deleteByTaxtreeId(String taxtreeId);
	/**
	 * 查询某个数据集下的所有分类树
	 * title: TaxtreeService.java
	 * @param datasetId
	 * @return
	 * @author ZXY
	 */
	List<Taxtree> findTaxtreeByDataset(String datasetId);
	/**
	 * 查询某个分类树下某节点的所有孩子节点
	 * @param nodeId
	 * @param taxtreeId
	 * ZXY 
	 * @return
	 */
	List<TaxonHasTaxtree> findNodeAndAllChildren(String nodeId,String taxtreeId);
    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param thisTaxtree 实体
     * @return void
     */
    void saveOne(Taxtree thisTaxtree);
    
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param Id 实体的id
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
     * @author WangTianshan (王天山)
     * @param thisTaxaSet 实体
     * @return void
     */
    void updateOneById(Taxtree thisTaxaSet);
    
    /**
     *<b>根据TaxtreeId查找一个Taxtree实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param Id 实体的id
     * @return org.big.entity.Taxtree
     */
    Taxtree findOneById(String Id);
    
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findTaxtreeList(HttpServletRequest request);

    /**
     *<b>Taxtree的select列表</b>
     *<p> 当前Dataset下的Taxtree的select检索列表</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findBySelect(HttpServletRequest request);
    /**
     *<b>Taxtree的select列表(含新建)</b>
     *<p> 当前Dataset下的Taxtree的select检索列表(含新建)</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
    JSON findBySelectAndNew(HttpServletRequest request);

    /**
     *<b>存储一个新的Taxtree实体</b>
     *<p> 存储一个新的Taxtree实体</p>
     * @author WangTianshan (王天山)
     * @param thisTaxtree 实体
     * @return com.alibaba.fastjson.JSON
     */
	JSON newOne(Taxtree thisTaxtree, HttpServletRequest request);

    /**
     *<b>存储一个新的tree结构</b>
     *<p> 存储一个新的tree结构</p>
     * @author WangTianshan (王天山)
     * @param thisTaxonHasTaxtree
     * @return void
     */
    void saveOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree);


    /**
     *<b>根据treeid和taxonid查找一个tree结构</b>
     *<p> 根据treeid和taxonid查找一个tree结构</p>
     * @author WangTianshan (王天山)
     * @param thisTaxonHasTaxtree
     * @return void
     */
    TaxonHasTaxtree findOneTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree);

    /**
     *<b>判断节点是否存在</b>
     *<p> 判断节点是否存在</p>
     * @author WangTianshan (王天山)
     * @param thisTaxonHasTaxtree
     * @return void
     */
    boolean hasTaxonHasTaxtree(TaxonHasTaxtree thisTaxonHasTaxtree);

    /**
     *<b>浏览Taxtree时根据父节点检索子节点</b>
     *<p> 浏览Taxtree时根据父节点检索子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    JSONArray showChildren(String taxonId,String taxtreeId);

    /**
     *<b>查询Taxtree时根据父节点检索子节点</b>
     *<p> 查询Taxtree时根据父节点检索子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return List<TaxonHasTaxtree>
     */
    List<TaxonHasTaxtree> findChildren(String taxonId,String taxtreeId);

    /**
     *<b>删除单一节点</b>
     *<p> 删除单一节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    boolean removeOneNode(String taxonId,String taxtreeId);

    /**
     *<b>删除所有子节点</b>
     *<p> 删除所有子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    boolean removeChindren(String taxonId,String taxtreeId);

    /**
     *<b>根据id删除自己和所有一级子节点</b>
     *<p> 根据id删除自己和所有一级子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    boolean removeNodeAndChindren(String taxonId,String taxtreeId);

    /**
     *<b>根据id删除自己和所有n级子节点</b>
     *<p> 根据id删除自己和所有一级子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    boolean removeNodeAndAllChindren(String taxonId,String taxtreeId);

    /**
     *<b>添加节点和所有n级子节点</b>
     *<p> 添加节点和所有n级子节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    boolean addNodeAndAllChindren(String taxonId,String taxtreeId,String origTaxtreeId);

    /**
     *<b>根据treeid和taxonid查找一个tree节点</b>
     *<p> 根据treeid和taxonid查找一个tree节点</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return TaxonHasTaxtree
     */
    TaxonHasTaxtree findOneTaxonHasTaxtreeByIds(String taxonId,String taxtreeId);

    /**
     *<b>排序</b>
     *<p> 排序</p>
     * @author WangTianshan (王天山)
     * @param nodeList
     * @return List<TaxonHasTaxtree>
     */
    List<TaxonHasTaxtree> sortTaxonHasTaxtree(List<TaxonHasTaxtree> nodeList);

    /**
     *<b>找到下一个节点</b>
     *<p> 找到下一个节点</p>
     * @author WangTianshan (王天山)
     * @param nodeList
     * @return TaxonHasTaxtree
     */
    TaxonHasTaxtree findNextNode(List<TaxonHasTaxtree> nodeList,String thisNodeId);

    /**
     *<b>找到第一个节点</b>
     *<p> 找到第一个节点</p>
     * @author WangTianshan (王天山)
     * @param nodeList
     * @return TaxonHasTaxtree
     */
    TaxonHasTaxtree findHeadNode(List<TaxonHasTaxtree> nodeList);

    /**
     *<b>找到最后一个节点</b>
     *<p> 找到最后一个节点</p>
     * @author WangTianshan (王天山)
     * @param nodeList
     * @return TaxonHasTaxtree
     */
    TaxonHasTaxtree findLastNode(List<TaxonHasTaxtree> nodeList);

    /**
     *<b>找到下一个节点</b>
     *<p> 找到下一个节点</p>
     * @author WangTianshan (王天山)
     * @return TaxonHasTaxtree
     */
    TaxonHasTaxtree findNextNode(String taxonId,String taxtreeId);


    /**
     *<b>统计子节点数</b>
     *<p> 统计子节点数</p>
     * @author WangTianshan (王天山)
     * @param taxonId
     * @param taxtreeId
     * @return com.alibaba.fastjson.JSON
     */
    int countChindren(String taxonId,String taxtreeId);
    /**
     * <b>logic:根据taxon 的remark字段实现树的构建。</b>
     * <b>remark为空，此taxon为根节点；remark的parentId为空，此taxon为根节点；否则为子节点。</b>
     * <b>根节点的 pid 为 taxtreeId</b>
     * title: TaxtreeService.java
     * @param taxonlist
     * @param taxtreeId
     * @return
     * @author ZXY
     */
    int saveTreeByJsonRemark(List<Taxon> taxonlist,String taxtreeId);
    /**
     * 
     * @Description 根据taxon.order_num字段更新整棵树的preTaxon字段
     * @Description 第一个节点的preTaxon字段值为null
     * @param taxtreeId
     * @author ZXY
     */
    public void updatePreTaxonByOrderNum(String taxtreeId);
    /**
     * 
     * @Description 查询一棵树的所有孩子节点，
     * @param taxtreeId
     * @return
     * @author ZXY
     */
    public List<TaxonHasTaxtree> findAllChildrenByTxtree(String taxtreeId);

}