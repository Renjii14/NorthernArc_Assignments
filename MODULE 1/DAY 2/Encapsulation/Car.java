public class Car{
    private String make;
    private String model;
    private int year;

    public String getmake(String make){
        return make;
    }
    public String model(String model){
        return model;
    }
    public int year(int year){
        return year;
    }

    public Car(String make,String model,int year){
        this.make=make;
        this.model=model;
        this.year=year;
    }

    public void start(){
        System.out.println("I will start my car today at 5pm.");
         }

         public void getDetails(){
            System.out.println("MAKE: "+make+"\nMODEL: "+model+"\nYEAR: "+year);
         }

         public void stop(){
            System.out.println("I will stop the car probably by 9pm.");
         }
}