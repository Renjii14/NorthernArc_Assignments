package Abstraction;
import java.util.Scanner;
public class MAin1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the animal: \n1.Lion\n2.Dog\n3.Deer");
        int val=sc.nextInt();
        switch(val){

            case 1:
                Animal an=new Lion();
                an.eat();
                an.shelter();
                an.talk();
                break;

            case 2:
                Animal a2=new Dog();
                a2.eat();
                a2.talk();
                a2.shelter();
                break;

            case 3:
                  Animal a3=new Deer();
                    a3.eat();
                    a3.talk();
                    a3.shelter();
                    break;

            default:
                System.out.println("Invalid choice");
                return ;





      
        }
    }
}
