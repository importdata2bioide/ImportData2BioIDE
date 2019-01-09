package org.big.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 *<p><b>自定义用户登陆验证实体类</b></p>
 *<p> 构造自定义的用户验证实体</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private static final long serialVersionUID = 6975601077710753878L;
    private final String token;

    /**
     *<b>构造函数</b>
     *<p> 加入验证码部分</p>
     * @author WangTianshan (王天山)
     * @param request
     * @return
     */
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        token = request.getParameter("token");
    }

    /**
     *<b>获取验证码</b>
     *<p> 获取验证码</p>
     * @author WangTianshan (王天山)
     * @param
     * @return java.lang.String
     */
	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; Token: ").append(this.getToken());
		return sb.toString();
	}
}