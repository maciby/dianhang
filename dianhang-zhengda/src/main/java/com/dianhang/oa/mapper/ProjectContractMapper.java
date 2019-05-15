package com.dianhang.oa.mapper;

import com.dianhang.oa.domain.ProjectContract;

public interface ProjectContractMapper {

	int insertProjectContract(ProjectContract projectContract);

	int selectProjectContractCount(ProjectContract projectContract);

	ProjectContract selectProjectContractById(String contractId);

	int updateProjectContract(ProjectContract projectContract);
	
}
