package ttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ttp.algorithm.Algorithm;
import ttp.algorithm.GeneticAlgorithm;
import ttp.algorithm.ImproveStrategy;
import ttp.algorithm.SimulatedAnnealing;
import ttp.algorithm.TabuSearch;
import ttp.algorithm.fitness.CachedFitnessFunction;
import ttp.algorithm.fitness.FitnessFunction;
import ttp.algorithm.fitness.TtpFitnessFunction;
import ttp.algorithm.greedy.CachedKnapsachSolver;
import ttp.algorithm.greedy.KnapsackSolver;
import ttp.algorithm.greedy.SimpleGreedyKnapsackSolver;
import ttp.loader.exception.LoadException;
import ttp.loader.problem.Loader;
import ttp.loader.problem.LoaderFactory;
import ttp.loader.properties.PropertyLoader;
import ttp.loader.properties.PropertyLoaderFactory;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.Problem;
import ttp.model.Statistics;
import ttp.model.factory.HybridIndividualFactory;
import ttp.model.factory.IndividualFactory;
import ttp.model.factory.SimpleIndividualFactory;
import ttp.model.params.GeneticParams;
import ttp.model.params.PropertyGeneticParamsProvider;
import ttp.model.params.PropertySimulatedAnnealingParamsProvider;
import ttp.model.params.PropertyTabuSearchParamsProvider;
import ttp.model.params.SimulatedAnnealingParams;
import ttp.model.params.TabuSearchParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.GaXChartResultPresenter;
import ttp.presenter.TsSaXChartResultPresenter;
import ttp.utils.StatisticsUtils;

@SuppressWarnings("unused")
public class App {

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_PROBLEM_SHORT = "p";
    private static final String OPTION_PROPERTIES_SHORT = "g";
    private static final String OPTION_NUMBER_SHORT = "n";

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
        this.options = prepareOptions();
        this.cases = prepareCasesInfo();
    }

    private String prepareCasesInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator()).append("Available cases:").append(System.lineSeparator());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Optional
                .ofNullable(getClass().getClassLoader().getResourceAsStream(BASE_CASES_DIRECTORY + "/" + INDEX_FILE))
                .orElseThrow(IOException::new)))) {
            return builder.append(br.lines().collect(Collectors.joining(System.lineSeparator()))).toString();
        } catch (IOException e) {
            epw.println("Resource management exception");
            return "";
        }
    }

    private Options prepareOptions() {
        Options opts = new Options();
        opts.addOption(Option.builder(OPTION_HELP_SHORT).longOpt("help").desc("print this message").build());
        opts.addOption(Option.builder(OPTION_PROBLEM_SHORT).longOpt("problem").desc("problem to solve").hasArg()
                .argName("PROBLEM").valueSeparator().required().build());
        opts.addOption(Option.builder(OPTION_PROPERTIES_SHORT).longOpt("geneticProps").desc("genetic properties file")
                .hasArg().argName("FILE").valueSeparator().required().build());
        opts.addOption(Option.builder(OPTION_NUMBER_SHORT).longOpt("numberOfRuns").desc("number of runs").hasArg()
                .argName("N").valueSeparator().required().build());
        return opts;
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
        if (line == null || line.hasOption(OPTION_HELP_SHORT)) {
            printHelp();
            return;
        }
        runInternal(line);
    }

    private void runGeneticAlgorithm(ProblemInfo problemInfo, GeneticParams geneticParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        IndividualFactory individualFactory = SimpleIndividualFactory.instance(problemInfo, knapsackSolver,
                fitnessFunction);
        Algorithm<Population> geneticAlgorithm = GeneticAlgorithm.instance(geneticParams, individualFactory);
        List<List<Population>> geneticAlgorithmSolution = Stream.generate(() -> geneticAlgorithm.solve(problemInfo))
                .limit(runs).collect(Collectors.toList());
        List<Statistics> gaStatistics = StatisticsUtils.analyzeMultiplePopulationListsEach(geneticAlgorithmSolution);
        GaXChartResultPresenter.instance("ga.png", CHART_WIDTH, CHART_HEIGHT).present(gaStatistics);
        // ResultPresenter consolePresenter = ConsoleResultPresenter.instance(pw);
        pw.println("GeneticAlgorithm statistics:");
        // consolePresenter.present(gaStatistics);
        Statistics res = StatisticsUtils.analyzeMultiplePopulationLists(geneticAlgorithmSolution);
        pw.println(res);
    }

    private void runTabuSearch(ProblemInfo problemInfo, TabuSearchParams tabuSearchParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        IndividualFactory individualFactory = SimpleIndividualFactory.instance(problemInfo, knapsackSolver,
                fitnessFunction);
        Algorithm<Individual> tabuSearch = TabuSearch.instance(tabuSearchParams, individualFactory);
        List<List<Individual>> tabuSearchSolution = Stream.generate(() -> tabuSearch.solve(problemInfo)).limit(runs)
                .collect(Collectors.toList());
        List<Statistics> tabuStatistics = StatisticsUtils.analyzeMultipleIndividualListsEach(tabuSearchSolution);
        TsSaXChartResultPresenter.instance("ts.png", CHART_WIDTH, CHART_HEIGHT).present(tabuStatistics);
        // ResultPresenter consolePresenter = ConsoleResultPresenter.instance(pw);
        pw.println("TabuSearch statistics:");
        // consolePresenter.present(tabuStatistics);
        Statistics res = StatisticsUtils.analyzeMultipleIndividualLists(tabuSearchSolution);
        pw.println(res);
    }

    private void runSimulatedAnnealing(ProblemInfo problemInfo, SimulatedAnnealingParams simulatedAnnealingParams,
            FitnessFunction fitnessFunction, KnapsackSolver knapsackSolver, int runs) {
        IndividualFactory individualFactory = SimpleIndividualFactory.instance(problemInfo, knapsackSolver,
                fitnessFunction);
        Algorithm<Individual> simulatedAnnealing = SimulatedAnnealing.instance(simulatedAnnealingParams,
                individualFactory);
        List<List<Individual>> simulatedAnnealingSolution = Stream.generate(() -> simulatedAnnealing.solve(problemInfo))
                .limit(runs).collect(Collectors.toList());
        List<Statistics> saStatistics = StatisticsUtils.analyzeMultipleIndividualListsEach(simulatedAnnealingSolution);
        TsSaXChartResultPresenter.instance("sa.png", CHART_WIDTH, CHART_HEIGHT).present(saStatistics);
        // ResultPresenter consolePresenter = ConsoleResultPresenter.instance(pw);
        pw.println("SimulatedAnnealing statistics:");
        // consolePresenter.present(saStatistics);
        Statistics res = StatisticsUtils.analyzeMultipleIndividualLists(simulatedAnnealingSolution);
        pw.println(res);
    }

    private void runHybrid(ProblemInfo problemInfo, GeneticParams geneticParams,
            SimulatedAnnealingParams simulatedAnnealingParams, FitnessFunction fitnessFunction,
            KnapsackSolver knapsackSolver, int runs) {
        HybridIndividualFactory individualFactory = HybridIndividualFactory.instance(problemInfo, knapsackSolver,
                fitnessFunction);
        ImproveStrategy improveStrategy = SimulatedAnnealing.instance(simulatedAnnealingParams, individualFactory);
        individualFactory.setImproveStrategy(improveStrategy);
        Algorithm<Population> geneticAlgorithm = GeneticAlgorithm.instance(geneticParams, individualFactory);
        List<List<Population>> geneticAlgorithmSolution = Stream.generate(() -> geneticAlgorithm.solve(problemInfo))
                .limit(runs).collect(Collectors.toList());
        List<Statistics> gaStatistics = StatisticsUtils.analyzeMultiplePopulationListsEach(geneticAlgorithmSolution);
        GaXChartResultPresenter.instance("ga.png", CHART_WIDTH, CHART_HEIGHT).present(gaStatistics);
        // ResultPresenter consolePresenter = ConsoleResultPresenter.instance(pw);
        pw.println("GeneticAlgorithm statistics:");
        // consolePresenter.present(gaStatistics);
        Statistics res = StatisticsUtils.analyzeMultiplePopulationLists(geneticAlgorithmSolution);
        pw.println(res);
    }

    public void runInternal(CommandLine line) {
        int runs = Integer.parseInt(line.getOptionValue(OPTION_NUMBER_SHORT));
        String resource = BASE_CASES_DIRECTORY + "/" + line.getOptionValue(OPTION_PROBLEM_SHORT);
        String propertyFile = Paths.get(line.getOptionValue(OPTION_PROPERTIES_SHORT)).normalize().toString();
        PropertyLoader propertyLoader = PropertyLoaderFactory.getInstance(DEFAULT_PROPERTIES);
        GeneticParams geneticParams = null;
        TabuSearchParams tabuSearchParams = null;
        SimulatedAnnealingParams simulatedAnnealingParams = null;
        Loader loader = LoaderFactory.getInstance();
        Problem problem = null;
        try {
            problem = loader.load(resource);
            Properties properties = propertyLoader.load(propertyFile);
            geneticParams = PropertyGeneticParamsProvider.forProperties(properties);
            tabuSearchParams = PropertyTabuSearchParamsProvider.forProperties(properties);
            simulatedAnnealingParams = PropertySimulatedAnnealingParamsProvider.forProperties(properties);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(problem);
        pw.println(geneticParams);
        pw.println(tabuSearchParams);
        pw.println(simulatedAnnealingParams);

        ProblemInfo problemInfo = ProblemInfo.of(problem);
        FitnessFunction fitnessFunction = CachedFitnessFunction.instance(TtpFitnessFunction.instance(problemInfo));
        KnapsackSolver knapsackSolver = CachedKnapsachSolver.instance(SimpleGreedyKnapsackSolver.instance(problemInfo));

        // runGeneticAlgorithm(problemInfo, geneticParams, fitnessFunction,
        // knapsackSolver, runs);
        // runTabuSearch(problemInfo, tabuSearchParams, fitnessFunction, knapsackSolver,
        // runs);
        // runSimulatedAnnealing(problemInfo, simulatedAnnealingParams, fitnessFunction,
        // knapsackSolver, runs);
        runHybrid(problemInfo, geneticParams, simulatedAnnealingParams, fitnessFunction, knapsackSolver, runs);
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
}
