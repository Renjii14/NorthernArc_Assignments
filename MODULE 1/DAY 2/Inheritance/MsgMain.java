import java.util.Scanner;
public class MsgMain{
    public static void main(String[] args){
         Scanner sc=new Scanner(System.in);
        System.out.println("Enter the type of msg to send:\n1:Whatsapp\n2:Email\n3:Text");
        int value=sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the message to be sent:");
        String msg=sc.nextLine();
        
        switch(value){
            case 1:
                Simplemsg m=new Whatsappmsg();
                m.send(msg);
          
                break;

                case 2:
                Simplemsg m1=new Emailmsg();
                m1.send(msg);
                break;

                case 3:
                Simplemsg m2=new Textmsg();
                m2.send(msg);
                break;

                default:
                    System.out.println("Invalid choice");


        }
    }
}