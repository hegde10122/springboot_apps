package com.hegde.springbootlearning.repository;

import com.hegde.springbootlearning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {



}
