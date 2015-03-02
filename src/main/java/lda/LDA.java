/*
* Copyright 2015 Kohei Yamamoto
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package lda;

import java.io.IOException;

public class LDA {
    private LDAHyperparameters hyperparameters;
    private final int numTopics;
    private final BagOfWords bow;
    private Vocabularies vocabs;
    private final LDAInference inference;
    private LDAInferenceProperties properties;
    private boolean trained;

    /**
     * @param alpha doc-topic hyperparameter
     * @param beta topic-vocab hyperparameter
     * @param numTopics the number of topics
     * @param bow bag-of-words
     * @param method inference method
     */
    public LDA(final double alpha, final double beta, final int numTopics,
            final BagOfWords bow, LDAInferenceMethod method) {
        this.hyperparameters = new LDAHyperparameters(alpha, beta, numTopics);
        this.numTopics       = numTopics;
        this.bow             = bow;
        this.inference       = LDAInferenceFactory.getInstance(method);
        this.properties      = null;
        this.trained         = false;
    }

    /**
     * @param alpha doc-topic hyperparameter
     * @param beta topic-vocab hyperparameter
     * @param numTopics the number of topics
     * @param bow bag-of-words
     * @param method inference method
     * @param propertiesFilePath the path of the properties file 
     */
    public LDA(final double alpha, final double beta, final int numTopics,
            final BagOfWords bow, LDAInferenceMethod method, String propertiesFilePath) {
        this.hyperparameters = new LDAHyperparameters(alpha, beta, numTopics);
        this.numTopics       = numTopics;
        this.bow             = bow;
        this.inference       = LDAInferenceFactory.getInstance(method);
        this.properties      = new LDAInferenceProperties();
        this.trained         = false;
        
        try {
            this.properties.load(propertiesFilePath);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Read vocabs file.
     * @param filePath
     * @throws IOException
     * @throws NullPointerException filePath is null
     */
    public void readVocabs(String filePath) throws IOException {
        if (filePath == null) throw new NullPointerException();
        vocabs = new Vocabularies(filePath);
    }

    /**
     * Get the vocabulary from its ID.
     * @param vocabID
     * @return the vocabulary
     * @throws IllegalArgumentException vocabID <= 0 || the number of vocabularies < vocabID
     */
    public String getVocab(int vocabID) {
        if (vocabID <= 0 || vocabs.size() < vocabID) {
            throw new IllegalArgumentException();
        }
        return vocabs.get(vocabID).toString();
    }

    /**
     * Run model inference.
     */
    public void run() {
        if (properties == null) inference.setUp(this);
        else inference.setUp(this, properties);
        inference.run();
        trained = true;
    }
    
    /**
     * Get hyperparameter alpha corresponding to topic.
     * @param topic
     * @return alpha corresponding to topicID
     * @throws ArrayIndexOutOfBoundsException topic < 0 || #topics <= topic
     */
    public double getAlpha(final int topic) {
        if (topic < 0 || numTopics <= topic) {
            throw new ArrayIndexOutOfBoundsException(topic);
        }
        return hyperparameters.alpha(topic);
    }

    public double getBeta() {
        return hyperparameters.beta();
    }

    public int getNumTopics() {
        return numTopics;
    }

    public BagOfWords getBow() {
        return bow;
    }

    /**
     * Get the value of doc-topic probability \theta_{docID, topicID}.
     * @param docID
     * @param topicID
     * @return the value of doc-topic probability
     * @throws IllegalArgumentException docID <= 0 || #docs < docID || topicID < 0 || #topics <= topicID
     * @throws IllegalStateException call this method when the inference has not been finished yet
     */
    public double getTheta(final int docID, final int topicID) {
        if (docID <= 0 || bow.getNumDocs() < docID
                || topicID < 0 || numTopics <= topicID) {
            throw new IllegalArgumentException();
        }
        if (!trained) {
            throw new IllegalStateException();
        }

        return inference.getTheta(docID, topicID);
    }

    /**
     * Get the value of topic-vocab probability \phi_{topicID, vocabID}.
     * @param topicID
     * @param vocabID
     * @return the value of topic-vocab probability
     * @throws IllegalArgumentException topicID < 0 || #topics <= topicID || vocabID <= 0
     * @throws IllegalStateException call this method when the inference has not been finished yet
     */
    public double getPhi(final int topicID, final int vocabID) {
        if (topicID < 0 || numTopics <= topicID || vocabID <= 0) {
            throw new IllegalArgumentException();
        }
        if (!trained) {
            throw new IllegalStateException();
        }

        return inference.getPhi(topicID, vocabID);
    }
    
    public Vocabularies getVocabularies() {
        return vocabs;
    }
}
