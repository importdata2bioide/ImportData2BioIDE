package org.big.common;

import org.hibernate.dialect.MySQLDialect;

import java.sql.Types;

/**
 *<p><b>自定一个Dialect，注册到hibernate框架中</b></p>
 *<p> 用于SpringDataJPA的拓展</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/28 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class JsonMySQLDialect extends MySQLDialect {

    public JsonMySQLDialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }

}