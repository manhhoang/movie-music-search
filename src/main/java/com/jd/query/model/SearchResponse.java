package com.jd.query.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    List<SearchItem> searchItems;

    public void addItem(SearchItem searchItem) {
        if(searchItems == null)
            searchItems = new ArrayList<>();
        searchItems.add(searchItem);
    }

    public List<SearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}
