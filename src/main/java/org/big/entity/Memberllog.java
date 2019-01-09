package org.big.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 *<p><b>Memberllog的Entity类</b></p>
 *<p> Memberllog的Entity类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
public class Memberllog {
    private String id;
    private String teamid;
    private String initiator;
    private String operator;
    private Timestamp starttime;
    private String type;
    private Integer status;
    private Timestamp endtime;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "teamid")
    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    @Basic
    @Column(name = "initiator")
    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    @Basic
    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Basic
    @Column(name = "starttime")
    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "endtime")
    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Memberllog that = (Memberllog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (teamid != null ? !teamid.equals(that.teamid) : that.teamid != null) return false;
        if (initiator != null ? !initiator.equals(that.initiator) : that.initiator != null) return false;
        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (starttime != null ? !starttime.equals(that.starttime) : that.starttime != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (endtime != null ? !endtime.equals(that.endtime) : that.endtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (teamid != null ? teamid.hashCode() : 0);
        result = 31 * result + (initiator != null ? initiator.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (starttime != null ? starttime.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (endtime != null ? endtime.hashCode() : 0);
        return result;
    }
}
