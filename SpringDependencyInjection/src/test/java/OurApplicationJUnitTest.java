import demo2.Consumer;
import demo2.MessageService;
import demo2.MessageServiceInjector;
import demo2.OurApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OurApplicationJUnitTest {

    private MessageServiceInjector injector;

    @Before
    public void setUp(){
        //mock the injector with anonymous class
        injector = new MessageServiceInjector() {

            @Override
            public Consumer getConsumer() {
                //mock the message service
                return new OurApplication(new MessageService() {

                    @Override
                    public void sendMessage(String message, String receiver) {
                        System.out.println("Mock Message Service implementation");

                    }
                });
            }
        };
    }

    @Test
    public void test() {
        Consumer consumer = injector.getConsumer();
        consumer.processMessages("Ganesh Hegde", "ganesh@hegde.com");
    }

    @After
    public void tear(){
        injector = null;
    }

}
/*
We have used anonymous classes to mock the injector and service classes for testing. We have used JUnit 4 for the above test class.
* */