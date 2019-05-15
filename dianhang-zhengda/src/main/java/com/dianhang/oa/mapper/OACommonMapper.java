package com.dianhang.oa.mapper;

import java.util.List;
import java.util.Map;

public interface OACommonMapper {

	List<Map<String, Object>> getBaseMap(String tableName, String columnKey, String columnValue);

}
