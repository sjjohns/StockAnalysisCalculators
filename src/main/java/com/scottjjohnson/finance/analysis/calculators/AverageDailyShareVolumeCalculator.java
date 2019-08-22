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

public class AverageDailyShareVolumeCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AverageDailyShareVolumeCalculator.class);

    /**
     * Calculates the average volume
     *
     * @param quotes pre-sorted List of quotes
     * @param days   number of days in the past for which to calculate the average. Only days when the market is open are counted
     *
     * @return average daily share volume
     */
    public long calculate(List<DailyQuoteBean> quotes, int days) {

        int numberOfQuotes = quotes.size();
        int daysToLookBack = Math.min(numberOfQuotes, days);

        List<DailyQuoteBean> sublist = quotes.subList(numberOfQuotes - daysToLookBack, numberOfQuotes);

        return sublist.stream().mapToLong(DailyQuoteBean::getVolume).sum() / daysToLookBack;
    }
}
