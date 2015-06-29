package com.lmiky.platform.database.dao.mybatis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import javax.persistence.JoinColumn;
import javax.persistence.Table;

import net.sf.cglib.proxy.Enhancer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.lmiky.platform.database.dao.BaseDAO;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyCompareType;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.model.Sort;
import com.lmiky.platform.database.pojo.BasePojo;
import com.lmiky.platform.tree.pojo.BaseTreePojo;
import com.lmiky.platform.util.BundleUtils;

/**
 * 基础dao
 * @author lmiky
 * @date 2013-4-15
 */
@Repository("baseDAO")
public class BaseDAOImpl extends AbstractBaseDAOImpl implements BaseDAO {

	// 参数字段
	/**
	 * 表名
	 */
	protected static final String PARAM_NAME_TABLENAME= "tableName";
	/**
	 * 表别名
	 */
	protected static final String PARAM_NAME_TABLEALIAS = "tableAlias";
	/**
	 * 是否有级联其他表
	 */
	protected static final String PARAM_NAME_HAS_JOIN = "hasJoin";
	/**
	 * 是否有级联其他表
	 */
	protected static final String PARAM_NAME_JOIN_TABLEALISA = "joinTableAlias";
	/**
	 * 是否有级联其他对象
	 */
	protected static final String PARAM_NAME_JOIN_POJOALISA = "joinPojoAlias";
	/**
	 * 过滤条件
	 */
	protected static final String PARAM_NAME_FILTERS = "filters";
	/**
	 * 排序
	 */
	protected static final String PARAM_NAME_SORTS = "sorts";
	/**
	 * 分页：起始位置
	 */
	protected static final String PARAM_NAME_PAGE_FIRST = "pageFirst";
	/**
	 * 分页：查询记录数
	 */
	protected static final String PARAM_NAME_PAGE_SIZE = "pageSize";

	// 映射文件命名空间
	/**
	 * 公共映射
	 */
	protected static final String MAPPER_NAMESPACE_COMMON = "common";

	// sql方法名
	/**
	 * sql方法名：查询
	 */
	protected static final String SQLNAME_FIND = "find";

	/**
	 * sql方法名：添加
	 */
	protected static final String SQLNAME_ADD = "add";

	/**
	 * sql方法名：修改
	 */
	protected static final String SQLNAME_UPDATE = "update";
	
	/**
	 * sql方法名：根据参数修改修改
	 */
	protected static final String SQLNAME_UPDATE_BY_PARAMS = "updateByParams";
	/**
	 * sql方法名：根据条件修改修改
	 */
	protected static final String SQLNAME_UPDATE_BY_FILTERS = "updateByFilters";
	
	/**
	 * sql方法名：删除
	 */
	protected static final String SQLNAME_DELETE = "delete";
	
	/**
	 * sql方法名：根据ID批量删除
	 */
	protected static final String SQLNAME_DELETE_BATCH_BY_IDS = "batchDeleteByIds";

	/**
	 * sql方法名：获取列表
	 */
	protected static final String SQLNAME_LIST = "list";

	/**
	 * sql方法名：统计
	 */
	protected static final String SQLNAME_COUNT = "count";

	/**
	 * 对象对应的数据库表名
	 */
	protected Map<String, String> pojoTableNames = new HashMap<String, String>();
	
	/**
	 * 操作配置：自定义执行，或通用的执行
	 */
	protected Map<String, String> operateConfig = new HashMap<String, String>();
	
	//操作配置值
	/**
	 * 通用
	 */
	protected static final String OPEARATE_CONFIG_COMMON = "common";
	/**
	 * 自定义
	 */
	protected static final String OPEARATE_CONFIG_CUSTOM = "custom";
	
	/**
	 * 获取实体类的表名
	 * @author lmiky
	 * @date 2014年8月25日 下午10:12:01
	 * @param pojoClass
	 * @return
	 * @throws DatabaseException
	 */
	protected String getPojoTabelName(Class<?> pojoClass) throws DatabaseException {
		Class<?> executePojoClass = getExecutePojoClass(pojoClass);
		// 先读缓存
		String cacheKey = executePojoClass.getName();
		//String cacheTableName = pojoTableNames.get(pojoClass);
		if (pojoTableNames.containsKey(cacheKey)) {
			return pojoTableNames.get(cacheKey);	//不管是否null
		}
		// 根据反射获取
		Table annotation = executePojoClass.getAnnotation(Table.class);
		// 没有对应的注解
		//if (annotation == null) {
		//	throw new DatabaseException(cacheKey + " is not a db pojo!");
		//}
		String cacheTableName = null;
		if(annotation != null) {
			cacheTableName = annotation.name();
		}
		// 放入缓存
		pojoTableNames.put(cacheKey, cacheTableName);
		return cacheTableName;
	}

	/**
	 * 获取操作配置
	 * @author lmiky
	 * @date 2014年9月17日 上午10:21:45
	 * @param pojoClass
	 * @param operateName
	 * @return
	 */
	protected String getOperateConfig(Class<?> pojoClass, String operateName) {
		//缓存
		String cacheKey = pojoClass.getName() + "_" + operateName;
		String cacheValue = operateConfig.get(cacheKey);
		if(!StringUtils.isBlank(cacheValue)) {
			return cacheValue;
		}
		try {
			cacheValue = BundleUtils.getStringValue("config/mappers/config", cacheKey);
		} catch(MissingResourceException e) {	//没有配置，则默认为通用
			cacheValue = OPEARATE_CONFIG_COMMON;
		}
		operateConfig.put(cacheKey, cacheValue);
		return cacheValue;
	}
	
	/**
	 * 获取执行类
	 * @author lmiky
	 * @date 2014年10月2日 上午10:21:45
	 * @param pojo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BasePojo> Class<T> getExecutePojoClass(BasePojo pojo) {
		if(Enhancer.isEnhanced(pojo.getClass())) {	//判断是否CGLIB代理类
			return (Class<T>) pojo.getClass().getSuperclass();
		} else {
			return (Class<T>) pojo.getClass();
		}
	}
	
	/**
	 * 获取执行类
	 * @author lmiky
	 * @date 2014年10月2日 上午10:21:45
	 * @param pojoClass
	 * @return
	 */
	protected Class<?> getExecutePojoClass(Class<?> pojoClass) {
		if(Enhancer.isEnhanced(pojoClass)) {	//判断是否CGLIB代理类
			return pojoClass.getSuperclass();
		} else {
			return pojoClass;
		}
	}
	
	/**
	 * 获取对象需要执行的类
	 * @author lmiky
	 * @date 2014年10月4日 上午10:21:45
	 * @param pojoClass
	 * @param sqlName	mapper名，如果为空，则不需要判断
	 * @return
	 */
	protected <T extends BasePojo> List<Class<?>> listPojoExecuteClass(Class<?> pojoClass, String sqlName) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Class<?> executeClazz = getExecutePojoClass(pojoClass);
		//循环执行继承
		while(!executeClazz.equals(BasePojo.class)) {
			if(getPojoTabelName(executeClazz) != null) {
				String executeClassName = executeClazz.getName();
				if(sqlName != null) {	//需要判断
					//判断是否有配置
					if(sqlSessionTemplate.getConfiguration().hasStatement(executeClassName + "." + sqlName)) {
						classList.add(0, executeClazz);
					}
				} else {
					classList.add(0, executeClazz);
				}
				executeClazz = executeClazz.getSuperclass();
			} else {
				break;
			}
		}
		return classList;
	}
	
	/**
	 * 获取操作空间名
	 * @author lmiky
	 * @date 2014年9月17日 上午10:25:14
	 * @param pojoClass
	 * @param operateName
	 * @return
	 */
	protected String getOperateNameSpace(Class<?> pojoClass, String operateName) {
		String operateConfig = getOperateConfig(pojoClass, operateName);
		if(OPEARATE_CONFIG_COMMON.equals(operateConfig)) {
			return MAPPER_NAMESPACE_COMMON + "." + operateName;
		} else {
			return pojoClass.getName() + "." + operateName;
		}
	}
	
	/**
	 * 生成参数
	 * @author lmiky
	 * @date 2014年9月9日 上午10:23:01
	 * @param pojoClass
	 * @return
	 * @throws Exception 
	 */
	protected <T extends BasePojo> Map<String, Object> generateParameterMap(Class<T> pojoClass) throws Exception {
		return generateParameterMap(pojoClass, new ArrayList<PropertyFilter>());
	}
	
	/**
	 * 生成参数
	 * @author lmiky
	 * @date 2014年11月4日 下午12:04:58
	 * @param pojoClass
	 * @param propertyFilters
	 * @return
	 * @throws Exception
	 */
	protected <T extends BasePojo> Map<String, Object> generateParameterMap(Class<T> pojoClass, PropertyFilter... propertyFilters) throws Exception {
		return generateParameterMap(pojoClass, Arrays.asList(propertyFilters));
	}
	
	/**
	 * 生成参数
	 * @author lmiky
	 * @date 2014年9月17日 下午2:24:46
	 * @param pojoClass
	 * @param propertyFilters
	 * @return
	 * @throws Exception 
	 */
	protected <T extends BasePojo> Map<String, Object> generateParameterMap(Class<T> pojoClass,  List<PropertyFilter> propertyFilters) throws Exception {
		return generateParameterMap(pojoClass, propertyFilters, null);
	}
	
	/**
	 * 生成参数
	 * @author lmiky
	 * @date 2014年9月17日 下午2:24:46
	 * @param pojoClass
	 * @param propertyFilters
	 * @return
	 * @throws Exception 
	 */
	protected <T extends BasePojo> Map<String, Object> generateParameterMap(Class<T> pojoClass,  List<PropertyFilter> propertyFilters, List<Sort> sorts) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PARAM_NAME_TABLENAME, getPojoTabelName(pojoClass));
		params.put(PARAM_NAME_TABLEALIAS, pojoClass.getSimpleName());	
		boolean hasJoin = false;	//是否有级联别的表
		List<String> joinTableAlias = new ArrayList<String>();	
		List<String> joinPojoAlias = new ArrayList<String>();	
		String pojoClassName = pojoClass.getName();
		if((propertyFilters == null || propertyFilters.isEmpty()) && (sorts == null || sorts.isEmpty())) {
			params.put(PARAM_NAME_HAS_JOIN, false);
		} else {
			List<PropertyFilter> dbFilters = new ArrayList<PropertyFilter>();
			if(propertyFilters != null) {
				for(PropertyFilter filter : propertyFilters) {
					if(filter.getCompareClass() == null) {	//默认为当初操作的对象
						filter.setCompareClass(pojoClass);
					}
					dbFilters.add((PropertyFilter)filter.clone());	//因为可能需要重新设置条件，为了不改掉页面传递的数据，所以使用了拷贝
				}
			}
			params.put(PARAM_NAME_FILTERS, dbFilters);
			//条件列表
			for(PropertyFilter filter : dbFilters) {
				Class<?> filterClass = filter.getCompareClass();
				String filterClassName = (filterClass == null) ? "" : filterClass.getName();
				if(!pojoClassName.equals(filterClassName)) {	//是否是其他的表
					hasJoin = true;
					joinTableAlias.add(filterClass.getSimpleName());	//添加到所级联的表列表中
				} else {	//自身级联
					rebuildFilter(filter);
					String filterPropertyName = filter.getPropertyName();
					int splitIndex = filterPropertyName.indexOf(".");
					if(splitIndex != -1) {	//其他级联，比如树状的，级联的类跟对象本身是同一种类
						String pojoName = filterPropertyName.substring(0, splitIndex);
						if(!joinPojoAlias.contains(pojoName)) {
							joinPojoAlias.add(pojoName);
						}
						hasJoin = true;
					}
				}
			}
		}
		//排序列表
		if(sorts != null) {
			for(Sort sort : sorts) {
				Class<?> sortClass = sort.getSortClass();
				if(sortClass == null) {	//设置默认实体类
					sortClass = pojoClass;
					sort.setSortClass(sortClass);
				}
				String sortClassName = (sortClass == null) ? "" : sortClass.getName();
				if(!pojoClassName.equals(sortClassName)) {	//是否是其他的表
					hasJoin = true;
					joinTableAlias.add(sortClass.getSimpleName());	//添加到所级联的表列表中
				} else {	//自身级联
					//TODO 暂时没时间
				}
			}
			setSortParameter(params, sorts);
		}
		params.put(PARAM_NAME_HAS_JOIN, hasJoin);
		params.put(PARAM_NAME_JOIN_TABLEALISA, joinTableAlias);
		params.put(PARAM_NAME_JOIN_POJOALISA, joinPojoAlias);
		return params;
	}

	/**
	 * 重构过滤条件
	 * @author lmiky
	 * @date 2014年10月16日 下午3:32:20
	 * @param filter
	 * @throws Exception
	 */
	protected void rebuildFilter(PropertyFilter filter) throws Exception {
		String filterPropertyName = filter.getPropertyName();
		int splitIndex = filterPropertyName.indexOf(".");
		if(splitIndex != -1) {	//级联
			String pojoName = filterPropertyName.substring(0, splitIndex);
			String fieldName = filterPropertyName.substring(splitIndex + 1);	//属性字段
			if(BasePojo.POJO_FIELD_NAME_ID.equals(fieldName)) {	//获取外键字段
				Method fieldMethod = BaseTreePojo.class.getMethod("get" + com.lmiky.platform.util.StringUtils.firstLetterUpperCase(pojoName));
				JoinColumn joinColumnAnnotation = fieldMethod.getAnnotation(JoinColumn.class); 
				filter.setPropertyName(joinColumnAnnotation.name());//外键注解
			}
		}
	}
	
	/**
	 * 设置排序参数
	 * @author lmiky
	 * @date 2014年9月13日 下午5:33:48
	 * @param parameterMap
	 * @param sorts
	 */
	protected void setSortParameter(Map<String, Object> parameterMap, List<Sort> sorts) {
		parameterMap.put(PARAM_NAME_SORTS, sorts);
	}
	/**
	 * 设置排序参数
	 * @author lmiky
	 * @date 2014年9月13日 下午5:33:58
	 * @param parameterMap
	 * @param sort
	 */
	protected void setSortParameter(Map<String, Object> parameterMap, Sort sort) {
		List<Sort> sorts = new ArrayList<Sort>();
		sorts.add(sort);
		setSortParameter(parameterMap, sorts);
	}
	
	/**
	 * 设置分页参数
	 * @author lmiky
	 * @date 2014年9月13日 下午5:37:58
	 * @param parameterMap
	 * @param pageFirst	起始位置，如果不指定，则设置为null
	 * @param pageSize 获取记录数，如果不指定，则设置为null
	 */
	protected void setPageParameter(Map<String, Object> parameterMap, Integer pageFirst, Integer pageSize) {
		parameterMap.put(PARAM_NAME_PAGE_FIRST, pageFirst);
		parameterMap.put(PARAM_NAME_PAGE_SIZE, pageSize);
	}
	
	/**
	 * 设置排序参数
	 * @author lmiky
	 * @date 2014年9月13日 下午5:34:02
	 * @param parameterMap
	 * @param propertyName
	 * @param sortType
	 * @param sortClass
	 */
	protected void setSortParameter(Map<String, Object> parameterMap, String propertyName, String sortType, Class<?> sortClass) {
		Sort sort = new Sort();
		sort.setPropertyName(propertyName);
		sort.setSortType(sortType);
		sort.setSortClass(sortClass);
		setSortParameter(parameterMap, sort);
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#isDBPojo(java.lang.Class)
	 */
	public <T extends BasePojo> boolean isDBPojo(Class<T> pojoClass) throws DatabaseException {
		try {
			return getPojoTabelName(pojoClass) != null;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		} 
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#find(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <T extends BasePojo> T find(Class<T> pojoClass, Long id) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(BasePojo.POJO_FIELD_NAME_ID + "");
			propertyFilter.setPropertyValue(id);
			return find(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#find(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(propertyName);
			propertyFilter.setPropertyValue(propertyValue);
			return find(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#find(java.lang.Class, java.util.List)
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass, propertyFilters);
			return sqlSessionTemplate.selectOne(pojoClass.getName() + "." + SQLNAME_FIND, params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#find(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return find(pojoClass, Arrays.asList(propertyFilters));
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#find(java.lang.Class, java.util.Map)
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, Map<String, Object> params) throws DatabaseException {
		try {
			List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				propertyFilters.add(new PropertyFilter(key, entry.getValue(), PropertyCompareType.EQ, pojoClass));
			}
			return find(pojoClass, propertyFilters);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#save(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void save(T pojo) throws DatabaseException {
		try {
			if (pojo.getId() == null) {
				add(pojo);
			} else {
				update(pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#save(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void save(List<T> pojos) throws DatabaseException {
		try {
			for (T pojo : pojos) {
				save(pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#add(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void add(T pojo) throws DatabaseException {
		try {
			List<Class<?>> classList = listPojoExecuteClass(pojo.getClass(), SQLNAME_ADD);
			for(Class<?> clazz : classList) {
				sqlSessionTemplate.insert(clazz.getName() + "." + SQLNAME_ADD, pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#add(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void add(List<T> pojos) throws DatabaseException {
		try {
			for (T pojo : pojos) {
				add(pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void update(T pojo) throws DatabaseException {
		try {
			List<Class<?>> classList = listPojoExecuteClass(pojo.getClass(), SQLNAME_UPDATE);
			for(Class<?> clazz : classList) {
				sqlSessionTemplate.update(clazz.getName() + "." + SQLNAME_UPDATE, pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void update(List<T> pojos) throws DatabaseException {
		try {
			for (T pojo : pojos) {
				update(pojo);
			}
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.lang.Class, java.lang.Long, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(propertyName, propertyValue);
			return update(pojoClass, id, params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.lang.Class, java.lang.Long, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, Map<String, Object> params) throws DatabaseException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(BasePojo.POJO_FIELD_NAME_ID, id);
			return update(pojoClass, condition, params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Map<String, Object> condition, Map<String, Object> updateValue) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass);
			params.put("condition", condition);
			params.put("updateValue", updateValue);
			return sqlSessionTemplate.update(getOperateNameSpace(pojoClass, SQLNAME_UPDATE_BY_PARAMS), params) > 0;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.lang.Class, java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, String conditionFieldName, Object conditionFieldValue, String updateFieldName, Object updateFieldValue) throws DatabaseException {
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put(conditionFieldName, conditionFieldValue);
			Map<String, Object> updateValue = new HashMap<String, Object>();
			updateValue.put(updateFieldName, updateFieldValue);
			return update(pojoClass, condition, updateValue);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#update(java.lang.Class, java.util.List, java.util.Map)
	 */
	public <T extends BasePojo> boolean update(Class<T> pojoClass, List<PropertyFilter> propertyFilters, Map<String, Object> updateValue) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass);
			params.put(PARAM_NAME_FILTERS, propertyFilters);
			params.put("updateValue", updateValue);
			return sqlSessionTemplate.update(getOperateNameSpace(pojoClass, SQLNAME_UPDATE_BY_FILTERS), params) > 0;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void delete(T pojo) throws DatabaseException {
		try {
			delete(getExecutePojoClass(pojo), pojo.getId());
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void delete(List<T> pojos) throws DatabaseException {
		try {
			if (pojos.isEmpty()) {
				return;
			}
			Long[] ids = new Long[pojos.size()];
			for (int i = 0; i < pojos.size(); i++) {
				ids[i] = pojos.get(i).getId();
			}
			delete(pojos.get(0).getClass(), ids);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long id) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(BasePojo.POJO_FIELD_NAME_ID + "");
			propertyFilter.setPropertyValue(id);
			delete(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.lang.Class, java.lang.Long[])
	 */
	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long[] ids) throws DatabaseException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tableName", getPojoTabelName(pojoClass));
			params.put("ids", ids);
			sqlSessionTemplate.delete(getOperateNameSpace(pojoClass, SQLNAME_DELETE_BATCH_BY_IDS), params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(propertyName);
			propertyFilter.setPropertyValue(propertyValue);
			return delete(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass, propertyFilters);
			return sqlSessionTemplate.delete(getOperateNameSpace(pojoClass, SQLNAME_DELETE), params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#delete(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return delete(pojoClass, Arrays.asList(propertyFilters));
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass) throws DatabaseException {
		try {
			return list(pojoClass, 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			return list(pojoClass, new PropertyFilter(propertyName, propertyValue, pojoClass));
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Map<String, Object> params) throws DatabaseException {
		try {
			PropertyFilter[] propertyFilters = new PropertyFilter[params.size()];
			int index = 0;
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				propertyFilters[index++] = new PropertyFilter(key, entry.getValue(), pojoClass);
			}
			return list(pojoClass, propertyFilters);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.platform.database.model.Sort)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort sort) throws DatabaseException {
		try {
			List<Sort> sorts = new ArrayList<Sort>();
			if (sort != null) {
				sorts.add(sort);
			}
			return list(pojoClass, sorts);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter, com.lmiky.platform.database.model.Sort)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter propertyFilter, Sort sort) throws DatabaseException {
		try {
			List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
			if (propertyFilter != null) {
				propertyFilters.add(propertyFilter);
			}
			List<Sort> sorts = new ArrayList<Sort>();
			if (sort != null) {
				sorts.add(sort);
			}
			return list(pojoClass, propertyFilters, sorts);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<Sort> sorts) throws DatabaseException {
		try {
			return list(pojoClass, new ArrayList<PropertyFilter>(), sorts);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, java.util.List, java.util.List)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) throws DatabaseException {
		try {
			return list(pojoClass, propertyFilters, sorts, 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return list(pojoClass, 0, Integer.MAX_VALUE, propertyFilters);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.platform.database.model.Sort[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort... sorts) throws DatabaseException {
		try {
			return list(pojoClass, 0, Integer.MAX_VALUE, sorts);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, int, int)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize) throws DatabaseException {
		try {
			return list(pojoClass, null, null, pageFirst, pageSize);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, java.util.List, java.util.List, int, int)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts, int pageFirst, int pageSize) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass, propertyFilters, sorts);
			setSortParameter(params, sorts);
			setPageParameter(params, pageFirst < 0 ? 0 : pageFirst, pageSize < 1 ? 1 : pageSize);
			return sqlSessionTemplate.selectList(pojoClass.getName() + "." + SQLNAME_LIST, params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, int, int, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return list(pojoClass, Arrays.asList(propertyFilters), null, pageFirst, pageSize);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#list(java.lang.Class, int, int, com.lmiky.platform.database.model.Sort[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, Sort... sorts) throws DatabaseException {
		try {
			return list(pojoClass, null, Arrays.asList(sorts), pageFirst, pageSize);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#count(java.lang.Class)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass) throws DatabaseException {
		try {
			return count(pojoClass, new ArrayList<PropertyFilter>());
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#count(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(pojoClass, propertyFilters);
			return (Integer)sqlSessionTemplate.selectOne(getOperateNameSpace(pojoClass, SQLNAME_COUNT), params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#count(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(propertyName);
			propertyFilter.setPropertyValue(propertyValue);
			return count(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#count(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return count(pojoClass, Arrays.asList(propertyFilters));
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#count(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, Map<String, Object> params) throws DatabaseException {
		try {
			List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				propertyFilters.add(new PropertyFilter(key, entry.getValue(), PropertyCompareType.EQ, pojoClass));
			}
			return count(pojoClass, propertyFilters);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#exist(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			return count(pojoClass, propertyFilters) > 0;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#exist(java.lang.Class, com.lmiky.platform.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		try {
			return exist(pojoClass, Arrays.asList(propertyFilters));
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#exist(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(propertyName);
			propertyFilter.setPropertyValue(propertyValue);
			return exist(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.database.dao.BaseDAO#exist(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, Map<String, Object> params) throws DatabaseException {
		try {
			List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				propertyFilters.add(new PropertyFilter(key, entry.getValue(), PropertyCompareType.EQ, pojoClass));
			}
			return exist(pojoClass, propertyFilters);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}