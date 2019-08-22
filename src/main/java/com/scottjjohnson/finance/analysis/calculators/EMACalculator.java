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

public class EMACalculator {

    /**
     * Calculates the current day's EMA based all available quotes. The EMA is a cumulative calculation over all
     * available quotes. So the larger the quote list, the more accurate the result will be.
     *
     * @param quotes List of quote beans
     * @param days   number of days in the past for which to calculate the EMA. Only days when the market is open are counted
     *
     * @return exponential moving average
     */
    public double calculate(List<DailyQuoteBean> quotes, int days) {

        double calculatedAnswer = 0.0d;

        for (DailyQuoteBean quote : quotes) {
            calculatedAnswer = calculate(calculatedAnswer, quote.getClose(), days);
        }

        return calculatedAnswer;
    }

    /**
     * Calculates the current day's EMA based on the previous day's EMA
     *
     * @param previousEma  EMA from previous day
     * @param currentClose adjusted close for the day for which we are calculating the EMA
     * @param days         number of days in the past for which to calculate the EMA. Only days when the market is open are counted
     *
     * @return exponential moving average
     */
    public double calculate(double previousEma, double currentClose, int days) {
        double multiplier = 2.0d / (days + 1.0d);
        return (currentClose - previousEma) * multiplier + previousEma;
    }
}
