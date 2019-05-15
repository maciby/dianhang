package com.dianhang.oa.mapper;

import java.util.List;

import com.dianhang.oa.domain.Customer;

public interface CustomerMapper {

    List<Customer> selectCustomerAll();

	List<Customer> selectCustomerList(Customer customer);

	Customer selectCustomerById(String customerId);

}
