package org.big.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.big.common.BuildEntity;
import org.big.common.IdentityVote;
import org.big.common.QueryTool;
import org.big.common.UUIDUtils;
import org.big.entity.Message;
import org.big.entity.User;
import org.big.entity.UserDetail;
import org.big.repository.MessageRepository;
import org.big.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *<p><b>Message的Service类</b></p>
 *<p> Message的Service类，与Message有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}") // -- 换成当前登录用户
    private String fromEmail;

    @Override
    @Transactional
    public JSON findbyInfo(HttpServletRequest request) {
        JSON json= null;
        String searchText=request.getParameter("search");
        if(searchText==null || searchText.length()<=0){
            searchText="";
        }
        int limit_serch=Integer.parseInt(request.getParameter("limit"));
        int offset_serch=Integer.parseInt(request.getParameter("offset"));
        String sort = "sendtime";
		String order = "desc";
		/*sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "sendtime";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}*/
        JSONObject thisTable= new JSONObject();
        JSONArray rows = new JSONArray();
        List<Object> thisList=new ArrayList<>();
        Page<Object> thisPage=this.messageRepository.searchInfo(searchText,QueryTool.buildPageRequest(offset_serch,limit_serch,sort,order));
        thisTable.put("total",thisPage.getTotalElements());
        thisList=thisPage.getContent();
        for(int i=0;i<thisList.size();i++){
            JSONObject row= new JSONObject();
            Message thisMessage= BuildEntity.buildMessage(thisList.get(i));
            String title="<a target='_blank' href='super/message/read/"+thisMessage.getId()+"'>"+thisMessage.getTitle()+"</a>";
            row.put("title",title);
            row.put("sender",thisMessage.getSender());
            row.put("addressee",thisMessage.getAddressee());
            String type=thisMessage.getType();
            switch(type){
                case "information":
                    type="通知";
                    break;
                case "invitation":
                    type="邀请信";
                    break;
                default:
                    break;
            }
            row.put("type",type);
            String status="";
            int statusCode=thisMessage.getStatus();
            switch(statusCode){
                case 0:
                    status="未读";
                    break;
                case 1:
                    status="已读";
                    break;
                case 2:
                    status="同意";
                    break;
                case 3:
                    status="拒绝";
                    break;
                default:
                    break;
            }
            row.put("status",status);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sendTime="";
            try {
                sendTime=formatter.format(thisMessage.getSendtime());
            } catch (Exception e) {
                //e.printStackTrace();
            }
            row.put("sendtime",sendTime);
            rows.add(i,row);
        }
        thisTable.put("rows",rows);
        json=thisTable;
        return json;
    }

	@Override
	@Transactional
	public JSON findInfoByAddressee(HttpServletRequest request) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "sendtime";
		String order = "desc";
		/*sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "sendtime";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}*/
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Object> thisList = new ArrayList<>();
		Page<Object> thisPage = this.messageRepository.searchInfoByAddressee(searchText, thisUser.getEmail(), QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			Message thisMessage = BuildEntity.buildMessage(thisList.get(i));
			String title = "<a target='_blank' href='console/message/read/" + thisMessage.getId() + "'>"
					+ thisMessage.getTitle() + "</a>";
			row.put("title", title);
			row.put("sender", thisMessage.getSender());
			row.put("addressee", thisMessage.getAddressee());
			String type = thisMessage.getType();
			switch (type) {
			case "information":
				type = "通知";
				break;
			case "invitation":
				type = "邀请信";
				break;
			default:
				break;
			}
			row.put("type", type);
			String status = "";
			int statusCode = thisMessage.getStatus();
			switch (statusCode) {
			case 0:
				status = "未读";
				break;
			case 1:
				status = "已读";
				break;
			case 2:
				status = "同意";
				break;
			case 3:
				status = "拒绝";
				break;
			default:
				break;
			}
			row.put("status", status);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendTime = "";
			try {
				sendTime = formatter.format(thisMessage.getSendtime());
			} catch (Exception e) {
				// e.printStackTrace();
			}
			row.put("sendtime", sendTime);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
	}

    @Override
    @Transactional
    public JSON findInfoBySender(HttpServletRequest request) {
        UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		JSON json = null;
		String searchText = request.getParameter("search");
		if (searchText == null || searchText.length() <= 0) {
			searchText = "";
		}
		int limit_serch = Integer.parseInt(request.getParameter("limit"));
		int offset_serch = Integer.parseInt(request.getParameter("offset"));
		String sort = "sendtime";
		String order = "desc";
		/*sort = request.getParameter("sort");
		order = request.getParameter("order");
		if (StringUtils.isBlank(sort)) {
			sort = "sendtime";
		}
		if (StringUtils.isBlank(order)) {
			order = "desc";
		}*/
		
		JSONObject thisTable = new JSONObject();
		JSONArray rows = new JSONArray();
		List<Object> thisList = new ArrayList<>();
		Page<Object> thisPage = this.messageRepository.searchInfoBySender(searchText, thisUser.getId(),
				QueryTool.buildPageRequest(offset_serch, limit_serch, sort, order));
		thisTable.put("total", thisPage.getTotalElements());
		thisList = thisPage.getContent();
		for (int i = 0; i < thisList.size(); i++) {
			JSONObject row = new JSONObject();
			Message thisMessage = BuildEntity.buildMessage(thisList.get(i));
			String title = "<a target='_blank' href='console/message/read/" + thisMessage.getId() + "'>"
					+ thisMessage.getTitle() + "</a>";
			row.put("title", title);
			row.put("sender", thisMessage.getSender());
			row.put("addressee", thisMessage.getAddressee());
			String type = thisMessage.getType();
			switch (type) {
			case "information":
				type = "通知";
				break;
			case "invitation":
				type = "邀请信";
				break;
			default:
				break;
			}
			row.put("type", type);
			String status = "";
			int statusCode = thisMessage.getStatus();
			switch (statusCode) {
			case 0:
				status = "未读";
				break;
			case 1:
				status = "已读";
				break;
			case 2:
				status = "同意";
				break;
			case 3:
				status = "拒绝";
				break;
			default:
				break;
			}
			row.put("status", status);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendTime = "";
			try {
				sendTime = formatter.format(thisMessage.getSendtime());
			} catch (Exception e) {
				// e.printStackTrace();
			}
			row.put("sendtime", sendTime);
			rows.add(i, row);
		}
		thisTable.put("rows", rows);
		json = thisTable;
		return json;
	}

    @Override
    public Message findbyID(String ID) {
        return this.messageRepository.getOne(ID);
    }
    
    @Override
    public void removeOne(String ID) {
        this.messageRepository.deleteById(ID);
    }

    @Override
    public void changeStatus(Message thisMessage, int newStatus) {
        //身份判断
        IdentityVote identityVote = new IdentityVote();
        if(identityVote.isAddresseeByMessage(thisMessage)){
            this.messageRepository.changeStatus(thisMessage.getId(),newStatus);
        }
    }

	@Override
	public int countStatus(int statusNum) {
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return this.messageRepository.countStatus(thisUser.getEmail(), statusNum);
	}
    
	@Override
	public JSON sendInviteEmail(Message thisMessage, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		thisMessage.setId(UUIDUtils.getUUID32());
	    thisMessage.setStatus(0); 
	    thisMessage.setMark("1");
	    thisMessage.setSendtime(new Timestamp(System.currentTimeMillis()));
	    
		String email = request.getParameter("email");
		thisMessage.setAddressee(email);					// 收信人
		
		User thisUser = userRepository.findOneByEmail(email);	// 根据邮箱判断用户是否存在(只能向已有用户发出邀请)
		if (thisUser != null) {
			try {
				String contextPath = request.getContextPath();	// base_url
				String base_url = request.getServerName().toString() + ":" + request.getServerPort();
				if (StringUtils.isNotBlank(contextPath)) {
					base_url = base_url + contextPath;
				}
				// 邮件的内容(通知被邀请人)
				StringBuffer sb = new StringBuffer("<h4>D-Taxon 物种数据采集系统<h4><p><p/><p><p/>");
				
				sb.append("<b>" + thisMessage.getTitle() + "，</b>");
				sb.append("请登录采集系统处理！<br/>");
				
				StringBuffer str = new StringBuffer("<h4>D-Taxon 物种数据采集系统<h4><p><p/><p><p/>");
				str.append("<b>" + thisMessage.getTitle() + ",</b>");
				str.append("同意加入团队，请点击下方链接！<br/>");
				str.append("<a href=\"http://" + base_url + "/console/team/invite/");
				str.append(thisUser.getUserName());
				str.append("/");
				str.append(thisMessage.getTeamid());
				str.append("/");

				str.append("\">http://" + base_url + "/console/message/invite/");
				str.append(thisUser.getUserName());
				str.append("/");
				str.append(thisMessage.getTeamid());
				str.append("/");
				str.append("</a>");
				thisMessage.setText(str.toString());
				this.messageRepository.save(thisMessage);				// 保存Message对象
				
				// 邮件信息
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(fromEmail);
				helper.setTo(thisMessage.getAddressee());
				helper.setSubject("D-Taxon 物种采集系统团队邀请函");
				helper.setText(sb.toString(), true);
				// 发送邮件
				mailSender.send(mimeMessage);
				thisResult.put("sendMsg", "发送成功");
			} catch (Exception e) {
				thisResult.put("sendMsg", "邮件发送失败");
			}
		}else{
			try {
				String contextPath = request.getContextPath();	// base_url
				String base_url = request.getServerName().toString() + ":" + request.getServerPort();
				if (StringUtils.isNotBlank(contextPath)) {
					base_url = base_url + contextPath;
				}
				// 邮件的内容(通知被邀请人)
				/*StringBuffer sb = new StringBuffer("<h4>物种数据采集系统<h4><p><p/><p><p/>");
				
				sb.append("<b>" + thisMessage.getTitle() + "，</b>");
				sb.append("但您尚未注册D-Taxon 物种采集系统！点击<a></a>");
				
				sb.append("加入邀请团队！<br/>");*/
				
				// 邀请邮件的内容
				StringBuffer str = new StringBuffer("<h4>D-Taxon 物种数据采集系统<h4><p><p/><p><p/>");
				str.append("<b>" + thisMessage.getTitle() + "，</b>");
				str.append("但您尚未注册D-Taxon 物种采集系统！点击<a></a>");
				str.append("<a href=\"http://" + base_url + "/register/");
				str.append(thisMessage.getTeamid());
				str.append("/");
				str.append("\">" + "注册");
				str.append("</a>");
				str.append("加入邀请团队！<br/>");
				thisMessage.setText(str.toString());
				this.messageRepository.save(thisMessage);				// 保存Message对象
				
				// 邮件信息
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(fromEmail);
				helper.setTo(thisMessage.getAddressee());
				helper.setSubject("D-Taxon 物种采集系统团队邀请函");
				helper.setText(str.toString(), true);
				// 发送邮件
				mailSender.send(mimeMessage);
				thisResult.put("sendMsg", "发送成功");
			} catch (Exception e) {
				thisResult.put("sendMsg", "邮件发送失败");
			}
		}
		return thisResult;
	}

	@Override
	public JSON sendEmail(Message thisMessage, HttpServletRequest request) {
		JSONObject thisResult = new JSONObject();
		thisMessage.setId(UUIDUtils.getUUID32());
	    thisMessage.setStatus(0); 
	    thisMessage.setMark("1");
	    thisMessage.setSendtime(new Timestamp(System.currentTimeMillis()));
	    
		String email = request.getParameter("email");
		if (StringUtils.isNotBlank(email)) {
			thisMessage.setAddressee(email);					// 收信人
		}
		
		User thisUser = userRepository.findOneByEmail(email);	// 根据邮箱判断用户是否存在(只能向已有用户发出邀请)
		if (thisUser != null) {
			try {
				// base_url
				String contextPath = request.getContextPath();
				String base_url = request.getServerName().toString() + ":" + request.getServerPort();
				if (contextPath != null && contextPath.length() > 0) {
					base_url = base_url + contextPath;
				}
				/// 邮件的内容
				StringBuffer sb = new StringBuffer("<h4>D-Taxon 物种数据采集系统<h4><p><p/><p><p/>");
				sb.append("<b>" + thisMessage.getTitle() + ",</b>");

				// 邮件信息
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(fromEmail); // 邮件发送人
				helper.setTo(thisMessage.getAddressee()); // 邮件接收人
				helper.setSubject("D-Taxon 物种采集系统团队邀请函"); // 邮件主题
				helper.setText(sb.toString() + thisMessage.getText(), true);

				// 发送邮件
				mailSender.send(mimeMessage);
				
				thisMessage.setText(sb.toString());
				this.messageRepository.save(thisMessage);
				thisResult.put("sendMsg", "发送成功");
			} catch (Exception e) {
				thisResult.put("sendMsg", "邮件发送失败");
			}
		} else {
			thisResult.put("sendMsg", "该邮箱没有注册");
		}
		return thisResult;
	}
}
