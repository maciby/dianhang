package com.dianhang.oa.mapper;

import java.util.List;

import com.dianhang.oa.domain.FlowProcess;

public interface FlowProcessMapper {

	int insertFlowProcess(FlowProcess flowProcess);

	List<FlowProcess> selectFlowProcessByInstUuid(String instUuid);

	FlowProcess selectCurrentFlowProcessByInstUuid(String contractId, String userId);

	void updateFlowProcess(FlowProcess flowProcessCur);

	FlowProcess selectBackFlowProcess(String contractId, String activity);

}
