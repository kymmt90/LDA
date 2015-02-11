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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

public class LDA {
    private double[] alphas;
    private final double beta;
    private final int numTopics;
    private final BagOfWords bow;
    private final Map<Integer, String> vocabs;
    private final LDAInference inference;
    private boolean trained;

    public LDA(final double alpha, final double beta, final int numTopics,
            final BagOfWords bow, LDAInferenceMethod method) {
        this.alphas    = DoubleStream.generate(() -> alpha).limit(numTopics).toArray();
        this.beta      = beta;
        this.numTopics = numTopics;
        this.bow       = bow;
        this.vocabs    = new HashMap<>();
        this.inference = LDAInferenceFactory.getInstance(method);
        this.trained   = false;
    }
    
    public void readVocabs(String filePath) throws IOException {
        if (filePath == null) throw new NullPointerException();

        BufferedReader reader
            = new BufferedReader(new FileReader(filePath));
        int vocabID = 1;
        String s = null;
        while ((s = reader.readLine()) != null) {
            vocabs.put(vocabID, s);
            vocabID += 1;
        }
        reader.close();
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
        return vocabs.get(vocabID);
    }

    public void run() {
        inference.setUp(this);
        inference.run();
        trained = true;
    }

    public double getAlpha(final int topic) {
        if (topic < 0 || numTopics <= topic) {
            throw new ArrayIndexOutOfBoundsException(topic);
        }
        return alphas[topic];
    }

    public double getBeta() {
        return beta;
    }

    public int getNumTopics() {
        return numTopics;
    }

    public BagOfWords getBow() {
        return bow;
    }

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

    public double getPhi(final int topicID, final int vocabID) {
        if (topicID < 0 || numTopics <= topicID || vocabID <= 0) {
            throw new IllegalArgumentException();
        }
        if (!trained) {
            throw new IllegalStateException();
        }

        return inference.getPhi(topicID, vocabID);
    }
}
