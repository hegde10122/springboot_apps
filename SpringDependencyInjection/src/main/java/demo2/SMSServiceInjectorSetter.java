package demo2;

public class SMSServiceInjectorSetter implements MessageServiceInjector{
    @Override
    public Consumer getConsumer() {
        OurApplicationSetter applicationSetter = new OurApplicationSetter();
        applicationSetter.setService(new SMSServiceImpl());
        return applicationSetter;
    }
}
