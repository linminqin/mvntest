package com.lmiky.jdp.user.service;

import java.util.List;

import com.lmiky.admin.service.BaseService;
import com.lmiky.admin.service.exception.ServiceException;
import com.lmiky.admin.user.pojo.Operator;

/**
 * 角色业务
 * @author lmiky
 * @date 2013-5-14
 */
public interface RoleService extends BaseService {
	
	/**
	 * 获取角色用户
	 * @author lmiky
	 * @date 2013-5-14
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 */
	public List<Operator> listRoleUser(Long roleId) throws ServiceException;
	
	/**
	 * 获取非角色用户
	 * @author lmiky
	 * @date 2013-5-14
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 */
	public List<Operator> listNoRoleUser(Long roleId) throws ServiceException;
}
