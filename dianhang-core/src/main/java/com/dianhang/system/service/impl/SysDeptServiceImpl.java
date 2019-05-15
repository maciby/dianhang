package com.dianhang.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianhang.common.annotation.DataScope;
import com.dianhang.common.constants.UserConstants;
import com.dianhang.system.domain.SysDept;
import com.dianhang.system.domain.SysRole;
import com.dianhang.system.mapper.SysDeptMapper;
import com.dianhang.system.service.ISysDeptService;
import com.dianhang.util.StringUtils;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
	
	@Autowired
	private SysDeptMapper deptMapper;

	/**
	 * 查询部门管理数据
	 * 
	 * @param dept
	 *            部门信息
	 * @return 部门信息集合
	 */
	@Override
	@DataScope(tableAlias = "d")
	public List<SysDept> selectDeptList(SysDept dept) {
		return deptMapper.selectDeptList(dept);
	}

	/**
	 * 查询部门管理树
	 * 
	 * @param dept
	 *            部门信息
	 * @return 所有部门信息
	 */
	@Override
	@DataScope(tableAlias = "d")
	public List<Map<String, Object>> selectDeptTree(SysDept dept) {
		List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
		List<SysDept> deptList = deptMapper.selectDeptList(dept);
		trees = getTrees(deptList, false, null);
		return trees;
	}

	/**
	 * 根据角色ID查询部门（数据权限）
	 *
	 * @param role
	 *            角色对象
	 * @return 部门列表（数据权限）
	 */
	@Override
	public List<Map<String, Object>> roleDeptTreeData(SysRole role) {
		String roleId = role.getRoleId();
		List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
		List<SysDept> deptList = selectDeptList(new SysDept());
		if (StringUtils.isNotNull(roleId)) {
			List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
			trees = getTrees(deptList, true, roleDeptList);
		} else {
			trees = getTrees(deptList, false, null);
		}
		return trees;
	}

	/**
	 * 对象转部门树
	 *
	 * @param deptList
	 *            部门列表
	 * @param isCheck
	 *            是否需要选中
	 * @param roleDeptList
	 *            角色已存在菜单列表
	 * @return
	 */
	public List<Map<String, Object>> getTrees(List<SysDept> deptList, boolean isCheck, List<String> roleDeptList) {

		List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
		for (SysDept dept : deptList) {
			if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
				Map<String, Object> deptMap = new HashMap<String, Object>();
				deptMap.put("id", dept.getDeptId());
				deptMap.put("pId", dept.getParentId());
				deptMap.put("name", dept.getDeptName());
				deptMap.put("title", dept.getDeptName());
				if (isCheck) {
					deptMap.put("checked", roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
				} else {
					deptMap.put("checked", false);
				}
				trees.add(deptMap);
			}
		}
		return trees;
	}

	/**
	 * 查询部门人数
	 * 
	 * @param parentId
	 *            部门ID
	 * @return 结果
	 */
	@Override
	public int selectDeptCount(String parentId) {
		SysDept dept = new SysDept();
		dept.setParentId(parentId);
		return deptMapper.selectDeptCount(dept);
	}

	/**
	 * 查询部门是否存在用户
	 * 
	 * @param deptId
	 *            部门ID
	 * @return 结果 true 存在 false 不存在
	 */
	@Override
	public boolean checkDeptExistUser(String deptId) {
		int result = deptMapper.checkDeptExistUser(deptId);
		return result > 0 ? true : false;
	}

	/**
	 * 删除部门管理信息
	 * 
	 * @param deptId
	 *            部门ID
	 * @return 结果
	 */
	@Override
	public int deleteDeptById(String deptId) {
		return deptMapper.deleteDeptById(deptId);
	}

	/**
	 * 新增保存部门信息
	 * 
	 * @param dept
	 *            部门信息
	 * @return 结果
	 */
	@Override
	public int insertDept(SysDept dept) {
		SysDept info = deptMapper.selectDeptById(dept.getParentId());
		dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
		return deptMapper.insertDept(dept);
	}

	/**
	 * 修改保存部门信息
	 * 
	 * @param dept
	 *            部门信息
	 * @return 结果
	 */
	@Override
	public int updateDept(SysDept dept) {
		SysDept info = deptMapper.selectDeptById(dept.getParentId());
		if (StringUtils.isNotNull(info)) {
			String ancestors = info.getAncestors() + "," + dept.getParentId();
			dept.setAncestors(ancestors);
			updateDeptChildren(dept.getDeptId(), ancestors);
		}
		return deptMapper.updateDept(dept);
	}

	/**
	 * 修改子元素关系
	 * 
	 * @param deptId
	 *            部门ID
	 * @param ancestors
	 *            元素列表
	 */
	public void updateDeptChildren(String deptId, String ancestors) {
		SysDept dept = new SysDept();
		dept.setParentId(deptId);
		List<SysDept> childrens = deptMapper.selectDeptList(dept);
		for (SysDept children : childrens) {
			children.setAncestors(ancestors + "," + dept.getParentId());
		}
		if (childrens.size() > 0) {
			deptMapper.updateDeptChildren(childrens);
		}
	}

	/**
	 * 根据部门ID查询信息
	 * 
	 * @param deptId
	 *            部门ID
	 * @return 部门信息
	 */
	@Override
	public SysDept selectDeptById(String deptId) {
		return deptMapper.selectDeptById(deptId);
	}

	/**
	 * 校验部门名称是否唯一
	 * 
	 * @param dept
	 *            部门信息
	 * @return 结果
	 */
	@Override
	public String checkDeptNameUnique(SysDept dept) {
		if(StringUtils.isNotBlank(dept.getDeptId())) {
//			String deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
			SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
			if (StringUtils.isNotNull(info) && ! dept.getDeptId().equals(info.getDeptId())) {
				return UserConstants.DEPT_NAME_NOT_UNIQUE;
			}
		}
		return UserConstants.DEPT_NAME_UNIQUE;
	}
}
