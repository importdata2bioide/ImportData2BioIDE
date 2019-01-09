package org.big.test;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ClassLoader myLoader = new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				String fileName = name.substring(name.lastIndexOf(".")+1)+".class";
				InputStream is = getClass().getResourceAsStream(fileName);
				if(is== null) {
					return super.loadClass(name);
				}
				try {
					byte[] b = new byte[is.available()];
					is.read(b);
					return defineClass(name, b, 0, b.length);
					
				} catch (IOException e) {
					throw new ClassNotFoundException();
				}
				
			}
		};
		Object object = myLoader.loadClass("org.big.test.ClassLoaderTest").newInstance();
		System.out.println(object.getClass());
		System.out.println(object instanceof org.big.test.ClassLoaderTest);// false 
		
		
	}

}
