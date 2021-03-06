package com.dianhang.framework.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dianhang.core.config.Global;
import com.dianhang.framework.web.BaseController;
import com.dianhang.system.domain.SysMenu;
import com.dianhang.system.domain.SysUser;
import com.dianhang.system.service.ISysMenuService;
//import com.ruoyi.system.domain.SysMenu;
//import com.ruoyi.system.service.ISysMenuService;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController {
	
	@Autowired
	private ISysMenuService menuService;

	// 系统首页
	@GetMapping("/index")
	public String index(ModelMap mmap) {
		// 取身份信息
		SysUser user = getSysUser();
		// 根据用户id取出菜单
		List<SysMenu> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("copyrightYear", Global.getCopyrightYear());
		return "index";
	}

	// 系统介绍
	@GetMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.put("version", Global.getVersion());
		return "main";
	}
}
