package lda;

public class Topic {
    private final int id;
    private VocabularyCounter counter;
    
    public Topic(int id, int numVocabs) {
        if (id < 0 || numVocabs <= 0) throw new IllegalArgumentException();
        this.id = id;
        this.counter = new VocabularyCounter(numVocabs);
    }

    public int id() {
        return id;
    }
    
    public int getVocabCount(int vocabID) {
        return counter.getVocabCount(vocabID);
    }
    
    public int getSumCount() {
        return counter.getSumCount();
    }
    
    public void incrementVocabCount(int vocabID) {
        counter.incrementVocabCount(vocabID);
    }
    
    public void decrementVocabCount(int vocabID) {
        counter.decrementVocabCount(vocabID);
    }
}
