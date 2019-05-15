package com.dianhang.oa.mapper;

import java.util.List;

import com.dianhang.oa.domain.ContractProduct;

public interface ContractProductMapper {

	int batchContractProduct(List<ContractProduct> list);

	List<ContractProduct> selectContractProductByContractId(String contractId);

	int deleteContractProductByContractId(String contractId);
}
