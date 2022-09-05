package com.example.springboot.learner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.learner.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // public Department findByDeptName(String deptName);

    public List<Department> findByDeptName(String deptName);

}
