import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import impl.ClassifierImplementation;
import impl.Clusterization;
import impl.ShortQueries;
import impl.WikipediaDocuments;
import lucene.LuceneGlobal;
import lucene.LuceneWithClasses;
import lucene.LuceneWithClusters;
import models.SearchResults;
import models.ShortQuery;
import models.WikipediaDocument;
import org.apache.commons.lang3.time.StopWatch;
import weka.WekaHelper;
import weka.classifiers.meta.FilteredClassifier;
import weka.clusterers.FilteredClusterer;
import weka.core.Instances;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class InformationRetrieval {

    List classes;
    Instances instances;
    Instances instancesWithoutClass;
    WikipediaDocuments wikipediaDocuments;
    ShortQueries queries;

    FilteredClassifier naiveBayesClassifier;
    FilteredClusterer clusterer;

    final Integer NUMBER_OF_SEARCH_RESULTS = 10000;

    LuceneGlobal luceneGlobal;
    LuceneWithClasses luceneWithClasses;
    LuceneWithClusters luceneWithClusters;

    String workingDir = System.getProperty("user.dir");

    public InformationRetrieval() throws Exception {
        classes = new ArrayList<String>();
        classes.add("architecture");
        classes.add("art");
        classes.add("biology");
        classes.add("chemistry");
        classes.add("computer science");
        classes.add("literature");
        classes.add("mathematics");
        classes.add("music");
        classes.add("philosophy");
        classes.add("physics");

        luceneGlobal = new LuceneGlobal();
        luceneWithClasses = new LuceneWithClasses(classes);
        luceneWithClusters = new LuceneWithClusters(classes);
    }

    public void run() {
        try {
            // Get data file name
            String dataFileName = workingDir + "/DATA/" + "wiki.arff";

            // Load instances from arff file
            instances = WekaHelper.loadDataSet(dataFileName);
            instancesWithoutClass = WekaHelper.loadDataSetWithoutClass(dataFileName);

            // Load all wiki files from the wiki documents
            wikipediaDocuments = new WikipediaDocuments(classes, instances);

            // Load all search queries used for testing
            queries = new ShortQueries(instances);

            // Create global lucene search with all wikipedia files in one
            luceneGlobal.putDocumentInGlobalLuceneIndex(wikipediaDocuments.documents);

            // Create NaiveBayes model, classify wikipedia documents and add them to lucene
            classification();

            // Create SimpleKMeans model, cluster wikipedia documents and add them to lucene
            clusterization();

            // Prepare lucene for searching
            luceneGlobal.prepareForSearch();
            luceneWithClusters.prepareForSearch();
            luceneWithClasses.prepareForSearch();

            // Find the documents with specific search queries
            searchInFull();
            searchInClasses();
            searchInClusters();


            // Compare this list with the expected list that was generated manually
            // The list was generated to show what are the documents that make sense to be returned
            // for a specific short query


            // Measure time and quality of searching over specific cluster
            // in lucene, after getting the cluster of query.

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void classification() throws Exception {
        ClassifierImplementation classifier = new ClassifierImplementation();

        // Build classifier
        naiveBayesClassifier = classifier.buildClassifier(instances);

        // Save this classifier for later use
        WekaHelper.saveModel("filtered_naive_bayes_classifier.model", naiveBayesClassifier);

        // Classify all the wiki text files with this classifier, and for every one get the class it should be part of
        System.out.println("Classify all wiki text files and add them to resulting lucene index");
        for (WikipediaDocument wikiDocument :
                wikipediaDocuments.documents) {
            // Classify instance
            double instanceClass = naiveBayesClassifier.classifyInstance(wikiDocument.instance);
            String className = classes.get((int) instanceClass).toString();

            // Add to specific lucene index
            luceneWithClasses.addDocument(wikiDocument, className);
        }
        System.out.println("Finished classifying all wiki text files and adding them to resulting lucene index");
        // Close and commit all lucene indexes
        luceneWithClasses.commitAndCloseAllWriters();
    }

    private void clusterization() throws Exception {
        Clusterization clusterization = new Clusterization();

        // Build clusterer
        // K-means is an unsupervised learning algorithm, meaning that there should be no class defined.
        clusterer = clusterization.buildClusterer(instancesWithoutClass);

        // Save this clusterer for later use
        WekaHelper.saveModel("filtered_simple_k_means_clusterer.model", clusterer);

        // Find a cluster for each of the wiki documents, add to correct lucene cluster
        System.out.println("Find cluster for all wiki text files and add them to resulting lucene index");
        for (WikipediaDocument wikiDocument :
                wikipediaDocuments.documents) {
            // Classify instance
            double instanceCluster = clusterer.clusterInstance(wikiDocument.instance);
            System.out.println(instanceCluster);

            // Add to specific lucene index
            luceneWithClusters.addDocument(wikiDocument, (int) instanceCluster);
        }
        System.out.println("Finished finding cluster for all wiki text files and adding them to resulting lucene index");
        // Close and commit all lucene indexes
        luceneWithClusters.commitAndCloseAllWriters();
    }

    private void searchInFull() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        // Measure time and quality of searching over all documents in 1 lucene index
        List<SearchResults> fullLuceneSearchResults = new ArrayList<>();
        for (ShortQuery query :
                queries.items) {
            // Search lucene index and store returned results
            SearchResults searchResult = luceneGlobal.search(query.text, NUMBER_OF_SEARCH_RESULTS);
            fullLuceneSearchResults.add(searchResult);
        }

        watch.stop();
        System.out.println("Time for searching in full: " + watch.getTime());

        // Store search results from full lucene scans in file
        writeJsonToFile(workingDir + "/DATA/results/full.json", fullLuceneSearchResults);
    }

    private void searchInClasses() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        // Measure time and quality of searching over specific class
        // in lucene, after getting the class of query.
        List<SearchResults> classifiedLuceneSearchResults = new ArrayList<>();
        for (ShortQuery query :
                queries.items) {
            // Get class for this search query
            double instanceClass = naiveBayesClassifier.classifyInstance(query.instance);
            String className = classes.get((int) instanceClass).toString();
            System.out.println(className);

            // Search lucene index and store returned results
            SearchResults searchResult = luceneWithClasses.search(className, query.text, NUMBER_OF_SEARCH_RESULTS);
            classifiedLuceneSearchResults.add(searchResult);
//
//            System.out.println(searchResult.TotalHits);
//            for (SearchResult searchResultItem :
//                    searchResult.SearchResults) {
//                System.out.println(searchResultItem.Title);
//            }
        }

        watch.stop();
        System.out.println("Time for searching in classes only: " + watch.getTime());

        // Store search results from lucene scans in file
        writeJsonToFile(workingDir + "/DATA/results/classfied.json", classifiedLuceneSearchResults);
    }

    private void searchInClusters() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        // Measure time and quality of searching over specific class
        // in lucene, after getting the class of query.
        List<SearchResults> searchResults = new ArrayList<>();
        for (ShortQuery query :
                queries.items) {
            // Classify instance
            double instanceCluster = clusterer.clusterInstance(query.instance);

            // Search lucene index and store returned results
            SearchResults searchResult = luceneWithClusters.search((int) instanceCluster, query.text, NUMBER_OF_SEARCH_RESULTS);
            searchResults.add(searchResult);
        }

        watch.stop();
        System.out.println("Time for searching in clusters only: " + watch.getTime());

        // Store search results from lucene scans in file
        writeJsonToFile(workingDir + "/DATA/results/clustered.json", searchResults);
    }

    private void writeJsonToFile(String fileName, Object o) throws Exception {
        // Move to json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(o);

        // Write to file
        FileWriter writer = new FileWriter(fileName);
        writer.write(json);
        writer.close();
    }
}
