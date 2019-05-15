package com.dianhang.oa.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 工程合同
 * 
 * @author thinkpad
 *
 */
@Getter
@Setter
public class ProjectContract extends Contract {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String useUnitName;

	private String useUnitLinkman;

	private String useUnitLinktel;

	private Date applyDate;

	private String projectAddress;

	private String brandType;

	private String direct;

	private String applyAdjust;

	private String stockUp;

	private String stockUpMemo;

	private String agencyId;

	private String agencyName;

	private String agencyLinkman;

	private String agencyLinktel;
	
	private String distributorId;
	
	private String distributorName;
	
	private String distributorLinkman;
	
	private String distributorLinktel;

	private String projectType;

	private String byoutType;

	private String specialReq;

	private String areaLinkid;
	
	private String areaLinkman;

	private String areaLinktel;

	private String commonNo;

//	private String remark;

	private String status;

//	private String createBy;

//	private Date createTime;

	@Override
	public String getContractType() {
		return "1"; // 工程合同
	}

}
