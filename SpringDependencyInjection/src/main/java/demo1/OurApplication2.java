package demo1;

public class OurApplication2 {

    private SMSService sms = null;

    public OurApplication2(SMSService sms) {
        this.sms = sms;
    }

    public void processMessages(String message, String mobileNumber){
        //do some message validation and other manipulation logic here
         sms.sendSMS(message,mobileNumber);
    }
}

/*
* But in this case, we are asking client applications or test classes to initializing the sms service that is not a good design decision.
* */


/*
Dependency Injection in java requires at least the following:

1) Service components should be designed with base class or interface.
2) It’s better to prefer interfaces or abstract classes that would define contract for the services.
3) Consumer classes should be written in terms of service interface.
4) Injector classes that will initialize the services and then the consumer classes.

* */