package weka;

import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

public class WekaHelper {

    /**
     * Loads the data from a given fileName and creates the dataset for classifying
     *
     * @param fileName name of the .arff file
     * @return created Instances dataset
     * @throws Exception
     */
    public static Instances loadDataSet(String fileName) throws Exception {
        System.out.println("Loading weka arff file: " + fileName);
        ConverterUtils.DataSource loader = new ConverterUtils.DataSource(fileName);
        Instances loadedData = loader.getDataSet();
        loadedData.setClassIndex(loadedData.numAttributes() - 1);
        System.out.println("Loaded weka arff file: " + fileName);

        return loadedData;
    }

    /**
     * Loads the data from a given fileName and creates the dataset for classifying
     *
     * @param fileName name of the .arff file
     * @return created Instances dataset
     * @throws Exception
     */
    public static Instances loadDataSetWithoutClass(String fileName) throws Exception {
        System.out.println("Loading weka arff file: " + fileName);
        ConverterUtils.DataSource loader = new ConverterUtils.DataSource(fileName);
        Instances loadedData = loader.getDataSet();
        System.out.println("Loaded weka arff file: " + fileName);

        return loadedData;
    }

    /**
     * Saves the generated weka model to file system
     *
     * @param name name of the .model file
     * @param model model object
     * @throws Exception
     */
    public static void saveModel(String name, Object model) throws Exception {
        String workingDir = System.getProperty("user.dir");
        System.out.println("Saving model " + name + " to " + workingDir + "/DATA/models/" + name);
        SerializationHelper.write(workingDir + "/DATA/models/" + name, model);
        System.out.println("Saved model " + name + " to " + workingDir + "/DATA/models/" + name);
    }
}
