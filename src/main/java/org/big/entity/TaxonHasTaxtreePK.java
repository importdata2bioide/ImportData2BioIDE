package org.big.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName TaxonHasTaxtreePK
 * @Author WangTianshan (王天山)
 * @Date 2018/8/9 22:17</p>
 * @Version: 0.1
 * <p>Copyright: WangTianshan - 王天山</p>
 * @Since JDK 1.80_171
 */
public class TaxonHasTaxtreePK implements Serializable {

	private static final long serialVersionUID = 1L;
	private String taxonId;
    private String taxtreeId;

    @Column(name = "taxon_id")
    @Id
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Column(name = "taxtree_id")
    @Id
    public String getTaxtreeId() {
        return taxtreeId;
    }

    public void setTaxtreeId(String taxtreeId) {
        this.taxtreeId = taxtreeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonHasTaxtreePK that = (TaxonHasTaxtreePK) o;
        return Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(taxtreeId, that.taxtreeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taxonId, taxtreeId);
    }
}
