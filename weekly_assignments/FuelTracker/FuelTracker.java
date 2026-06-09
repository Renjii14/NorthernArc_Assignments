package FuelTracker;

public class FuelTracker {
    double fuelCapacity;
    double milage;
    double currentFuelAmount;

    public FuelTracker(double fuelCapacity,double milage,double currentFuelAmount){
        this.currentFuelAmount=currentFuelAmount;
        this.fuelCapacity=fuelCapacity;
        this.milage=milage;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public double getMilage() {
        return milage;
    }

    public void setMilage(double milage) {
        this.milage = milage;
    }

    public double getCurrentFuelAmount() {
        return currentFuelAmount;
    }

    public void setCurrentFuelAmount(double currentFuelAmount) {
        this.currentFuelAmount = currentFuelAmount;
    }

    public void fillFuel(double amount){
        if(amount>fuelCapacity){
            System.out.println("More than capacity: Not Possible");
        }
        else {
            currentFuelAmount+=amount;
        }
        System.out.println("Filled fuel successfully");
    }

    public void checkFuel(){
        System.out.println(currentFuelAmount);
    }
    public void drive(double km){
        double fuelUsed = km / milage;
        if(fuelUsed>currentFuelAmount){
            System.out.println("Fuel less.");
        }
        else{
            currentFuelAmount=currentFuelAmount-fuelUsed;
        }
        System.out.println("Driven: "+km+" Fuel used: "+fuelUsed+ " Balance fuel: "+currentFuelAmount);

    }
}
