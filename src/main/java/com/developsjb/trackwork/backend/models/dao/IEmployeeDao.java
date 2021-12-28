package com.developsjb.trackwork.backend.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developsjb.trackwork.backend.entity.Employee;

public interface IEmployeeDao extends JpaRepository<Employee, Long> {

}
