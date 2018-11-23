package ttp.loader.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.loader.exception.LoadException;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PropertyLoaderImpl implements PropertyLoader {

    private final String defaultPropertiesResource;

    @Override
    public Properties load(String file) throws LoadException {
        Properties defaultProperties = null;
        try {
            defaultProperties = loadPropertiesFromResource(defaultPropertiesResource);
        } catch (IOException e) {
            throw new LoadException("Exception reading resource: " + defaultPropertiesResource, e);
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties properties = new Properties(defaultProperties);
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            return new Properties(defaultProperties);
        }
    }

    private Properties loadPropertiesFromResource(String resource) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resource)) {
            properties.load(is);
        }
        return properties;
    }
}
