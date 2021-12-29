package com.developsjb.trackwork.backend.controllers;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.developsjb.trackwork.backend.entity.Employee;
import com.developsjb.trackwork.backend.models.services.IEmployeeService;
import com.developsjb.trackwork.backend.models.services.IUploadFileService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class EmployeeRestController {
	
	@Autowired
	private IEmployeeService employeeSvc;
	
	@Autowired
	private IUploadFileService uploadService;
	
	private final Logger log = LoggerFactory.getLogger(EmployeeRestController.class);
	
	@GetMapping("/employees")
	public List<Employee> index(){
		return employeeSvc.findAll();
	}
	
	@GetMapping("/employees/page/{page}")
	public Page<Employee> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 5);
		return employeeSvc.findAll(pageable);
	}
	
	
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<?> showEmployee(@PathVariable Long id){
		Employee employee = null;
		Map<String, Object> response = new HashMap<>();
		//forma de poder manejar errores con el try errores en la bdd y el if vigila lo que devuelve la bbdd
		//creamos el response para obtener una map de los errores.
		try {
			employee = employeeSvc.findById(id);
		} catch(DataAccessException e) {
			response.put("message", "Error during the query");
			response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(employee == null) {
			response.put("message", "Employee ID: ".concat(id.toString().concat("There is not registers in the data  base")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}
	
	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee, BindingResult result) {
		Employee newEmployee = null;
		Map<String, Object>response = new HashMap<>();
		
		/*
		 * Forma antes de jdk 8 de validar los datos de un form.
		 * if(result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			for(FieldError err: result.getFieldErrors()) {
				errors.add("El campo" +  "'" + err.getField() + "'" +err.getDefaultMessage());
			}
			response.put("mensaje", "");
			response.put("Empleado", newEmployee);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}*/
		
		//Forma de validar después de jdk 8 y mejor
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo " +  "' " + err.getField() + " ' " +err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			 newEmployee =  employeeSvc.save(employee);
		}
		catch(DataAccessException e){
			response.put("message", "Error during the insert");
			response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El empleado ha sido creado con exito");
		response.put("Empleado", newEmployee);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/employees/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Employee employee) {
		Employee currentEmployee = employeeSvc.findById(id);
		Employee updateEmployee = null;
		
		Map<String, Object>response = new HashMap<>();

		if(currentEmployee == null) {
			response.put("message", "Employee Id: ".concat(id.toString().concat("There is not register with this Id, it can not be updated")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
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
		updateEmployee = employeeSvc.save(currentEmployee);
		}catch(DataAccessException e) {
			response.put("message", "Error during the update");
			response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The employee has been updated successfully");
		response.put("Empleado", updateEmployee);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object>response = new HashMap<>();
		try {
			Employee employee = employeeSvc.findById(id);
			String lastNamePhoto = employee.getPhoto();
			uploadService.deletePhoto(lastNamePhoto);
			employeeSvc.delete(id);
		}catch(DataAccessException e) {
			response.put("message", "Error removing employee");
			response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The employee has been removed successfully");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	@PostMapping("/employees/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,@RequestParam("id") Long id){
		Map<String, Object>response = new HashMap<>();
		Employee employee = employeeSvc.findById(id);
		
		if(!file.isEmpty()) {
			String nameFile = null;
			try {
				nameFile = uploadService.copy(file);
			} catch (IOException e) {
				response.put("message", "Error uploading image");
				response.put("Error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nameLastPhoto = employee.getPhoto();
			
			uploadService.deletePhoto(nameLastPhoto);
			
			employee.setPhoto(nameFile);
			employeeSvc.save(employee);
			response.put("employee", employee);
			response.put("message", "La imagen ha sido subida con éxito.");
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/upload/img/{namePhoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String namePhoto){
		
		Resource resource = null;
		
		try {
			resource = uploadService.load(namePhoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filaname=\""+ resource.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(resource,header, HttpStatus.OK);
	}
}
