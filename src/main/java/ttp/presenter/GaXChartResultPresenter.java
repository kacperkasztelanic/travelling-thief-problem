package ttp.presenter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ttp.model.Statistics;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GaXChartResultPresenter extends AbstractXChartResultPresenter {

    private static final String X_LABEL = "Generation";
    private static final String MIN_SERIES_LABEL = "minValue";
    private static final String MAX_SERIES_LABEL = "maxValue";
    private static final String AVG_SERIES_LABEL = "avgValue";

    public static GaXChartResultPresenter instance(String file, int width, int height) {
        return new GaXChartResultPresenter(file, width, height);
    }

    private GaXChartResultPresenter(String file, int width, int height) {
        super(file, width, height, X_LABEL);
    }

    @Override
    protected List<ChartSeries> prepareSeriesCollection(List<Statistics> statistics) {
        List<Number> xSeries = IntStream.range(0, statistics.size())//
                .boxed()//
                .collect(Collectors.toList());
        ChartSeries minSeries = ChartSeries.of(//
                MIN_SERIES_LABEL,//
                xSeries,//
                prepareSeries(statistics, Statistics::getMinValue)//
        );
        ChartSeries maxSeries = ChartSeries.of(//
                MAX_SERIES_LABEL,//
                xSeries,//
                prepareSeries(statistics, Statistics::getMaxValue)//
        );
        ChartSeries avgSeries = ChartSeries.of(AVG_SERIES_LABEL,//
                xSeries,//
                prepareSeries(statistics, Statistics::getAvgValue),//
                prepareSeries(statistics, Statistics::getStdDev)//
        );
        return Arrays.asList(minSeries, maxSeries, avgSeries);
    }

    private List<Number> prepareSeries(List<Statistics> statistics, Function<Statistics, Number> extractor) {
        return statistics.stream()//
                .map(extractor)//
                .collect(Collectors.toList());
    }
}
