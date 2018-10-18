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

import lombok.AllArgsConstructor;
import ttp.model.Population;

@AllArgsConstructor(staticName = "instance")
public class ResultPresenter {

    public void present(List<Population> results) {
        List<Statistics> statistics = StatisticsEngine.analyze(results);
        JFreeChart chart = prepareChart(statistics);
        try {
            saveChartAsPng(chart, "chart.png", 1280, 960);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textualPresent(statistics);
    }
    
    public void textualPresent(List<Statistics> results) {
        for(Statistics stats : results) {
            System.out.println(stats);
        }
    }

    private XYSeriesCollection prepareSeriesCollection(List<Statistics> statistics) {
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(prepareSeries(statistics, Statistics::getMinValue, "minValue"));
        collection.addSeries(prepareSeries(statistics, Statistics::getMaxValue, "maxValue"));
        collection.addSeries(prepareSeries(statistics, Statistics::getAvgValue, "avgValue"));
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

    private JFreeChart prepareChart(List<Statistics> statistics) {
        XYSeriesCollection collection = prepareSeriesCollection(statistics);
        return ChartFactory.createXYLineChart("Travelling Thief Problem", "Generation", "Value", collection,
                PlotOrientation.VERTICAL, true, false, false);
    }

    private void saveChartAsPng(JFreeChart chart, String file, int width, int height) throws IOException {
        File chartFile = new File(file);
        ChartUtils.saveChartAsPNG(chartFile, chart, width, height);
    }
}
