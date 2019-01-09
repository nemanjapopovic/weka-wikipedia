package lucene;

import models.SearchResults;
import models.WikipediaDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuceneWithClasses {

    List<String> classes;

    HashMap<String, IndexWriter> classificationWriterHashMap = new HashMap<>();
    HashMap<String, IndexSearcher> classificationSearcherHashMap = new HashMap<>();

    public LuceneWithClasses(List<String> classes) throws Exception {
        this.classes = classes;
        // Create 10 different index writer's, each for one class
        for (String className :
                classes) {
            String key = getKey(className);
            IndexWriter writer = LuceneHelper.createWriter("classified/" + key);
            // Clear in order to have clean state every time
            writer.deleteAll();
            classificationWriterHashMap.put(key, writer);
        }
    }

    public void prepareForSearch() throws Exception {
        // Create 10 different index writer's, each for one class
        for (String className :
                classes) {
            String key = getKey(className);
            classificationSearcherHashMap.put(key, LuceneHelper.createSearcher("classified/" + key));
        }
    }

    public SearchResults search(String className, String textQuery, Integer numberOfResults) throws Exception {
        IndexSearcher searcher = classificationSearcherHashMap.get(className);

        return LuceneHelper.getSearchResults(searcher, textQuery, numberOfResults);
    }

    public void addDocument(WikipediaDocument wikiDocument, String className) throws Exception {
        String key = getKey(className);
        Document document = LuceneHelper.createDocument(wikiDocument);
        classificationWriterHashMap.get(key).addDocument(document);
    }

    public void commitAndCloseAllWriters() throws Exception {
        System.out.println("Committing and closing all lucene with classes writers");
        // Commit and close all writers
        for (Map.Entry<String, IndexWriter> entry : classificationWriterHashMap.entrySet()) {
            IndexWriter writer = entry.getValue();
            writer.commit();
            writer.close();
        }
        System.out.println("Committed and closed all lucene with classes writers");
    }

    private String getKey(String key) {
        return key.replace(" ", "_");
    }
}
