package com.lmiky.platform.tree.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.platform.database.pojo.BasePojo;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.service.impl.BaseServiceImpl;
import com.lmiky.platform.tree.pojo.BaseTreePojo;

/**
 * 树
 * @author lmiky
 * @date 2014-1-5
 */
@Service("treeService")
public class TreeServiceImpl extends BaseServiceImpl {

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.impl.BaseServiceImpl#add(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public <T extends BasePojo> void add(T pojo) throws ServiceException {
		// 如果是树
		if (pojo instanceof BaseTreePojo) {
			BaseTreePojo parent = ((BaseTreePojo) pojo).getParent();
			// 非顶层
			if (parent != null) {
				// 修改父节点叶子数
				parent.setLeaf(parent.getLeaf() + 1);
				super.save(parent);
			}
		}
		super.add(pojo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.platform.service.impl.BaseServiceImpl#delete(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public <T extends BasePojo> void delete(T pojo) throws ServiceException {
		super.delete(pojo);
		if (pojo instanceof BaseTreePojo) {
			BaseTreePojo pojoTree = (BaseTreePojo) pojo;
			if (pojoTree.getLeaf() > 0) {
				throw new ServiceException("无法删除，该节点下有子节点！");
			}
			BaseTreePojo parent = pojoTree.getParent();
			// 非顶层
			if (parent != null) {
				// 修改父节点叶子数
				parent.setLeaf(parent.getLeaf() - 1);
				super.save(parent);
			}
			super.delete(BaseTreePojo.class, pojo.getId()); // 删除树内容
		}
	}
}
