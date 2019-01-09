package org.big.common;

import java.io.Serializable;
import java.util.Objects;

/**
 *<p><b>获取java对象内部特征</b></p>
 *<p> 获取传入的java对象的内部特征</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/8 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class Characteristics implements Serializable {

	private static final long serialVersionUID = 1L;
	private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field= field;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Characteristics other = (Characteristics) obj;
        return Objects.equals(this.field, other.field);

    }

    @Override
    public int hashCode() {

        return Objects.hash(this.field);

    }
}