package dao;

import entity.Person;
import Connection.DBmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {

    private Connection connection;

    public PersonDaoImpl() throws SQLException {
        connection = DBmanager.getConnection();
    }

    @Override
    public void create(Person person) throws SQLException {

        String sql =
                "INSERT INTO person(name,email,age) VALUES(?,?,?)";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setString(1, person.getName());
        ps.setString(2, person.getEmail());
        ps.setFloat(3, person.getAge());

        ps.executeUpdate();

        System.out.println("Person Added");
    }

    @Override
    public List<Person> findAll() throws SQLException {

        List<Person> persons = new ArrayList<>();

        String sql = "SELECT * FROM person";

        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while(rs.next()) {

            Person p = new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getFloat("age")
            );

            persons.add(p);
        }

        return persons;
    }

    @Override
    public Person findById(int id) throws SQLException {

        String sql =
                "SELECT * FROM person WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            return new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getFloat("age")
            );
        }

        return null;
    }

    @Override
    public void update(Person person)
            throws SQLException {

        String sql =
                "UPDATE person SET name=?,email=?,age=? WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setString(1, person.getName());
        ps.setString(2, person.getEmail());
        ps.setFloat(3, person.getAge());
        ps.setInt(4, person.getId());

        ps.executeUpdate();

        System.out.println("Person Updated");
    }

    @Override
    public void delete(int id)
            throws SQLException {

        String sql =
                "DELETE FROM person WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();

        System.out.println("Person Deleted");
    }
}