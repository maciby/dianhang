package com.dianhang.oa.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianhang.framework.web.BaseController;
import com.dianhang.framework.web.TableDataInfo;
import com.dianhang.oa.domain.Customer;
import com.dianhang.oa.service.ICustomerService;
import com.dianhang.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/oa/customer")
@Slf4j
public class CustomerController extends BaseController {

	private String prefix = "oa/customer";

	@Autowired
	private ICustomerService customerService;

	/**
	 * 
	 * @return
	 */
	@RequiresPermissions("customer:list")
	@GetMapping("/list")
	public String list() {
		return prefix + "/list";
	}

	@RequiresPermissions("customer:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Customer contract) {
		startPage();
		List<Customer> list = customerService.selectCustomerList(contract);
		return getDataTable(list);
	}

	/**
	 * 查看合同信息
	 */
	@GetMapping("/detail/{customerId}")
	public String detail(@PathVariable("customerId") String customerId, ModelMap mmap) {
		mmap.put("customer", customerService.getCustomer(customerId));
		return prefix + "/detail";
	}

	/**
	 * 选择菜单树
	 */
	@GetMapping("/selectCustomerTree")
	public String selectCustomerTree() {
		return prefix + "/tree";
	}

	/**
	 * 加载所有客户列表树
	 */
	@GetMapping("/customerTreeData")
	@ResponseBody
	public List<Map<String, Object>> customerTreeData(Customer customer) {

		if (null != customer) {
			if (StringUtils.isNotBlank(customer.getParentId())) {
				customer.setParentId("00001" + customer.getParentId());
			}
		}
		log.error(customer.toString());
		List<Map<String, Object>> tree = customerService.customerTreeData(customer);
		return tree;
	}
}
