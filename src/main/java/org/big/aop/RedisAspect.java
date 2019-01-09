package org.big.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.big.common.RedisTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisAspect {
	@Autowired
	private RedisTools redisTools;

	@Pointcut("execution( * org.big.repository..find*(..)) ")
	public void redisCacheMethod() {
	}

	@Around("redisCacheMethod()")
	public Object around(ProceedingJoinPoint joinPoint) {
		//before 前置：到redis中查询缓存
		System.out.println("调用从redis中查询的方法...");
		String applId = null;
		Object[] args = joinPoint.getArgs();// //先获取目标方法参数
		String longString = joinPoint.getSignature().toLongString();
        //从redis中查询
        Object objectFromRedis = redisTools.getDataFromRedis(args,longString);
        //如果查询到了
        if(null != objectFromRedis){
            System.out.println("从redis中查询到了数据...不需要查询数据库");
            return objectFromRedis;
        }
        System.out.println("没有从redis中查到数据...");
		// 没有查到，那么查询数据库
		Object object = null;
		try {
			object = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
        System.out.println("从数据库中查询的数据...");
        
        //after：将数据库中查询的数据放到redis中
        System.out.println("调用把数据库查询的数据存储到redis中的方法...");
        redisTools.setDataToRedis(args,longString, object);
        
		return object;

	}
}
