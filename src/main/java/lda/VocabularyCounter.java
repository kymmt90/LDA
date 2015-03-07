package lda;

public class VocabularyCounter {
    private AssignmentCounter vocabCount;
    private int sumCount;
    
    public VocabularyCounter(int numVocabs) {
        this.vocabCount = new AssignmentCounter(numVocabs);
        this.sumCount = 0;
    }

    public int getVocabCount(int vocabID) {
        return vocabCount.get(vocabID - 1);
    }
    
    public int getSumCount() {
        return sumCount;
    }
    
    public void incrementVocabCount(int vocabID) {
        vocabCount.increment(vocabID - 1);
        ++sumCount;
    }
    
    public void decrementVocabCount(int vocabID) {
        vocabCount.decrement(vocabID - 1);
        --sumCount;
    }
}
