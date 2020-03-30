package ttp.app;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OptionsProvider {

    public static final String OPTION_HELP_SHORT = "h";
    public static final String OPTION_PROBLEM_SHORT = "p";
    public static final String OPTION_PROPERTIES_SHORT = "g";
    public static final String OPTION_NUMBER_SHORT = "n";

    public static Options prepareOptions() {
        Options opts = new Options();
        Stream.of(helpOption(), problemOption(), propertiesOption(), numberOption()).forEach(opts::addOption);
        return opts;
    }

    private static Option helpOption() {
        return Option.builder(OPTION_HELP_SHORT)//
                .longOpt("help")//
                .desc("print this message")//
                .build();
    }

    private static Option problemOption() {
        return Option.builder(OPTION_PROBLEM_SHORT)//
                .longOpt("problem")//
                .desc("problem to solve")//
                .hasArg()//
                .argName("PROBLEM")//
                .valueSeparator()//
                .required()//
                .build();
    }

    private static Option propertiesOption() {
        return Option.builder(OPTION_PROPERTIES_SHORT)//
                .longOpt("geneticProps")//
                .desc("genetic properties file")//
                .hasArg()//
                .argName("FILE")//
                .valueSeparator()//
                .required()//
                .build();
    }

    private static Option numberOption() {
        return Option.builder(OPTION_NUMBER_SHORT)//
                .longOpt("numberOfRuns")//
                .desc("number of runs")//
                .hasArg()//
                .argName("N")//
                .valueSeparator()//
                .required()//
                .build();
    }
}
