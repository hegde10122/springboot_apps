package demo2;

//Java Dependency Injection - Service Component
//dependency injection java service for sending email
public class EmailServiceImpl implements MessageService
{
    @Override
    public void sendMessage(String message, String receiver) {
        System.out.println("Email sent to "+receiver+ " and the message is "+message);
    }
}
