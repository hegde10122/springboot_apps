package demo3;

import org.springframework.stereotype.Component;

@Component
public class Dell implements Laptop{
    @Override
    public void model() {
        System.out.println("I am Dell");
    }
}
