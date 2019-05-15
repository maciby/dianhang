package com.dianhang.oa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianhang.oa.domain.Customer;
import com.dianhang.oa.mapper.CustomerMapper;
import com.dianhang.oa.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查询所有菜单
     * 
     * @return 菜单列表
     */
    @Override
    public List<Map<String, Object>> customerTreeData(Customer customer) {
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        List<Customer> menuList = customerMapper.selectCustomerList(customer);
        trees = getTrees(menuList, false, null, false);
        return trees;
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList
     *            菜单列表
     * @param isCheck
     *            是否需要选中
     * @param roleMenuList
     *            角色已存在菜单列表
     * @param permsFlag
     *            是否需要显示权限标识
     * @return
     */
    public static List<Map<String, Object>> getTrees(List<Customer> menuList, boolean isCheck, List<String> roleMenuList, boolean permsFlag) {
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        for (Customer menu : menuList) {
            Map<String, Object> deptMap = new HashMap<String, Object>();
            deptMap.put("id", menu.getCustomerId());
            deptMap.put("pId", menu.getParentId());
            deptMap.put("name", transMenuName(menu, roleMenuList, permsFlag));
            deptMap.put("title", menu.getCustomerName());
            deptMap.put("linkman", menu.getLinkman());
            deptMap.put("linktel", menu.getLinktel());
            // if (isCheck) {
            // deptMap.put("checked", roleMenuList.contains(menu.getMenuId() +
            // menu.getPerms()));
            // } else {
            deptMap.put("checked", false);
            // }
            trees.add(deptMap);
        }
        return trees;
    }

    public static String transMenuName(Customer menu, List<String> roleMenuList, boolean permsFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getCustomerName());
        if (permsFlag) {
            // sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() +
            // "</font>");
        }
        return sb.toString();
    }

	@Override
	public List<Customer> selectCustomerList(Customer customer) {
		return customerMapper.selectCustomerList(customer);
	}

	@Override
	public Customer getCustomer(String customerId) {
		return customerMapper.selectCustomerById(customerId);
	}

}
