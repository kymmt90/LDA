package lda;

public class Vocabulary {
    private final int id;
    private String vocabulary;
    
    public Vocabulary(int id, String vocabulary) {
        if (vocabulary == null) throw new NullPointerException(); 
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
