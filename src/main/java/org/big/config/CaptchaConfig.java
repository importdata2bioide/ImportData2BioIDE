package org.big.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 *<p><b>创建验证码图片类</b></p>
 *<p> 创建验证码图片</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/12 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Configuration
public class CaptchaConfig {

    /**
     *<b>创建验证码图片</b>
     *<p> 创建验证码图片</p>
     * @author WangTianshan (王天山)
     * @param
     * @return com.google.code.kaptcha.impl.DefaultKaptcha
     */
	@Bean(name = "captchaProducer")
	public DefaultKaptcha getKaptchaBean() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "yes");
		properties.setProperty("kaptcha.border.color", "210,214,222");
		properties.setProperty("kaptcha.textproducer.font.color", "199,21,133");
		properties.setProperty("kaptcha.image.width", "125");
		properties.setProperty("kaptcha.image.height", "48");
		properties.setProperty("kaptcha.session.key", "code");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
    }
}