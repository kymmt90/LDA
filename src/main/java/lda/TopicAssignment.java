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
        if (wordID < 0 || topicID < 0) {
            throw new IllegalArgumentException();
        }
        topicAssignment.set(wordID, topicID);
    }
    
    public Integer get(int wordID) {
        if (!ready) throw new IllegalStateException();
        if (wordID < 0) throw new IllegalArgumentException();
        return topicAssignment.get(wordID);
    }
    
    public void initialize(int docLength, int numTopics, long seed) {
        Random random = new Random(seed);
        topicAssignment = random.ints(docLength, 0, numTopics)
                                .boxed()
                                //.mapToObj(id -> topics.get(id))
                                .collect(Collectors.toList());
        ready = true;
    }
}
