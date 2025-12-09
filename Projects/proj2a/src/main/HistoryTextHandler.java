package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;
import java.util.StringJoiner;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private final NGramMap ngordnet;

    public HistoryTextHandler(NGramMap ngordnet) {
        this.ngordnet = ngordnet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startyear = q.startYear();
        int endyear = q.endYear();
        StringBuilder sb = new StringBuilder();
        for(String word : words) {

            TimeSeries ts = ngordnet.weightHistory(word, startyear, endyear);
            if(ts == null) {
                sb.append("No requested result found!Because ts is null\n");
                return sb.toString();
            }else if(ts.isEmpty()){
                String skipWordString = "Ignoring word '" + word + "' as its history is empty.\n";
                sb.append(skipWordString);
                continue;
            }

            sb.append(word);
            sb.append(": ");
//            ts = new TimeSeries(ts, startyear, endyear);
            sb.append(ts.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
