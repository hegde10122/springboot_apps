package demo3;

import org.springframework.stereotype.Component;

@Component //adding the annotation and asking spring to create a bean for us
public class Acer implements Laptop{
    @Override
    public void model() {
        System.out.println("I am Acer");
    }
}
