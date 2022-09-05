package com.example.springboot.learner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deptId;

    private String deptName;
    private String deptAddress;
    private String deptCode;

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddress() {
        return this.deptAddress;
    }

    public void setDeptAddress(String deptAddress) {
        this.deptAddress = deptAddress;
    }

    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Department() {
    }

    public Department(Long deptId, String deptName, String deptAddress, String deptCode) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptAddress = deptAddress;
        this.deptCode = deptCode;
    }

    public Department(Long departmentID, String string) {
    }

    @Override
    public String toString() {
        return "{" +
                " deptId='" + getDeptId() + "'" +
                ", deptName='" + getDeptName() + "'" +
                ", deptAddress='" + getDeptAddress() + "'" +
                ", deptCode='" + getDeptCode() + "'" +
                "}";
    }

}
