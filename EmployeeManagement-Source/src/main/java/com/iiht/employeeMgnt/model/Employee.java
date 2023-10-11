package com.iiht.employeeMgnt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Employee {
    @Id
    private String id;
    private String name;
    private int age;
    private String phoneNumber;
    private String designation;
    //private Date dateOfJoin;
}
