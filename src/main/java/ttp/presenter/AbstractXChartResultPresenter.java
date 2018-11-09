package ttp.presenter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ttp.model.Statistics;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractXChartResultPresenter implements ResultPresenter {

    protected static final String TITLE = "Traveling Thief Problem";
    protected static final String Y_LABEL = "Value";

    protected final String file;
    protected final int width;
    protected final int height;
    protected final String xLabel;

    @Override
    public void present(List<Statistics> results) {
        XYChart chart = prepareChart(results);
        try {
            saveChartAsPng(chart, file);
        } catch (IOException e) {
            // nothing to do here
        }
    }

    protected XYChart prepareChart(List<Statistics> statistics) {
        XYChart chart = new XYChartBuilder().width(width).height(height).title(TITLE).xAxisTitle(xLabel)
                .yAxisTitle(Y_LABEL).build();
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax((double) statistics.size());
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setAxisTitlesVisible(true);
        for (ChartSeries chartSeries : prepareSeriesCollection(statistics)) {
            chart.addSeries(chartSeries.getLabel(), chartSeries.getXSeries(), chartSeries.getYSeries(),
                    chartSeries.getErrors().orElse(null));
        }
        return chart;
    }

    protected void saveChartAsPng(XYChart chart, String file) throws IOException {
        BitmapEncoder.saveBitmap(chart, file, BitmapFormat.PNG);
    }

    protected abstract List<ChartSeries> prepareSeriesCollection(List<Statistics> statistics);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = { "label" })
    @ToString(includeFieldNames = false, of = { "label" })
    protected static class ChartSeries {

        public static ChartSeries of(String label, List<? extends Number> xSeries, List<? extends Number> ySeries,
                List<? extends Number> errors) {
            return new ChartSeries(label, xSeries, ySeries, Optional.ofNullable(errors));
        }

        public static ChartSeries of(String label, List<? extends Number> xSeries, List<? extends Number> ySeries) {
            return of(label, xSeries, ySeries, null);
        }

        @Getter
        private final String label;
        @Getter
        private final List<? extends Number> xSeries;
        @Getter
        private final List<? extends Number> ySeries;
        @Getter
        private final Optional<List<? extends Number>> errors;
    }
}
