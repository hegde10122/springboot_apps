package demo4;

import demo3.Laptop;
import demo3.MacBook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        //We asre not using the xml for bean configuration but using the config class
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        //we are calling the bean defined using the @Bean annotation whose return type is Laptop object--It will create new macbook object as Laptop
        Laptop macbook = context.getBean(MacBook.class);
        macbook.model();
    }
}
