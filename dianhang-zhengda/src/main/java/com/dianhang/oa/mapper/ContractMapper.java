package com.dianhang.oa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import com.dianhang.oa.domain.Contract;

public interface ContractMapper {

	/**
	 * @return
	 */
	List<Contract> selectContractList();

	List<Contract> selectContractListByUser(Contract contract);

	/**
	 * <br/>
	 * 第一层List，结果集集合
	 * 
	 * <br/>
	 * 第二层List，结果集
	 * 
	 * <br/>
	 * 第三层Map，行记录【key:字段名,value:字段值】
	 * 
	 * @param params
	 * @return
	 */
	@Options(statementType = StatementType.CALLABLE)
	@Select("exec GZ_CONTRACT_TRANS_ORDER #{contractId, mode=IN, jdbcType=VARCHAR},#{code, mode=OUT, jdbcType=INTEGER} ")
	List<Map<String, Object>> procedure(Map<String, Object> params);
	// List<Map<String, Object>> procedure(@Param("contractId") String contractId);

}
