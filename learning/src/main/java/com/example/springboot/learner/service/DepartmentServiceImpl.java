package com.example.springboot.learner.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.learner.entity.Department;
import com.example.springboot.learner.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {

        return departmentRepository.save(department);
    }

    @Override
    public List<Department> fetchDepartmentList() {
        return departmentRepository.findAll();
    }

    @Override
    public Department fetchDepartmentByID(Long departmentID) {

        Optional<Department> dept = departmentRepository.findById(departmentID);

        if (dept.isPresent()) {
            return dept.get();
        } else
            return new Department(departmentID, "Not exist department ID --- " + departmentID);
    }

    @Override
    public void deleteDeptById(Long departmentID) {
        departmentRepository.deleteById(departmentID);

    }

    @Override
    public Department updateDepartment(Long departmentID, Department department) {

        Optional<Department> depDB = departmentRepository.findById(departmentID);

        if (depDB.isPresent()) {

            if (setterDept(department.getDeptAddress()))
                depDB.get().setDeptAddress(department.getDeptAddress());

            if (setterDept(department.getDeptCode()))
                depDB.get().setDeptCode(department.getDeptCode());

            if (setterDept(department.getDeptName()))
                depDB.get().setDeptName(department.getDeptName());

            return departmentRepository.save(depDB.get());
        }
        return new Department();
    }

    private boolean setterDept(String param) {
        return param != null && !param.isBlank() && !param.isEmpty();
    }

    @Override
    public List<Department> fetchDeptByName(String deptName) {
        return departmentRepository.findByDeptName(deptName);
    }

}
