package org.big.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>IndexController类</b></p>
 *<p> Index的Controller</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Controller
public class IndexController {
   
    /**
     *<b>默认页面</b>
     *<p>访问项目默认访问的方法</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @return java.lang.String
     */
    @RequestMapping(value="/", method = {RequestMethod.GET})
    public String Index(HttpServletRequest request) {
    	
        return "redirect:/JumpPageController_index";
    }

   
    
}
