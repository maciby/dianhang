package com.dianhang.oa.domain;

import java.math.BigDecimal;

import com.dianhang.common.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 合同商品
 * 
 * @author thinkpad
 *
 */
@Setter
@Getter
@ToString
public class ContractProduct extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String contractId;

    private String productId;

    private String productName;

    private BigDecimal applyQuantity;

    private BigDecimal contractPrice;

    private BigDecimal agencyApplyPrice;

    private BigDecimal agencyApprovePrice;

    private BigDecimal distributorPrice;

    private String balanceType;

}
