package com.dianhang.oa.service;

import java.util.List;
import java.util.Map;

import com.dianhang.oa.domain.Contract;
import com.dianhang.oa.domain.ContractFile;
import com.dianhang.oa.domain.ContractProduct;
import com.dianhang.oa.domain.FlowProcess;
import com.dianhang.oa.domain.ProjectContract;
import com.dianhang.oa.domain.SpecialContract;
import com.dianhang.system.domain.SysUser;

public interface IContractService {

	int saveProjectContract(SysUser sysUser, ProjectContract projectContract, List<ContractProduct> contractProductList,
			Map<String, String> userMap, Map<String, String> fileMap) throws Exception;

	int saveProjectContract(SysUser sysUser, ProjectContract projectContract, List<ContractProduct> contractProductList,
			Map<String, String> userMap, Map<String, String> fileMap, FlowProcess flowProcess) throws Exception;

	int saveSppecialContract(SysUser sysUser, SpecialContract specialContract,
			List<ContractProduct> contractProductList) throws Exception;

	List<Contract> selectContractList(Contract contract);

	Object getContract(String contractId, String contractType);

	List<ContractProduct> queryContractProductList(String contractId);

	List<ContractFile> queryContractFileList(String contractId);

	List<FlowProcess> queryFlowProcessList(String contractId);

	ContractFile getContractFile(String id);

}
