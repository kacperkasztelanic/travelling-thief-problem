package ttp.presenter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ttp.model.Statistics;

public class JFreeChartResultPresenter extends AbstractChartResultPresenter {

    public static JFreeChartResultPresenter instance(String file, int width, int height) {
        return new JFreeChartResultPresenter(file, width, height);
    }

    private JFreeChartResultPresenter(String file, int width, int height) {
        super(file, width, height);
    }

    @Override
    public void present(List<Statistics> results) {
        JFreeChart chart = prepareChart(results);
        try {
            saveChartAsPng(chart, file, width, height);
        } catch (IOException e) {
            // nothing to do here
        }
    }

    private JFreeChart prepareChart(List<Statistics> statistics) {
        XYSeriesCollection collection = prepareSeriesCollection(statistics);
        return ChartFactory.createXYLineChart(TITLE, X_LABEL, Y_LABEL, collection, PlotOrientation.VERTICAL, true,
                false, false);
    }

    private void saveChartAsPng(JFreeChart chart, String file, int width, int height) throws IOException {
        File chartFile = new File(file);
        ChartUtils.saveChartAsPNG(chartFile, chart, width, height);
    }

    private XYSeriesCollection prepareSeriesCollection(List<Statistics> statistics) {
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(prepareSeries(statistics, Statistics::getMinValue, MIN_SERIES_LABEL));
        collection.addSeries(prepareSeries(statistics, Statistics::getMaxValue, MAX_SERIES_LABEL));
        collection.addSeries(prepareSeries(statistics, Statistics::getAvgValue, AVG_SERIES_LABEL));
        return collection;
    }

    private XYSeries prepareSeries(List<Statistics> statistics, Function<Statistics, Number> extractor, String label) {
        List<XYDataItem> minItems = IntStream.range(0, statistics.size())
                .mapToObj(i -> new XYDataItem(i, extractor.apply(statistics.get(i)))).collect(Collectors.toList());
        XYSeries xySeries = new XYSeries(label);
        for (XYDataItem item : minItems) {
            xySeries.add(item);
        }
        return xySeries;
    }
}
