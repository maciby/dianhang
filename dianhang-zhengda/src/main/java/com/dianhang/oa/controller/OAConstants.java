package com.dianhang.oa.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OAConstants {

	public static Map<String, String> FLOW_ACTIVITY_ROLE = new HashMap<>();
	public static Map<String, String> FLOW_ACTIVITY_TRANS = new HashMap<>();
	
	static {
		FLOW_ACTIVITY_ROLE.put("1", "area_manager");// 填报阶段需要“区域经理”
		FLOW_ACTIVITY_ROLE.put("2", "branch_manager");
		FLOW_ACTIVITY_ROLE.put("3", "company_manager");

//		Map<String, String> map1 = new LinkedHashMap<>();
//		map1.put("1", "2");
		FLOW_ACTIVITY_TRANS.put("1", "[{\"id\":\"1\",\"name\":\"提交\",\"toId\":\"2\"}]");

//		Map<String, String> map2 = new LinkedHashMap<>();
//		map2.put("1", "3");
//		map2.put("0", "1");
//		FLOW_ACTIVITY_TRANS.put("2", map2);
		FLOW_ACTIVITY_TRANS.put("2", "[{\"id\":\"1\",\"name\":\"提交\",\"toId\":\"3\"},{\"id\":\"2\",\"name\":\"退回\",\"toId\":\"1\"}]");

//		Map<String, String> map3 = new LinkedHashMap<>();
//		map3.put("1", "4");
//		map3.put("0", "2");
//		FLOW_ACTIVITY_TRANS.put("3", map3);
		FLOW_ACTIVITY_TRANS.put("3", "[{\"id\":\"1\",\"name\":\"提交\",\"toId\":\"4\"},{\"id\":\"2\",\"name\":\"退回\",\"toId\":\"2\"}]");

//		Map<String, String> map4 = new LinkedHashMap<>();
//		map4.put("1", "999");
//		map4.put("0", "3");
//		FLOW_ACTIVITY_TRANS.put("4", map4);
		FLOW_ACTIVITY_TRANS.put("4", "[{\"id\":\"1\",\"name\":\"提交\",\"toId\":\"999\"},{\"id\":\"2\",\"name\":\"退回\",\"toId\":\"3\"}]");

	}
}
