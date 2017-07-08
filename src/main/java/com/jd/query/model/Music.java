package com.jd.query.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Music {

    @JsonProperty("albummatches")
    private AlbumMatches albumMatches;

    public AlbumMatches getAlbumMatches() {
        return albumMatches;
    }

    public void setAlbumMatches(AlbumMatches albumMatches) {
        this.albumMatches = albumMatches;
    }
}
