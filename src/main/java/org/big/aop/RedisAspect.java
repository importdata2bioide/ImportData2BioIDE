//package org.big.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.big.common.RedisTools;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class RedisAspect {
//	@Autowired
//	private RedisTools redisTools;
//	//   execution( * org.big.repository.GeoobjectRepository.findOneByCngeoname(..))
//	@Pointcut("execution( * org.big.repository.DescriptiontypeRepository.findOneByName(..)) or execution( * org.big.repository.GeoobjectRepository.find*(..))")
//	public void redisCacheMethod() {
//	}
//
//	@Around("redisCacheMethod()")
//	public Object around(ProceedingJoinPoint joinPoint) {
//		// before 前置：到redis中查询缓存
//		Object[] args = joinPoint.getArgs();// //先获取目标方法参数
//		String shortString = joinPoint.getSignature().toShortString();
//		
//		// 从redis中查询
//		Object objectFromRedis = redisTools.getDataFromRedis(args, shortString);
//		// 如果查询到了
//		if (null != objectFromRedis) {
//			return objectFromRedis;
//		}
//		// 没有查到，那么查询数据库
//		Object object = null;
//		try {
//			object = joinPoint.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		// after：将数据库中查询的数据放到redis中
//		redisTools.setDataToRedis(args, shortString, object);
//		return object;
//
//	}
//}
