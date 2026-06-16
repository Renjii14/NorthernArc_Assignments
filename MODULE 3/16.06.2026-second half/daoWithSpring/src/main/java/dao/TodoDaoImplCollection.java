package dao;

import entity.Todo;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TodoDaoImplCollection implements TodoDao {

    Map<Integer, Todo> map = new LinkedHashMap<>();

    @Override
    public void save(Todo todo) throws SQLException {
        map.put(todo.getTodoId(), todo);
        System.out.println("Saved...");
    }

    @Override
    public Todo findById(int id) throws SQLException {
        return map.get(id);
    }

    @Override
    public Map<Integer, Todo> findAll() throws SQLException {
        return map;
    }

    @Override
    public void update(int id, Todo todo) throws SQLException {

        if(map.containsKey(id)) {

            Todo existingTodo = map.get(id);

            existingTodo.setTask(todo.getTask());
            existingTodo.setStatus(todo.getStatus());

            System.out.println("Updated successfully...");
        }
        else {

            System.out.println("Todo not found...");
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        if (map.containsKey(id)) {
            map.remove(id);
            System.out.println("Task removed successfully...");
        }
    }
}
