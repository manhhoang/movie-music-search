package com.jd.query.service;

import com.jd.query.model.Album;
import com.jd.query.model.LastMusic;
import com.jd.query.model.Movie;
import com.jd.query.model.SearchItem;
import com.jd.query.model.SearchResponse;
import com.jd.query.model.TheMovieDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.jd.query.utils.Constants.IMDB;
import static com.jd.query.utils.Constants.MUSIC;

@Service
public class SearchService {

    @Autowired
    APIDelegate apiDelegate;

    @Autowired
    APILookup apiLookup;

    public CompletableFuture<SearchResponse> search(String api, String name) throws Exception {
        setupApiDelegate(api);
        return processResponse(apiDelegate.doSearch(name).get());
    }

    public CompletableFuture<SearchResponse> lookup(String api, String name) throws Exception {
        setupApiDelegate(api);
        return processResponse(apiDelegate.doLookup(name).get());
    }

    private void setupApiDelegate(String api) {
        apiDelegate.setLookupService(apiLookup);
        if (api.equalsIgnoreCase(IMDB)) {
            apiDelegate.setServiceType(APIType.IMDB);
        } else if (api.equalsIgnoreCase(MUSIC)) {
            apiDelegate.setServiceType(APIType.MUSIC);
        }
    }

    private CompletableFuture<SearchResponse> processResponse(Object object) {
        SearchResponse searchResponse = new SearchResponse();
        if (object instanceof TheMovieDB) {
            TheMovieDB theMovieDB = (TheMovieDB) object;
            List<Movie> movies = Optional.ofNullable(theMovieDB.getMovies()).orElse(new ArrayList<>());
            for (Movie movie : movies) {
                SearchItem searchItem = new SearchItem();
                searchItem.setName(movie.getTitle());
                searchItem.setReleaseDate(movie.getReleaseDate());
                searchResponse.addItem(searchItem);
            }
        }
        if (object instanceof LastMusic) {
            LastMusic lastMusic = (LastMusic) object;
            List<Album> albums = Optional.ofNullable(lastMusic.getMusic().getAlbumMatches().getAlbums()).orElse(new ArrayList<>());
            for (Album album : albums) {
                SearchItem searchItem = new SearchItem();
                searchItem.setName(album.getName());
                searchItem.setArtist(album.getArtist());
                searchResponse.addItem(searchItem);
            }
        }
        return CompletableFuture.completedFuture(searchResponse);
    }

}
