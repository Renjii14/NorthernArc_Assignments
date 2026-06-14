public class Flight{
    private String airline;
    private int flightNumber;
    private String source;
    private String destination;
    private String departdatetime;
    private String arrivaldatetime;

    public String getairline(){
        return airline;
    }
     public int getflightno(){
        return flightNumber;
    }
     public String getsource(){
        return source;
    }
     public String getdestination(){
        return destination;
    }
     public String getdepart(){
        return departdatetime;
    }
     public String getarrival(){
        return arrivaldatetime;
    }
     
    public Flight(String airline,int flightNumber,String source,String destination,String departdatetime,String arrivaldatetime){
        this.airline=airline;
        this.flightNumber=flightNumber;
        this.source=source;
        this.destination=destination;
        this.departdatetime=departdatetime;
        this.arrivaldatetime=arrivaldatetime;
    }

    public void getstatus(){
        System.out.println("Whats the staus of my airline ticket booking for airline: "+airline);
    }
    public void showDetails(){
        System.out.println("AIRLINE: "+airline+"\nFLIGHT NUMBER: "+flightNumber+"\nSOURCE: "+source+"\nDESTINATION: "+destination+"\nDEPART DATE TIME: "+departdatetime+"\nARRIVAL DATE TIME: "+arrivaldatetime);
    }
}
