package com.northernArc.firstbootapp.dao;

import com.northernArc.firstbootapp.model.Todo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@Service
public class TodoDaoImpl implements TodoDao {

    private Map<Integer, Todo> map;

    @Override
    public void save(Todo todo){
        map.put(todo.getTodoId(), todo);
        System.out.println("Saved...");
    }

    @Override
    public Todo findById(int id){
        return map.get(id);
    }

    @Override
    public Map<Integer, Todo> findAll(){
        return map;
    }

    @Override
    public void update(int id, Todo todo) {

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
    public void deleteById(int id) {
        if (map.containsKey(id)) {
            map.remove(id);
            System.out.println("Task removed successfully...");
        }
    }
    @PostConstruct
    public void init(){
        map=new HashMap<>();
        map.put(1,new Todo(1,"wake up",true));
        map.put(2,new Todo(2,"have breakfast",true));
        map.put(3,new Todo(3,"go office",true));
        map.put(4,new Todo(4,"study sptringboot",true));
    }

    @PreDestroy
    void destroy(){
        System.out.println("Destroying...");
        map.clear();
    }
}