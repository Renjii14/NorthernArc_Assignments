package org.northernarc.jpa.model;

public class FixedDeposit {

    private int fdId;
    private String customerName;
    private double depositAmount;
    private double interestRate;
    private int tenureMonths;

    public FixedDeposit() {
    }

    public FixedDeposit(int fdId, String customerName, double depositAmount, double interestRate, int tenureMonths) {
        this.fdId = fdId;
        this.customerName = customerName;
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }
    public FixedDeposit(String customerName, double depositAmount, double interestRate, int tenureMonths) {
        this.customerName = customerName;
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    public int getFdId() {
        return fdId;
    }

    public void setFdId(int fdId) {
        this.fdId = fdId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    @Override
    public String toString() {
        return "FixedDeposit{" + "fdId=" + fdId + ", customerName='" + customerName + '\'' + ", depositAmount=" + depositAmount + ", interestRate=" + interestRate + ", tenureMonths=" + tenureMonths + '}';
    }
}