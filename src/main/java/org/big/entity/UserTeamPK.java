package org.big.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *<p><b>UserTeam外键类</b></p>
 *<p> UserTeam外键类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/5 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class UserTeamPK implements Serializable {
   
	private static final long serialVersionUID = 1L;
	private String userId;
    private String teamId;

    @Column(name = "user_id")
    @Id
    public String getUserId() {
        return userId;
    }

	public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "team_id")
    @Id
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTeamPK userTeam = (UserTeamPK) o;

        if (userId != null ? !userId.equals(userTeam.userId) : userTeam.userId != null) return false;
        if (teamId != null ? !teamId.equals(userTeam.teamId) : userTeam.teamId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (teamId != null ? teamId.hashCode() : 0);
        return result;
    }
}
