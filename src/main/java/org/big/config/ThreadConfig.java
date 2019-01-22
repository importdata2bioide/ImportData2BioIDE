package org.big.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * @EnableAsync 利用EnableAsync来开启Springboot对于异步任务的支持
 * @Description 配置类实现接口AsyncConfigurator，返回一个ThreadPoolTaskExecutor线程池对象。
 * @Description 异步方法和调用方法一定要写在“不同的类”中如果写在一个类中，是没有效果的
 * @Description 异步方法使用注解@Async ,返回值为void或者Future<T>
 * @author ZXY
 */
@Configuration
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		executor.setCorePoolSize(15);//核心线程数
		executor.setMaxPoolSize(21);//最大线程数
		executor.setQueueCapacity(5);//队列大小
		executor.setKeepAliveSeconds(15);//线程最大空闲时间
//		executor.setAwaitTerminationSeconds(60);//线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		SimpleAsyncUncaughtExceptionHandler handler = new SimpleAsyncUncaughtExceptionHandler();
		return handler;
	}
	
	

}
