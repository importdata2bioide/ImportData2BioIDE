package org.big.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.big.entity.Ref;
import org.big.entityVO.RefTypeEnum;
import org.big.sp2000.entity.Reference;


public interface RefService {
	/**
	 * 
	 * @Description 完整引证解析：作者、年代、标题、语言
	 * @param ref
	 * @param line
	 * @author ZXY
	 */
	void parseLineChineseOrEng(Ref ref, String line);
	
	List<Reference> findRefByUserTurnToReference(String userId) throws Exception;
	
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
	 * <b>查询失败，创建一条参考文献</b>
	 * title: RefService.java
	 * @param refstr
	 * @param inputerId
	 * @param remark
	 * @return
	 * @author ZXY
	 */
	Ref insertRefIfNotExist(String refstr,String inputerId,String remark);
	/**
	 * 
	 * @Description 
	 * @param oldRefstr 数据库中存储的字符串
	 * @param newRefId 需要增加到数据库中的参考文献Id
	 * @param refType 参考文献类型
	 * @return
	 * @author ZXY
	 */
	String addRefJson(String oldRefstr,String newRefId,RefTypeEnum refType,String refS,String refE);
}
