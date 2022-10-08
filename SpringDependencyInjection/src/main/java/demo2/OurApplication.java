package demo2;

public class OurApplication implements Consumer {

    private MessageService service;

    public OurApplication(MessageService service) {
        this.service = service;
    }

    @Override
    public void processMessages(String message, String receiver) {
        service.sendMessage(message,receiver);
    }
}


/*
  OurApplication class is just using the service.
  It does not initialize the MessageService that leads to better "separation of concerns".
  Also use of MessageService interface allows us to easily test the application by mocking
  the MessageService and bind the services at runtime rather than compile time.
  Now we will write java dependency injector classes that will initialize the service and also consumer classes.

  */