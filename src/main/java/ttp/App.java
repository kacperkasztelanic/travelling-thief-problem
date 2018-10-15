package ttp;

import java.io.PrintWriter;
import java.util.Properties;

import ttp.loader.exception.LoadException;
import ttp.loader.problem.Loader;
import ttp.loader.problem.LoaderFactory;
import ttp.loader.properties.PropertyLoader;
import ttp.loader.properties.PropertyLoaderFactory;
import ttp.model.GeneticParams;
import ttp.model.GeneticParams.GeneticParamsBuilder;
import ttp.model.GeneticProperties;
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
            params = initializeParams(propertyLoader.load(propertyFile));
            problem = loader.load(resource);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(params);
        pw.println(problem);
    }

    private GeneticParams initializeParams(Properties props) {
        GeneticParamsBuilder params = GeneticParams.builder();
        params.populationSize(Integer.parseInt(props.getProperty(GeneticProperties.POPULATION_SIZE.getKey())));
        params.numberOfGenerations(
                Integer.parseInt(props.getProperty(GeneticProperties.NUMBER_OF_GENERATIONS.getKey())));
        params.crossoverProbability(
                Double.parseDouble(props.getProperty(GeneticProperties.CROSSOVER_PROBABILITY.getKey())));
        params.mutationProbability(
                Double.parseDouble(props.getProperty(GeneticProperties.MUTATION_PROBABILITY.getKey())));
        params.tourParticipation(
                Double.parseDouble(props.getProperty(GeneticProperties.TOURNAMENT_PARTICIPATION.getKey())));
        return params.build();
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
