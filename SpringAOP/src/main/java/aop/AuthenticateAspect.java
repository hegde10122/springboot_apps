package aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticateAspect {

    //Pointcuts to be defined for packages---authenticate and authorise to be done ---- using within---for which type of methods
    //within aop.. ----> aop package---for any methods any class
    @Pointcut("within(aop..*)")
    public void authenticationPointCut(){

    }

    //similar to above this pointcut within the package ---any method of any class-----
    @Pointcut("within(aop..*)")
    public void authorizationPointCut(){

    }

    //before a method is called----we need to define the pointcuts
    @Before("authenticationPointCut() && authorizationPointCut()")
    public void authenticate(){
        System.out.println("Authenticate and authorise the request");
    }
}
