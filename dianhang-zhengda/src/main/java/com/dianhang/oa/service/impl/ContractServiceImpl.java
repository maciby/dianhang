package com.dianhang.oa.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dianhang.oa.domain.Contract;
import com.dianhang.oa.domain.ContractFile;
import com.dianhang.oa.domain.ContractProduct;
import com.dianhang.oa.domain.FlowProcess;
import com.dianhang.oa.domain.ProjectContract;
import com.dianhang.oa.domain.SpecialContract;
import com.dianhang.oa.mapper.ContractFileMapper;
import com.dianhang.oa.mapper.ContractMapper;
import com.dianhang.oa.mapper.ContractProductMapper;
import com.dianhang.oa.mapper.FlowProcessMapper;
import com.dianhang.oa.mapper.ProjectContractMapper;
import com.dianhang.oa.service.IContractService;
import com.dianhang.system.domain.SysUser;
import com.dianhang.util.DateUtils;
import com.dianhang.util.StringUtils;

@Service
public class ContractServiceImpl implements IContractService {

	@Autowired
	private ProjectContractMapper projectContractMapper;

	@Autowired
	private ContractProductMapper contractProductMapper;

	@Autowired
	private ContractFileMapper contractFileMapper;

	@Autowired
	private FlowProcessMapper flowProcessMapper;

	@Autowired
	private ContractMapper contractMapper;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int saveProjectContract(SysUser sysUser, ProjectContract projectContract,
			List<ContractProduct> contractProductList, Map<String, String> userMap, Map<String, String> fileMap)
			throws Exception {

		// 系统自动生成合同编号,并且该字段传递到OrderBill索引表的billNumberId字段(编码规则：OAGCSP-20181220-流水号00005)
		if (StringUtils.isBlank(projectContract.getContractCode())) {
			ProjectContract condition = new ProjectContract();
			condition.getParams().put("beginTime", DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
			int count = projectContractMapper.selectProjectContractCount(condition);
			++count;

			StringBuilder contractCode = new StringBuilder();
			contractCode.append("00000").append(String.valueOf(count));

			projectContract.setContractCode("OAGCSP-" + DateUtils.dateTimeNow("yyyyMMdd") + "-"
					+ contractCode.substring(contractCode.length() - 5));
		}

		projectContract.setCreateUserId(sysUser.getUserId());
		projectContract.setCreateBy(sysUser.getUserName());
		projectContract.setStatus("2");

		int rows = projectContractMapper.insertProjectContract(projectContract);
		System.err.println("保存到数据的合同UUID：" + projectContract.getContractId());

		Iterator<ContractProduct> it = contractProductList.iterator();
		while (it.hasNext()) {
			ContractProduct contractProduct = it.next();
			if (StringUtils.isBlank(contractProduct.getProductId())) {
				it.remove();
				continue;
			}

			contractProduct.setContractId(projectContract.getContractId());
			System.err.println(contractProduct);
		}

		if (CollectionUtils.isEmpty(contractProductList)) {
			throw new Exception("商品列表不允许为空！");
		}
		contractProductMapper.batchContractProduct(contractProductList);

		// 过程数据
		FlowProcess flowProcess = new FlowProcess();
		flowProcess.setInstUuid(projectContract.getContractId());
		flowProcess.setActivity("1");
		flowProcess.setUserId(sysUser.getUserId());
		flowProcess.setUserName(sysUser.getUserName());
		flowProcess.setDeptId(sysUser.getDeptId());
		flowProcess.setDeptName(sysUser.getDept().getDeptName());
		flowProcess.setCreateBy(sysUser.getUserName());
		flowProcess.setCreateTime(new Date());
		flowProcess.setUpdateTime(new Date());
		flowProcess.setStatus("2");
		flowProcessMapper.insertFlowProcess(flowProcess);

		// 抄送数据
		if (MapUtils.isNotEmpty(userMap)) {
			for (Map.Entry<String, String> entry : userMap.entrySet()) {
				FlowProcess _flowProcess = new FlowProcess();
				_flowProcess.setInstUuid(projectContract.getContractId());
				_flowProcess.setActivity("1");
				_flowProcess.setUserId(sysUser.getUserId());
				_flowProcess.setUserName(sysUser.getUserName());
				_flowProcess.setDeptId(sysUser.getDeptId());
				_flowProcess.setDeptName(sysUser.getDept().getDeptName());
				_flowProcess.setCreateBy(sysUser.getUserName());
				_flowProcess.setCreateTime(new Date());
				_flowProcess.setUpdateTime(new Date());
				_flowProcess.setOpinion("抄送给："+entry.getValue());
				_flowProcess.setStatus("9");
				flowProcessMapper.insertFlowProcess(_flowProcess);
			}
		}

		// 待办数据
		FlowProcess flowProcess2 = new FlowProcess();
		flowProcess2.setInstUuid(projectContract.getContractId());
		flowProcess2.setActivity("2");
		flowProcess2.setUserId(projectContract.getAreaLinkid());
		flowProcess2.setUserName(projectContract.getAreaLinkman());
		flowProcess2.setCreateBy(sysUser.getUserName());
		flowProcess2.setCreateTime(new Date());
		flowProcess2.setStatus("0");
		flowProcessMapper.insertFlowProcess(flowProcess2);

		// 附件数据
		if (MapUtils.isNotEmpty(fileMap)) {
			List<ContractFile> list = new LinkedList<>();
			for (Map.Entry<String, String> entry : fileMap.entrySet()) {
				ContractFile contractFile = new ContractFile();
				contractFile.setContractId(projectContract.getContractId());
				contractFile.setFileName(entry.getKey());
				contractFile.setOriginalFileName(entry.getValue());
				list.add(contractFile);
			}
			contractFileMapper.batchContractFile(list);
		}

		return rows;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int saveProjectContract(SysUser sysUser, ProjectContract projectContract,
			List<ContractProduct> contractProductList, Map<String, String> userMap, Map<String, String> fileMap,
			FlowProcess flowProcess) throws Exception {

		// 保存合同基础信息
		if (StringUtils.isNotBlank(projectContract.getAgencyId()))
			projectContractMapper.updateProjectContract(projectContract);

		// 保存合同产品信息
		if (CollectionUtils.isNotEmpty(contractProductList)) {

			// 整理商品数据
			Iterator<ContractProduct> it = contractProductList.iterator();
			while (it.hasNext()) {
				ContractProduct contractProduct = it.next();
				if (StringUtils.isBlank(contractProduct.getProductId())) {
					it.remove();
					continue;
				}

				contractProduct.setContractId(projectContract.getContractId());
				System.err.println(contractProduct);
			}

			if (CollectionUtils.isEmpty(contractProductList)) {
				throw new Exception("商品列表不允许为空！");
			}

			// 先删除数据库中现有的商品
			contractProductMapper.deleteContractProductByContractId(projectContract.getContractId());
			// 添加新的商品
			contractProductMapper.batchContractProduct(contractProductList);
		}
		
		// 保存附件
		if (MapUtils.isNotEmpty(fileMap)) {
			List<ContractFile> list = new LinkedList<>();
			for (Map.Entry<String, String> entry : fileMap.entrySet()) {
				ContractFile contractFile = new ContractFile();
				contractFile.setContractId(projectContract.getContractId());
				contractFile.setFileName(entry.getKey());
				contractFile.setOriginalFileName(entry.getValue());
				list.add(contractFile);
			}
			contractFileMapper.batchContractFile(list);
		}

		System.err.println(projectContract.getContractId());
		// 修改当前待办的状态
		FlowProcess flowProcessCur = flowProcessMapper
				.selectCurrentFlowProcessByInstUuid(projectContract.getContractId(), sysUser.getUserId());
		flowProcessCur.setUpdateTime(new Date());
		flowProcessCur.setOpinion(flowProcess.getOpinion());
		flowProcessCur.setStatus("2");
		flowProcessCur.setDeptId(sysUser.getDeptId());
		flowProcessCur.setDeptName(sysUser.getDept().getDeptName());
		flowProcessMapper.updateFlowProcess(flowProcessCur);

		// 流程结束
		if ("999".equals(flowProcess.getActivity())) {

			// 将数据插入到ERP的订单中
			Map<String, Object> params = new HashMap<>();
			params.put("contractId", projectContract.getContractId());
			// List<List<?>> b = contractMapper.procedure(params);
			// for (List<?> list : b) {
			// for (Object o : list) {
			// System.err.println(o);
			// }
			// }
			List<Map<String, Object>> b = contractMapper.procedure(params);
			System.err.println("---------------------结果集-------------------------");
			for (Map<String, Object> map : b) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					System.err.print(entry.getKey());
					System.err.println(entry.getValue());
				}
			}
			System.err.println(params.get("code"));

			return 1;
		} else {

			flowProcess.setUuid(null);
			flowProcess.setOpinion(null);
			flowProcess.setStatus("0");
			flowProcess.setInstUuid(projectContract.getContractId());
			if (StringUtils.isBlank(flowProcess.getUserId())) {
				// 说明是退回
				FlowProcess flowProcessBack = flowProcessMapper.selectBackFlowProcess(projectContract.getContractId(),
						flowProcess.getActivity());
				if (null == flowProcessBack) {
					throw new Exception("退回失败，无法找到关系人！");
				}
				flowProcess.setUserId(flowProcessBack.getUserId());
				flowProcess.setUserName(flowProcessBack.getUserName());
			}
			return flowProcessMapper.insertFlowProcess(flowProcess);
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int saveSppecialContract(SysUser sysUser, SpecialContract specialContract,
			List<ContractProduct> productList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Contract> selectContractList(Contract contract) {
		return contractMapper.selectContractListByUser(contract);
	}

	@Override
	public Object getContract(String contractId, String contractType) {

		Contract contract = null;
		if ("1".equals(contractType)) {
			contract = projectContractMapper.selectProjectContractById(contractId);
		}
		return contract;
	}

	@Override
	public List<ContractProduct> queryContractProductList(String contractId) {
		return contractProductMapper.selectContractProductByContractId(contractId);
	}

	@Override
	public List<ContractFile> queryContractFileList(String contractId) {
		return contractFileMapper.selectContractFileByContractId(contractId);
	}

	@Override
	public List<FlowProcess> queryFlowProcessList(String contractId) {
		return flowProcessMapper.selectFlowProcessByInstUuid(contractId);
	}

	@Override
	public ContractFile getContractFile(String id) {
		return contractFileMapper.selectContractFileById(id);
	}

}
