package com.hegde.springbootlearning.batch;

import com.hegde.springbootlearning.model.Employee;
import com.hegde.springbootlearning.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Employee> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends Employee> list) throws Exception {

        System.out.println("Data saved for users "+list);
        userRepository.saveAll(list);

    }
}
