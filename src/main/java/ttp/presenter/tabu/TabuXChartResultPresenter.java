package ttp.presenter.tabu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ttp.model.Statistics;
import ttp.presenter.ga.AbstractChartResultPresenter;

public class TabuXChartResultPresenter extends AbstractChartResultPresenter {

    private static final String TITLE = "Travelling Thief Problem";
    protected static final String X_LABEL = "Iteration";
    protected static final String Y_LABEL = "Value";

    public static TabuXChartResultPresenter instance(String file, int width, int height) {
        return new TabuXChartResultPresenter(file, width, height);
    }

    private TabuXChartResultPresenter(String file, int width, int height) {
        super(file, width, height);
    }

    @Override
    public void present(List<Statistics> current) {
        List<Double> maxValues = current.stream().map(Statistics::getMaxValue).collect(Collectors.toList());
        List<Double> bestSoFar = prepareBestSoFar(maxValues);
        XYChart chart = prepareChart(maxValues, bestSoFar);
        try {
            saveChartAsPng(chart, file);
        } catch (IOException e) {
            // nothing to do here
        }
    }

    private List<Double> prepareBestSoFar(List<Double> current) {
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

    private XYChart prepareChart(List<Double> current, List<Double> bestSoFar) {
        XYChart chart = new XYChartBuilder().width(width).height(height).title(TITLE).xAxisTitle(X_LABEL)
                .yAxisTitle(Y_LABEL).build();
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax((double) current.size());
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setAxisTitlesVisible(true);
        for (ChartSeries chartSeries : prepareSeriesCollection(current, bestSoFar)) {
            chart.addSeries(chartSeries.getLabel(), chartSeries.getXSeries(), chartSeries.getYSeries());
        }
        return chart;
    }

    private void saveChartAsPng(XYChart chart, String file) throws IOException {
        BitmapEncoder.saveBitmap(chart, file, BitmapFormat.PNG);
    }

    private List<ChartSeries> prepareSeriesCollection(List<Double> current, List<Double> bestSoFar) {
        List<Number> xSeries = IntStream.range(0, current.size()).boxed().collect(Collectors.toList());
        ChartSeries currentSeries = ChartSeries.of("current", xSeries, current);
        ChartSeries bestSoFarSeries = ChartSeries.of("best so far", xSeries, bestSoFar);
        return Arrays.asList(currentSeries, bestSoFarSeries);
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode(of = { "label" })
    @ToString(includeFieldNames = false, of = { "label" })
    private static class ChartSeries {

        @Getter
        private final String label;
        @Getter
        private final List<? extends Number> xSeries;
        @Getter
        private final List<? extends Number> ySeries;
    }
}
