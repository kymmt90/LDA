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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;

public class LDACollapsedGibbsSampler implements LDAInference {
    private LDA lda;
    private List<List<Integer>> topicAssignment;
    private List<List<Integer>> docTopicCount;
    private List<Map<Integer, Integer>> topicVocabCount;
    private List<Integer> topicSumCount;
    private int numIteration;
    
    private static final long DEFAULT_SEED = 0L;
    private static final int DEFAULT_NUM_ITERATION = 100;
    
    // ready for Gibbs sampling
    private boolean ready;

    public LDACollapsedGibbsSampler() {
        ready = false;
    }

    public void setUp(LDA lda, LDAInferenceProperties properties) {
        if (properties == null) {
            setUp(lda);
            return;
        }
        
        this.lda = lda;
        initializeContainers();
        
        final long seed = properties.seed() != null ? properties.seed() : DEFAULT_SEED;
        initializeTopicAssignment(seed);
        
        this.numIteration
            = properties.numIteration() != null ? properties.numIteration() : DEFAULT_NUM_ITERATION;
        this.ready = true;
    }
    
    public void setUp(LDA lda) {
        if (lda == null) throw new NullPointerException();

        this.lda = lda;
        initializeContainers();
        
        initializeTopicAssignment(DEFAULT_SEED);
        
        this.numIteration = DEFAULT_NUM_ITERATION;
        this.ready = true;
    }
    
    private void initializeContainers() {
        assert lda != null;
        topicAssignment = Stream.generate(() -> new ArrayList<Integer>())
                                .limit(this.lda.getBow().getNumDocs())
                                .collect(Collectors.toList());
        docTopicCount   = Stream.generate(() -> new ArrayList<Integer>())
                                .limit(this.lda.getBow().getNumDocs())
                                .collect(Collectors.toList());
        topicVocabCount = Stream.generate(() -> new HashMap<Integer, Integer>())
                                .limit(this.lda.getNumTopics())
                                .collect(Collectors.toList());
        topicSumCount   = Stream.generate(() -> 0)
                                .limit(this.lda.getNumTopics())
                                .collect(Collectors.toList());
    }

    public boolean isReady() {
        return ready;
    }

    public int getNumIteration() {
        return numIteration;
    }
    
    public void setNumIteration(final int numIteration) {
        this.numIteration = numIteration;
    }

    @Override
    public void run() {
        if (!ready) {
            throw new IllegalStateException("instance has not set up yet");
        }

        for (int i = 1; i <= numIteration; ++i) {
            System.out.println("Iteraion " + i + ".");
            runSampling();
        }
    }

    /**
     * Run collapsed Gibbs sampling [Griffiths and Steyvers 2004].
     */
    void runSampling() {
        for (int d = 1; d <= lda.getBow().getNumDocs(); ++d) {
            List<Integer> words = lda.getBow().getWords(d);
            for (int w = 0; w < words.size(); ++w) {
                final int oldTopic = getTopicAssignment(d).get(w);
                final int vocabID  = words.get(w);

                // Decrement DT count
                List<Integer> topicCounts = docTopicCount.get(d - 1);
                topicCounts.set(oldTopic, topicCounts.get(oldTopic) - 1);
                
                // Decrement TV count
                Map<Integer, Integer> vocabCounts = topicVocabCount.get(oldTopic);
                vocabCounts.replace(vocabID, vocabCounts.get(vocabID) - 1);
                topicSumCount.set(oldTopic, topicSumCount.get(oldTopic) - 1);

                // Create the discrete distribution over toipcs from the current topic assignment
                IntegerDistribution distribution
                    = getFullConditionalDistribution(lda.getNumTopics(), d, vocabID);
                
                // Sample the new topic and assign it to the word
                final int newTopic = distribution.sample();
                topicAssignment.get(d - 1).set(w, newTopic);
                
                // Increment DT count
                topicCounts.set(newTopic, topicCounts.get(newTopic) + 1);
                
                // Increment TV count
                Map<Integer, Integer> newVocabCounts = topicVocabCount.get(newTopic);
                Optional<Integer> currentCount
                    = Optional.ofNullable(newVocabCounts.putIfAbsent(vocabID, 1));
                if (currentCount.isPresent()) {
                    newVocabCounts.replace(vocabID, currentCount.get().intValue() + 1);
                }
                topicSumCount.set(newTopic, topicSumCount.get(newTopic) + 1);
            }
        }
    }
    
    /**
     * Get the full conditional distribution over topics.
     * docID and vocabID are passed to this distribution for parameters.
     * @param numTopics
     * @param docID
     * @param vocabID
     * @return the integer distribution over topics
     */
    IntegerDistribution getFullConditionalDistribution(final int numTopics, final int docID, final int vocabID) {
        int[]    topics        = IntStream.range(0, numTopics).toArray();
        double[] probabilities = Arrays.stream(topics)
                                       .mapToDouble(t -> getTheta(docID, t) * getPhi(t, vocabID))
                                       .toArray();
        return new EnumeratedIntegerDistribution(topics, probabilities); 
    }

    /**
     * Initialize the topic assignment.
     * @param seed the seed of a pseudo random number generator
     */
    void initializeTopicAssignment(final long seed) {
        Random random = new Random(seed);
        for (int d = 1; d <= lda.getBow().getNumDocs(); ++d) {
            topicAssignment.set(d - 1, new ArrayList<>());
            List<Integer> topicCounts = Stream.generate(() -> 0).limit(lda.getNumTopics()).collect(Collectors.toList()); 
            docTopicCount.set(d - 1, topicCounts);
            for (int w = 0; w < lda.getBow().getDocLength(d); ++w) {
                // Assign random topic
                final int topicID = random.nextInt(lda.getNumTopics());
                topicAssignment.get(d - 1).add(topicID);

                // Increment DT count
                docTopicCount.get(d - 1).set(topicID, docTopicCount.get(d - 1).get(topicID) + 1);

                // Increment TV count
                final int vocabID = lda.getBow().getWords(d).get(w);
                Map<Integer, Integer> vocabCounts = topicVocabCount.get(topicID);
                Optional<Integer> currentCount
                    = Optional.ofNullable(vocabCounts.putIfAbsent(vocabID, 1));
                if (currentCount.isPresent()) {
                    vocabCounts.replace(vocabID, currentCount.get().intValue() + 1);
                }
                topicSumCount.set(topicID, topicSumCount.get(topicID) + 1);
            }
        }
    }

    /**
     * Get the count of topicID assigned to docID. 
     * @param docID
     * @param topicID
     * @return the count of topicID assigned to docID
     */
    int getDTCount(final int docID, final int topicID) {
        if (!ready) throw new IllegalStateException();
        if (docID <= 0 || lda.getBow().getNumDocs() < docID
                || topicID < 0 || lda.getNumTopics() <= topicID) {
            throw new IllegalArgumentException();
        }
        return docTopicCount.get(docID - 1).get(topicID);
    }

    /**
     * Get the count of vocabID assigned to topicID.
     * @param topicID
     * @param vocabID
     * @return the count of vocabID assigned to topicID
     */
    int getTVCount(final int topicID, final int vocabID) {
        if (!ready) throw new IllegalStateException();
        if (topicID < 0 || lda.getNumTopics() <= topicID || vocabID <= 0) {
            throw new IllegalArgumentException();
        }
        if (!topicVocabCount.get(topicID).containsKey(vocabID)) return 0;
        else return topicVocabCount.get(topicID).get(vocabID);
    }
    
    /**
     * Get the sum of counts of vocabs assigned to topicID.
     * This is the sum of topic-vocab count over vocabs. 
     * @param topicID
     * @return the sum of counts of vocabs assigned to topicID
     * @throws IllegalArgumentException topicID < 0 || #topic <= topicID
     */
    int getTSumCount(final int topicID) {
        if (topicID < 0 || lda.getNumTopics() <= topicID) {
            throw new IllegalArgumentException();
        }
        return topicSumCount.get(topicID);
    }

    @Override
    public double getTheta(final int docID, final int topicID) {
        if (!ready) throw new IllegalStateException();
        if (docID <= 0 || lda.getBow().getNumDocs() < docID
                || topicID < 0 || lda.getNumTopics() <= topicID) {
            throw new IllegalArgumentException();
        }
        double sumAlpha = 0.0;
        for (int t = 0; t < lda.getNumTopics(); ++t) {
            sumAlpha += lda.getAlpha(t);
        }
        return (getDTCount(docID, topicID) + lda.getAlpha(topicID))
                / (lda.getBow().getDocLength(docID) + sumAlpha);
    }

    @Override
    public double getPhi(int topicID, int vocabID) {
        if (!ready) throw new IllegalStateException();
        if (topicID < 0 || lda.getNumTopics() <= topicID || vocabID <= 0) {
            throw new IllegalArgumentException();
        }
        return (getTVCount(topicID, vocabID) + lda.getBeta())
                / (getTSumCount(topicID) + lda.getBow().getNumVocabs() * lda.getBeta());
    }

    /**
     * Get the unmodifiable list of topics assigned to the document. 
     * @param docID
     * @return the sequence of topics
     * @throws IllegalArgumentException docID <= 0 || #docs < docID
     */
    public List<Integer> getTopicAssignment(final int docID) {
        if (docID <= 0 || lda.getBow().getNumDocs() < docID) {
            throw new IllegalArgumentException();
        }
        return Collections.unmodifiableList(topicAssignment.get(docID - 1));
    }
}
