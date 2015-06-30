package com.lmiky.admin.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.admin.user.dao.UserDAO;
import com.lmiky.admin.user.pojo.User;
import com.lmiky.admin.user.service.UserService;
import com.lmiky.platform.database.dao.BaseDAO;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.database.model.PropertyCompareType;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.service.impl.BaseServiceImpl;

/**
 * 用户管理业务实现类
 * @author lmiky
 * @date 2013-4-24
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.UserService#findByLoginName(java.lang.String)
	 */
	@Transactional(readOnly=true)
	public User findByLoginName(String loginName) throws ServiceException {
		return findByLoginName(loginName, User.class);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.UserService#findByLoginName(java.lang.String, java.lang.Class)
	 */
	@Transactional(readOnly=true)
	public <T extends User> T findByLoginName(String loginName,  Class<T> userClass) throws ServiceException {
		return find(userClass, new PropertyFilter(User.POJO_FIELD_NAME_LOGINNAME, loginName, PropertyCompareType.EQ, userClass));
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.UserService#deleteOperatorUser()
	 */
	@Transactional(rollbackFor={Exception.class})
	public void deleteOperatorUser() throws ServiceException {
		try {
			((UserDAO)getDAO()).deleteOperatorUser();
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.UserService#deleteUserRole(java.lang.Long)
	 */
	@Transactional(rollbackFor={Exception.class})
	public void deleteUserRole(Long userId) throws ServiceException {
		try {
			((UserDAO)getDAO()).deleteUserRole(userId);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.user.service.UserService#addUserRole(java.lang.Long, java.lang.Long)
	 */
	@Transactional(rollbackFor={Exception.class})
	public void addUserRole(Long userId, Long roleId) throws ServiceException {
		try {
			((UserDAO)getDAO()).addUserRole(userId, roleId);
		} catch (DatabaseException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.admin.service.impl.BaseServiceImpl#setDAO(com.lmiky.admin.database.dao.BaseDAO)
	 */
	@Override
	@Resource(name="userDAO")
	public void setDAO(BaseDAO dao) {
		super.setDAO(dao);
	}
}