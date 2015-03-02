package lda;

import java.util.Collections;
import java.util.List;

public class Words {
    private List<Vocabulary> words;
    
    public Words(List<Vocabulary> words) {
        this.words = words; 
    }

    public int getNumWords() {
        return words.size();
    }
    
    public Vocabulary get(int id) {
        return words.get(id);
    }
    
    public List<Vocabulary> getWords() {
        return Collections.unmodifiableList(words);
    }
}
