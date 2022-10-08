package demo1;

public class Main {

    public static void main(String[] args) {

        OurApplication ourApplication = new OurApplication();
        ourApplication.sendMessage("1234567890","Hello SMS to you !!");

        SMSService smsService = new SMSService();

        OurApplication2 ourApplication2 = new OurApplication2(smsService);
        ourApplication2.processMessages("Hello SMS to you by second method --- constructor !!","1234567890");


    }
}


/*
 Dependency injection  - We will see how to use dependency injection pattern to achieve loose coupling and extendability in this demo application.
  Let’s say we have an application where we consume SMSService to send SMS.
* */


/*
 The above code has drawbacks

 OurApplication class is responsible to initialize the sms service before using it. This leads to hard-coded dependency.
 If we want to switch to some other advanced sms service in the future, then it will require code changes in OurApplication class.

 This makes our application hard to extend and if sms service is used in multiple classes then that would be even harder.

If we want to extend our application to provide an additional messaging feature, such as email or whatsapp message then we would need to write another application for that.
This will involve code changes in application classes and in client classes too.
Testing the application will be very difficult since our application is directly creating the sms service instance.
There is no way we can mock these objects in our test classes.

An argument to mitigate this problem is this --- We will remove the sms service instance creation from OurApplication class by having a constructor that requires sms service as an argument.
We will create OurApplication2 class to show this solition.
* */