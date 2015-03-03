package lda;

public class LDAHyperparameters {
    private Alpha alpha;
    private Beta beta;
    
    public LDAHyperparameters(double alpha, double beta, int numTopics, int numVocabs) {
        this.alpha = new Alpha(alpha, numTopics);
        this.beta  = new Beta(beta, numVocabs);
    }
    
    public LDAHyperparameters(double alpha, double beta, int numTopics) {
        this.alpha = new Alpha(alpha, numTopics);
        this.beta  = new Beta(beta);
    }

    public double alpha(int i) {
        return alpha.get(i);
    }
    
    public double sumAlpha() {
        return alpha.sum();
    }
    
    public double beta() {
        return beta.get();
    }
    
    public double beta(int i) {
        return beta.get(i);
    }
    
    public void setAlpha(int i, double value) {
        alpha.set(i, value);
    }
    
    public void setBeta(int i, double value) {
        beta.set(i, value);
    }
    
    public void setBeta(double value) {
        beta.set(0, value);
    }
}
