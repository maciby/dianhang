package com.dianhang.oa.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianhang.oa.mapper.OACommonMapper;
import com.dianhang.system.domain.SysUser;
import com.dianhang.system.service.ISysUserService;
import com.dianhang.system.service.impl.SysUserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service("oaCommon")
@Slf4j
public class OACommonService {

	@Autowired
	private OACommonMapper commonMapper;

	@Autowired
	private ISysUserService sysUserService;

	public Map<String, Object> getSingleMap(String tableName, String columnKey, String columnValue) {
		Map<String, Object> baseMap = new LinkedHashMap<>();
		List<Map<String, Object>> baseList = commonMapper.getBaseMap(tableName, columnKey, columnValue);
		// 遍历list
		for (Map<String, Object> map : baseList) {
			String base = null;
			Object fare = null;
			// 遍历map的key集合 获取对应key的value
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (columnKey.equals(entry.getKey())) {
					base = String.valueOf(entry.getValue());
				} else if (columnValue.equals(entry.getKey())) {
					fare = entry.getValue();
				}
			}
			baseMap.put(base, fare);
			log.error(base);
			log.error(fare.toString());
		}
		return baseMap;

	}

	public Object getSingleLabel(String tableName, String columnKey, String columnValue, String defaultValue) {
		Map<String, Object> a = getSingleMap(tableName, columnKey, columnValue);
		if (MapUtils.isEmpty(a)) {
			return defaultValue;
		}
		System.err.println(defaultValue);
		return a.get(defaultValue);
	}

	public String getFlowActivityRole(String activity) {
		return OAConstants.FLOW_ACTIVITY_ROLE.get(activity);
	}

	public String getOpinionListByActivity(String activity) {
		return OAConstants.FLOW_ACTIVITY_TRANS.get(activity);
	}
	
	public List<SysUser> getUserListByActivity(String activity) {
		String roeCode = getFlowActivityRole(activity);
		List<SysUser> userList = sysUserService.selectUserListByRole(roeCode);
		return userList;
	}
}
