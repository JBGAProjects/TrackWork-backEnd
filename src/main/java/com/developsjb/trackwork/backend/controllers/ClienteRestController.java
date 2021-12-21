package com.developsjb.trackwork.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.developsjb.trackwork.backend.entity.Employee;
import com.developsjb.trackwork.backend.models.services.IEmployeeService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IEmployeeService employeeSvc;
	
	@GetMapping("/employees")
	public List<Employee> index(){
		return employeeSvc.findAll();
	}
	
	
	@GetMapping("/employees/{id}")
	public Employee showEmployee(@PathVariable Long id){
		return employeeSvc.findById(id);
	}
	
	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeSvc.save(employee);
	}
	
	@PutMapping("/employees/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee update(@PathVariable Long id, @RequestBody Employee employee) {
		Employee currentEmployee = employeeSvc.findById(id);
		
		currentEmployee.setName(employee.getName());
		currentEmployee.setFirstSurname(employee.getFirstSurname());
		currentEmployee.setSecondSurname(employee.getSecondSurname());
		currentEmployee.setEmail(employee.getEmail());
		currentEmployee.setPhone(employee.getPhone());
		currentEmployee.setDni(employee.getDni());
		currentEmployee.setAvailable(employee.isAvailable());
		currentEmployee.setStudies(employee.getStudies());
		currentEmployee.setEmail(employee.getEmail());
		currentEmployee.setAddress(employee.getAddress());
		
		return employeeSvc.save(currentEmployee);
	}
	
	@DeleteMapping("/employees/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		employeeSvc.delete(id);
	}

}
