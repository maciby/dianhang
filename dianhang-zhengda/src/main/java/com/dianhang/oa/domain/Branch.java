package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;

/**
 * 客户
 * 
 * @author thinkpad
 *
 */
@Getter
public class Branch extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2257862186186527726L;

	private String branchId;
	private String branchName;

}
