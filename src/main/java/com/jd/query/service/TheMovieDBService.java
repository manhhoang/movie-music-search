package com.jd.query.service;

import com.jd.query.exception.AppException;
import com.jd.query.model.Movie;
import com.jd.query.model.TheMovieDB;
import com.jd.query.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class TheMovieDBService implements ProviderService<TheMovieDB> {

    @Value("${themoviedb.key}")
    private String theMovieDbKey;

    @Value("${themoviedb.url}")
    private String theMovieDbUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    private static final Logger logger = LoggerFactory.getLogger(TheMovieDBService.class);

    @Override
    public CompletableFuture<TheMovieDB> search(String name) {
        final String url = theMovieDbUrl + theMovieDbKey;
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, TheMovieDB.class, name))
                .thenApply(theMovieDB -> {
                    TheMovieDB retTheMovieDB = new TheMovieDB();
                    int count = 1;
                    for (Movie movie : theMovieDB.getBody().getMovies()) {
                        if (count > 4)
                            break;
                        count++;
                        retTheMovieDB.addMovie(movie);
                    }
                    movieRepository.save(retTheMovieDB.getMovies());
                    return retTheMovieDB;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from api.themoviedb.org");
                    throw new AppException("100", "Failed to get movie from api.themoviedb.org");
                });
    }

    @Override
    @Cacheable("movie")
    public CompletableFuture<TheMovieDB> lookup(String name) {
        return CompletableFuture.supplyAsync(() -> movieRepository.findByTitle(name))
                .thenApply(movies -> {
                    TheMovieDB retTheMovieDB = new TheMovieDB();
                    for (Movie movie : movies.get()) {
                        retTheMovieDB.addMovie(movie);
                    }
                    return retTheMovieDB;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from database");
                    throw new AppException("100", "Failed to get movie from database");
                });
    }
}
