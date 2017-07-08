package com.jd.query.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastMusic {

    @JsonProperty("results")
    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
