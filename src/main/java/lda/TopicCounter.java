package lda;

public class TopicCounter {
    private AssignmentCounter topicCount;

    public TopicCounter(int numTopics) {
        this.topicCount = new AssignmentCounter(numTopics);
    }

    public int getTopicCount(int topicID) {
        return topicCount.get(topicID);
    }
    
    public int getDocLength() {
        return topicCount.getSum();
    }
    
    public void incrementTopicCount(int topicID) {
        topicCount.increment(topicID);
    }
    
    public void decrementTopicCount(int topicID) {
        topicCount.decrement(topicID);
    }

    public int size() {
        return topicCount.size();
    }
}
