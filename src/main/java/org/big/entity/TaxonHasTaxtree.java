package org.big.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName TaxonHasTaxtree
 * @Author WangTianshan (王天山)
 * @Date 2018/8/9 22:17</p>
 * @Version: 0.1
 * <p>Copyright: WangTianshan - 王天山</p>
 * @Since JDK 1.80_171
 */
@Entity
@Table(name = "taxon_has_taxtree", schema = "biodata", catalog = "")
@IdClass(TaxonHasTaxtreePK.class)
public class TaxonHasTaxtree implements Serializable{
   
	private static final long serialVersionUID = 1L;
	private String taxonId;
    private String taxtreeId;
    private String pid;
    private String prevTaxon;

    @Id
    @Column(name = "taxon_id")
    public String getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }

    @Id
    @Column(name = "taxtree_id")
    public String getTaxtreeId() {
        return taxtreeId;
    }

    public void setTaxtreeId(String taxtreeId) {
        this.taxtreeId = taxtreeId;
    }

    @Basic
    @Column(name = "pid")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "prev_taxon")
    public String getPrevTaxon() {
        return prevTaxon;
    }

    public void setPrevTaxon(String prevTaxon) {
        this.prevTaxon = prevTaxon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaxonHasTaxtree that = (TaxonHasTaxtree) o;
        return Objects.equals(taxonId, that.taxonId) &&
                Objects.equals(taxtreeId, that.taxtreeId) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(prevTaxon, that.prevTaxon);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taxonId, taxtreeId, pid, prevTaxon);
    }
}
