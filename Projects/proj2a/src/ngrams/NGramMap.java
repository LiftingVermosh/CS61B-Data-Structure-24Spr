package ngrams;

import java.util.*;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

import edu.princeton.cs.algs4.In;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    // 每个词项的计数
    private HashMap<String, TimeSeries> counts4EachToken;
    // 每年所有词项的计数
    private TreeMap<Integer, Long> counts4TokensYearly;
    // 缓存计数的 TimesSeries 对象
    private TimeSeries totalCountsTS;

    /**
     * Constructs an NGramMap from WORDSFIL ENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        counts4EachToken = new HashMap<>();
        counts4TokensYearly = new TreeMap<>();
        totalCountsTS = new TimeSeries();
        In in4words = new In(wordsFilename);
        In in4counts = new In(countsFilename);

        while(in4words.hasNextLine()) {
            String curLine = in4words.readLine();
            String [] data =  curLine.split("\t");

            if (data.length != 4) {
                throw new IllegalArgumentException("Wrong number of words in words file");
            }

            String word = data[0];
            int year = Integer.parseInt(data[1]);
            int count = Integer.parseInt(data[2]);

            if (year < MIN_YEAR || year > MAX_YEAR) {
                throw new IllegalArgumentException("Wrong year in words file");
            }

            if (!counts4EachToken.containsKey(word)) {
                TimeSeries ts = new TimeSeries();
                ts.put(year, count * 1.0);
                counts4EachToken.put(word, ts);
//                System.out.println("counts4EachToken has add a new K-V:("+word+","+count+")" );
            } else {
                counts4EachToken.get(word).put(year, count * 1.0);
            }
        }

        while (in4counts.hasNextLine()) {
            String curLine = in4counts.readLine();
            String [] data =  curLine.split(",");
            if (data.length != 4) {
                throw new IllegalArgumentException("Wrong number of words in words file");
            }

            int year = Integer.parseInt(data[0]);
            long count = Long.parseLong(data[1]);

            if (year < MIN_YEAR || year > MAX_YEAR) {
                throw new IllegalArgumentException("Wrong year in words file");
            }

            if (!counts4TokensYearly.containsKey(year)) {
                counts4TokensYearly.put(year, (long)count);
            } else {
                counts4TokensYearly.put(year, counts4TokensYearly.get(year) + count);
            }
        }

        for(Integer year: counts4TokensYearly.keySet()) {
            double total = counts4TokensYearly.get(year).doubleValue();
            totalCountsTS.put(year, total);
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (startYear < MIN_YEAR || startYear > MAX_YEAR) {
            throw new IllegalArgumentException("Wrong year in Arguments");
        }

        if (!counts4EachToken.containsKey(word)) {
            return new TimeSeries();
        } else {
            return new TimeSeries(counts4EachToken.get(word), startYear, endYear);
        }
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCountsTS, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        if (startYear < MIN_YEAR || startYear > MAX_YEAR) {
            throw new IllegalArgumentException("Wrong year in Arguments");
        }
        TimeSeries ts = countHistory(word, startYear, endYear);
        if (ts == null || ts.isEmpty()) {
            return new TimeSeries();
        }
        return ts.dividedBy(totalCountHistory());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        int n =  words.size();
        TimeSeries ts = new TimeSeries();
        for (String word : words) {
            ts = ts.plus(weightHistory(word, startYear, endYear));
        }
        return ts;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }
}
