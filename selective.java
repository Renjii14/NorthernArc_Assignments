import java.util.Scanner;
public class selective {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter date:");
        int date=sc.nextInt();
        if(date==31){
            System.out.println("Invalid");
        }
        int date1=date%7;
        switch(date1){

            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wedday");
                break;
            case 4:
                System.out.println("Thurday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:
                System.out.println("Sunday");
                break;
            default:
                


        }

    }
}

