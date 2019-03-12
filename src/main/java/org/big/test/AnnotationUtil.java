package org.big.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {

	public static List<Annotation> validAnnotation(List<Class<?>> clsList, Class annotationClass) {
		List<Annotation> annotationList = new ArrayList<>();
		if (clsList != null && clsList.size() > 0) {
			for (Class<?> cls : clsList) {
				// 获取类中的所有的方法
				Annotation annotation = cls.getAnnotation(annotationClass);
				if(annotation != null)
					annotationList.add(annotation);
//				Method[] methods = cls.getDeclaredMethods();
//				if (methods != null && methods.length > 0) {
//					for (Method method : methods) {
//						Annotation annotation = method.getAnnotation(annotationClass);
//						if(annotation != null)
//							annotationList.add(annotation);
//					}
//				}
			}
		}
		return annotationList;
	}

}