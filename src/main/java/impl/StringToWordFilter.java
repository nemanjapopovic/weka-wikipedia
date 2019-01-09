package impl;

import weka.core.stemmers.NullStemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class StringToWordFilter {

    public static StringToWordVector getFilter() {
        WordTokenizer tokenizer = new WordTokenizer();
        NullStemmer stemmer = new NullStemmer();

        StringToWordVector stringToWordVector = new StringToWordVector();
        stringToWordVector.setIDFTransform(true);
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setTFTransform(true);
        stringToWordVector.setWordsToKeep(1000);
        stringToWordVector.setTokenizer(tokenizer);
        stringToWordVector.setStemmer(stemmer);

        return stringToWordVector;
    }
}
