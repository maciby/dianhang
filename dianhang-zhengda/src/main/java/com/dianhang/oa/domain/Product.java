package com.dianhang.oa.domain;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品信息
 * 
 * @author thinkpad
 *
 */
@Getter
@Setter
@ToString
public class Product extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
	private String productName;
	private String parentId;

}
