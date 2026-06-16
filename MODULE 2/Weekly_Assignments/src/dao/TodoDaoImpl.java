package dao;

import entity.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoImpl
        implements TodoDao {

    @Override
    public int save(Todo todo)
            throws SQLException {

        String sql =
                "insert into todo values(?,?,?)";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1,
                todo.getTodoId());

        ps.setString(2,
                todo.getTask());

        ps.setString(3,
                todo.getStatus());

        return ps.executeUpdate();
    }

    @Override
    public Todo findById(int id)
            throws SQLException {

        String sql =
                "select * from todo where todo_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1,id);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return new Todo(
                    rs.getInt("todo_id"),
                    rs.getString("task"),
                    rs.getString("status")
            );
        }

        return null;
    }

    @Override
    public List<Todo> findAll()
            throws SQLException {

        List<Todo> todos =
                new ArrayList<>();

        String sql =
                "select * from todo";

        Connection con =
                DBConnection.getConnection();

        Statement st =
                con.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

        while(rs.next()) {

            todos.add(
                    new Todo(
                            rs.getInt("todo_id"),
                            rs.getString("task"),
                            rs.getString("status")
                    )
            );
        }

        return todos;
    }

    @Override
    public int update(Todo todo)
            throws SQLException {

        String sql =
                "update todo set task=?, status=? where todo_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1,
                todo.getTask());

        ps.setString(2,
                todo.getStatus());

        ps.setInt(3,
                todo.getTodoId());

        return ps.executeUpdate();
    }

    @Override
    public int deleteById(int id)
            throws SQLException {

        String sql =
                "delete from todo where todo_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1,id);

        return ps.executeUpdate();
    }
}