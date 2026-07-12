package todo.daoImpl;

import connection.DBManager;
import todo.dao.TodoDao;
import todo.entity.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TodoDaoImplJdbc implements TodoDao {
    private final DBManager dbManager;

    public TodoDaoImplJdbc(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    public  Todo mapTotodo(ResultSet rs) throws SQLException{
        return new Todo(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getBoolean("completed"));
    }

    @Override
    public void save(Todo todo) throws SQLException {
        Connection con= dbManager.getConnection();
        String sql="Insert into todo(title,description,completed) values(?,?,?)";
        PreparedStatement stmt=con.prepareStatement(sql);
        stmt.setString(1,todo.getTitle());
        stmt.setString(2,todo.getDescription());
        stmt.setBoolean(3,todo.getCompleted());
        int row=stmt.executeUpdate();
        System.out.println("Rows updated:"+row);

        con.close();

    }

    @Override
    public Todo findById(int id) throws SQLException {
        try(Connection con= dbManager.getConnection()) {
            String sql = "select * from todo where id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapTotodo(rs);
            }
        }catch (Exception e){
            System.out.println("db connectivity issues:"+e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<Todo> findAll() throws SQLException {
        List<Todo> todoList=new LinkedList<>();
        Connection con= dbManager.getConnection();
        String sql="select * from todo";
        PreparedStatement stmt=con.prepareStatement(sql);

        ResultSet rs=stmt.executeQuery();
        while(rs.next()){
            todoList.add(mapTotodo(rs));
        }
        con.close();
        return todoList;
    }

    @Override
    public void deleteById(int id) throws SQLException {
        Connection con= dbManager.getConnection();
        String sql="delete from todo where id=?";
        PreparedStatement stmt=con.prepareStatement(sql);
        stmt.setInt(1,id);
        int row=stmt.executeUpdate();
        System.out.println("Rows deleted:"+row);

        con.close();
    }

    @Override
    public void updateById(int id, Todo todo) throws SQLException {
        Connection con= dbManager.getConnection();
        String sql="update todo set title=? ,description=? ,completed=? where id=?";
        PreparedStatement stmt=con.prepareStatement(sql);
        stmt.setString(1,todo.getTitle());
        stmt.setString(2,todo.getDescription());
        stmt.setBoolean(3,todo.getCompleted());
        stmt.setInt(4,id);
        int row=stmt.executeUpdate();
        System.out.println("Rows updated:"+row);

        con.close();

    }
}

