package lucene;

import models.SearchResult;
import models.SearchResults;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.ArrayList;

public class LuceneHelper {

    public static SearchResults getSearchResults(IndexSearcher searcher, String textQuery, Integer numberOfResults) throws Exception {
        QueryParser qp = new QueryParser("text", new StandardAnalyzer());
        Query idQuery = qp.parse(textQuery);

        TopDocs hits = searcher.search(idQuery, numberOfResults, Sort.RELEVANCE);

        ArrayList<SearchResult> searchItems = new ArrayList<SearchResult>();
        SearchResults result = new SearchResults(0, searchItems);

        ScoreDoc[] docHits = hits.scoreDocs;

        for (int i = 0; i < hits.totalHits; i++) {
            Document d = searcher.doc(docHits[i].doc);
            searchItems.add(new SearchResult(d.get("name"), d.get("text"), docHits[i].score));
        }

        result.TotalHits = hits.totalHits;
        result.SearchResults = searchItems;

        return result;
    }

    public static Document createDocument(String name, String text) {
        Document document = new Document();
        document.add(new StringField("name", name, Field.Store.YES));
        document.add(new TextField("text", text, Field.Store.YES));
        return document;
    }

    public static IndexWriter createWriter(String indexName) throws Exception {
        String workingDir = System.getProperty("user.dir");
        FSDirectory dir = FSDirectory.open(Paths.get(workingDir + "/DATA/lucene/" + indexName));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    public static IndexSearcher createSearcher(String indexName) throws Exception {
        String workingDir = System.getProperty("user.dir");
        FSDirectory dir = FSDirectory.open(Paths.get(workingDir + "/DATA/lucene/" + indexName));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}
