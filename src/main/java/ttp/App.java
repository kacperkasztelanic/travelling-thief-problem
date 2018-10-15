package ttp;

import java.io.PrintWriter;

import ttp.loader.exception.LoadException;
import ttp.loader.problem.Loader;
import ttp.loader.problem.LoaderFactory;
import ttp.loader.properties.PropertyLoader;
import ttp.loader.properties.PropertyLoaderFactory;
import ttp.model.GeneticParams;
import ttp.model.PropertyGeneticParamsProvider;
import ttp.model.Problem;

public class App {

    private final PrintWriter pw;
    private final PrintWriter epw;

    public App(PrintWriter pw, PrintWriter epw) {
        this.pw = pw;
        this.epw = epw;
    }

    public void run() {
        String resource = "ttp/trivial_0.ttp";
        String propertyFile = "example.properties";
        PropertyLoader propertyLoader = PropertyLoaderFactory.getInstance("default.properties");
        GeneticParams params = null;
        Loader loader = LoaderFactory.getInstance();
        Problem problem = null;
        try {
            params = PropertyGeneticParamsProvider.forProperties(propertyLoader.load(propertyFile));
            problem = loader.load(resource);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(params);
        pw.println(problem);
    }

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out);
        PrintWriter epw = new PrintWriter(System.err);
        App app = new App(pw, epw);
        app.run();
        pw.close();
        epw.close();
    }
}
