package com.jd.query.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TheMovieDB {

    @JsonProperty("results")
    private List<Movie> movies;

    public void addMovie(Movie movie) {
        if(movies == null)
            movies = new ArrayList<>();
        movies.add(movie);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
