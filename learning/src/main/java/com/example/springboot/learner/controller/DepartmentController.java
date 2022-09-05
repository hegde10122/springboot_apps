package com.example.springboot.learner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.springboot.learner.entity.Department;
import com.example.springboot.learner.service.DepartmentService;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/departments")
    // check this on the browser url ---- http://localhost:8082/departments
    public Department saveDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    // fetch list of all department records in the db table
    @GetMapping("/departments")
    public List<Department> fetchDepartmentList() {
        return departmentService.fetchDepartmentList();

    }

    // fetch the particular department record using id as a PATH variable
    @GetMapping("/departments/{id}")
    public Department fetchDeptById(@PathVariable("id") Long departmentID) {
        return departmentService.fetchDepartmentByID(departmentID);
    }

    // Delete by ID
    @DeleteMapping("departments/{id}")
    public String deleteDeptbyId(@PathVariable("id") Long departmentID) {
        departmentService.deleteDeptById(departmentID);
        return "Deletion done";
    }

    // Update by ID
    @PutMapping("departments/{id}")
    public Department updateDepartment(@PathVariable("id") Long departmentID, @RequestBody Department department) {
        return departmentService.updateDepartment(departmentID, department);
    }

    @GetMapping("/departments/name/{name}")
    public List<Department> fetchDeptByName(@PathVariable("name") String deptName) {
        return departmentService.fetchDeptByName(deptName);
    }

}
