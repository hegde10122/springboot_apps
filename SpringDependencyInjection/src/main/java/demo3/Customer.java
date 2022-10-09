package demo3;

public class Customer{

    //Address is a collaborator class
    private Address address;

    private String myRoad;

    public String getMyRoad() {
        return myRoad;
    }

    public void setMyRoad(String myRoad) {
        this.myRoad = myRoad;
    }

    public Customer() {
    }

    public Customer(String myRoad) {
        this.myRoad = myRoad;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Customer(Address address) {
        this.address = address;
    }
    public void getStreet(){
        System.out.println("Streets");
    }
}

