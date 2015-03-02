package lda;

import java.util.Collections;
import java.util.List;

public class Document {
    private final int id;
    private TopicCounter topicCount;
    private List<Vocabulary> words;
    private TopicAssignment assignment;
    
    public Document(int id, int numTopics, List<Vocabulary> words) {
        if (id <= 0 || numTopics <= 0) throw new IllegalArgumentException();
        this.id = id;
        this.topicCount = new TopicCounter(numTopics);
        this.words = words;
        this.assignment = new TopicAssignment();
    }

    public int id() {
        return id;
    }
    
    public int getTopicCount(int topicID) {
        return topicCount.getTopicCount(topicID);
    }
    
    public int getDocLength() {
        return words.size();
    }
    
    public void incrementTopicCount(int topicID) {
        topicCount.incrementTopicCount(topicID);
    }
    
    public void decrementTopicCount(int topicID) {
        topicCount.decrementTopicCount(topicID);
    }
    
    public void initializeTopicAssignment(int numTopics, long seed) {
        if (numTopics <= 0) throw new IllegalArgumentException();
        assignment.initialize(getDocLength(), numTopics, seed);
        for (int w = 0; w < getDocLength(); ++w) {
            incrementTopicCount(assignment.get(w));
        }
    }
    
    public int getTopicID(int wordID) {
        return assignment.get(wordID);
    }
    
    public void setTopicID(int wordID, int topicID) {
        assignment.set(wordID, topicID);
    }

    public Vocabulary getVocabulary(int wordID) {
        return words.get(wordID);
    }

    public List<Vocabulary> getWords() {
        return Collections.unmodifiableList(words);
    }
}
