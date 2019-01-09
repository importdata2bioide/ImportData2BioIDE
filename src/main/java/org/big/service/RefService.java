package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.big.entity.Ref;


public interface RefService {
	
	/**
	 * <b>根据id查询Datasource实体</b>
	 * <p> 根据id查询Datasource实体</p>
	 * @param id
	 * @return
	 */
	Ref findOneById(String id);
	
	/**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisRef 实体
     * @return void
     */
	void saveOne(@Valid Ref thisRef);
	
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
     * @param thisRef 实体
     * @return void
     */
	void updateOneById(@Valid Ref thisRef);

	/**
     *<b>导出当前登录用户的数据源</b>
     *<p> 导出当前登录用户的数据源</p>
     * @author BINZI
	 * @param response
	 * @return
	 */
	List<Ref> export(HttpServletResponse response);
	/**
	 * <b>查询失败，创建一条参考文献</b>
	 * title: RefService.java
	 * @param refstr
	 * @param inputerId
	 * @param remark
	 * @return
	 * @author ZXY
	 */
	Ref insertRefIfNotExist(String refstr,String inputerId,String remark);
}
