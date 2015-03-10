package lda;

import java.util.Collections;
import java.util.List;

public class Words {
    private List<Vocabulary> words;
    
    public Words(List<Vocabulary> words) {
        if (words == null) throw new NullPointerException();
        this.words = words; 
    }

    public int getNumWords() {
        return words.size();
    }
    
    public Vocabulary get(int id) {
        if (id < 0 || words.size() <= id) {
            throw new IllegalArgumentException();
        }
        return words.get(id);
    }
    
    public List<Vocabulary> getWords() {
        return Collections.unmodifiableList(words);
    }
}
