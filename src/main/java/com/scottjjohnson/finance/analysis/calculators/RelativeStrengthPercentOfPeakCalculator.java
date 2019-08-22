/*
 * Copyright 2019 Scott J. Johnson (https://scottjjohnson.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottjjohnson.finance.analysis.calculators;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.util.DateUtils;

public class RelativeStrengthPercentOfPeakCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelativeStrengthPercentOfPeakCalculator.class);

    /**
     * Calculates the ratio of current relative strength vs peak relative strength
     *
     * @param quotes           pre-sorted List of quotes
     * @param comparisonQuotes pre-sorted List of quotes for the stock/ETF/index that the stock should be compared to
     * @param years            number of years in the past for which to calculate the ratio
     *
     * @return RS Ratio Percent Of Peak
     */
    public double calculate(List<DailyQuoteBean> quotes, Map<Date, DailyQuoteBean> comparisonQuotes, int years) {

        double maxRSRatio = 0.0d;
        double minRSRatio = Double.MAX_VALUE;
        double currentRSRatio = 0.0d;
        int numberOfQuotes = quotes.size();

        // get the date 1 year prior to the last quote in the quote list. That will be our filter below.
        Date filterDate = DateUtils.addYearsToDate(quotes.get(quotes.size() - 1).getDate(), -years);

        if (numberOfQuotes > 0) {
            DailyQuoteBean currentQuote = quotes.get(numberOfQuotes - 1);
            currentRSRatio = calculateRSRatio(currentQuote, comparisonQuotes.get(currentQuote.getDate()));

            for (DailyQuoteBean quote : quotes) {
                if (quote.getDate().after(filterDate)) {
                    double ratio = calculateRSRatio(quote, comparisonQuotes.get(quote.getDate()));
                    maxRSRatio = Math.max(maxRSRatio, ratio);
                    minRSRatio = Math.min(minRSRatio, ratio);
                }
            }
        }

        if (maxRSRatio != minRSRatio)
            return (currentRSRatio - minRSRatio) / (maxRSRatio - minRSRatio) * 100.0d;
        else
            return 0.0d;
    }

    private double calculateRSRatio(DailyQuoteBean quote, DailyQuoteBean comparisonQuote) {

        if (comparisonQuote != null) {
            return quote.getClose() / comparisonQuote.getClose() * 100;
        } else {
            return 0.0d;
        }
    }
}
