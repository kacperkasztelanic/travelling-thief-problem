package ttp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import ttp.model.Individual;
import ttp.model.Population;
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

    private static class DoubleStatistics extends DoubleSummaryStatistics {

        private double sumOfSquare = 0.0d;
        private double sumOfSquareCompensation;
        private double simpleSumOfSquare;

        public static Collector<Double, ?, DoubleStatistics> collector() {
            return Collector.of(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
        }

        @Override
        public void accept(double value) {
            super.accept(value);
            double squareValue = value * value;
            simpleSumOfSquare += squareValue;
            sumOfSquareWithCompensation(squareValue);
        }

        public DoubleStatistics combine(DoubleStatistics other) {
            super.combine(other);
            simpleSumOfSquare += other.simpleSumOfSquare;
            sumOfSquareWithCompensation(other.sumOfSquare);
            sumOfSquareWithCompensation(other.sumOfSquareCompensation);
            return this;
        }

        public final double getStandardDeviation() {
            return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - Math.pow(getAverage(), 2)) : 0.0d;
        }

        private void sumOfSquareWithCompensation(double value) {
            double tmp = value - sumOfSquareCompensation;
            double velvel = sumOfSquare + tmp;
            sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
            sumOfSquare = velvel;
        }

        private double getSumOfSquare() {
            double tmp = sumOfSquare + sumOfSquareCompensation;
            if (Double.isNaN(tmp) && Double.isInfinite(simpleSumOfSquare)) {
                return simpleSumOfSquare;
            }
            return tmp;
        }
    }
}
