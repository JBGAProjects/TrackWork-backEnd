package com.developsjb.trackwork.backend.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.developsjb.trackwork.backend.entity.Employee;

public interface IEmployeeService {
	
	public List<Employee> findAll();
	
	public Page<Employee> findAll(Pageable pageable);
	
	public Employee findById(Long id);
	
	public Employee save(Employee employee);
	
	public void delete(Long id);

}
