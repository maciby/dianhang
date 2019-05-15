package com.dianhang.oa.service;

import java.util.List;
import java.util.Map;

import com.dianhang.oa.domain.Customer;

public interface ICustomerService {

	List<Map<String, Object>> customerTreeData(Customer customer);

	List<Customer> selectCustomerList(Customer customer);

	Customer getCustomer(String customerId);

}
