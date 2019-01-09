package org.big.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 *<p><b>Message的Entity类</b></p>
 *<p> Message的Entity类</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/19 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
public class Message {
    private String id;
    private String sender;
    private String addressee;
    private Timestamp sendtime;
    private String title;
    private String text;
    private Integer status;
    private String type;
    private String mark;
    private String teamid;
    
    public Message(String id, String sender) {
		super();
		this.id = id;
		this.sender = sender;
	}

	public Message() {
		super();
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
    @Column(name = "sender")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Basic
    @Column(name = "addressee")
    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    @Basic
    @Column(name = "sendtime")
    public Timestamp getSendtime() {
        return sendtime;
    }

    public void setSendtime(Timestamp sendtime) {
        this.sendtime = sendtime;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "mark")
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
	public String getTeamid() {
		return teamid;
	}

	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;
        if (addressee != null ? !addressee.equals(message.addressee) : message.addressee != null) return false;
        if (sendtime != null ? !sendtime.equals(message.sendtime) : message.sendtime != null) return false;
        if (title != null ? !title.equals(message.title) : message.title != null) return false;
        if (text != null ? !text.equals(message.text) : message.text != null) return false;
        if (status != null ? !status.equals(message.status) : message.status != null) return false;
        if (type != null ? !type.equals(message.type) : message.type != null) return false;
        if (mark != null ? !mark.equals(message.mark) : message.mark != null) return false;
        if (teamid != null ? !teamid.equals(message.teamid) : message.teamid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (addressee != null ? addressee.hashCode() : 0);
        result = 31 * result + (sendtime != null ? sendtime.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        result = 31 * result + (teamid != null ? teamid.hashCode() : 0);
        return result;
    }

	@Override
	public String toString() {
		return "Message [id=" + id + ", sender=" + sender + ", addressee=" + addressee + ", sendtime=" + sendtime
				+ ", title=" + title + ", text=" + text + ", status=" + status + ", type=" + type + ", mark=" + mark
				+ ", teamid=" + teamid + "]";
	}
    
}
