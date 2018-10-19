package ttp.presenter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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
import ttp.statistics.Statistics;

@AllArgsConstructor(staticName = "instance")
public class XChartResultPresenter implements ResultPresenter {

    private final String file;
    private final int width;
    private final int height;

    @Override
    public void present(List<Statistics> results) {
        XYChart chart = prepareChart(results, "Travelling Thief Problem", width, height);
        try {
            saveChartAsPng(chart, file);
        } catch (IOException e) {
            // nothing to do here
        }
    }

    private XYChart prepareChart(List<Statistics> statistics, String title, int width, int height) {
        XYChart chart = new XYChartBuilder().width(width).height(height).title(title).xAxisTitle("Generation")
                .yAxisTitle("Value").build();
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax((double) statistics.size());
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setAxisTitlesVisible(true);
        for (ChartSeries chartSeries : prepareSeriesCollection(statistics)) {
            chart.addSeries(chartSeries.getLabel(), chartSeries.getXSeries(), chartSeries.getYSeries());
        }
        return chart;
    }

    private void saveChartAsPng(XYChart chart, String file) throws IOException {
        BitmapEncoder.saveBitmap(chart, file, BitmapFormat.PNG);
    }

    private List<ChartSeries> prepareSeriesCollection(List<Statistics> statistics) {
        List<Number> xSeries = IntStream.range(0, statistics.size()).boxed().collect(Collectors.toList());
        ChartSeries minSeries = ChartSeries.of("minValue", xSeries, prepareSeries(statistics, Statistics::getMinValue));
        ChartSeries maxSeries = ChartSeries.of("maxValue", xSeries, prepareSeries(statistics, Statistics::getMaxValue));
        ChartSeries avgSeries = ChartSeries.of("avgValue", xSeries, prepareSeries(statistics, Statistics::getAvgValue));
        return Arrays.asList(minSeries, maxSeries, avgSeries);
    }

    private List<Number> prepareSeries(List<Statistics> statistics, Function<Statistics, Number> extractor) {
        return statistics.stream().map(extractor::apply).collect(Collectors.toList());
    }

    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode(of = { "label" })
    @ToString(includeFieldNames = false, of = { "label" })
    private static class ChartSeries {

        @Getter
        private final String label;
        @Getter
        private final List<Number> xSeries;
        @Getter
        private final List<Number> ySeries;
    }
}
