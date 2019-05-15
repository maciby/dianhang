package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contract extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5127079160719939880L;

	private String relationUserId;

	private String contractId;
	/** 项目登录号 */
	private String contractCode;

	public String contractType;

	private String distributorId;

	private String distributorName;

	private String status;

	private String createUserId;
}
