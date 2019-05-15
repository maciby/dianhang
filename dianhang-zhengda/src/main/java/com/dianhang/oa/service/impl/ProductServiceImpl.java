package com.dianhang.oa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianhang.oa.domain.Product;
import com.dianhang.oa.mapper.ProductMapper;
import com.dianhang.oa.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询所有菜单
     * 
     * @return 菜单列表
     */
    @Override
    public List<Map<String, Object>> productTreeData() {
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        List<Product> menuList = productMapper.selectProductAll();
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
    public static List<Map<String, Object>> getTrees(List<Product> menuList, boolean isCheck, List<String> roleMenuList, boolean permsFlag) {
        List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
        for (Product menu : menuList) {
            Map<String, Object> deptMap = new HashMap<String, Object>();
            deptMap.put("id", menu.getProductId());
            deptMap.put("pId", menu.getParentId());
            deptMap.put("name", transMenuName(menu, roleMenuList, permsFlag));
            deptMap.put("title", menu.getProductName());
            deptMap.put("checked", false);
            trees.add(deptMap);
        }
        return trees;
    }

    public static String transMenuName(Product menu, List<String> roleMenuList, boolean permsFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getProductName());
        if (permsFlag) {
        }
        return sb.toString();
    }

}
