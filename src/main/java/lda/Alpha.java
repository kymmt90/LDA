package lda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Alpha {
    private List<Double> alphas;
    
    public Alpha(double alpha, int numTopics) {
        this.alphas = Stream.generate(() -> alpha)
                            .limit(numTopics)
                            .collect(Collectors.toList());
    }

    public double get(int i) {
        return alphas.get(i);
    }
    
    public void set(int i, double value) {
        alphas.set(i, value);
    }
}
