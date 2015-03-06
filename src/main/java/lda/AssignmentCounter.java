package lda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssignmentCounter {
    private List<Integer> counter;

    public AssignmentCounter(int size) {
        if (size <= 0) throw new IllegalArgumentException();
        this.counter = IntStream.generate(() -> 0)
                                .limit(size)
                                .boxed()
                                .collect(Collectors.toList());
    }
    
    public int size() {
        return counter.size();
    }
    
    public int get(int id) {
        if (id < 0 || counter.size() <= id) {
            throw new IllegalArgumentException();
        }
        return counter.get(id);
    }
    
    public int getSum() {
        return counter.stream().reduce(Integer::sum).get();
    }
    
    public void increment(int id) {
        if (id < 0 || counter.size() <= id) {
            throw new IllegalArgumentException();
        }
        counter.set(id, counter.get(id) + 1);
    }
    
    public void decrement(int id) {
        if (id < 0 || counter.size() <= id) {
            throw new IllegalArgumentException();
        }
        if (counter.get(id) == 0) {
            throw new IllegalStateException();
        }
        counter.set(id, counter.get(id) - 1);
    }
}
