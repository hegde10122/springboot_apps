package demo2;

public class EmailServiceInjector implements MessageServiceInjector
{
    @Override
    public Consumer getConsumer() {
        return new OurApplication(new EmailServiceImpl());
    }
}
