package lda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LDAInferenceProperties {
    PropertiesLoader loader = new PropertiesLoader();
    private Properties properties;
    
    public LDAInferenceProperties() {
        this.properties = new Properties();
    }
    
    /**
     * Load properties.
     * @param fileName
     * @throws IOException
     * @throws NullPointerException fileName is null
     */
    public void load(String fileName) throws IOException {
        if (fileName == null) throw new NullPointerException();
        InputStream stream = loader.getInputStream(fileName);
        if (stream == null) throw new NullPointerException();
        properties.load(stream);
    }
    
    public Long seed() {
        return Long.parseLong(properties.getProperty("seed"));
    }

    public Integer numIteration() {
        return Integer.parseInt(properties.getProperty("numIteration"));
    }
}

class PropertiesLoader {
    public InputStream getInputStream(String fileName) throws FileNotFoundException {
        if (fileName == null) throw new NullPointerException();
        return new FileInputStream(fileName);
    }
}
