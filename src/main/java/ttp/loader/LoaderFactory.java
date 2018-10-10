package ttp.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoaderFactory {

    public static Loader getInstance() {
        return new LoaderImpl();
    }
}
