package demo2;

public class SMSServiceInjector implements  MessageServiceInjector{
    @Override
    public Consumer getConsumer() {
        return new OurApplication(new SMSServiceImpl());
    }
}
