package aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {

    @Before("execution(* aop.ShoppingCart.checkout(..))")
    public void beforelogger(JoinPoint jp){
        System.out.println(jp.getSignature());
        System.out.println("Before Loggers with argument"+jp.getArgs()[0].toString());
    }

    @After("execution(* aop.ShoppingCart.checkout(..))")
    public void afterlogger(){
        System.out.println("After Loggers");
    }

    @Pointcut("execution(* aop.ShoppingCart.quantity(..))")
    public void afterReturningPointCut(){}

    @AfterReturning(pointcut = "afterReturningPointCut()",returning = "retVal")
    public void afterReturning(int retVal){
        System.out.println("After returning "+retVal);
    }

}
