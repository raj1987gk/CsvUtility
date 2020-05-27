package com.support.csv.utility;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class PojoGenerator {

	public static Class generate(String className, Map<String, Class<?>>  properties) throws NotFoundException,
			CannotCompileException {

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(className);

		// add this to define a super class to extend
		// cc.setSuperclass(resolveCtClass(MySuperClass.class));

		// add this to define an interface to implement
		cc.addInterface(resolveCtClass(Serializable.class));

		for (Entry<String, Class<?>> entry : properties.entrySet()) {

			cc.addField(new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc));

			// add getter
			cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue()));

			// add setter
			cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
		}

		return cc.toClass();
	}

	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ")
				.append(getterName).append("(){").append("return this.")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(")
				.append(fieldClass.getName()).append(" ").append(fieldName)
				.append(")").append("{").append("this.").append(fieldName)
				.append("=").append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}
	
	
	public static void main(String[] args) throws Exception {

		Map<String, Class<?>> props = new HashMap<>();
		props.put("foo", Integer.class);
		props.put("bar", String.class);

		Class<?> clazz = PojoGenerator.generate(
				"net.javaforge.blog.javassist.Pojo$Generated", props);

		Object obj = clazz.newInstance();

		System.out.println("Clazz: " + clazz);
		System.out.println("Object: " + obj);
		System.out.println("Serializable? " + (obj instanceof Serializable));

		for (final Method method : clazz.getDeclaredMethods()) {
			System.out.println(method);
		}

		// set property "bar"
		clazz.getMethod("setBar", String.class).invoke(obj, "Hello World!");

		// get property "bar"
		String result = (String) clazz.getMethod("getBar").invoke(obj);
		System.out.println("Value for bar: " + result);

	}
	
	
}