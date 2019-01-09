package org.big.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration // 标识该类被纳入spring容器中实例化并管理
@ServletComponentScan // 用于扫描所有的Servlet、filter、listener
public class DruidConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.druid") // 加载时读取指定的配置信息,前缀为spring.datasource.druid
	public DataSource druidDataSource() {
		return new DruidDataSource();
	}

	/**
	 * http://127.0.0.1:端口/项目名/druid/index.html，使用下面配置的账户密码登陆 
	 * SpringSecurity的CSRF会导致登陆无反应，
	 * 这里有两个办法，一个是关闭CSRF，不过这种办法不友好，还有就是对druid开放 
	 * title: DruidConfig.java
	 * @return
	 * @author ZXY
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean statViewServlet() {
		// 创建servlet注册实体
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1"); // 设置ip白名单，为空则允许所有
		servletRegistrationBean.addInitParameter("deny", "192.168.0.19"); // 设置ip黑名单，如果allow与deny共同存在时,deny优先于allow
		servletRegistrationBean.addInitParameter("loginUsername", "admin");// 设置控制台管理用户
		servletRegistrationBean.addInitParameter("loginPassword", "admin"); // 设置控制台管理用户 密码
		servletRegistrationBean.addInitParameter("resetEnable", "false");// 是否可以重置数据
		return servletRegistrationBean;
	}
	/**
	 *  创建过滤器
	 * title: DruidConfig.java
	 * @return
	 * @author ZXY
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean statFilter() { 
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter()); // 设置过滤器过滤路径
		filterRegistrationBean.addUrlPatterns("/*"); // 忽略过滤的形式
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	@Bean
	public Slf4jLogFilter logFilter() {
		Slf4jLogFilter filter = new Slf4jLogFilter();
		filter.setResultSetLogEnabled(false);
		filter.setConnectionLogEnabled(false);
		filter.setStatementParameterClearLogEnable(false);
		filter.setStatementCreateAfterLogEnabled(false);
		filter.setStatementCloseAfterLogEnabled(false);
		filter.setStatementParameterSetLogEnabled(false);
		filter.setStatementPrepareAfterLogEnabled(false);
		return filter;
	}

}