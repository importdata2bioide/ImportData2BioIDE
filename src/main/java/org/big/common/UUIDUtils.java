package org.big.common;

import java.util.UUID;

/**
 *<p><b>UUID构造类</b></p>
 *<p> 通过该类即可在普通工具类里获取spring管理的bean</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/01 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class UUIDUtils {

    /**
     *<b>创建32位的UUID</b>
     *<p> 构造UUID后去掉“-”</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
    public static String getUUID32(){
        UUID uuid = UUID.randomUUID();
        String uuidStr =  uuid.toString();
        uuidStr = uuidStr.replace("-", "");
        return uuidStr;
    }
}
