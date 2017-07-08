package com.jd.query.repository;

import com.jd.query.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    Optional<List<Movie>> findByTitle(String title);
}

