package com.lmiky.platform.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.platform.database.dao.BaseDAO;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.model.Sort;
import com.lmiky.platform.database.pojo.BasePojo;
import com.lmiky.platform.service.BaseService;
import com.lmiky.platform.service.exception.ServiceException;

/**
 * 业务基类
 * @author lmiky
 * @date 2013-4-15
 */
@Service("baseService")
public class BaseServiceImpl implements BaseService {
	protected BaseDAO baseDAO;

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#isDBPojo(java.lang.Class)
	 */
	public <T extends BasePojo> boolean isDBPojo(Class<T> pojoClass) throws ServiceException {
		try {
			return getDAO().isDBPojo(pojoClass);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#find(java.lang.Class, java.io.Serializable)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> T find(Class<T> pojoClass, Long id) throws ServiceException {
		try {
			return getDAO().find(pojoClass, id);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#find(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> T find(Class<T> pojoClass, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().find(pojoClass, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#find(java.lang.Class, java.util.List)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> T find(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws ServiceException {
		try {
			return getDAO().find(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#find(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> T find(Class<T> pojoClass, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().find(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#find(java.lang.Class, java.util.Map)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> T find(Class<T> pojoClass, Map<String, Object> params) throws ServiceException {
		try {
			return getDAO().find(pojoClass, params);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#save(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void save(T pojo) throws ServiceException {
		try {
			if(pojo.getId() == null) {
				add(pojo);
			} else {
				update(pojo);
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#save(java.util.List)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void save(List<T> pojos) throws ServiceException {
		try {
			for(T t: pojos) {
				save(t);
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#add(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void add(T pojo) throws ServiceException {
		try {
			getDAO().add(pojo);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#add(java.util.List)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void add(List<T> pojos) throws ServiceException {
		try {
			for(T t: pojos) {
				add(t);
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void update(T pojo) throws ServiceException {
		try {
			getDAO().update(pojo);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.util.List)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void update(List<T> pojos) throws ServiceException {
		try {
			for(T t: pojos) {
				update(t);
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.lang.Class, java.lang.Long, java.lang.String, java.lang.Object)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().update(pojoClass, id, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.lang.Class, java.lang.Long, java.util.Map)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, Map<String, Object> params) throws ServiceException {
		try {
			return getDAO().update(pojoClass, id, params);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Map<String, Object> condition, Map<String, Object> updateValue) throws ServiceException {
		try {
			return getDAO().update(pojoClass, condition, updateValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.lang.Class, java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> boolean update(Class<T> pojoClass, String conditionFieldName, Object conditionFieldValue, String updateFieldName, Object updateFieldValue) throws ServiceException {
		try {
			return getDAO().update(pojoClass, conditionFieldName, conditionFieldValue, updateFieldName, updateFieldValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#update(java.lang.Class, java.util.List, java.util.Map)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> boolean update(Class<T> pojoClass, List<PropertyFilter> propertyFilters, Map<String, Object> updateValue) throws ServiceException {
		try {
			return getDAO().update(pojoClass, propertyFilters, updateValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void delete(T pojo) throws ServiceException {
		try {
			getDAO().delete(pojo);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.util.List)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void delete(List<T> pojos) throws ServiceException {
		try {
			for(T pojo: pojos) {
				delete(pojo);
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.lang.Class, java.io.Serializable)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long id) throws ServiceException {
		try {
			getDAO().delete(pojoClass, id);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.lang.Class, java.lang.Long[])
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long[] ids) throws ServiceException {
		try {
			getDAO().delete(pojoClass, ids);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> int delete(Class<T> pojoClass, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().delete(pojoClass, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.lang.Class, java.util.List)
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> int delete(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws ServiceException {
		try {
			return getDAO().delete(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#delete(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> int delete(Class<T> pojoClass, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().delete(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass) throws ServiceException {
		try {
			return getDAO().list(pojoClass);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().list(pojoClass, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, java.util.Map)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Map<String, Object> params) throws ServiceException {
		try {
			return getDAO().list(pojoClass, params);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, com.lmiky.platform.database.model.Sort)
	 */
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort sort) throws ServiceException {
		try {
			return getDAO().list(pojoClass,  sort);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter, com.lmiky.platform.database.model.Sort)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter propertyFilter, Sort sort) throws ServiceException {
		try {
			return getDAO().list(pojoClass, propertyFilter, sort);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, java.util.List)
	 */
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<Sort> sorts) throws ServiceException {
		try {
			return getDAO().list(pojoClass,  sorts);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, java.util.List, java.util.List)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) throws ServiceException {
		try {
			return getDAO().list(pojoClass, propertyFilters, sorts);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().list(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, com.lmiky.platform.database.model.Sort[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort... sorts) throws ServiceException {
		try {
			return getDAO().list(pojoClass, sorts);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, int, int)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize) throws ServiceException {
		try {
			return getDAO().list(pojoClass, pageFirst, pageSize);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, java.util.List, java.util.List, int, int)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts, int pageFirst, int pageSize) throws ServiceException {
		try {
			return getDAO().list(pojoClass, propertyFilters, sorts, pageFirst, pageSize);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, int, int, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().list(pojoClass, pageFirst, pageSize, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#list(java.lang.Class, int, int, com.lmiky.platform.database.model.Sort[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, Sort... sorts) throws ServiceException {
		try {
			return getDAO().list(pojoClass, pageFirst, pageSize, sorts);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#count(java.lang.Class)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> int count(Class<T> pojoClass) throws ServiceException {
		try {
			return getDAO().count(pojoClass);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#count(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> int count(Class<T> pojoClass, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().count(pojoClass, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#count(java.lang.Class, java.util.List)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> int count(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws ServiceException {
		try {
			return getDAO().count(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#count(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> int count(Class<T> pojoClass, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().count(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#count(java.lang.Class, java.util.Map)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> int count(Class<T> pojoClass, Map<String, Object> params) throws ServiceException {
		try {
			return getDAO().count(pojoClass, params);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#exist(java.lang.Class, java.util.List)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws ServiceException {
		try {
			return getDAO().exist(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#exist(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, PropertyFilter... propertyFilters) throws ServiceException {
		try {
			return getDAO().exist(pojoClass, propertyFilters);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#exist(java.lang.Class, java.util.Map)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, Map<String, Object> params) throws ServiceException {
		try {
			return getDAO().exist(pojoClass, params);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.BaseService#exist(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Transactional(readOnly=true)
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, String propertyName, Object propertyValue) throws ServiceException {
		try {
			return getDAO().exist(pojoClass, propertyName, propertyValue);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 获取DAO对象
	 * @author lmiky
	 * @date 2013-4-15
	 * @return
	 */
	public BaseDAO getDAO() {
		return baseDAO;
	}

	/**
	 * 注入DAO
	 * @author lmiky
	 * @date 2013-4-15
	 * @param dao
	 */
	@Resource(name="baseDAO")
	public void setDAO(BaseDAO dao) {
		this.baseDAO = dao;
	}

}