package com.developsjb.trackwork.backend.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.developsjb.trackwork.backend.entity.Employee;

public interface IEmployeeDao extends CrudRepository<Employee, Long> {

}
