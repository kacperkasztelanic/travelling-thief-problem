package ttp;

import java.io.File;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
import ttp.model.Problem;
import ttp.model.ProblemInfo;
import ttp.model.PropertyGeneticParamsProvider;

public class App {

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_PROBLEM_SHORT = "p";
    private static final String OPTION_PROPERTIES_SHORT = "g";
    private static final String OPTION_CASES_SHORT = "l";

    private static final String BASE_CASES_DIRECTORY = "cases";

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
        try {
            File[] files = new File(getClass().getClassLoader().getResource(BASE_CASES_DIRECTORY).toURI()).listFiles();
            return Arrays.stream(files).map(File::getName).collect(Collectors.joining(System.lineSeparator()));
        } catch (URISyntaxException e) {
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
        opts.addOption(Option.builder(OPTION_CASES_SHORT).longOpt("listCases").desc("list available cases").build());
        return opts;
    }

    private void printHelp() {
        helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, "ttp", null, options, HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD, null);
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
        if (line.hasOption(OPTION_CASES_SHORT)) {
            pw.println(cases);
            return;
        }
        String resource = Paths.get(BASE_CASES_DIRECTORY).resolve(line.getOptionValue(OPTION_PROBLEM_SHORT)).normalize()
                .toString();
        String propertyFile = Paths.get(line.getOptionValue(OPTION_PROPERTIES_SHORT)).normalize().toString();
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
        CommandLine line = app.parseInput(args);
        app.run(line);
        pw.close();
        epw.close();
    }
}
