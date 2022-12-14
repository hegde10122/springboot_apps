package com.hegde.springbootlearning.batch;

import com.hegde.springbootlearning.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<Employee, Employee> {

private static final Map<String,String> DEPT_NAMES = new HashMap<>();

public Processor(){
    DEPT_NAMES.put("001","Technology");
    DEPT_NAMES.put("002","Operations");
    DEPT_NAMES.put("003","Accounts");
}

    @Override
    public Employee process(Employee employee) throws Exception {

    String deptCode = employee.getDept();
    String dept = DEPT_NAMES.get(deptCode);
    employee.setDept(dept);
        System.out.println(String.format("Converted from [%s] to [%s]",deptCode,dept));
        return employee;
    }
}
