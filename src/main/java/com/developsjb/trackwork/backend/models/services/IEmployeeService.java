package com.developsjb.trackwork.backend.models.services;

import java.util.List;

import com.developsjb.trackwork.backend.entity.Employee;

public interface IEmployeeService {
	
	public List<Employee> findAll();
	
	public Employee findById(Long id);
	
	public Employee save(Employee employee);
	
	public void delete(Long id);

}
