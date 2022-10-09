package demo3;

public class Address {
    private String street;
    private int phoneNumber;

    public Address(String street, int number) {
        this.street = street;
        this.phoneNumber = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

