package com.northernarc.restcontroller.dao;

import com.northernarc.restcontroller.model.Movie;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Component
public class MovieDaoImpl implements MovieDao {

    Map<Integer,Movie> map;

    @PostConstruct
    public void init(){
        map=new HashMap<>();
        map.put(1, new Movie(1, "Interstellar", "Christopher Nolan", 2014, "Sci-Fi", 8.7));
        map.put(2, new Movie(2, "Leo", "Lokesh Kanagaraj", 2023, "Action", 7.5));
        map.put(3, new Movie(3, "96", "C. Prem Kumar", 2018, "Romance", 8.5));

        System.out.println("MovieDao initialized");
    }

    @Override
    public void saveMovie(Movie movie) {
        map.put(movie.getId(), movie);

    }

    @Override
    public Movie findById(int id) {
        return map.get(id);
    }

    @Override
    public Collection<Movie> findAll() {
        return map.values();
    }

    @Override
    public void updateMovie(int id, Movie movie) {
        map.put(id,movie);
    }

    @Override
    public void deleteById(int id) {
        map.remove(id);
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroying mapping..");
        map.clear();
    }
}
