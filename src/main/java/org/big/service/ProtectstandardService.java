package org.big.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;

public interface ProtectstandardService {
	
    /**
     *<b>Protectstandard的select列表(保护标准名称)</b>
     *<p> 当前Taxon下的Protectstandard的select检索列表(保护标准名称)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelectStandard(HttpServletRequest request);
	
    /**
     *<b>Protectstandard的select列表(保护标准版本)</b>
     *<p> 当前Taxon下的Protectstandard的select检索列表(保护标准版本)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelectVersion(HttpServletRequest request, String standardname);
	
	/**
     *<b>Protectstandard的select列表(保护标准级别)</b>
     *<p> 当前Taxon下的Protectstandard的select检索列表(保护标准级别)</p>
     * @author BINZI
     * @param request 页面请求
     * @return com.alibaba.fastjson.JSON
     */
	JSON findBySelectProtlevel(HttpServletRequest request, String version, String standardname);
}
