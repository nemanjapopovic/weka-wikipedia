package lucene;

import models.WikipediaDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuceneWithClusters {

    List<String> classes;

    HashMap<Integer, IndexWriter> clusterWriterHashMap = new HashMap<>();
    HashMap<Integer, IndexSearcher> clusterSearcherHashMap = new HashMap<>();

    public LuceneWithClusters(List<String> classes) throws Exception {
        this.classes = classes;
        // Create 10 different index writer's, each for one cluster
        for (int i = 0; i < classes.size(); i++) {
            clusterWriterHashMap.put(i, LuceneHelper.createWriter("clustered/" + i));
        }
    }

    public void prepareForSearch() throws Exception {
        // Create 10 different index writer's, each for one class
        for (int i = 0; i < classes.size(); i++) {
            clusterSearcherHashMap.put(i, LuceneHelper.createSearcher("clustered/" + i));
        }
    }

    public void addDocument(WikipediaDocument wikiDocument, Integer cluster) throws Exception {
        Document document = LuceneHelper.createDocument(wikiDocument.name, wikiDocument.text);
        clusterWriterHashMap.get(cluster).addDocument(document);
    }

    public void commitAndCloseAllWriters() throws Exception {
        System.out.println("Committing and closing all lucene with clusters writers");
        // Commit and close all writers
        for (Map.Entry<Integer, IndexWriter> entry : clusterWriterHashMap.entrySet()) {
            IndexWriter writer = entry.getValue();
            writer.commit();
            writer.close();
        }
        System.out.println("Committed and closed all lucene with clusters writers");
    }
}