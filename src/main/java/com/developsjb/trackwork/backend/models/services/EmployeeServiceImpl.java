package com.developsjb.trackwork.backend.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.developsjb.trackwork.backend.entity.Employee;
import com.developsjb.trackwork.backend.models.dao.IEmployeeDao;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	
	@Autowired
	private IEmployeeDao employeeDao;
	
	@Override
	@Transactional(readOnly=true) 
	public List<Employee> findAll() {
		return (List<Employee>) employeeDao.findAll();
	}

}
