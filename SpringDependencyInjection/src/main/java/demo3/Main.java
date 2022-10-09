package demo3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

       ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Car car = context.getBean(Car.class);
        car.color();

        Address address = context.getBean(Address.class);
        address.bill();
        System.out.println(address.getStreet());

        Customer customer = (Customer) context.getBean("customer");
        customer.getStreet();
        Address ak = new Address("2 arthur rasta",12345600);
        customer.setAddress(ak);
        System.out.println(customer.getAddress().getPhoneNumber());
        System.out.println(customer.getMyRoad());

        Laptop dell = context.getBean(Dell.class);
        dell.model();

     Laptop acer = context.getBean(Acer.class);
     acer.model();


    }
}
