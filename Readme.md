# Information Retrieval System

This repository contains Java code that is using Weka and Lucene in order to implement 3 different ways of information
retrieval systems.

## Approaches

1. First approach is searching over 1 Lucene index with Wikipedia articles indexed. This solution is the slowest one
as it has to go over different categories of data that are not relevant for a given search query.

2. Second approach is using classification model in order to create Lucene indexes that are holding documents
grouped by their classes. Later on, when the search is performed, search query is classified and search is performed
only on the type of documents. This solution has additional step at the beginning, classification of search query,
but later on is searching only in a subset of data.

3. Third approach is very similar to the second one, the only difference is that instead of classes different clusters
of data are created using SimpleKMeans algorithm.

## Example program flow

Once the application is started the following steps will be performed:

- Weka .arff file is loaded and Weka Instances are populated. These instances are used for generating models.
- Short queries are loaded, these are example queries we are going to use to test the system with.
- Wikipedia corpus is loaded and added to the Lucene index that is used in first approach.
- Classification:
    - Naive Bayes classifier model is created with StringToWordVector filter.
    - This model is saved in file system
    - Every Wikipedia document is classified with this Naive Bayes model, and the class is determined
    - Every Wikipedia document is then added to a specific Lucene index containing the documents that are classified the same
- Clusterization:
    - SimpleKMeans clusterer is created with StringToWordVector filter.
    - This model is saved in file system
    - Every Wikipedia document is clustered using this model model, and the cluster it belongs to is determined
    - Every Wikipedia document is then added to a specific Lucene index containing all the documents belonging to the same cluster
- Searching using short queries
    Searching using short queries is using all 3 approaches and also storing results, as well as times of the search executions.
    These results should be compared to the manual search in order to measure the relevance of results.