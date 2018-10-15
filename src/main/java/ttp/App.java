package ttp;

import java.io.PrintWriter;

import ttp.algorithm.Algorithm;
import ttp.algorithm.FittnessFunction;
import ttp.algorithm.GeneticAlgorithm;
import ttp.algorithm.GreedyKnapsackSolver;
import ttp.algorithm.KnapsackSolver;
import ttp.loader.exception.LoadException;
import ttp.loader.problem.Loader;
import ttp.loader.problem.LoaderFactory;
import ttp.loader.properties.PropertyLoader;
import ttp.loader.properties.PropertyLoaderFactory;
import ttp.model.GeneticParams;
import ttp.model.Individual;
import ttp.model.PropertyGeneticParamsProvider;
import ttp.model.Problem;
import ttp.model.ProblemInfo;

public class App {

    private final PrintWriter pw;
    private final PrintWriter epw;

    public App(PrintWriter pw, PrintWriter epw) {
        this.pw = pw;
        this.epw = epw;
    }

    public void run() {
        String resource = "ttp/hard_4.ttp";
        String propertyFile = "example.properties";
        PropertyLoader propertyLoader = PropertyLoaderFactory.getInstance("default.properties");
        GeneticParams geneticParams = null;
        Loader loader = LoaderFactory.getInstance();
        Problem problem = null;
        try {
            geneticParams = PropertyGeneticParamsProvider.forProperties(propertyLoader.load(propertyFile));
            problem = loader.load(resource);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(geneticParams);
        pw.println(problem);
        ProblemInfo problemInfo = ProblemInfo.of(problem);
        FittnessFunction fittnessFunction = FittnessFunction.instance();
        KnapsackSolver knapsackSolver = GreedyKnapsackSolver.instance(problemInfo);
        Algorithm algorithm = GeneticAlgorithm.instance(fittnessFunction, geneticParams, knapsackSolver);
        Individual solution = algorithm.solveForBest(problemInfo);
        pw.println(solution);
        pw.println(solution.getResult());
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
