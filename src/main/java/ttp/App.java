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
import ttp.algorithm.TabuSearch;
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
import ttp.model.params.GeneticParams;
import ttp.model.params.PropertyGeneticParamsProvider;
import ttp.model.params.PropertyTabuSearchParamsProvider;
import ttp.model.params.TabuSearchParams;
import ttp.model.wrapper.ProblemInfo;
import ttp.presenter.ConsoleResultPresenter;
import ttp.presenter.ResultPresenter;
import ttp.presenter.XChartResultPresenter;
import ttp.utils.StatisticsUtils;

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

    public void runInternal(CommandLine line) {
        long runs = Long.parseLong(line.getOptionValue(OPTION_NUMBER_SHORT));
        String resource = BASE_CASES_DIRECTORY + "/" + line.getOptionValue(OPTION_PROBLEM_SHORT);
        String propertyFile = Paths.get(line.getOptionValue(OPTION_PROPERTIES_SHORT)).normalize().toString();
        PropertyLoader propertyLoader = PropertyLoaderFactory.getInstance(DEFAULT_PROPERTIES);
        GeneticParams geneticParams = null;
        TabuSearchParams tabuSearchParams = null;
        Loader loader = LoaderFactory.getInstance();
        Problem problem = null;
        try {
            problem = loader.load(resource);
            Properties properties = propertyLoader.load(propertyFile);
            geneticParams = PropertyGeneticParamsProvider.forProperties(properties);
            tabuSearchParams = PropertyTabuSearchParamsProvider.forProperties(properties);
        } catch (LoadException e) {
            e.printStackTrace(epw);
            return;
        }
        pw.println(problem);
        pw.println(geneticParams);
        pw.println(tabuSearchParams);

        ProblemInfo problemInfo = ProblemInfo.of(problem);
        FitnessFunction fittnessFunction = TtpFitnessFunction.instance();
        KnapsackSolver knapsackSolver = CachedKnapsachSolver.instance(SimpleGreedyKnapsackSolver.instance(problemInfo));
        Algorithm<Population> geneticAlgorithm = GeneticAlgorithm.instance(fittnessFunction, geneticParams,
                knapsackSolver);
        List<List<Population>> geneticAlgorithmSolution = Stream.generate(() -> geneticAlgorithm.solve(problemInfo))
                .limit(runs).collect(Collectors.toList());

        Algorithm<Individual> tabuSearch = TabuSearch.instance(fittnessFunction, tabuSearchParams, knapsackSolver);
        List<List<Individual>> tabuSearchSolution = Stream.generate(() -> tabuSearch.solve(problemInfo))
                .limit(runs * tabuSearchParams.getMultiplier()).collect(Collectors.toList());

        List<Statistics> gaStatistics = StatisticsUtils.analyzeMultiplePopulationLists(geneticAlgorithmSolution);
        List<Statistics> tabuStatistics = StatisticsUtils.analyzeMultipleIndividualLists(tabuSearchSolution);
        XChartResultPresenter.instance("ga.png", CHART_WIDTH, CHART_HEIGHT).present(gaStatistics);
        XChartResultPresenter.instance("tabu.png", CHART_WIDTH, CHART_HEIGHT).present(tabuStatistics);
        ResultPresenter consolePresenter = ConsoleResultPresenter.instance(pw);
        pw.println("GeneticAlgorithm statistics:");
        consolePresenter.present(gaStatistics);
        pw.println("TabuSearch statistics:");
        consolePresenter.present(tabuStatistics);
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
