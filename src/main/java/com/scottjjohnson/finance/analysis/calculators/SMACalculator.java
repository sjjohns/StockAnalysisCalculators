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

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;

public class SMACalculator {

    /**
     * Scans a list of quote beans for the SMA
     *
     * @param quotes list of stock quotes
     * @param days   number of days in the past for which to calculate the SMA. Only days when the market is open are counted
     *
     * @return simple moving average
     */
    public double calculate(List<DailyQuoteBean> quotes, int days) {
        int numberOfQuotes = quotes.size();
        int daysToLookBack = Math.min(numberOfQuotes, days);

        List<DailyQuoteBean> sublist = quotes.subList(numberOfQuotes - daysToLookBack, numberOfQuotes);

        return sublist.stream().mapToDouble(DailyQuoteBean::getClose).sum() / daysToLookBack;
    }
}
