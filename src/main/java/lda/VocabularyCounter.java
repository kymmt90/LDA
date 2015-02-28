package lda;

public class VocabularyCounter {
    private AssignmentCounter vocabCount;
    private int sumCount;
    
    public VocabularyCounter(int numVocabs) {
        this.vocabCount = new AssignmentCounter(numVocabs);
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
        vocabCount.increment(vocabID - 1);
        ++sumCount;
    }
    
    public void decrementVocabCount(int vocabID) {
        if (vocabID <= 0 || vocabCount.size() < vocabID) {
            throw new IllegalArgumentException();
        }
        vocabCount.decrement(vocabID - 1);
        --sumCount;
    }
}
