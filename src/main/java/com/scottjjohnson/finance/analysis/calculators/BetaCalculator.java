package com.scottjjohnson.finance.analysis.calculators;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;

public class BetaCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BetaCalculator.class);

    public double calculate(List<DailyQuoteBean> quotes, Map<Date, DailyQuoteBean> comparisonQuotes) {

        int numberOfQuotes = quotes.size();

        double[][] combinedQuotesPercentChange = new double[2][numberOfQuotes - 1];

        // get percent change array
        double previousComparisonClose = 0.0d;
        double previousStockClose = 0.0d;
        int numberOfQuotesWithAComparison = 0;

        for (DailyQuoteBean q : quotes) {
            Date d = q.getDate();

            DailyQuoteBean comparisonQuote = comparisonQuotes.get(d);
            if (comparisonQuote != null) {

                if (previousStockClose != 0.0d) {
                    combinedQuotesPercentChange[0][numberOfQuotesWithAComparison] =
                            q.getClose() / previousStockClose - 1.0d;
                    combinedQuotesPercentChange[1][numberOfQuotesWithAComparison] =
                            comparisonQuote.getClose() / previousComparisonClose - 1.0d;
                    numberOfQuotesWithAComparison++;
                }

                previousStockClose = q.getClose();
                previousComparisonClose = comparisonQuote.getClose();

            } else {
                LOGGER.warn("Missing comparison quote for date {}", d);
            }
        }

        // calculate averages
        double stockTotal = 0.0d;
        double comparisonTotal = 0.0d;
        for (int i = 0; i < numberOfQuotesWithAComparison; i++) {
            stockTotal += combinedQuotesPercentChange[0][i];
            comparisonTotal += combinedQuotesPercentChange[1][i];
        }

        double stockAveragePercentChange = stockTotal / numberOfQuotesWithAComparison;
        double comparisonAveragePercentChange = comparisonTotal / numberOfQuotesWithAComparison;

        // calculate variance
        double totalVariance = 0.0d;
        double totalCovariance = 0.0d;
        for (int i = 0; i < numberOfQuotesWithAComparison; i++) {
            totalVariance += Math.pow(combinedQuotesPercentChange[1][i] - comparisonAveragePercentChange, 2.0d);
            totalCovariance +=
                    (combinedQuotesPercentChange[0][i] - stockAveragePercentChange) * (combinedQuotesPercentChange[1][i]
                            - comparisonAveragePercentChange);
        }

        // calculate covariance
        return totalCovariance / totalVariance;
    }

}
