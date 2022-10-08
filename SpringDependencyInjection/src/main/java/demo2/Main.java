package demo2;

public class Main {

    public static void main(String[] args) {
        String message = "Ganesh Hegde,Dependency injection works !!";
        String email = "ganesh@ganesh.com";
        String mobileNumber = "1234567890";
        MessageServiceInjector injector;
        Consumer app;

        //Send email
        injector = new EmailServiceInjector();
        app = injector.getConsumer();
        app.processMessages(message, email);

        //Send SMS
        injector = new SMSServiceInjector();
        app = injector.getConsumer();
        app.processMessages(message, mobileNumber);

        //Send email
        injector = new EmailServiceInjectorSetter();
        app = injector.getConsumer();
        app.processMessages(message, email);

        //Send SMS
        injector = new SMSServiceInjectorSetter();
        app = injector.getConsumer();
        app.processMessages(message, mobileNumber);
    }
}

/*
 Our application classes are responsible only for using the service.
 Service classes are created in injectors.
 Also if we have to further extend our application to allow whatsapp messaging, we will have to write Service classes and injector classes only.
 So dependency injection implementation solved the problem with hard-coded dependency and helped us in making our application flexible and easy to extend.
 Now let’s see how easily we can test our application class by mocking the injector and service classes.
* */

/*
 benefits of using Dependency Injection in Java are:

Separation of Concerns
Boilerplate Code reduction in application classes because all work to initialize dependencies is handled by the injector component
Configurable components makes application easily extendable
Unit testing is easy with mock objects

Disadvantages :

If overused, it can lead to maintenance issues because the effect of changes are known at runtime.
Dependency injection in java hides the service class dependencies that can lead to runtime errors that would have been caught at compile time.

* */