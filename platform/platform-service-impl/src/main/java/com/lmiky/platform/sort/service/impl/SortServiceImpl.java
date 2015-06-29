package com.lmiky.platform.sort.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.pojo.BasePojo;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.service.impl.BaseServiceImpl;
import com.lmiky.platform.sort.pojo.BaseSortPojo;
import com.lmiky.platform.sort.service.SortService;

/**
 * 排序
 * @author lmiky
 * @date 2014-1-17
 */
@Service("sortService")
public class SortServiceImpl extends BaseServiceImpl implements SortService {

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.sort.SortService#sort(java.lang.Class, java.lang.String[])
	 */
	@Transactional(rollbackFor = { Exception.class })
	public <T extends BaseSortPojo> void sort(Class<T> sortPojoClass, String[] sortedIds) throws ServiceException {
		try {
			this.sort(sortPojoClass, sortedIds, null);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.sort.service.SortService#sort(java.lang.Class, java.lang.String[], java.util.List)
	 */
	@Transactional(rollbackFor = { Exception.class })
	public <T extends BaseSortPojo> void sort(Class<T> sortPojoClass, String[] sortedIds, List<PropertyFilter> propertyFilters) throws ServiceException {
		try {
			//设置更新值
			Map<String, Object> updateValue = new HashMap<String, Object>();
			updateValue.put(BaseSortPojo.POJO_FIELD_NAME_SORT, BaseSortPojo.DEFAULT_SORT);
			// 清除原先的排序
			this.update(sortPojoClass, propertyFilters, updateValue);
			int sortValue = sortedIds.length;
			for (String id : sortedIds) {
				this.update(sortPojoClass, BasePojo.POJO_FIELD_NAME_ID, Long.valueOf(id), BaseSortPojo.POJO_FIELD_NAME_SORT, sortValue);
				sortValue--;
			}
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
