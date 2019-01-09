package org.big.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 *<p><b>排序支持工具</b></p>
 *<p> 用于复制SpringData排序</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public final class QueryTool {

    /**
     *<b>创建排序实体</b>
     *<p> 传入排序字段和排序类型关键参数返回SpringData规定排序Sort类型</p>
     * @author WangTianshan (王天山)
     * @param sortRow 排序字段
     * @param sortType 排序类型(顺序)
     * @return org.springframework.data.domain.Sort
     */
    public static Sort buildSort(String sortRow, String sortType){
        Sort sort = null;
        if("asc".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, sortRow);
        } else {
            sort = new Sort(Sort.Direction.DESC, sortRow);
        }
        return sort;
    }

    /**
     *<b>创建分页排序实体</b>
     *<p> 传入页码起始页、页面大小、排序字段和排序类型关键参数返回SpringData规定排序PageRequest类型</p>
     * @author WangTianshan (王天山)
     * @param offsetNumber 起始页
     * @param pageSize 页面大小
     * @param sortRow 排序字段
     * @param sortType 排序类型(顺序)
     * @return org.springframework.data.domain.PageRequest
     */
    @SuppressWarnings("deprecation")
	public static PageRequest buildPageRequest(int offsetNumber, int pageSize, String sortRow, String sortType){
        int pageNumber=offsetNumber/pageSize;
        Sort sort = null;
        if("asc".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, sortRow);
        } else {
            sort = new Sort(Sort.Direction.DESC, sortRow);
        }
        return new PageRequest(pageNumber, pageSize, sort);
    }
}
