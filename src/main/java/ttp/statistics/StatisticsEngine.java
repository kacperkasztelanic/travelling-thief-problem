package ttp.statistics;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ttp.model.Individual;
import ttp.model.Population;
import ttp.model.Result;

@UtilityClass
public class StatisticsEngine {

    public static List<Statistics> analyze(List<Population> results) {
        return results.stream().map(StatisticsEngine::analyze).collect(Collectors.toList());
    }

    public static Statistics analyze(Population results) {
        DoubleSummaryStatistics internalStatistics = Arrays.stream(results.getMembers()).map(Individual::getResult)
                .collect(Collectors.summarizingDouble(Result::getValue));
        return Statistics.of(internalStatistics.getMin(), internalStatistics.getMax(), internalStatistics.getAverage());
    }
}
