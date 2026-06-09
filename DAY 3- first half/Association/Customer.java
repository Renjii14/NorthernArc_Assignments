package Association;

public class Customer {

    private String customerName;
    private String customerId;

    public Customer(String customerName, String customerId) {
        this.customerName = customerName;
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "customerName=" + customerName +
                ", customerId=" + customerId;
    }
}