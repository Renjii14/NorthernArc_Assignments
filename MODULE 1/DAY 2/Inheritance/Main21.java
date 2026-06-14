import java.util.Scanner;
public class Main21 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the object to be created:\n1:Lion\n2:Dog\n3:Deer");
        int value=sc.nextInt();
    switch(value){
        case 1:
            Lion l=new Lion();
            l.eat();
            l.talk();
            l.ruleForest();
            break;
        
        case 2:
            Dog d1=new Dog();
            d1.eat();
            d1.talk();
            d1.guard();
            break;
        
        case 3:
           Deer d2=new Deer();
            d2.eat();
            d2.talk();
            d2.live();
            break; 

        default:
            System.out.println("Invalid choice");
        }
    }

    }



