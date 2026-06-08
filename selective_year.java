
import java.time.LocalDate;
import java.util.Scanner;

public class selective_year {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter year: ");
        int year = sc.nextInt();

        System.out.print("Enter month: ");
        int month = sc.nextInt();

        System.out.print("Enter date: ");
        int day = sc.nextInt();

        try {
            LocalDate date = LocalDate.of(year, month, day);

            switch (date.getDayOfWeek()) {
                case MONDAY:
                    System.out.println("Monday");
                    break;
                case TUESDAY:
                    System.out.println("Tuesday");
                    break;
                case WEDNESDAY:
                    System.out.println("Wednesday");
                    break;
                case THURSDAY:
                    System.out.println("Thursday");
                    break;
                case FRIDAY:
                    System.out.println("Friday");
                    break;
                case SATURDAY:
                    System.out.println("Saturday");
                    break;
                case SUNDAY:
                    System.out.println("Sunday");
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Invalid Date");
        }
    }
}