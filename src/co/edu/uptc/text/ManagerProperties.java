package co.edu.uptc.text;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ManagerProperties {
    private final Properties properties = new Properties();
    private String fileName;

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void load() throws IOException{
        properties.load(new FileInputStream(fileName));

    }
    public String getValue(String key){
        if (properties.isEmpty()){
            try {
                load();
            } catch (Exception e) {
                return null;
            }

        }
        return properties.getProperty(key);
    }
}