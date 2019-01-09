package impl;

import models.WikipediaDocument;
import weka.core.Instances;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WikipediaDocuments {

    public List<WikipediaDocument> documents;

    public WikipediaDocuments(List<String> classes, Instances instances) throws Exception{
        documents = new ArrayList<>();

        // Load all text files
        String workingDir = System.getProperty("user.dir");

        for (String classType: classes) {
            File dir = new File(workingDir + "/DATA/wikipedia/" + classType);
            File[] files = dir.listFiles();
            for (File file : files) {
                // Get text
                String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                documents.add(new WikipediaDocument(file.getName(), classType, text, instances));
            }
        }

    }
}
