package com.northernarc.restcontroller.controller;

import com.northernarc.restcontroller.dao.MovieDao;
import com.northernarc.restcontroller.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieDao movieDao;

    @RequestMapping("")
    public Collection<Movie> getAllmovies(){
        return movieDao.findAll();
    }

    @RequestMapping("/{id}")
    public Movie findById(@PathVariable int id) {
        return movieDao.findById(id);
    }

    @RequestMapping("/add/{id}/{title}/{director}/{year}/{genre}/{rating}")
    public Movie addMovie(@PathVariable int id, @PathVariable String title, @PathVariable String director, @PathVariable int year, @PathVariable String genre, @PathVariable double rating) {

        Movie movie = new Movie(id, title, director, year, genre, rating);
        movieDao.saveMovie(movie);
        return movie;
    }

    @RequestMapping("/update/{id}/{title}/{director}/{year}/{genre}/{rating}")
    public void updateMovie(@PathVariable int id, @PathVariable String title, @PathVariable String director, @PathVariable int year, @PathVariable String genre, @PathVariable double rating) {

        Movie movie = new Movie(id, title, director, year, genre, rating);
        movieDao.updateMovie(id, movie);
    }

    @RequestMapping("/delete/{id}")
    public void deleteMovie(@PathVariable int id) {
        movieDao.deleteById(id);
    }
}
