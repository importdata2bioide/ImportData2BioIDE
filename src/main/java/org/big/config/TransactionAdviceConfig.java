//package org.big.config;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.aop.Advisor;
//import org.springframework.aop.aspectj.AspectJExpressionPointcut;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
//import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
//import org.springframework.transaction.interceptor.TransactionInterceptor;
//
///**
// * 声明式事务配置
// * 
// * @author BIGIOZ
// *
// */
//@Aspect
//@Configuration
//public class TransactionAdviceConfig {
//	private static final String AOP_POINTCUT_EXPRESSION = "execution (* org.***.service.*.*(..))";
//
//	@Autowired
//	private PlatformTransactionManager transactionManager;
//
//	// 创建事务通知
//	@Bean
//	public TransactionInterceptor txAdvice() {
//
//		DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
//		txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//		DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
//		txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		txAttr_REQUIRED_READONLY.setReadOnly(true);
//
//		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//		source.addTransactionalMethod("insertList*", txAttr_REQUIRED);
//		source.addTransactionalMethod("addList*", txAttr_REQUIRED);
//		source.addTransactionalMethod("saveList*", txAttr_REQUIRED);
//		source.addTransactionalMethod("delete*", txAttr_REQUIRED);
//		source.addTransactionalMethod("updateList*", txAttr_REQUIRED);
////		source.addTransactionalMethod("exec*", txAttr_REQUIRED);
////		source.addTransactionalMethod("set*", txAttr_REQUIRED);
////		source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("select*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
//		source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
////		source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
////		source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
////		source.addTransactionalMethod("is*", txAttr_REQUIRED_READONLY);
//		return new TransactionInterceptor(transactionManager, source);
//	}
//
//	/**
//	 * Advisor通知器:将pointcut和Advice结合起来 
//	 * title: TransactionAdviceConfig.java
//	 * 
//	 * @return
//	 * @author ZXY
//	 */
//	@Bean
//	public Advisor txAdviceAdvisor() {
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
//		return new DefaultPointcutAdvisor(pointcut, txAdvice());
//	}
//}