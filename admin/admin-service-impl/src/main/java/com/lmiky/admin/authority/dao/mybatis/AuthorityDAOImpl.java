package com.lmiky.admin.authority.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lmiky.admin.authority.dao.AuthorityDAO;
import com.lmiky.admin.authority.pojo.Authority;
import com.lmiky.admin.system.menu.pojo.LatelyOperateMenu;
import com.lmiky.admin.user.pojo.Role;
import com.lmiky.platform.database.dao.mybatis.BaseDAOImpl;
import com.lmiky.platform.database.exception.DatabaseException;
import com.lmiky.platform.service.exception.ServiceException;

/**
 * 权限dao
 * @author lmiky
 * @date 2014-8-13 下午5:03:53
 */
@Repository("authorityDAO")
public class AuthorityDAOImpl extends BaseDAOImpl implements AuthorityDAO {

	/* (non-Javadoc)
	 * @see com.lmiky.admin.authority.dao.AuthorityDAO#listAuthorizedOperator(java.lang.String)
	 */
	public List<Role> listAuthorizedOperator(String modulePath) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(LatelyOperateMenu.class);
			params.put("modulePath", modulePath);
			return sqlSessionTemplate.selectList(Authority.class.getName() + ".listAuthorizedOperator", params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.authority.dao.AuthorityDAO#listUnauthorizedOperator(java.lang.String)
	 */
	public List<Role> listUnauthorizedOperator(String modulePath) throws DatabaseException {
		try {
			Map<String, Object> params = generateParameterMap(LatelyOperateMenu.class);
			params.put("modulePath", modulePath);
			return sqlSessionTemplate.selectList(Authority.class.getName() + ".listUnauthorizedOperator", params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.admin.authority.dao.AuthorityDAO#checkAuthority(java.lang.String, java.lang.Long)
	 */
	public boolean checkAuthority(String authorityCode, Long userId) throws ServiceException {
		try {
			Map<String, Object> params = generateParameterMap(LatelyOperateMenu.class);
			params.put("authorityCode", authorityCode);
			params.put("userId", userId);
			Integer result = sqlSessionTemplate.selectOne(Authority.class.getName() + ".checkAuthority", params);
			return result > 0;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
}
