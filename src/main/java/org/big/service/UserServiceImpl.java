package org.big.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.big.common.MD5Utils;
import org.big.common.UUIDUtils;
import org.big.entity.Dataset;
import org.big.entity.Team;
import org.big.entity.User;
import org.big.entity.UserTeam;
import org.big.repository.DatasetRepository;
import org.big.repository.TeamRepository;
import org.big.repository.UserRepository;
import org.big.repository.UserTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;

/**
 *<p><b>User的Service类</b></p>
 *<p> User的Service类，与User有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserTeamRepository userTeamRepository;
    @Value("${spring.mail.username}") // -- BINZIDYB@163.com
    private String fromEmail;
    @Autowired
    private DatasetRepository datasetRepository;
    
   
    @Override
    public User findbyID(String ID) {
        return this.userRepository.getOne(ID);
    }

    @Override
    public void addUserBySuoer(User thisUser) {
        this.userRepository.save(thisUser);
    	
        createDefaultTeamAndDatasetForNewUser(thisUser);
    }
    
    //为新用户创建默认Team和Dataset
	private void createDefaultTeamAndDatasetForNewUser(User thisUser) {
		// 为用户创建默认团队
    	Team newTeam = new Team();
    	newTeam.setId(UUIDUtils.getUUID32());
    	newTeam.setName(thisUser.getNickname());
    	newTeam.setLeader(thisUser.getId());
    	newTeam.setNote("Default");
    	newTeam.setMark("Default");
    	newTeam.setAdddate(new Timestamp(System.currentTimeMillis()));
    	
    	// 保存用户团队关系到中间表
    	UserTeam newUserTeam = new UserTeam();
    	newUserTeam.setTeamId(newTeam.getId());
    	newUserTeam.setUserId(thisUser.getId());
    	
    	//为用户创建默认数据集
    	Dataset newDataset = new Dataset();
    	newDataset.setId(UUIDUtils.getUUID32());
    	newDataset.setDsname(thisUser.getNickname());
    	newDataset.setDsabstract("Default");
        newDataset.setCreator(this.userRepository.findOneById(thisUser.getId()));
        newDataset.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        newDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
        newDataset.setSynchstatus(0);
        newDataset.setStatus(1);						//公开
        newDataset.setMark("Default");
        newDataset.setTeam(newTeam);
        
        this.teamRepository.save(newTeam);
        this.datasetRepository.save(newDataset);
        this.userTeamRepository.save(newUserTeam);
        //设置默认观测记录集
		this.changeStatus(thisUser, 1);
	}

    @Override
	public void editUserBySuoer(User thisUser) {
    	if(StringUtils.isNotBlank(thisUser.getId())){
            thisUser.setId(UUIDUtils.getUUID32());
            thisUser.setAdddate(new Timestamp(System.currentTimeMillis()));
        }
        this.userRepository.save(thisUser);
	}
    @Override
    public void removeOne(String ID) {
        this.userRepository.deleteById(ID);
    }

    @Override
    public User findOneByName(String user_name) {
        return this.userRepository.findOneByUserName(user_name);
    }
    
    @Override
    public User findOneByEmail(String email) {
    	return this.userRepository.findOneByEmail(email);
    }

	
    @Override
    public void changeStatus(User thisUser,int status) {
        thisUser.setStatus((byte)status);
        this.userRepository.save(thisUser);
    }
    
    // 用户注册 -- 同时发送邮件
	@Override
	public String registerNewOne(HttpServletRequest request, User newUser) {
		String registerMsg="none";
        //Token判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //username判断
            if(this.findOneByName(newUser.getUserName())==null) {
                //email判断
                if (this.findOneByEmail(newUser.getEmail()) == null) {
                    //设置用户信息
                    newUser.setStatus((byte)0);									// 注册用户状态默认为0 -- 未激活状态
                    newUser.setScore(0);
                    newUser.setLevel(0);
                    newUser.setUploadnum(0);
                    newUser.setIdnum(0);
                    newUser.setAvatar("");
                    newUser.setMark(UUIDUtils.getUUID32());						// 为当前注册用户生成用户激活码 -- 用于激活
                    newUser.setRole("ROLE_USER");								// 用户角色
                    newUser.setPassword(MD5Utils.MD532(newUser.getPassword()));	// 密码加密
                    newUser.setId(UUIDUtils.getUUID32());
                    newUser.setAdddate(new Timestamp(System.currentTimeMillis()));
                    
                    this.userRepository.save(newUser);
                    
                    // 被邀请人加入邀请团队
            		String tid = request.getParameter("tid");
            		if (StringUtils.isNotBlank(tid)) {
            			UserTeam thisUserTeam = new UserTeam();
            			thisUserTeam.setTeamId(tid);
            			thisUserTeam.setUserId(newUser.getId());
            			this.userTeamRepository.save(thisUserTeam);
            		}

                    //设置激活流程
                    try{
                        //base_url
                        String contextPath=request.getContextPath();
                        String base_url=request.getServerName().toString()+":"+request.getServerPort();
                        if(contextPath!=null && contextPath.length()>0){
                            base_url=base_url+contextPath;
                        }
                        ///邮件的内容
                        StringBuffer sb=new StringBuffer("D-Taxon 物种数据采集系统<br/>");
                        sb.append("点击下面链接激活账号，请尽快激活！<br/>");
                        sb.append("<a href=\"http://"+base_url+"/register/active/");
                        sb.append(newUser.getUserName());
                        sb.append("/");
                        sb.append(newUser.getMark());
                        sb.append("/");
                        sb.append("\">http://"+base_url+"/register/active/");
                        sb.append(newUser.getUserName());
                        sb.append("/");
                        sb.append(newUser.getMark());
                        sb.append("/");
                        sb.append("</a>");

                        //邮件信息
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(fromEmail);
                        helper.setTo(newUser.getEmail());
                        helper.setSubject("D-Taxon 物种数据采集系统");
                        helper.setText(sb.toString(), true);

                        //发送邮件
                        mailSender.send(mimeMessage);
                    }catch(Exception e){
                    }
                    registerMsg="success";
                }
                else{
                	registerMsg = "邮箱不可用，请更换";
                }
            }
            else{
            	registerMsg = "用户名不可用，请更换";
            }
        }
        else{
        	registerMsg = "验证码错误";
        }
        return registerMsg;
    }

	
	@Override
	public String sendActiveEmail(HttpServletRequest request, HttpServletResponse response) {
        String sendMsg="error";
        //验证码判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //邮箱判断
            User thisUser=this.findOneByEmail(request.getParameter("email"));
            if(thisUser!=null){
                //判断是否激活
                if(thisUser.getStatus()==0){
                    try{
                        //base_url
                        String contextPath=request.getContextPath();
                        String base_url=request.getServerName().toString()+":"+request.getServerPort();
                        if(contextPath!=null && contextPath.length()>0){
                            base_url=base_url+contextPath;
                        }
                        ///邮件的内容
                        StringBuffer sb=new StringBuffer("D-Taxon 物种数据采集系统<br/>");
                        sb.append("点击下面链接激活账号，请在10分钟内激活！<br/>");
                        sb.append("<a href=\"http://"+base_url+"/register/active/");
                        sb.append(thisUser.getUserName());
                        sb.append("/");
                        sb.append(thisUser.getMark());
                        sb.append("/");
                        sb.append("\">http://"+base_url+"/register/active/");
                        sb.append(thisUser.getUserName());
                        sb.append("/");
                        sb.append(thisUser.getMark());
                        sb.append("/");
                        sb.append("</a>");

                        //邮件信息
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.setFrom(fromEmail);
                        helper.setTo(thisUser.getEmail());
                        helper.setSubject("D-Taxon 物种数据采集系统");
                        helper.setText(sb.toString(), true);

                        //发送邮件
                        mailSender.send(mimeMessage);

                        sendMsg="发送成功";
                    }catch(Exception e){
                    	sendMsg="邮件发送失败";
                    }
                }
                else{
                	sendMsg="此用户已激活，请直接登录";
                }
            }
            else{
            	sendMsg="该邮箱没有注册";
            }
        }
        else{
        	sendMsg="验证码错误";
        }
        return sendMsg;
    }


	@Override
    public String activeUser(String userName, String mark, HttpServletRequest request) {
		String activeMsg = "error";
        // 用户点击激活链接 -- http://localhost:8081/register/active/BINZI/ccbf732b-1880-4cca-b2f5-8b3566a987d2/
		User thisUser = this.findOneByName(userName);
		if (thisUser != null) {	// 是否有此用户
			if (thisUser.getStatus() == 0) { // 是否能够正常激活
                if(thisUser.getMark().equals(mark)){ // 创建默认Team及该Team下的数据集
                	createDefaultTeamAndDatasetForNewUser(thisUser);
                    activeMsg="账户已激活";
                }
                else{
                    //激活失败
                    activeMsg="无效激活链接";
                }
            }
            else if(thisUser.getStatus()==1){
                //已经激活
                activeMsg="账户已激活";
            }
            else{
                //账户异常
                activeMsg="无效激活链接";
            }

        }
        else{
            //无效激活链接
            activeMsg="无效激活链接";
        }
        return activeMsg;
    }
	
	/**
	 * 逻辑：token正确 --> 邮箱判断 --> 设置找回流程
	 */
	@Override
    public String sendPasswordEmail(HttpServletRequest request, HttpServletResponse response) {
        String errorMsg="none";
        //Token判断
        if(request.getParameter("token").equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
            //email判断
            User thisUser=this.userRepository.findOneByEmail(request.getParameter("email"));
            if(thisUser!=null){
                //设置找回流程
                try{
                    //base_url
                    String contextPath=request.getContextPath();
                    String base_url=request.getServerName().toString()+":"+request.getServerPort();
                    if(contextPath!=null && contextPath.length()>0){
                        base_url=base_url+contextPath;
                    }
                    
                    thisUser.setResetmark(UUIDUtils.getUUID32());
                    //过期时间
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.MINUTE, 10);
                    thisUser.setResettime(c.getTime());
                    this.userRepository.save(thisUser);
                    ///邮件的内容
                    StringBuffer sb=new StringBuffer("D-Taxon 物种数据采集系统<br/>");
                    sb.append("点击下面链接重设密码，请在10分钟内完成！<br/>");
                    sb.append("<a href=\"http://"+base_url+"/password/reset/");
                    sb.append(thisUser.getUserName());
                    sb.append("/");
                    sb.append(thisUser.getResetmark());
                    sb.append("/");
                    sb.append("\">http://"+base_url+"/password/reset/");
                    sb.append(thisUser.getUserName());
                    sb.append("/");
                    sb.append(thisUser.getResetmark());
                    sb.append("/");
                    sb.append("</a>");

                    //邮件信息
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(fromEmail);
                    helper.setTo(thisUser.getEmail());
                    helper.setSubject("物种数据采集系统");
                    helper.setText(sb.toString(), true);

                    //发送邮件
                    mailSender.send(mimeMessage);
                }catch(Exception e){
                    errorMsg="error";
                }
            }
            else{
                errorMsg="email";
            }
        }
        else{
            errorMsg="token";
        }
        return errorMsg;
    }

	@Override
    public Boolean canRestPassword(String username,String mark) {
        try{
            User thisUser=this.userRepository.findOneByUserName(username);
            //mark验证
            if(thisUser.getResetmark().equals(mark)){
                //时间验证
                Date nowTime=new Date();
                Date pastTime=thisUser.getResettime();
                if(nowTime.compareTo(pastTime)==-1){
                    return true;
                }
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean restPassword(String username,String password) {
        try{
            User thisUser=this.userRepository.findOneByUserName(username);
            thisUser.setPassword(MD5Utils.MD532(password));
            thisUser.setResetmark("");
            this.userRepository.save(thisUser);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}