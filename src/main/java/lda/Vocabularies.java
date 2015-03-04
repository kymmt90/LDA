package lda;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Vocabularies {
    private List<Vocabulary> vocabs;
    
    public Vocabularies(String filePath) {
        if (filePath == null) throw new NullPointerException();

        try {
            Path vocabFilePath = Paths.get(filePath);
            List<String> lines = Files.readAllLines(vocabFilePath);
            vocabs = lines.stream().map(v -> new Vocabulary(lines.indexOf(v) + 1, v))
                                   .collect(Collectors.toList());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public Vocabulary get(int id) {
        return vocabs.get(id - 1);
    }
    
    public int size() {
        return vocabs.size();
    }
}
