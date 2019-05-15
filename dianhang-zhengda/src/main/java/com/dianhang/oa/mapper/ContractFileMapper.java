package com.dianhang.oa.mapper;

import java.util.List;

import com.dianhang.oa.domain.ContractFile;

public interface ContractFileMapper {

	void insertContractFile(ContractFile contractFile);

	int batchContractFile(List<ContractFile> list);

	List<ContractFile> selectContractFileByContractId(String contractId);

	ContractFile selectContractFileById(String id);
}
