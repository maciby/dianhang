package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 特价合同
 * 
 * @author thinkpad
 *
 */
@Setter
@Getter
public class FlowProcess extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uuid;
	private String instUuid;
	private String activity;
	private String userId;
	private String userName;
	private String deptId;
	private String deptName;
	private String opinion;
	private String status; // 0=待处理，2=已完成，9=抄送
}
