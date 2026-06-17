package entity;

public class Customer {

    private int customerId;
    private String customerName;
    private String city;
    private String mobileNumber;
    private String email;

    public Customer() {
    }

    public Customer(int customerId, String customerName,
                    String city, String mobileNumber,
                    String email) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.city = city;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", customerName='" + customerName + '\'' + ", city='" + city + '\'' + ", mobileNumber='" + mobileNumber + '\'' + ", email='" + email + '\'' + '}';
    }
}