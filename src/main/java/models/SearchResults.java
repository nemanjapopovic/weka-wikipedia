package models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResults implements Serializable {
    public String query;
    public int TotalHits;
    public ArrayList<SearchResult> SearchResults;

    public SearchResults(String query, int totalHits, ArrayList<SearchResult> searchResults) {
        this.query = query;
        TotalHits = totalHits;
        SearchResults = searchResults;
    }
}
