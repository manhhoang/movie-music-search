package com.jd.query.controller;

import com.jd.query.model.SearchResponse;
import com.jd.query.service.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/api")
public class SearchEndpoint {

    private final SearchService searchService;

    @Autowired
    public SearchEndpoint(SearchService searchService) {
        this.searchService = searchService;
    }

    @ApiOperation(value = "Search movies and musics in remote service providers.", notes = "", response = SearchResponse.class, tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SearchResponse.class),
            @ApiResponse(code = 400, message = "Request failed.", response = SearchResponse.class),
            @ApiResponse(code = 401, message = "Authorization has been denied for this request.", response = SearchResponse.class),
            @ApiResponse(code = 422, message = "Model validation failed.", response = SearchResponse.class),
            @ApiResponse(code = 429, message = "Too many requests.", response = SearchResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = SearchResponse.class)})
    @RequestMapping(value = "/v1/search/{api}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public CompletableFuture<SearchResponse> search(@PathVariable String api, @RequestParam String name) throws Exception{
        return this.searchService.search(api, name);
    }

    @ApiOperation(value = "Lookup movies and musics in database.", notes = "", response = SearchResponse.class, tags = {"Search"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SearchResponse.class),
            @ApiResponse(code = 400, message = "Request failed.", response = SearchResponse.class),
            @ApiResponse(code = 401, message = "Authorization has been denied for this request.", response = SearchResponse.class),
            @ApiResponse(code = 422, message = "Model validation failed.", response = SearchResponse.class),
            @ApiResponse(code = 429, message = "Too many requests.", response = SearchResponse.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = SearchResponse.class)})
    @RequestMapping(value = "/v1/lookup/{api}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public CompletableFuture<SearchResponse> lookup(@PathVariable String api, @RequestParam String name) throws Exception{
        return this.searchService.lookup(api, name);
    }
}
