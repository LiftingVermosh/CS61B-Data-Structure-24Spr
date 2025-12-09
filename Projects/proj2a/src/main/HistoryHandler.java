package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap ngp;

    public HistoryHandler(NGramMap ngp) {
        this.ngp = ngp;
    }

    @Override
    public String handle(NgordnetQuery q) {
        System.out.println("Got query that looks like:");
        System.out.println("Words: " + q.words());
        System.out.println("Start Year: " + q.startYear());
        System.out.println("End Year: " + q.endYear());

        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        ArrayList<String> wordLegends =  new ArrayList<>();
        ArrayList<TimeSeries> wordsInstances =  new ArrayList<>();
        for(String word : words) {
            wordLegends.add(word);
            TimeSeries curWordTS = ngp.weightHistory(word,  startYear, endYear);
            wordsInstances.add(curWordTS);
        }
        XYChart chart = Plotter.generateTimeSeriesChart(wordLegends, wordsInstances);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
