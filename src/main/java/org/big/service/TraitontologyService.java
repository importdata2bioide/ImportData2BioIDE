package org.big.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Traitontology;

import com.alibaba.fastjson.JSON;

public interface TraitontologyService {
	/**
     *<b>Traitontology的select列表</b>
     *<p> Traitontology的select列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request, String trainsetid);

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findTraitontologyServiceList(HttpServletRequest request);
	
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
     * @param thisTraitset 实体
     * @return void
     */
	void updateOneById(@Valid Traitontology thisTraitontology);
	
	/**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisTraitset 实体
     * @return void
     */
	void saveTraitontology(@Valid Traitontology thisTraitontology);

	/**
     *<b>根据Id查找一个实体</b>
     *<p> 根据Id查找一个实体</p>
     * @author BINZI
     * @param id 实体
     * @return 
     */
	Traitontology findOneById(String id);

}
