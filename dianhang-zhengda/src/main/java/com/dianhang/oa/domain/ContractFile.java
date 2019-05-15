package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 合同附件
 * 
 * @author thinkpad
 *
 */
@Setter
@Getter
@ToString
public class ContractFile extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String contractId;

	private String fileName; // 磁盘文件名称

	private String originalFileName; // 源文件名称

}
