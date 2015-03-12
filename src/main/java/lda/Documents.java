package lda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Documents {
    private List<Document> documents;
    
    public Documents(LDA lda) {
        if (lda == null) throw new NullPointerException();
        
        documents = new ArrayList<>();
        for (int d = 1; d <= lda.getBow().getNumDocs(); ++d) {
            List<Vocabulary> vocabList = getVocabularyList(d, lda.getBow(), lda.getVocabularies());
            Document doc = new Document(d, lda.getNumTopics(), vocabList);
            documents.add(doc);
        }
    }
    
    private List<Vocabulary> getVocabularyList(int docID, BagOfWords bow, Vocabularies vocabs) {
        assert docID > 0 && bow != null && vocabs != null;
        return bow.getWords(docID).stream()
                                  .map(id -> vocabs.get(id))
                                  .collect(Collectors.toList());
    }

    public int getTopicID(int docID, int wordID) {
        return documents.get(docID - 1).getTopicID(wordID);
    }
    
    public void setTopicID(int docID, int wordID, int topicID) {
        documents.get(docID - 1).setTopicID(wordID, topicID);
    }
    
    public Vocabulary getVocab(int docID, int wordID) {
        return documents.get(docID - 1).getVocabulary(wordID);
    }
    
    public List<Vocabulary> getWords(int docID) {
        return documents.get(docID - 1).getWords();
    }
    
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }
    
    public void incrementTopicCount(int docID, int topicID) {
        documents.get(docID - 1).incrementTopicCount(topicID);
    }
    
    public void decrementTopicCount(int docID, int topicID) {
        documents.get(docID - 1).decrementTopicCount(topicID);
    }
    
    public int getTopicCount(int docID, int topicID) {
        return documents.get(docID - 1).getTopicCount(topicID);
    }
    
    public double getTheta(int docID, int topicID, double alpha, double sumAlpha) {
        if (docID <= 0 || documents.size() < docID) throw new IllegalArgumentException();
        return documents.get(docID - 1).getTheta(topicID, alpha, sumAlpha);
    }
    
    void initializeTopicAssignment(Topics topics, long seed) {
        for (Document d : getDocuments()) {
            d.initializeTopicAssignment(topics.numTopics(), seed);
            for (int w = 0; w < d.getDocLength(); ++w) {
                final int topicID = d.getTopicID(w);
                final Topic topic = topics.get(topicID);
                final Vocabulary vocab = d.getVocabulary(w);
                topic.incrementVocabCount(vocab.id());
            }
        }
    }
}
