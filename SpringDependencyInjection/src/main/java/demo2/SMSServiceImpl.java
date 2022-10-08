package demo2;

//Java Dependency Injection - Service Component
//dependency injection java service fpr sms
public class SMSServiceImpl implements MessageService{
    @Override
    public void sendMessage(String message, String receiver) {
        System.out.println("SMS sent to "+receiver+ " with message "+message);
    }
}
