
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class jdbcupdate {
        public static void main(String[] args) {
            String url = "jdbc:postgresql://localhost:5432/northernarc";
            String user = "postgres";
            String password = "12345";

            try(Connection conn = DriverManager.getConnection(url,user,password)){
                System.out.println("Database connected successfully");

                //query
                String sql = "UPDATE person SET age = 20 WHERE name = 'Divya'";

                PreparedStatement smt = conn.prepareStatement(sql);

                System.out.println("Executing the query: " + sql);

                int rowsAffected = smt.executeUpdate();

                System.out.println(rowsAffected + " row(s) updated");

                String sql4="Delete from person where name not like 'Renjitha'";
                PreparedStatement stmt4=conn.prepareStatement(sql4);
                System.out.println("Deleting "+stmt4.executeUpdate());

            }
            catch (SQLException e){
                System.out.println("Failed");
                e.printStackTrace();
            }
        }
    }


