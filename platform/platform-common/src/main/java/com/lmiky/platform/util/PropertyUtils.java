package com.lmiky.platform.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 属性工具
 * @author lmiky
 * @date 2013-4-17
 */
public class PropertyUtils {
	
	/**
	 * 获取类的所有属性和属性类别
	 * @author lmiky
	 * @date 2013-4-17
	 * @param clazz
	 * @return
	 */
	public static Map<String, Class<?>> getPropertiesClassType(Class<?> clazz) {
		PropertyDescriptor[] descriptors = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(clazz);
		Map<String, Class<?>> classTypes = new HashMap<String, Class<?>>();
		for(int i=0; i<descriptors.length; i++) {
			if(!"class".equals(descriptors[i].getName())) {	//去掉Object的getClass
				classTypes.put(descriptors[i].getName(), descriptors[i].getPropertyType());
			}
		}
		return classTypes;
	}
	
	/**
	 * 获取类指定字段的类别
	 * @author lmiky
	 * @date 2013-4-17
	 * @param clazz
	 * @param property 属性
	 * @return
	 */
	public static Class<?> getPropertyClassType(Class<?> clazz, String property) {
		if(StringUtils.isBlank(property)) {
			return null;
		}
		PropertyDescriptor[] descriptors = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(clazz);
		for(int i=0; i<descriptors.length; i++) {
			if(descriptors[i].getName().equals(property)) {
				return descriptors[i].getPropertyType();
			}
		}
		return null;
	}

	/**
	 * 获取类所有的字段，包含父类的字段
	 * @author lmiky
	 * @date 2014年9月16日 下午2:51:38
	 * @param clazz
	 * @return
	 */
	public static List<String> getProperties(Class<?> clazz) {
		List<String> properties = new ArrayList<String>();
		PropertyDescriptor[] descriptors = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(clazz);
		for(PropertyDescriptor descriptor : descriptors) {
			if(!"class".equals(descriptor.getName())) {	//去掉Object的getClass
				properties.add(descriptor.getName());
			}
		}
		return properties;
	}
	
	/**
	 * 获取类所有的字段，不包含父类
	 * @author lmiky
	 * @date 2014年9月16日 下午2:56:46
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static List<String> getDeclaredProperties(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		List<String> properties = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		Object bean = clazz.newInstance();
		for(Field field : fields) {
			if(org.apache.commons.beanutils.PropertyUtils.isReadable(bean, field.getName())) {	//排除定义的一些属性，比如serialVersionUID和public static final VAR
				properties.add(field.getName());
			}
		}
		return properties;
	}
	
	/**
	 * 将对象的属性拷贝到map中
	 * @author lmiky
	 * @date 2015年1月7日 下午2:45:30
	 * @param bean
	 * @param map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void copyBeanProperty2Map(Object bean, Map<String, Object> map) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<String> properties = PropertyUtils.getProperties(bean.getClass());
		for(String property : properties) {
			map.put(property, BeanUtils.getProperty(bean, property));
		}
	}
}
