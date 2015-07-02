package com.lmiky.admin.user.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lmiky.admin.user.dao.UserDAO;
import com.lmiky.admin.user.pojo.Operator;
import com.lmiky.admin.user.pojo.Role;
import com.lmiky.admin.user.pojo.User;
import com.lmiky.platform.database.dao.mybatis.BaseDAOImpl;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.pojo.BasePojo;

/**
 * @author lmiky
 * @date 2014年8月13日 下午10:52:30
 */
@Repository("userDAO")
public class UserDAOImpl extends BaseDAOImpl implements UserDAO {

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.admin.user.dao.UserDAO#listNoUserRoles(java.lang.Long)
	 */
	public List<Role> listNoUserRoles(Long userId) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(Role.class);
			params.put("userId", userId);
			return sqlSessionTemplate.selectList(Role.class.getName() + ".listNoUserRoles", params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.admin.user.dao.UserDAO#listNoRoleUser(java.lang.Long)
	 */
	public List<Operator> listNoRoleUser(Long roleId) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(Operator.class);
			params.put("roleId", roleId);
			return sqlSessionTemplate.selectList(Operator.class.getName() + ".listNoRoleUser", params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.admin.user.dao.UserDAO#deleteUserRole(java.lang.Long)
	 */
	public void deleteUserRole(Long userId) throws DatabaseException {
		try {
			sqlSessionTemplate.delete(User.class.getName() + ".deleteUserRole", userId);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.admin.user.dao.UserDAO#addUserRole(java.lang.Long, java.lang.Long)
	 */
	public void addUserRole(Long userId, Long roleId) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(User.class);
			params.put("userId", userId);
			params.put("roleId", roleId);
			sqlSessionTemplate.delete(User.class.getName() + ".addUserRole", params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.database.dao.mybatis.BaseDAOImpl#delete(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			int ret = super.delete(pojoClass, propertyFilters);
			if(User.class.isAssignableFrom(pojoClass)) {	
				super.delete(User.class, propertyFilters);	//删除用户表记录
			}
			return ret;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.dao.UserDAO#deleteOperatorUser()
	 */
	public void deleteOperatorUser() throws DatabaseException {
		try {
			sqlSessionTemplate.delete(User.class.getName() + ".deleteOperatorUser");
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
}
