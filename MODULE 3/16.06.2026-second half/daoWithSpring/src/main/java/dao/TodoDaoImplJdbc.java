package dao;

import Connection.DBmanager;
import entity.Todo;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class TodoDaoImplJdbc implements TodoDao {

    private final DBmanager dbManager;

    public TodoDaoImplJdbc(DBmanager dbManager) {
        this.dbManager = dbManager;
    }

    public Todo mapToTodo(ResultSet rs)
            throws SQLException {
        return new Todo(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
    }

    @Override
    public void save(Todo todo)
            throws SQLException {

        Connection con=dbManager.getConnection();

        String sql=
                "insert into todo values(?,?,?)";

        PreparedStatement stmt=
                con.prepareStatement(sql);

        stmt.setInt(1,todo.getTodoId());
        stmt.setString(2,todo.getTask());
        stmt.setBoolean(3,todo.getStatus());

        int row=stmt.executeUpdate();

        System.out.println(
                "Rows inserted:"+row);

        con.close();
    }

    @Override
    public Todo findById(int id)
            throws SQLException {

        Connection con=dbManager.getConnection();

        String sql=
                "select * from todo where todo_id=?";

        PreparedStatement stmt=
                con.prepareStatement(sql);

        stmt.setInt(1,id);

        ResultSet rs=
                stmt.executeQuery();

        Todo todo=null;

        if(rs.next()) {
            todo=mapToTodo(rs);
        }

        con.close();

        return todo;
    }

    @Override
    public Map<Integer, Todo> findAll()
            throws SQLException {

        Connection con=dbManager.getConnection();

        Map<Integer,Todo> map=
                new LinkedHashMap<>();

        String sql=
                "select * from todo";

        PreparedStatement stmt=
                con.prepareStatement(sql);

        ResultSet rs=
                stmt.executeQuery();

        while(rs.next()) {

            Todo todo=
                    mapToTodo(rs);

            map.put(
                    todo.getTodoId(),
                    todo
            );
        }

        con.close();

        return map;
    }

    @Override
    public void update(
            int id,
            Todo todo)
            throws SQLException {

        Connection con=dbManager.getConnection();

        String sql=
                "update todo set task=?,status=? where todo_id=?";

        PreparedStatement stmt=
                con.prepareStatement(sql);

        stmt.setString(
                1,
                todo.getTask());

        stmt.setBoolean(
                2,
                todo.getStatus());

        stmt.setInt(
                3,
                id);

        int row=
                stmt.executeUpdate();

        System.out.println(
                "Rows updated:"+row);

        con.close();
    }

    @Override
    public void deleteById(int id)
            throws SQLException {

        Connection con=dbManager.getConnection();

        String sql=
                "delete from todo where todo_id=?";

        PreparedStatement stmt=
                con.prepareStatement(sql);

        stmt.setInt(1,id);

        int row=
                stmt.executeUpdate();

        System.out.println(
                "Rows deleted:"+row);

        con.close();
    }
}