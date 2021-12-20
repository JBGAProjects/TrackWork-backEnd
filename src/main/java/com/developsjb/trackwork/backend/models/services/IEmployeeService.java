package com.developsjb.trackwork.backend.models.services;

import java.util.List;

import com.developsjb.trackwork.backend.entity.Employee;

public interface IEmployeeService {
	
	public List<Employee> findAll();

}
