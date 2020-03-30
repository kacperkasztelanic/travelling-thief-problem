package ttp.loader.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyLoaderFactory {

    public static PropertyLoader getInstance(String defaultPropertiesResource) {
        return new PropertyLoaderImpl(defaultPropertiesResource);
    }
}
