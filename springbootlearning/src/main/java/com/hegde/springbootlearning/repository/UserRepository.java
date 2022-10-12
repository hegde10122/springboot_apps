package com.hegde.springbootlearning.repository;

import com.hegde.springbootlearning.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Employee,Integer> {



}
