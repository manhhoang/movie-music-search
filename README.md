# movie-music-search

## Run

```
http://localhost:8080/api/v1/search/imdb?name=Indiana%20Jones

Response:
{  
   "searchItems":[  
      {  
         "name":"Indiana Jones and the Last Crusade",
         "releaseDate":"1989-05-24",
         "artist":null
      },
      {  
         "name":"Indiana Jones and the Temple of Doom",
         "releaseDate":"1984-05-23",
         "artist":null
      }
   ]
}

http://localhost:8080/api/v1/lookup/imdb?name=Indiana%20Jones

```

```
localhost:8080/api/v1/search/music?name=Believe

Response:
{  
   "searchItems":[  
      {  
         "name":"Believe",
         "releaseDate":null,
         "artist":"Disturbed"
      },
      {  
         "name":"Make Believe",
         "releaseDate":null,
         "artist":"Weezer"
      }
   ]
}

localhost:8080/api/v1/lookup/music?name=Believe

```

Swagger api documents

```
http://localhost:8080/v2/api-docs

http://localhost:8080/swagger-ui.html

```

## Rest client using Spring RestTemplate

```
restTemplate.getForEntity(url, TheMovieDB.class, name)
```

## Asynchronous using Java 8 CompletableFuture

```
public CompletableFuture<TheMovieDB> search(String name) {
    final String url = theMovieDbUrl + theMovieDbKey;
    return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, TheMovieDB.class, name))
            .thenApply(theMovieDB -> {
                TheMovieDB retTheMovieDB = new TheMovieDB();
                int count = 1;
                for(Movie movie: theMovieDB.getBody().getMovies()){
                    if(count > 4)
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
```

## Spring JPA

```
@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    Optional<List<Movie>> findByTitle(String title);
}

```

## Business Delegate design pattern

```
apiDelegate.setLookupService(apiLookup);
if(api.equalsIgnoreCase(IMDB)) {
    apiDelegate.setServiceType(APIType.IMDB);
} else if(api.equalsIgnoreCase(MUSIC)){
    apiDelegate.setServiceType(APIType.MUSIC);
}

```

## Spring GuavaCache 

```
@Bean
public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    GuavaCache movieCache = new GuavaCache("movie", CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build());
    GuavaCache musicCache = new GuavaCache("music", CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build());
    simpleCacheManager.setCaches(Arrays.asList(movieCache, musicCache));
    return simpleCacheManager;
}
```

## Json parser using Jackson

```
@JsonProperty("results")
private List<Movie> movies;
```

## Junit test using Mockito

```
@Test
public void testSearchSuccess() throws ExecutionException, InterruptedException {
    String url = "nullnull";
    String movieName = "Indiana Jones";
    TheMovieDB theMovieDB = new TheMovieDB();
    Movie movie = new Movie();
    movie.setTitle("Indiana Jones Part 1");
    movie.setReleaseDate("5-12-1984");
    theMovieDB.addMovie(movie);
    when(restTemplate.getForEntity(url, TheMovieDB.class, movieName)).thenReturn(new ResponseEntity(theMovieDB, HttpStatus.OK));
    CompletableFuture<TheMovieDB> future = movieDBService.search(movieName);
    TheMovieDB result = future.get();
    Assert.assertEquals(result.getMovies().size(), 1);
    Assert.assertEquals(result.getMovies().get(0).getTitle(), "Indiana Jones Part 1");
    Assert.assertEquals(result.getMovies().get(0).getReleaseDate(), "5-12-1984");
}
```

## Integration test using Spring Boot test

```
@Test
public void testSearchMovie() throws IOException {
    MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
    header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
    HttpEntity<String> entity = new HttpEntity<>(header);

    ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/search/Indiana%20Jones", String.class, entity);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    ObjectMapper objectMapper = new ObjectMapper();
    SearchResponse searchResponse = objectMapper.readValue(response.getBody(), SearchResponse.class);
    Assert.assertEquals(searchResponse.getSearchItems().size(), 4);
}
```

## Docker file

```
FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD query-1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

## Docker hub

https://hub.docker.com/r/manhhoang/movie-music-search/