package lda;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Vocabularies {
    private Map<Integer, Vocabulary> vocabs;
    
    public Vocabularies(String filePath) {
        if (filePath == null) throw new NullPointerException();

        vocabs = new HashMap<>();
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            int id = 1;
            String s = null;
            while ((s = reader.readLine()) != null) {
                vocabs.put(id, new Vocabulary(id, s));
                id += 1;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public Vocabulary get(int id) {
        return vocabs.get(id);
    }
    
    public int size() {
        return vocabs.size();
    }
}
