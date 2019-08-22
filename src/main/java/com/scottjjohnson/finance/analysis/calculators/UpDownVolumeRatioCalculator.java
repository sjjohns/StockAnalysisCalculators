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
 * Calculates a stock's up/down volume ratio. It's the sum of the volume on up days divided by the volume on down (or
 * unchanged) days based on the stock's closing price.
 */
public class UpDownVolumeRatioCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpDownVolumeRatioCalculator.class);

    /**
     * Calculate the up/down volume
     *
     * @param quotes pre-sorted list of quotes
     * @param days   number of days to look back
     *
     * @return up/down volume ratio
     */
    public float calculate(List<DailyQuoteBean> quotes, int days) {

        long upVolume = 0;
        long downVolume = 0;

        int numberOfQuotes = quotes.size();
        int daysToLookBack = Math.min(numberOfQuotes - 1, days); // we need the previous day's closing price so we can't
        // look back more than quotes.size() - 1 days.

        for (int i = numberOfQuotes - 1; i >= numberOfQuotes - daysToLookBack; i--) {

            DailyQuoteBean quote = quotes.get(i);
            DailyQuoteBean previousQuote = quotes.get(i - 1);

            if (quote.getClose() > previousQuote.getClose()) {
                upVolume += quote.getVolume();
            } else if (quote.getClose() <= previousQuote.getClose()) {
                downVolume += quote.getVolume();
            }
        }

        float ratio;

        if (downVolume == 0) // shouldn't happen except for new IPOs...
            ratio = Integer.MAX_VALUE;
        else
            ratio = (float) (((double) upVolume) / ((double) downVolume));

        return ratio;
    }
}
