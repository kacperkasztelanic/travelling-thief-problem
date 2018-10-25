package ttp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.Result;
import ttp.model.Statistics;

@UtilityClass
public class StatisticsUtils {

    private static final double DEFAULT = Double.NaN;

    public static List<Statistics> analyzeMultiplePopulationLists(List<List<Population>> results) {
        return StatisticsUtils.transpose(results).stream().map(StatisticsUtils::analyzePopulations)
                .map(StatisticsUtils::analyzeStatistics).collect(Collectors.toList());
    }

    public static List<Statistics> analyzeMultipleIndividualLists(List<List<Individual>> results) {
        return StatisticsUtils.transpose(results).stream().map(StatisticsUtils::analyzeIndividuals)
                .collect(Collectors.toList());
    }

    public static List<Statistics> analyzePopulations(List<Population> results) {
        return results.stream().map(p -> Arrays.asList(p.getMembers())).map(StatisticsUtils::analyzeIndividuals)
                .collect(Collectors.toList());
    }

    public static Statistics analyzeIndividuals(List<Individual> results) {
        DoubleSummaryStatistics internalStatistics = results.stream().map(Individual::getResult)
                .collect(Collectors.summarizingDouble(Result::getValue));
        return Statistics.of(internalStatistics.getMin(), internalStatistics.getMax(), internalStatistics.getAverage());
    }

    private static Statistics analyzeStatistics(List<Statistics> statistics) {
        double avgMin = statistics.stream().mapToDouble(Statistics::getMinValue).average().orElse(DEFAULT);
        double avgMax = statistics.stream().mapToDouble(Statistics::getMaxValue).average().orElse(DEFAULT);
        double avgAvg = statistics.stream().mapToDouble(Statistics::getAvgValue).average().orElse(DEFAULT);
        return Statistics.of(avgMin, avgMax, avgAvg);
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
