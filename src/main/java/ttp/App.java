package ttp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import ttp.algorithm.fitness.CachedFitnessFunction;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.fitness.TtpFitnessFunction;
import ttp.algorithm.greedy.CachedKnapsackSolver;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.algorithm.greedy.SimpleGreedyKnapsackSolver;
import ttp.app.OptionsProvider;
import ttp.app.runner.GeneticAlgorithmRunner;
import ttp.app.runner.HybridRunner;
import ttp.app.runner.SimulatedAnnealingRunner;
import ttp.app.runner.TabuSearchRunner;
import ttp.loader.exception.LoadException;
import ttp.loader.problem.Loader;
import ttp.loader.problem.LoaderFactory;
import ttp.loader.properties.PropertyLoader;
import ttp.loader.properties.PropertyLoaderFactory;
import ttp.model.Problem;
import ttp.model.params.GeneticParams;
import ttp.model.params.HybridParams;
import ttp.model.params.PropertyGeneticParamsProvider;
import ttp.model.params.PropertyHybridParamsProvider;
import ttp.model.params.PropertySimulatedAnnealingParamsProvider;
import ttp.model.params.PropertyTabuSearchParamsProvider;
import ttp.model.params.SimulatedAnnealingParams;
import ttp.model.params.TabuSearchParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ConsoleResultPresenter;
import ttp.presenter.GaXChartResultPresenter;
import ttp.presenter.TsSaXChartResultPresenter;

@SuppressWarnings("unused")
public class App {

    private static final String DEFAULT_PROPERTIES = "default.properties";
    private static final String BASE_CASES_DIRECTORY = "cases";
    private static final String INDEX_FILE = "index";

    private static final int CHART_WIDTH = 1280;
    private static final int CHART_HEIGHT = 960;

    private final PrintWriter pw;
    private final PrintWriter epw;

    private final Options options;
    private final HelpFormatter helpFormatter = new HelpFormatter();
    private final CommandLineParser parser = new DefaultParser();

    private final String cases;

    public App(PrintWriter pw, PrintWriter epw) {
        this.pw = pw;
        this.epw = epw;
        this.options = OptionsProvider.prepareOptions();
        this.cases = prepareCasesInfo();
    }

    @SuppressWarnings("all")
    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out);
        PrintWriter epw = new PrintWriter(System.out);
        App app = new App(pw, epw);
        CommandLine line = app.parseInput(args);
        app.run(line);
        pw.close();
        epw.close();
    }

    public void runInternal(CommandLine line) {
        int runs = Integer.parseInt(line.getOptionValue(OptionsProvider.OPTION_NUMBER_SHORT));
        String resource = BASE_CASES_DIRECTORY + "/" + line.getOptionValue(OptionsProvider.OPTION_PROBLEM_SHORT);
        String propertyFile = Paths.get(line.getOptionValue(OptionsProvider.OPTION_PROPERTIES_SHORT)).normalize()
                .toString();
        PropertyLoader propertyLoader = PropertyLoaderFactory.getInstance(DEFAULT_PROPERTIES);
        GeneticParams geneticParams;
        TabuSearchParams tabuSearchParams;
        SimulatedAnnealingParams simulatedAnnealingParams;
        HybridParams hybridParams;
        Loader loader = LoaderFactory.getInstance();
        Problem problem;
        try {
            problem = loader.load(resource);
            Properties properties = propertyLoader.load(propertyFile);
            geneticParams = PropertyGeneticParamsProvider.forProperties(properties);
            tabuSearchParams = PropertyTabuSearchParamsProvider.forProperties(properties);
            simulatedAnnealingParams = PropertySimulatedAnnealingParamsProvider.forProperties(properties);
            hybridParams = PropertyHybridParamsProvider.forProperties(properties);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(problem);
        pw.println(geneticParams);
        pw.println(tabuSearchParams);
        pw.println(simulatedAnnealingParams);
        pw.println(hybridParams);

        ProblemInfo problemInfo = ProblemInfo.of(problem);
        FitnessFunction fitnessFunction = CachedFitnessFunction.instance(TtpFitnessFunction.instance(problemInfo));
        KnapsackSolver knapsackSolver = CachedKnapsackSolver.instance(SimpleGreedyKnapsackSolver.instance(problemInfo));

        //runGeneticAlgorithm(problemInfo, geneticParams, fitnessFunction, knapsackSolver, runs);
        //runTabuSearch(problemInfo, tabuSearchParams, fitnessFunction, knapsackSolver, runs);
        //runSimulatedAnnealing(problemInfo, simulatedAnnealingParams, fitnessFunction, knapsackSolver, runs);
        runHybrid(problemInfo, geneticParams, simulatedAnnealingParams, fitnessFunction, knapsackSolver, runs,
                hybridParams);
    }

    private String prepareCasesInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator())//
                .append("Available cases:")//
                .append(System.lineSeparator());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(//
                Optional.ofNullable(
                        getClass().getClassLoader().getResourceAsStream(BASE_CASES_DIRECTORY + "/" + INDEX_FILE))
                        .orElseThrow(IOException::new)))) {
            return builder.append(br.lines().collect(Collectors.joining(System.lineSeparator()))).toString();
        } catch (IOException e) {
            epw.println("Resource management exception");
            return "";
        }
    }

    private void printHelp() {
        helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, "ttp", null, options, HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD, null);
        pw.println(cases);
    }

    private CommandLine parseInput(String[] args) {
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            epw.println("Exception parsing input: " + Arrays.toString(args));
        }
        return line;
    }

    public void run(CommandLine line) {
        if (line == null || line.hasOption(OptionsProvider.OPTION_HELP_SHORT)) {
            printHelp();
            return;
        }
        runInternal(line);
    }

    private void runGeneticAlgorithm(ProblemInfo problemInfo, GeneticParams geneticParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        GeneticAlgorithmRunner runner = new GeneticAlgorithmRunner(problemInfo, geneticParams, fitnessFunction,
                knapsackSolver, pw);
        runner.run(//
                runs,//
                Arrays.asList(//
                        GaXChartResultPresenter.instance("ga.png", CHART_WIDTH, CHART_HEIGHT),//
                        ConsoleResultPresenter.instance(pw)//
                )//
        );
    }

    private void runTabuSearch(ProblemInfo problemInfo, TabuSearchParams tabuSearchParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        TabuSearchRunner runner = new TabuSearchRunner(problemInfo, tabuSearchParams, fitnessFunction, knapsackSolver,
                pw);
        runner.run(//
                runs,//
                Arrays.asList(//
                        TsSaXChartResultPresenter.instance("ts.png", CHART_WIDTH, CHART_HEIGHT),//
                        ConsoleResultPresenter.instance(pw)//
                )//
        );
    }

    private void runSimulatedAnnealing(ProblemInfo problemInfo, SimulatedAnnealingParams simulatedAnnealingParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        SimulatedAnnealingRunner runner = new SimulatedAnnealingRunner(problemInfo, simulatedAnnealingParams,
                fitnessFunction, knapsackSolver, pw);
        runner.run(//
                runs,//
                Arrays.asList(//
                        TsSaXChartResultPresenter.instance("ts.png", CHART_WIDTH, CHART_HEIGHT),//
                        ConsoleResultPresenter.instance(pw)//
                )//
        );
    }

    private void runHybrid(ProblemInfo problemInfo, GeneticParams geneticParams,
            SimulatedAnnealingParams simulatedAnnealingParams, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, int runs, HybridParams hybridParams) {
        HybridRunner runner = new HybridRunner(problemInfo, geneticParams, fitnessFunction, knapsackSolver, pw,
                simulatedAnnealingParams, hybridParams);
        runner.run(//
                runs,//
                Arrays.asList(//
                        GaXChartResultPresenter.instance("ga.png", CHART_WIDTH, CHART_HEIGHT),//
                        ConsoleResultPresenter.instance(pw)//
                )//
        );
    }
}
