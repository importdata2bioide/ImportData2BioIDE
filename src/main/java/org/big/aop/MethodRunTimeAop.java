package org.big.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Aspectj 切面
 * @author BIGIOZ
 *
 */
@Aspect
@Component
public class MethodRunTimeAop {
	
	private final static Logger logger = LoggerFactory.getLogger(MethodRunTimeAop.class);
	/**
	 * Aspectj 配置切面 @Around 环绕增强(参考SpringAop5种增强方式)
	 * @param pjp
	 * @return
	 */
	@Around("execution(* org.big.controller..*(..))") 
    public Object interceptor(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
        	long maxMemory = Runtime.getRuntime().maxMemory();
        	logger.error("堆内存大小：" + maxMemory / 1024 / 1024 + "MB");
            // 通过代理类调用业务逻辑执行，调用proceed()方法，就会触发切入点方法执行
            result = pjp.proceed();
        } catch (Throwable e) {
        	logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        // 记录程序运行时间
        logger.info("------方法名："+pjp.getSignature().getName() +
                "  运行时间: " + (System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
        return result;
    }

}
