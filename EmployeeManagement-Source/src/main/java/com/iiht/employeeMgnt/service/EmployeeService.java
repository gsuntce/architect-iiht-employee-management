package com.iiht.employeeMgnt.service;

import com.iiht.employeeMgnt.exception.ResourceNotFoundException;
import com.iiht.employeeMgnt.model.Employee;
import com.iiht.employeeMgnt.repo.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;


    public Employee createNewEmplyee(Employee employee) throws Exception {
        //allow whitespace, dots, hyphens (-) between the numbers
        Pattern pattern = Pattern.compile("^(\\d{3}[- .]?){2}\\d{4}$");
        Matcher matcher = pattern.matcher(employee.getPhoneNumber());
        if(matcher.matches()) {
           employee = employeeRepository.insert(employee);
           log.info("Employee created successfully");
           return employee;
        } else{
            throw new Exception("Invalid Phone number");
        }
    }

    public List<Employee> getAllEmployeeList() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeDetail(String id) {
       return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id",id));
    }

    public Employee updateEmployee(Employee employee) {
        employeeRepository.findById(employee.getId()).orElseThrow(() -> new ResourceNotFoundException("Employee","id", "employee.getId()"));
        employee = employeeRepository.save(employee);
        return employee;
    }

    public void deleteEmployee(String id) {
        employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee","id",id));
        employeeRepository.deleteById(id);
       // return "Deleted";
    }
}
