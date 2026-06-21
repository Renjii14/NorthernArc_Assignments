package com.northernarc.restcontroller.dao;

import com.northernarc.restcontroller.model.Movie;

import java.util.Collection;
import java.util.List;

public interface MovieDao {

    public void saveMovie(Movie movie);

    public Movie findById(int id);

    public Collection<Movie> findAll();

    public void updateMovie(int id, Movie movie);

    public void deleteById(int id);
}