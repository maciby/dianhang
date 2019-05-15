package com.dianhang.oa.controller;

import java.util.List;

import com.dianhang.oa.domain.ContractProduct;
import com.dianhang.oa.domain.FlowProcess;
import com.dianhang.oa.domain.ProjectContract;
import com.dianhang.oa.domain.SpecialContract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAForm {

	private ProjectContract projectContract;

	private SpecialContract specialContract;

	private List<ContractProduct> contractProductList;
	
	private FlowProcess flowProcess;

	private String informUserList;
	
}
