package demo1;

public class OurApplication {

    private SMSService smsService = new SMSService();

    protected void sendMessage(String mobileNumber,String message){
        smsService.sendSMS(message,mobileNumber);
    }
}
