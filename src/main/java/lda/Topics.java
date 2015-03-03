package lda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    
    public List<Pair<String, Double>> getVocabsSortedByPhi(LDA lda, int topicID) {
        if (lda == null || topicID < 0 || topics.size() <= topicID) {
            throw new IllegalArgumentException();
        }
        
        List<Pair<String, Double>> vocabProbPairs = new ArrayList<>();
        for (int v = 1; v <= lda.getBow().getNumVocabs(); ++v) {
            Vocabulary vocab = lda.getVocabularies().get(v);
            Topic topic = topics.get(topicID);
            Double phi = topic.getPhi(vocab.id(), lda.getBeta());
            vocabProbPairs.add(new ImmutablePair<String, Double>(vocab.toString(), phi));
        }
        Collections.sort(vocabProbPairs, (p1, p2) -> Double.compare(p2.getRight(), p1.getRight()));
        return vocabProbPairs;
    }
}
