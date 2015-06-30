package com.lmiky.admin.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.admin.user.dao.UserDAO;
import com.lmiky.admin.user.pojo.Operator;
import com.lmiky.admin.user.pojo.Role;
import com.lmiky.admin.user.service.RoleService;
import com.lmiky.platform.database.dao.BaseDAO;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyCompareType;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.model.Sort;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.service.impl.BaseServiceImpl;

/**
 * @author lmiky
 * @date 2013-5-14
 */
/**
 * 说明
 * @author lmiky
 * @date 2013-5-14
 */ 
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
	private UserDAO userDAO;

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.RoleService#listRoleUser(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public List<Operator> listRoleUser(Long roleId) throws ServiceException {
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		propertyFilters.add(new PropertyFilter("id", roleId, PropertyCompareType.EQ, true, Role.class));
		List<Sort> sorts = new ArrayList<Sort>();
		sorts.add(new Sort("name", Sort.SORT_TYPE_ASC, Operator.class));
		return list(Operator.class, propertyFilters, sorts);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.RoleService#listNoRoleUser(java.lang.Long)
	 */
	@Transactional(readOnly=true)
	public List<Operator> listNoRoleUser(Long roleId) throws ServiceException {
		try {
			return userDAO.listNoRoleUser(roleId);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.service.impl.BaseServiceImpl#setDAO(com.lmiky.admin.database.dao.BaseDAO)
	 */
	@Override
	@Resource(name="userDAO")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
		this.userDAO = (UserDAO)dao;
	}
	
}
