package com.example.springboot.learner.service;

import java.util.List;

import com.example.springboot.learner.entity.Department;

public interface DepartmentService {

    public Department saveDepartment(Department department);

    public List<Department> fetchDepartmentList();

    public Department fetchDepartmentByID(Long departmentID);

    public void deleteDeptById(Long departmentID);

    public Department updateDepartment(Long departmentID, Department department);

    public List<Department> fetchDeptByName(String deptName);
}
