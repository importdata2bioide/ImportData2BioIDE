package org.big.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *<p><b>获取SpringBean</b></p>
 *<p> 通过该类即可在普通工具类里获取spring管理的bean</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/30 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Component
public final class SpringTool implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    /**
     *<b>初始化环境配置</b>
     *<p> 初始化环境配置</p>
     * @author WangTianshan (王天山)
     * @param applicationContext
     * @return void
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringTool.applicationContext == null) {
            SpringTool.applicationContext = applicationContext;
        }
    }

    /**
     *<b>获取环境配置</b>
     *<p> 获取环境配置</p>
     * @author WangTianshan (王天山)
     * @param
     * @return org.springframework.context.ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     *<b>获取Bean</b>
     *<p> 根据name获取Bean</p>
     * @author WangTianshan (王天山)
     * @param name
     * @return java.lang.Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
}