package com.lmiky.platform.database.dao.mybatis;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * 说明
 * @author lmiky
 * @date 2015年5月15日 下午1:11:16
 */
public abstract class AbstractBaseDAOImpl {

	protected SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * @return the sqlSessionTemplate
	 */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	/**
	 * @param sqlSessionTemplate the sqlSessionTemplate to set
	 */
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
}
