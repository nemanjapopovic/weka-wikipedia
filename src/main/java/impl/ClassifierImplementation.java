package impl;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ClassifierImplementation {

    private FilteredClassifier filteredClassifier;


    /**
     * Loads the data from dataset and builds a FilteredClassifier using
     * NaiveBayes as a classifier and StringToWordVector as a filter.
     *
     * @throws Exception
     */
    public FilteredClassifier buildClassifier(Instances data) throws Exception {
        System.out.println("Build naive bayes filtered classified");
        Classifier nbClassifier = new NaiveBayes();

        filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(nbClassifier);
        StringToWordVector filter = StringToWordFilter.getFilter();
        filteredClassifier.setFilter(filter);

        filteredClassifier.buildClassifier(data);

        System.out.println("Finished building naive bayes filtered classified");

        return filteredClassifier;
    }
}
