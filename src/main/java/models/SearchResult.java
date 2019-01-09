package models;

public class SearchResult {
    public String Title;
    public transient String Content;
    public float Score;

    public SearchResult(String title, String content, float score){
        Title = title;
        Content = content;
        Score = score;
    }
}
