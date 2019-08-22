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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;

/**
 * Calculates the average true range of a stock for a given period.
 *
 * @see <a href="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr">Average True Range</a>
 */
public class AverageTrueRangeCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AverageTrueRangeCalculator.class);

    /**
     * Calculates the average true range
     *
     * @param quotes pre-sorted List of quotes
     * @param days   number of days in the past for which to calculate the average. Only days when the market is open are
     *               counted
     *
     * @return average true range
     */
    public double calculate(List<DailyQuoteBean> quotes, int days) {

        double sumOfPercentRanges = 0;

        int numberOfQuotes = quotes.size();
        int daysToLookBack = Math.min(numberOfQuotes - 1, days); // we need the previous day's closing price so we can't
        // look back more than quotes.size() - 1 days.

        for (int i = numberOfQuotes - 1; i >= numberOfQuotes - daysToLookBack; i--) {

            DailyQuoteBean currentQuote = quotes.get(i);
            DailyQuoteBean previousQuote = quotes.get(i - 1);

            sumOfPercentRanges += calculateDayPriceRange(currentQuote, previousQuote);
        }

        return sumOfPercentRanges / daysToLookBack;
    }

    private double calculateDayPriceRange(DailyQuoteBean currentQuote, DailyQuoteBean previousQuote) {

        double highLowRange = currentQuote.getHigh() - currentQuote.getLow();
        double highPreviousCloseRange = Math.abs(currentQuote.getHigh() - previousQuote.getClose());
        double lowPreviousCloseRange = Math.abs(currentQuote.getLow() - previousQuote.getClose());

        return Math.max(highLowRange, Math.max(highPreviousCloseRange, lowPreviousCloseRange));
    }
}
