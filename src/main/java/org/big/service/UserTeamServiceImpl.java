package org.big.service;

import java.sql.Timestamp;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.big.common.UUIDUtils;
import org.big.entity.Message;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.entity.UserTeam;
import org.big.repository.MessageRepository;
import org.big.repository.TeamRepository;
import org.big.repository.UserRepository;
import org.big.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *<p><b>UserTeam的Service类</b></p>
 *<p> UserTeam的Service类，与UserTeam有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class UserTeamServiceImpl implements UserTeamService  {

    @Autowired
    private UserTeamRepository userTeamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}") // -- 换成当前登录用户
    private String fromEmail;

    @Override
    public void saveOne(String userId,String teamId) {
        UserTeam thisUserTeam=new UserTeam();
        thisUserTeam.setUserId(userId);
        thisUserTeam.setTeamId(teamId);
        this.userTeamRepository.save(thisUserTeam);
    }

    
	@SuppressWarnings("unused")
	@Override
	public String SendEmailTransPermissionAdvice(HttpServletRequest request) {
		String teamId=request.getParameter("teamId");
        String userId=request.getParameter("userId");
        Team thisTeam = this.teamRepository.findOneById(teamId);
        User thisUser = this.userRepository.findOneById(userId);
        String email = thisUser.getEmail();
        UserDetail user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Message thisMessage = new Message();
        thisMessage.setId(UUIDUtils.getUUID32());
        thisMessage.setAddressee(email);
        thisMessage.setMark("1");
        thisMessage.setStatus(1);
        thisMessage.setSender(user.getEmail());
        thisMessage.setSendtime(new Timestamp(System.currentTimeMillis()));
        thisMessage.setTeamid(teamId);
        
        String sendMsg="error";
            //根据邮箱判断用户是否存在(只能向已有用户发出邀请)
            /*User thisUser= userRepository.findOneByEmail(email);*/
            if(thisUser!=null){
	            try{
	                //base_url
	                String contextPath=request.getContextPath();
	                String base_url=request.getServerName().toString()+":"+request.getServerPort();
	                if(contextPath!=null && contextPath.length()>0){
	                    base_url=base_url+contextPath;
	                }
	                String title = "团队邮件通知";
	                ///邮件的内容
	                StringBuffer sb=new StringBuffer("物种数据采集系统<br/>");
	                sb.append(thisTeam.getName() + "团队邮件通知：<br/>");
	                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + thisUser.getUserName()+"，您已成为"+ thisTeam.getName() +"团队负责人！<br/>");
	
	                //邮件信息
	                MimeMessage mimeMessage = mailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	                helper.setFrom(fromEmail);
	                helper.setTo(email);
	                helper.setSubject("BioIDE");
	                helper.setText(sb.toString(), true);
	
	                //发送邮件
	                mailSender.send(mimeMessage);
	                
	                thisMessage.setText(sb.toString());
	                thisMessage.setTitle(title);
	                thisMessage.setType("information");
	                
	                this.messageRepository.save(thisMessage);
	                sendMsg="发送成功";
	            }catch(Exception e){
	            	sendMsg="邮件发送失败";
	            }
            }else{
            	sendMsg="该邮箱没有注册";
	        }
        return sendMsg;
	}
}
