package lda;

public class Vocabulary {
    final int id;
    String vocabulary;
    
    public Vocabulary(int id, String vocabulary) {
        this.id         = id;
        this.vocabulary = vocabulary;
    }
    
    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return vocabulary;
    }
}
