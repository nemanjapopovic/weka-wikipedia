package lucene;

import models.SearchResults;
import models.WikipediaDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import java.util.ArrayList;
import java.util.List;

public class LuceneGlobal {

    IndexSearcher searcher;

    public void putDocumentInGlobalLuceneIndex(List<WikipediaDocument> documentList) throws Exception {
        IndexWriter writer = LuceneHelper.createWriter("global");
        List<Document> documents = new ArrayList<>();

        for (WikipediaDocument wikiDocument :
                documentList) {
            Document document = LuceneHelper.createDocument(wikiDocument);
            documents.add(document);
        }

        // Clear lucene first
        writer.deleteAll();

        // Add all documents to lucene
        writer.addDocuments(documents);
        writer.commit();
        writer.close();
    }

    public void prepareForSearch() throws Exception {
        searcher = LuceneHelper.createSearcher("global");
    }

    public SearchResults search(String textQuery, Integer numberOfResults) throws Exception {
        return LuceneHelper.getSearchResults(searcher, textQuery, numberOfResults);
    }
}
