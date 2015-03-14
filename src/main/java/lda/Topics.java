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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Topics {
    private List<Topic> topics;
    boolean ready;
    
    public Topics(LDA lda) {
        if (lda == null) throw new NullPointerException();
        
        topics = new ArrayList<>();
        for (int t = 0; t < lda.getNumTopics(); ++t) {
            topics.add(new Topic(t, lda.getBow().getNumVocabs()));
        }
        ready = false;
    }
    
    public int numTopics() {
        return topics.size();
    }
    
    public Topic get(int id) {
        return topics.get(id);
    }
    
    public int getVocabCount(int topicID, int vocabID) {
        return topics.get(topicID).getVocabCount(vocabID);
    }
    
    public int getSumCount(int topicID) {
        return topics.get(topicID).getSumCount();
    }
    
    public void incrementVocabCount(int topicID, int vocabID) {
        topics.get(topicID).incrementVocabCount(vocabID);
    }
    
    public void decrementVocabCount(int topicID, int vocabID) {
        topics.get(topicID).decrementVocabCount(vocabID);
    }
    
    public double getPhi(int topicID, int vocabID, double beta) {
        if (topicID < 0 || topics.size() <= topicID) throw new IllegalArgumentException();
        return topics.get(topicID).getPhi(vocabID, beta);
    }
    
    public List<Pair<String, Double>> getVocabsSortedByPhi(int topicID, Vocabularies vocabs, final double beta) {
        if (topicID < 0 || topics.size() <= topicID || vocabs == null || beta <= 0.0) {
            throw new IllegalArgumentException();
        }
        
        Topic topic = topics.get(topicID);
        List<Pair<String, Double>> vocabProbPairs
            = vocabs.getVocabularyList()
                    .stream()
                    .map(v -> new ImmutablePair<String, Double>(v.toString(), topic.getPhi(v.id(), beta)))
                    .sorted((p1, p2) -> Double.compare(p2.getRight(), p1.getRight()))
                    .collect(Collectors.toList());
        return Collections.unmodifiableList(vocabProbPairs);
    }
}
