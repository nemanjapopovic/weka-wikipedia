package models;

import java.util.ArrayList;

public class SearchResults {
    public int TotalHits;
    public ArrayList<SearchResult> SearchResults;

    public SearchResults(int totalHits, ArrayList<SearchResult> searchResults) {
        TotalHits = totalHits;
        SearchResults = searchResults;
    }
}
