package impl;

import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Clusterization {

    private FilteredClusterer filteredClusterer;

    /**
     * Loads the data from dataset and builds a FilteredClusterer using
     * SimpleKMeans as a clusterer and StringToWordVector as a filter.
     *
     * @throws Exception
     */
    public FilteredClusterer buildClusterer(Instances data) throws Exception {
        System.out.println("Build simple k means filtered clusterer");
        SimpleKMeans simpleKMeans = new SimpleKMeans();
        simpleKMeans.setSeed(10);
        simpleKMeans.setNumExecutionSlots(1);
        simpleKMeans.setMaxIterations(500);
        simpleKMeans.setPreserveInstancesOrder(false);
        simpleKMeans.setNumClusters(10);

        filteredClusterer = new FilteredClusterer();
        filteredClusterer.setClusterer(simpleKMeans);
        StringToWordVector filter = StringToWordFilter.getFilter();
        filteredClusterer.setFilter(filter);

        filteredClusterer.buildClusterer(data);

        System.out.println("Finished simple k means filtered clusterer");

        return filteredClusterer;
    }
}
