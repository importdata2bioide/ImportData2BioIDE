package org.big;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableCaching // 开启缓存支持
public class ImportData2BioIDE extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ImportData2BioIDE.class, args);
	}

	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ImportData2BioIDE.class);
	}

	
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}

}