package com.jd.query.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AlbumMatches {

    @JsonProperty("album")
    private List<Album> albums;

    public void addAlbum(Album album) {
        if(albums == null)
            albums = new ArrayList<>();
        albums.add(album);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
