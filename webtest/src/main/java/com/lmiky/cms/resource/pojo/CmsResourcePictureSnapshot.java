package com.lmiky.cms.resource.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.lmiky.platform.database.pojo.BasePojo;

/**
 * 图片快照
 * @author lmiky
 * @date 2014年7月21日 下午8:48:57
 */
@Entity
@Table(name="t_cms_resource_picture_snapshot")
public class CmsResourcePictureSnapshot extends BasePojo {
	private static final long serialVersionUID = 4928834781706515863L;
	private Long resourceId;
	private String path;
	
	/**
	 * @return the resourceId
	 */
	public Long getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * @return the path
	 */
	@Column(name="path")
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
