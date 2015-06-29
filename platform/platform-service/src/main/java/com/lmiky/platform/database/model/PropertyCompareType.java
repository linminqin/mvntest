package com.lmiky.platform.database.model;

/**
 * 属性比较类型
 * @author lmiky
 * @date 2013-4-17
 */
public enum PropertyCompareType {
	/**
	 * 等于：=
	 */
	EQ("eq"),
	/**
	 * 不等于：!=
	 */
	NE("ne"),
	/**
	 * 大于：>
	 */
	GT("gt"),
	/**
	 * 大于等于：>=
	 */
	GE("ge"),
	/**
	 * 小于：<
	 */
	LT("lt"),
	/**
	 * 小于等于：<=
	 */
	LE("le"),
	/**
	 * 模糊查询：%?%
	 */
	LIKE("like"),
	/**
	 * 左模糊查询：%?
	 */
	LLIKE("llike"),
	/**
	 * 右模糊查询：?%
	 */
	RLIKE("rlike"),
	/**
	 * 不匹配模糊查询：not like %?%
	 */
	NLIKE("nlike"),
	/**
	 * 不匹配左模糊查询：not like %?
	 */
	NLLIKE("nllike"), 
	/**
	 * 不匹配右模糊查询：not like ?%
	 */
	NRLIKE("nrlike"),
	/**
	 * 不为空：is not null
	 */
	NNULL("nnull"),
	/**
	 * 不为空：is null
	 */
	NULL("null");
	
	private String value;
	
	/**
	 * @param value
	 */
	private PropertyCompareType (String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}