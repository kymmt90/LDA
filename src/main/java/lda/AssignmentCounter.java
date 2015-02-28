package lda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssignmentCounter {
    private List<Integer> counter;

    public AssignmentCounter(int size) {
        this.counter = IntStream.generate(() -> 0)
                                .limit(size)
                                .boxed()
                                .collect(Collectors.toList());
    }
    
    public int size() {
        return counter.size();
    }
    
    public int get(int id) {
        return counter.get(id);
    }
    
    public int getSum() {
        return counter.stream().reduce(Integer::sum).get();
    }
    
    public void increment(int id) {
        counter.set(id, counter.get(id) + 1);
    }
    
    public void decrement(int topicID) {
        counter.set(topicID, counter.get(topicID) - 1);
    }
}
