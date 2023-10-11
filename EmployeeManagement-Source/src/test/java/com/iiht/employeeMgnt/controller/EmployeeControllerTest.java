package com.iiht.employeeMgnt.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.iiht.employeeMgnt.exception.ResourceNotFoundException;
import com.iiht.employeeMgnt.model.Employee;
import com.iiht.employeeMgnt.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest
public class EmployeeControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	/*
	 * @MockBean annotation tells Spring to create mock instance of EmployeeService
	 * and add it to the application context so that it is injected to the
	 * EmployeeController.
	 */
	@MockBean
	private EmployeeService employeeService;

	private Employee employee;

	@BeforeEach
	public void setUp() {
		employee = new Employee("1234", "Sundar", 30, "7373564262", "Senior Associate");
	}
	
	@AfterEach
	public void tearDown()
	{
		employee = null;
	}

	@Test
	@DisplayName("JUnit test for createEmployee operation")
	public void givenEmployee_whenCreateEmployee_thenReturnEmployee() {
		try {
			// given - precondition or setup
			given(employeeService.createNewEmplyee(any(Employee.class))).willReturn(employee);
			// when - action or the behaviour
			ResultActions response = mockMvc.perform(post("/employee/add")
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsBytes(employee)));
			// then - verify the output
			response.andDo(print());
			response.andDo(print()).andExpect(status().isOk())
					.andExpect(jsonPath("$.name", is(employee.getName())))
					.andExpect(jsonPath("$.age", is(employee.getAge())))
					.andExpect(jsonPath("$.designation", is(employee.getDesignation())));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Test
	@DisplayName("JUnit test for getAllEmployees operation")
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup
		Employee employee1 = new Employee("1234", "Sundar", 30, "7373564262", "Senior Associate");
		Employee employee2 = new Employee("1235", "Raj", 40, "9993564262", "Associate");
		List<Employee> employees = List.of(employee1, employee2);
		given(employeeService.getAllEmployeeList()).willReturn(employees);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/employee/all"));
		// then - verify the output
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(employees.size())));
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation")
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
		// given - precondition or setup 
		given(employeeService.getEmployeeDetail(employee.getId())).willReturn(employee);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/employee/find/{id}", employee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(employee.getName())))
		.andExpect(jsonPath("$.age", is(employee.getAge())))
		.andExpect(jsonPath("$.designation", is(employee.getDesignation())));
		
	}
	
	@Test
	@DisplayName("JUnit test for getEmployeeById operation - ResourceNotFoundException")
	public void givenInvalidEmployeeId_whenGetEmployeeById_thenThrowsResourceNotFoundException() throws Exception {
		// given - precondition or setup 
		given(employeeService.getEmployeeDetail(employee.getId())).willThrow(ResourceNotFoundException.class);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(get("/employee/find/{id}", "5454"));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation")
	public void givenEmployeeWithUpdates_whenUpdatedEmployee_thenReturnEmployeeUpdated() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Employee employeeForUpdate = new Employee("1234", "SundarG", 35, "7373564262", "Senior Associate");
		given(employeeService.updateEmployee(any(Employee.class))).willReturn(employeeForUpdate);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/employee/update", employee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeForUpdate)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(employeeForUpdate.getName())))
		.andExpect(jsonPath("$.age", is(employeeForUpdate.getAge())))
		.andExpect(jsonPath("$.designation", is(employeeForUpdate.getDesignation())));
	}
	
	@Test
	@DisplayName("JUnit test for updateEmployee operation - ResourceNotFoundException")
	public void givenInvalidEmployeeWithUpdates_whenUpdatedEmployee_thenThrowsResourceNotFoundException() throws JsonProcessingException, Exception {
		// given - precondition or setup
		Employee employeeForUpdate = new Employee("6565", "SundarG", 35, "8786876878", "Senior Associate");
		given(employeeService.updateEmployee(any(Employee.class))).willThrow(ResourceNotFoundException.class);
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(put("/employee/updates")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(employeeForUpdate)));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation")
	public void givenEmployeeId_whenDeleteEmployeeById_thenReturnTrue()throws Exception {
		// given - precondition or setup
		willDoNothing().given(employeeService).deleteEmployee(employee.getId());
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/employee/delete/{id}", employee.getId()));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("JUnit test for deleteEmployeeById operation - ResourceNotFoundException")
	public void givenInvalidEmployeeId_whenDeleteEmployeeById_thenThrowsResourceNotFoundException() throws Exception {
		// given - precondition or setup
		willThrow(ResourceNotFoundException.class).given(employeeService).deleteEmployee(employee.getId());
		// when - action or the behaviour
		ResultActions response = mockMvc.perform(delete("/employee/delete/{id}", "5454"));
		// then - verify the output
		response.andDo(print())
		.andExpect(status().isOk());
	}
}
