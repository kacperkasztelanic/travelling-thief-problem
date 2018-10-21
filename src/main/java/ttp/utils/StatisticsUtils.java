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

    public static List<Statistics> analyzeMultiple(List<List<Population>> results) {
        return StatisticsUtils.transpose(results).stream().map(StatisticsUtils::analyze)
                .map(StatisticsUtils::analyzeStatistics).collect(Collectors.toList());
    }

    public static List<Statistics> analyze(List<Population> results) {
        return results.stream().map(StatisticsUtils::analyze).collect(Collectors.toList());
    }

    public static Statistics analyze(Population results) {
        DoubleSummaryStatistics internalStatistics = Arrays.stream(results.getMembers()).map(Individual::getResult)
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