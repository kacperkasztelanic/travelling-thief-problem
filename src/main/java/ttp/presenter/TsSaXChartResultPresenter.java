package ttp.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ttp.model.Statistics;

public class TsSaXChartResultPresenter extends AbstractXChartResultPresenter {

    private static final String X_LABEL = "Iteration";
    private static final String CURRENT_LABEL = "current";
    private static final String BEST_SO_FAR_LABEL = "best so far";

    public static TsSaXChartResultPresenter instance(String file, int width, int height) {
        return new TsSaXChartResultPresenter(file, width, height);
    }

    private TsSaXChartResultPresenter(String file, int width, int height) {
        super(file, width, height, X_LABEL);
    }

    @Override
    protected List<ChartSeries> prepareSeriesCollection(List<Statistics> current) {
        List<Number> xSeries = IntStream.range(0, current.size()).boxed().collect(Collectors.toList());
        List<Double> maxValues = current.stream().map(Statistics::getMaxValue).collect(Collectors.toList());
        List<Double> bestSoFar = prepareBestSoFar(maxValues);
        ChartSeries currentSeries = ChartSeries.of(CURRENT_LABEL, xSeries, maxValues);
        ChartSeries bestSoFarSeries = ChartSeries.of(BEST_SO_FAR_LABEL, xSeries, bestSoFar);
        return Arrays.asList(currentSeries, bestSoFarSeries);
    }

    private List<Double> prepareBestSoFar(List<Double> current) {
        if(current.isEmpty()) {
            return Collections.emptyList();
        }
        List<Double> result = new ArrayList<>(current.size());
        double best = current.get(0);
        result.add(best);
        for (int i = 1; i < current.size(); i++) {
            if (best < current.get(i)) {
                best = current.get(i);
            }
            result.add(best);
        }
        return result;
    }
}
