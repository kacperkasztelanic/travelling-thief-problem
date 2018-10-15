package ttp.loader.properties;

public class PropertyLoaderFactory {

    public static PropertyLoader getInstance(String defaultPropertiesResource) {
        return new PropertyLoaderImpl(defaultPropertiesResource);
    }

    private PropertyLoaderFactory() {
    }
}
