package demo3;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;


@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MacBook implements Laptop{

    @Override
    public void model() {
        System.out.println("I am macbook");
    }
}
