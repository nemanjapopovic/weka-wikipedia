package models;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ShortQuery {
    public String text;
    public Instance instance;

    public ShortQuery(String text, Instances instances){
        this.text = text;
        this.instance = makeInstance(text, instances);
    }

    /**
     * Method that converts a text message into an instance.
     */
    private Instance makeInstance(String text, Instances instances) {

        // Create instance of length two.
        Instance instance = new DenseInstance(1);

        // Set value for message attribute
        instance.setValue(instances.attribute("text"), text);

        // Give instance access to attribute information from the dataset.
        instance.setDataset(instances);
        return instance;
    }
}
