package com.dianhang.oa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianhang.framework.web.BaseController;
import com.dianhang.oa.service.IProductService;

@Controller
@RequestMapping("/oa/product")
public class ProductController extends BaseController {

    private String prefix = "oa/product";

    @Autowired
    private IProductService productService;

    /**
     * 选择菜单树
     */
    @GetMapping("/selectProductTree")
    public String selectProductTree() {
        return prefix + "/tree";
    }

    /**
     * 加载所有客户列表树
     */
    @GetMapping("/productTreeData")
    @ResponseBody
    public List<Map<String, Object>> productTreeData() {
        List<Map<String, Object>> tree = productService.productTreeData();
        return tree;
    }
}
