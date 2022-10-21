package com.hegde.springbootjdbc.demo.json;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class Employee {

    private int id;
    private String name;
    private int age;
    private BigDecimal salary;
    private String designation;
    private Address address;
    private long[] phoneNumbers;
    private Map<String, String> personalInformation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(long[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Map<String, String> getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(Map<String, String> personalInformation) {
        this.personalInformation = personalInformation;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                ", designation='" + designation + '\'' +
                ", address=" + address +
                ", phoneNumbers=" + Arrays.toString(phoneNumbers) +
                ", personalInformation=" + personalInformation +
                '}';
    }
}
