package org.big.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.big.entity.Molecular;

import com.alibaba.fastjson.JSON;

public interface MolecularService {
	/**
	 *<b>Molecular的select列表</b>
	 *<p> Molecular的检索列表</p>
	 * @author BINZI
	 * @param request 页面请求
	 * @return com.alibaba.fastjson.JSON
	 */
	JSON findMolecularList(HttpServletRequest request);
	
	/**
	 * <b>添加Molecular基础数据</b>
	 * <p> 添加Molecular基础数据</p>
	 * @author BINZI
	 * @param thisMolecular
	 */
	JSON addMolecular(@Valid Molecular thisMolecular, HttpServletRequest request);
	
	/**
     *<b> 根据id逻辑删除一个实体</b>
     *<p> 据id逻辑删除一个实体</p>
     * @param id
     * @return
     */
	boolean logicRemove(String id);
	
}
