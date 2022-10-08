package demo2;

public class EmailServiceInjectorSetter implements MessageServiceInjector{
    @Override
    public Consumer getConsumer() {
        OurApplicationSetter applicationSetter = new OurApplicationSetter();
        applicationSetter.setService(new EmailServiceImpl());
        return applicationSetter;
    }
}
