package lda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VocabularyCounter {
    private List<Integer> vocabCount;
    private int sumCount;
    
    public VocabularyCounter(int numVocabs) {
        this.vocabCount = IntStream.generate(() -> 0)
                                   .limit(numVocabs)
                                   .boxed()
                                   .collect(Collectors.toList());
        this.sumCount = 0;
    }

    public int getVocabCount(int vocabID) {
        if (vocabID <= 0 || vocabCount.size() < vocabID) {
            throw new IllegalArgumentException();
        }
        return vocabCount.get(vocabID - 1);
    }
    
    public int getSumCount() {
        return sumCount;
    }
    
    public void incrementVocabCount(int vocabID) {
        if (vocabID <= 0 || vocabCount.size() < vocabID) {
            throw new IllegalArgumentException();
        }
        vocabCount.set(vocabID - 1, vocabCount.get(vocabID - 1) + 1);
        ++sumCount;
    }
    
    public void decrementVocabCount(int vocabID) {
        if (vocabID <= 0 || vocabCount.size() < vocabID) {
            throw new IllegalArgumentException();
        }
        vocabCount.set(vocabID - 1, vocabCount.get(vocabID - 1) - 1);
        --sumCount;
    }
}
