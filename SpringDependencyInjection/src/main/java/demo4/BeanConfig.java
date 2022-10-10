package demo4;

import demo3.Dell;
import demo3.Laptop;
import demo3.MacBook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//We can avoid using XML like spring.xml to configure bean objects. This is done using the Configuration annotation.


@Configuration
@ComponentScan(basePackages="demo3")
public class BeanConfig {

    @Bean
    public Laptop getLaptop(){

        MacBook macBook = new MacBook();
        System.out.println(macBook.toString());
       return macBook;
    }

}
