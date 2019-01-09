package org.big.common;

import java.sql.Timestamp;

import org.big.entity.Message;
import org.big.entity.Team;

/**
 *<p><b>构造实体类</b></p>
 *<p> 将传入的java对象对应至已有的Entity实体</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/12/04 21:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class BuildEntity {
    /**
     *<b>构造TeamEntity</b>
     *<p> 将传入的java对象对应至已有的TeamEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisIdrecord（Idrecord）
     */
    public static String buildUserInfoString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[0]+"<"+(String)objs[1]+">";
    }
    public static String buildEmailString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[1];
    }
    public static String buildString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[1];
    }
    public static String buildProtlevel(Object obj){
    	Object[] objs = (Object[]) obj;
    	return (String)objs[0];
    }
    
    /**
     *<b>构造TeamEntity</b>
     *<p> 将传入的java对象对应至已有的TeamEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisTeam（Team）
     */
    public static Team buildTeam(Object obj){
        Team thisTeam=new Team();
        Object[] objs = (Object[]) obj;
        thisTeam.setId((String)objs[0]);
        thisTeam.setName((String)objs[1]);
        thisTeam.setLeader((String)objs[3]+"("+(String)objs[2]+")");
        thisTeam.setNote((String)objs[4]);
        thisTeam.setAdddate((Timestamp) objs[5]);
        return thisTeam;
    }
    /**
     *<b>构造MessageEntity</b>
     *<p> 将传入的java对象对应至已有的MessageEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisMessage（Message）
     */
    public static Message buildMessage(Object obj){
        Message thisMessage=new Message();
        Object[] objs = (Object[]) obj;
        thisMessage.setId((String)objs[0]);
        thisMessage.setSender((String)objs[1]);
        thisMessage.setAddressee((String)objs[2]);
        thisMessage.setSendtime((Timestamp)objs[3]);
        thisMessage.setTitle((String) objs[4]);
        thisMessage.setText((String) objs[5]);
        thisMessage.setStatus((int) objs[6]);
        thisMessage.setType((String) objs[7]);
        thisMessage.setMark((String) objs[8]);
        return thisMessage;
    }
}
