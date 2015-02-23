package lda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Beta {
    private List<Double> betas;
    
    public Beta(double beta, int numVocabs) {
        this.betas = Stream.generate(() -> beta)
                           .limit(numVocabs)
                           .collect(Collectors.toList());
    }
    
    public Beta(double beta) {
        this.betas = Arrays.asList(beta);
    }

    public double get() {
        return get(0);
    }
    
    public double get(int i) {
        return betas.get(i);
    }
    
    public void set(int i, double value) {
        betas.set(i, value);
    }
}
