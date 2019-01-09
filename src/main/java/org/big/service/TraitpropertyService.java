package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Traitproperty;

import com.alibaba.fastjson.JSON;

public interface TraitpropertyService {

	/**
     *<b>Traitproperty的select列表</b>
     *<p> Traitproperty的select列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);

	/**
     *<b>根据Id查找一个实体</b>
     *<p> 根据Id查找一个实体</p>
     * @author BINZI
     * @param id 实体
     * @return 
     */
	Traitproperty findOneById(String id);
}
