package org.big.common;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//ElementType.METHOD参数来标明@MethodRunTime只能用于方法
@Retention(RetentionPolicy.RUNTIME)//表示该注解生存期是运行时
public @interface MethodRunTime {

}
