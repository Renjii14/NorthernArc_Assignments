import java.util.Scanner;
public class Main2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the object to be created:\n1:Lion\n2:Dog\n3:Deer");
        int value=sc.nextInt();
    switch(value){
        case 1:
            Animal l=new Lion();
            l.eat();
            l.talk();
            ((Lion) (l)).ruleForest();
            break;
        
        case 2:
            Animal d1=new Dog();
            d1.eat();
            d1.talk();
            ((Dog) (d1)).guard();
            break;
        
        case 3:
           Animal d2=new Deer();
            d2.eat();
            d2.talk();
            ((Deer)(d2)).live();
            break; 

        default:
            System.out.println("Invalid choice");
        }
    }

    }

