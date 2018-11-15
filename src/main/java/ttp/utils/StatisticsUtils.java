package ttp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.Statistics;

@UtilityClass
public class StatisticsUtils {

    private static final double DEFAULT = Double.NaN;

    public static Statistics analyzeMultipleIndividualLists(List<List<Individual>> results) {
        DoubleStatistics internalStatistics = results.stream().map(StatisticsUtils::maxValue)
                .collect(DoubleStatistics.collector());
        return Statistics.of(internalStatistics.getMin(), internalStatistics.getMax(), internalStatistics.getAverage(),
                internalStatistics.getStandardDeviation());
    }

    public static Statistics analyzeMultiplePopulationLists(List<List<Population>> results) {
        return analyzeMultipleIndividualLists(results.stream().map(l -> Arrays.asList(l.get(l.size() - 1).getMembers()))
                .collect(Collectors.toList()));
    }

    public static List<Statistics> analyzeMultiplePopulationListsEach(List<List<Population>> results) {
        return StatisticsUtils.transpose(results).stream().map(StatisticsUtils::analyzePopulations)
                .map(StatisticsUtils::analyzeStatistics).collect(Collectors.toList());
    }

    public static List<Statistics> analyzeMultipleIndividualListsEach(List<List<Individual>> results) {
        return StatisticsUtils.transpose(results).stream().map(StatisticsUtils::analyzeIndividuals)
                .collect(Collectors.toList());
    }

    private static List<Statistics> analyzePopulations(List<Population> results) {
        return results.stream().map(p -> Arrays.asList(p.getMembers())).map(StatisticsUtils::analyzeIndividuals)
                .collect(Collectors.toList());
    }

    private static Statistics analyzeIndividuals(List<Individual> results) {
        DoubleStatistics internalStatistics = results.stream().map(i -> i.getResult().getValue())
                .collect(DoubleStatistics.collector());
        return Statistics.of(internalStatistics.getMin(), internalStatistics.getMax(), internalStatistics.getAverage(),
                internalStatistics.getStandardDeviation());
    }

    private static Statistics analyzeStatistics(List<Statistics> statistics) {
        double avgMin = statistics.stream().mapToDouble(Statistics::getMinValue).average().orElse(DEFAULT);
        double avgMax = statistics.stream().mapToDouble(Statistics::getMaxValue).average().orElse(DEFAULT);
        double avgAvg = statistics.stream().mapToDouble(Statistics::getAvgValue).average().orElse(DEFAULT);
        double avgStdDev = statistics.stream().mapToDouble(Statistics::getStdDev).average().orElse(DEFAULT);
        return Statistics.of(avgMin, avgMax, avgAvg, avgStdDev);
    }

    private static double maxValue(List<Individual> results) {
        return results.stream().mapToDouble(i -> i.getResult().getValue()).max().orElse(DEFAULT);
    }

    private static <T> List<List<T>> transpose(List<List<T>> table) {
        List<List<T>> result = new ArrayList<>();
        final int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            List<T> col = new ArrayList<>();
            for (List<T> row : table) {
                col.add(row.get(i));
            }
            result.add(col);
        }
        return result;
    }
}
