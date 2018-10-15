package ttp.loader.problem;

public class LoaderFactory {

    public static Loader getInstance() {
        return new LoaderImpl();
    }

    private LoaderFactory() {
    }
}
