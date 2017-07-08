package com.jd.query.service;

import com.jd.query.exception.AppException;
import com.jd.query.model.Album;
import com.jd.query.model.AlbumMatches;
import com.jd.query.model.LastMusic;
import com.jd.query.model.Music;
import com.jd.query.repository.MusicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class LastMusicService implements ProviderService<LastMusic> {

    @Value("${lastmusic.key}")
    private String lastMusicKey;

    @Value("${lastmusic.url}")
    private String lastMusicUrl;

    private static final Logger logger = LoggerFactory.getLogger(LastMusicService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MusicRepository musicRepository;

    @Override
    public CompletableFuture<LastMusic> search(String name) {
        final String url = lastMusicUrl + lastMusicKey;
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, LastMusic.class, name))
                .thenApply(lastMusic -> {
                    LastMusic retLastMusic = new LastMusic();
                    Music music = new Music();
                    AlbumMatches albumMatches = new AlbumMatches();
                    int count = 1;
                    for (Album album : lastMusic.getBody().getMusic().getAlbumMatches().getAlbums()) {
                        if (count > 4)
                            break;
                        albumMatches.addAlbum(album);
                        count++;
                    }
                    music.setAlbumMatches(albumMatches);
                    retLastMusic.setMusic(music);
                    return retLastMusic;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from last music");
                    throw new AppException("100", "Failed to get movie from last music");
                });
    }

    @Override
    @Cacheable("music")
    public CompletableFuture<LastMusic> lookup(String name) {
        return CompletableFuture.supplyAsync(() -> musicRepository.findByName(name))
                .thenApply(albums -> {
                    LastMusic retLastMusic = new LastMusic();
                    Music music = new Music();
                    AlbumMatches albumMatches = new AlbumMatches();
                    for (Album album : albums.get()) {
                        albumMatches.addAlbum(album);
                    }
                    music.setAlbumMatches(albumMatches);
                    retLastMusic.setMusic(music);
                    return retLastMusic;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from database");
                    throw new AppException("100", "Failed to get movie from database");
                });
    }
}
