package impl;

import models.ShortQuery;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class ShortQueries {

    public List<ShortQuery> items;

    public ShortQueries(Instances instances){
        // Load all short queries
        items = new ArrayList<>();
        items.add(new ShortQuery("nasa", instances));
    }

}
