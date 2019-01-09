package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *<p><b>User的Entity类</b></p>
 *<p> User的Entity类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/5 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "user", schema = "biodata")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	@Basic
	@Column(name = "user_name")
    private String userName;
    private String password;
    private String email;
    private String nickname;
    private String phone;
    private String role;
	@Temporal(TemporalType.TIMESTAMP)
    private Date adddate;
    // 补充字段
    private String avatar;	
    private Timestamp dtime;
    private Integer idnum;
    private Integer level;	
    private String mark;	
    private String mobile;	
    private Integer score;	
    private Byte status; 		// 用户是否激活
    private Integer uploadnum;
	@Temporal(TemporalType.TIMESTAMP)
    private Date resettime;
    
    private String resetmark;
    
    private String profilePicture;
    
	private List<Dataset> datasets;
	
	@OneToMany(mappedBy="creator")
	public List<Dataset> getDatasets() {
		return datasets;
	}
	
	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	/** 保留默认无参构造 */
    public User() {
    }

    public User(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.status = user.getStatus();
        this.adddate = user.getAdddate();
        this.mobile = user.getMobile();
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "adddate")
    public Date getAdddate() {
        return adddate;
    }

    public void setAdddate(Timestamp adddate) {
        this.adddate = adddate;
    }

    @Basic
    @Column(name = "avatar")
    public String getAvatar() {
		return avatar;
	}
    
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Basic
    @Column(name = "dtime")
	public Timestamp getDtime() {
		return dtime;
	}

	public void setDtime(Timestamp dtime) {
		this.dtime = dtime;
	}
	
	@Basic
    @Column(name = "idnum")
	public Integer getIdnum() {
		return idnum;
	}

	public void setIdnum(Integer idnum) {
		this.idnum = idnum;
	}
	
	@Basic
    @Column(name = "level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Basic
    @Column(name = "mark")
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	@Basic
    @Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Basic
    @Column(name = "score")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Basic
    @Column(name = "status")
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Basic
    @Column(name = "uploadnum")
	public Integer getUploadnum() {
		return uploadnum;
	}

	public void setUploadnum(Integer uploadnum) {
		this.uploadnum = uploadnum;
	}
	
	@Basic
    @Column(name = "resetmark")
	public String getResetmark() {
		return resetmark;
	}

	public void setResetmark(String resetmark) {
		this.resetmark = resetmark;
	}
	
	@Basic
    @Column(name = "resettime")
	public Date getResettime() {
		return resettime;
	}

	public void setResettime(Date date) {
		this.resettime = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adddate == null) ? 0 : adddate.hashCode());
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((datasets == null) ? 0 : datasets.hashCode());
		result = prime * result + ((dtime == null) ? 0 : dtime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idnum == null) ? 0 : idnum.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((mark == null) ? 0 : mark.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((profilePicture == null) ? 0 : profilePicture.hashCode());
		result = prime * result + ((resetmark == null) ? 0 : resetmark.hashCode());
		result = prime * result + ((resettime == null) ? 0 : resettime.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((uploadnum == null) ? 0 : uploadnum.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (adddate == null) {
			if (other.adddate != null)
				return false;
		} else if (!adddate.equals(other.adddate))
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (datasets == null) {
			if (other.datasets != null)
				return false;
		} else if (!datasets.equals(other.datasets))
			return false;
		if (dtime == null) {
			if (other.dtime != null)
				return false;
		} else if (!dtime.equals(other.dtime))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idnum == null) {
			if (other.idnum != null)
				return false;
		} else if (!idnum.equals(other.idnum))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (mark == null) {
			if (other.mark != null)
				return false;
		} else if (!mark.equals(other.mark))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (profilePicture == null) {
			if (other.profilePicture != null)
				return false;
		} else if (!profilePicture.equals(other.profilePicture))
			return false;
		if (resetmark == null) {
			if (other.resetmark != null)
				return false;
		} else if (!resetmark.equals(other.resetmark))
			return false;
		if (resettime == null) {
			if (other.resettime != null)
				return false;
		} else if (!resettime.equals(other.resettime))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (uploadnum == null) {
			if (other.uploadnum != null)
				return false;
		} else if (!uploadnum.equals(other.uploadnum))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", nickname=" + nickname + ", phone=" + phone + ", role=" + role + "]";
	}
	
}