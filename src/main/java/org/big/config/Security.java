package org.big.config;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * <p>
 * <b>SpringSecurity配置类</b>
 * </p>
 * <p>
 * 配置SpringSecurity
 * </p>
 * run 项目启动时加载
 */
@Configuration
@EnableWebSecurity // 使用注解配置SpringSecurity，自定义类实现WebSecurityConfigurerAdapter
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class Security extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	@Autowired
	private AuthenticationProvider authenticationProvider;

	/**
	 * 通过重写configure方法实现请求拦截
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().and().headers().frameOptions().sameOrigin().xssProtection().block(true).and();

		http.headers().cacheControl().and().contentTypeOptions().and().httpStrictTransportSecurity().and()
				.xssProtection();
		// SpringSecurity 使用以下匹配器来匹配请求路径 antMatchers ：使用ant风格的路径配置
		// 在匹配了请求路径后，需要针对当前用户的信息对请求路径进行安全处理
		http.authorizeRequests()// 通过authorizeRequests方法来请求权限配置
				.antMatchers("/guest/**").permitAll()// permitAll() 用户可任意访问
				.antMatchers("/druid/**").permitAll()// permitAll() 用户可任意访问
				.antMatchers("/img/**").permitAll()// permitAll() 用户可任意访问
				.antMatchers("/plugins/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/bowerComponents").permitAll()
				.antMatchers("/captchaImg").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/console/**").permitAll()// hasAnyAuthority(String...) 如果用户有参数，则其中任一权限可访问
				.antMatchers("/super/**").hasAnyAuthority("ROLE_SUPER")// 只有拥有ROLE_SUPER角色的用户可以访问
				// .anyRequest().authenticated()//其余所有的请求都需要认证后（登陆后）才可访问
				.and().formLogin()// 通过formLogin()方法定制登陆操作
				.loginPage("/JumpPageController_index").permitAll()// 使用loginPage定制登陆页面的访问地址
				.defaultSuccessUrl("/JumpPageController_index", true)// 指定登陆成功后的转向页面
				.authenticationDetailsSource(authenticationDetailsSource).and().logout().logoutUrl("/logout")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/JumpPageController_index")// 指定注销成功后的转向页面
				// .csrf().disable() //关闭CSRF
				.and().csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
					// 放行这几种请求
					private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
					// 放行guest请求
					private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("^/guest/.*", null);

					@Override
					public boolean matches(HttpServletRequest request) {
						if (allowedMethods.matcher(request.getMethod()).matches()) {
							return false;
						}
						String servletPath = request.getServletPath();
						if (servletPath.contains("/druid")) {
							return false;
						}
						return !unprotectedMatcher.matches(request);
					}
				});
	}

	/**
	 * 全局应用
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
}
