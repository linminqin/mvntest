package com.lmiky.platform.database.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.lmiky.platform.database.model.PropertyCompareType;

/**
 * 属性过滤
 * @author lmiky
 * @date 2013-4-15
 */
public class PropertyFilter implements Serializable, Cloneable {
	private static final long serialVersionUID = -3212572805481845951L;
	
	private String propertyName; // 属性名
	private Object propertyValue; // 属性值
	private PropertyCompareType compareType; // 比较方式
	private boolean isCollectionField;		//是否集合字段
	private Class<?> compareClass;// 被比较的对象类

	/**
	 * 
	 */
	public PropertyFilter() {
		isCollectionField = false;
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 */
	public PropertyFilter(String propertyName, Object propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.compareType = PropertyCompareType.EQ;
		isCollectionField = false;
		this.compareClass = null;
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param compareType
	 */
	public PropertyFilter(String propertyName, Object propertyValue, PropertyCompareType compareType) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.compareType = compareType;
		isCollectionField = false;
		this.compareClass = null;
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param compareClass
	 */
	public PropertyFilter(String propertyName, Object propertyValue, Class<?> compareClass) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.compareType = PropertyCompareType.EQ;
		isCollectionField = false;
		this.compareClass = compareClass;
	}

	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param compareType
	 * @param compareClass
	 */
	public PropertyFilter(String propertyName, Object propertyValue, PropertyCompareType compareType, Class<?> compareClass) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.compareType = compareType;
		isCollectionField = false;
		this.compareClass = compareClass;
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param compareType
	 * @param isCollectionField
	 * @param compareClass
	 */
	public PropertyFilter(String propertyName, Object propertyValue, PropertyCompareType compareType, boolean isCollectionField, Class<?> compareClass) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.compareType = compareType;
		this.isCollectionField = isCollectionField;
		this.compareClass = compareClass;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyValue
	 */
	public Object getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the compareType
	 */
	public PropertyCompareType getCompareType() {
		return compareType;
	}

	/**
	 * @param compareType the compareType to set
	 */
	public void setCompareType(PropertyCompareType compareType) {
		this.compareType = compareType;
	}
	
	/**
	 * @return the isCollectionField
	 */
	public boolean isCollectionField() {
		return isCollectionField;
	}

	/**
	 * @param isCollectionField the isCollectionField to set
	 */
	public void setCollectionField(boolean isCollectionField) {
		this.isCollectionField = isCollectionField;
	}

	/**
	 * @return the compareClass
	 */
	public Class<?> getCompareClass() {
		return compareClass;
	}

	/**
	 * @param compareClass the compareClass to set
	 */
	public void setCompareClass(Class<?> compareClass) {
		this.compareClass = compareClass;
	}
	
	/**
	 * 获取属性类的别名
	 * @author lmiky
	 * @date 2014年9月14日 上午11:03:33
	 * @return
	 */
	public String getClassAlias() {
		if(this.compareClass != null) {
			return this.compareClass.getSimpleName();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * 深度克隆
	 * @author lmiky
	 * @date 2014年8月10日 下午5:30:05
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object deepClone() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(this);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		return oi.readObject();
	}
}
