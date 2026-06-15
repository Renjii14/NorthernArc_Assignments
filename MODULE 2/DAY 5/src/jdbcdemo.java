
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class jdbcdemo {

        public static void main(String[] args) {
            String url = "jdbc:postgresql://localhost:5432/northernarc";
            String user = "postgres";
            String password = "12345";

            try(Connection conn = DriverManager.getConnection(url,user,password)){
                System.out.println("Database connected successfully");

                //query
                String sql ="Create table if not exists person(id serial primary key,name varchar(255),email varchar(255),age real)";
                PreparedStatement smt = conn.prepareStatement(sql);
                System.out.println("Executing the query :"+ sql);
                smt.execute();
                System.out.println("Table created successfully");

                //        ddl queries -> use execute
                String sql2="Insert into person(name,email) values('renji','renji@gmail.com'),('gokul','gokul@gmail.com')";
                PreparedStatement stmt2=conn.prepareStatement(sql2);
                int data=stmt2.executeUpdate();
                System.out.println(data);

            }
            catch (SQLException e){
                System.out.println("Failed");
                e.printStackTrace();
            }

        }
    }


