package Demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new ClassPathXmlApplicationContext(
                        "applicationContext.xml");
        ExpenseManage expenseManage=context.getBean(ExpenseManage.class);
        expenseManage.payElectricityBill(1000);
        expenseManage.payWaterBill(200);
        expenseManage.payGasBill(100);
    }
}
