package aop;

import org.springframework.stereotype.Component;

@Component
public class ShoppingCart {

    public void checkout(String status){
        System.out.println("Checkout method !!");
    }


    public int quantity(){
        return 200;
    }
}
