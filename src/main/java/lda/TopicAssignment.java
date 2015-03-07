package lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TopicAssignment {
    private List<Integer> topicAssignment;
    private boolean ready;
    
    public TopicAssignment() {
        topicAssignment = new ArrayList<>();
        ready = false;
    }

    public void set(int wordID, int topicID) {
        if (!ready) throw new IllegalStateException();
        if (wordID < 0 || topicAssignment.size() <= wordID || topicID < 0) {
            throw new IllegalArgumentException();
        }
        topicAssignment.set(wordID, topicID);
    }
    
    public int get(int wordID) {
        if (!ready) throw new IllegalStateException();
        if (wordID < 0 || topicAssignment.size() <= wordID) {
            throw new IllegalArgumentException();
        }
        return topicAssignment.get(wordID);
    }
    
    public void initialize(int docLength, int numTopics, long seed) {
        if (docLength <= 0 || numTopics <= 0) {
            throw new IllegalArgumentException();
        }
        
        Random random = new Random(seed);
        topicAssignment = random.ints(docLength, 0, numTopics)
                                .boxed()
                                .collect(Collectors.toList());
        ready = true;
    }
}
