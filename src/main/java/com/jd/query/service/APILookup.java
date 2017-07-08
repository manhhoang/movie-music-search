package com.jd.query.service;

import com.jd.query.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APILookup {

    @Autowired
    private TheMovieDBService theMovieDBService;

    @Autowired
    private LastMusicService lastMusicService;

    public ProviderService getService(APIType apiType) {
        if(apiType.name().equals(Constants.IMDB)) {
            return theMovieDBService;
        } else if(apiType.name().equals(Constants.MUSIC)){
            return lastMusicService;
        }
        return null;
    }
}
