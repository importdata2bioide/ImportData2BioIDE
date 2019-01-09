package org.big.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Traitset;

import com.alibaba.fastjson.JSON;

public interface TraitsetService {
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findTraitsetList(HttpServletRequest request);
	/**
     *<b>Traitset的select列表</b>
     *<p> Traitset的select列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);
	/**
     *<b>根据Id查找一个实体</b>
     *<p> 根据Id查找一个实体</p>
     * @author BINZI
	 * @param id
     * @return org.big.entity.Traitset
     */
	Traitset findOneById(String id);
	 /**
     *<b>修改一个实体</b>
     *<p> 修改一个实体</p>
     * @author BINZI
     * @param thisTraitset 实体
     * @return void
     */
	void updateOneById(@Valid Traitset thisTraitset);
	/**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisTraitset 实体
     * @return void
     */
	void saveOne(@Valid Traitset thisTraitset);
	/**
     *<b>根据Id删除一个实体</b>
     *<p> 根据Id删除一个实体</p>
     * @author BINZI
	 * @param id
     * @return org.big.entity.Traitset
     */
	void removeOne(String Id);
	/**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
    boolean logicRemove(String id);
	
}
