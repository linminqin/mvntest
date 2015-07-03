package com.lmiky.cms.resource.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmiky.cms.resource.pojo.CmsResource;
import com.lmiky.cms.resource.pojo.CmsResourceContent;
import com.lmiky.cms.resource.pojo.CmsResourcePictureSnapshot;
import com.lmiky.platform.database.model.PropertyCompareType;
import com.lmiky.platform.database.model.PropertyFilter;
import com.lmiky.platform.database.pojo.BasePojo;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.service.impl.BaseServiceImpl;

/**
 * 文章
 * @author lmiky
 * @date 2014年8月3日 下午6:08:38
 */
@Service("cmsResourceService")
public class CmsResourceServiceImpl extends BaseServiceImpl {

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.impl.BaseServiceImpl#add(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void add(T pojo) throws ServiceException {
		super.add(pojo);
		if(pojo instanceof CmsResource) {
			CmsResource resource = (CmsResource)pojo;
			//内容
			CmsResourceContent content = resource.getContent();
			if(content != null) {
				content.setResourceId(pojo.getId());
				super.add(content);
			}
			//保存图片快照
			Set<CmsResourcePictureSnapshot> pictureSnapshots = resource.getPictureSnapshots();
			if(pictureSnapshots != null && !pictureSnapshots.isEmpty()) {
				for(CmsResourcePictureSnapshot pictureSnapshot : pictureSnapshots) {
					pictureSnapshot.setResourceId(pojo.getId());
					super.add(pictureSnapshot);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.service.impl.BaseServiceImpl#update(com.lmiky.platform.database.pojo.BasePojo)
	 */
	@Override
	@Transactional(rollbackFor={Exception.class})
	public <T extends BasePojo> void update(T pojo) throws ServiceException {
		super.update(pojo);
		if(pojo instanceof CmsResource) {
			CmsResource resource = (CmsResource)pojo;
			//内容
			CmsResourceContent content = resource.getContent();
			if(content != null) {
				content.setResourceId(pojo.getId());
				super.update(content);
			} else {
				super.delete(content);
			}
			//删除旧图片
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(CmsResourcePictureSnapshot.class);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName("resourceId");
			propertyFilter.setPropertyValue(resource.getId());
			super.delete(CmsResourcePictureSnapshot.class, propertyFilter);
			//保存图片快照
			Set<CmsResourcePictureSnapshot> pictureSnapshots = resource.getPictureSnapshots();
			if(pictureSnapshots != null && !pictureSnapshots.isEmpty()) {
				for(CmsResourcePictureSnapshot pictureSnapshot : pictureSnapshots) {
					pictureSnapshot.setResourceId(pojo.getId());
					super.add(pictureSnapshot);
				}
			}
		}
	}
}