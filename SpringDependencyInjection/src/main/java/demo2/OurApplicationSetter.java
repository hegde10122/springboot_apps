package demo2;

public class OurApplicationSetter implements  Consumer{

    private MessageService service;

    public OurApplicationSetter(){

    }
    @Override
    public void processMessages(String message, String receiver) {
        service.sendMessage(message,receiver);
    }

    //setter dependency injection
    public void setService(MessageService service) {
        this.service = service;

    }
}
