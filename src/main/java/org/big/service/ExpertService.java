package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.entity.Expert;

import com.alibaba.fastjson.JSON;

public interface ExpertService {
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询功能</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findExpertList(HttpServletRequest request);

	/**
     *<b>Expert的select列表</b>
     *<p> 当前Expert的select检索列表</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelect(HttpServletRequest request);

	/**
     *<b>导出审核专家数据</b>
     *<p> 导出审核专家数据</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	List<Expert> export(HttpServletResponse response);

	/**
     *<b>根据id查找Expert实体</b>
     *<p> 根据id查找Expert实体</p>
     * @author BINZI
	 * @param id
	 * @return
	 */
	Expert findOneById(String id);
	/**
	 * findOrCreateByNameAndInputer 根据姓名查询，结果为空则创建一条新记录。
	 * title: ExpertService.java
	 * @param expertNames
	 * @return
	 * @author ZXY
	 */
	Expert findOrCreateByNameAndInputer(String expertNames,String inputerId);

}
