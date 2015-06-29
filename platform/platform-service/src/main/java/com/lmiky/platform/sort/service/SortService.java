package com.lmiky.platform.sort.service;

import java.util.List;

import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.service.BaseService;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.sort.pojo.BaseSortPojo;

/**
 * 排序
 * @author lmiky
 * @date 2014-1-17
 */
public interface SortService extends BaseService {

	/**
	 * 排序，如果该实体类其他数据没在ID列表中，那么会被设置为默认的排序
	 * @author lmiky
	 * @date 2014-1-17
	 * @param sortPojoClass
	 * @param sortedIds 排完序的ID列表，越前面就排的越高
	 * @throws ServiceException
	 */
	public <T extends BaseSortPojo> void sort(Class<T> sortPojoClass, String[] sortedIds) throws ServiceException;
	
	
	/**
	 * 排序，如果该实体类其他数据（符合过滤条件的数据）没在ID列表中，那么会被设置为默认的排序
	 * @author lmiky
	 * @date 2014年8月16日 下午1:43:44
	 * @param sortPojoClass
	 * @param sortedIds
	 * @param propertyFilters
	 * @throws ServiceException
	 */
	public <T extends BaseSortPojo> void sort(Class<T> sortPojoClass, String[] sortedIds, List<PropertyFilter> propertyFilters) throws ServiceException;
}
