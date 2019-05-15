package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 客户
 * 
 * @author thinkpad
 *
 */
@Getter
@Setter
@ToString
public class Customer extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2257862186186527726L;

	private String customerId;
	private String parentId;
	private String customerCode;
	private String customerName;
	private String linkman;
	private String linktel;
	private String address;

}
