package com.jd.query.repository;

import com.jd.query.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends CrudRepository<Album, Long> {

    Optional<List<Album>> findByName(String name);
}

