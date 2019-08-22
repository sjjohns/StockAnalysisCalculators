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

import java.util.Comparator;
import java.util.List;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;

public class MaxPriceCalculator {

    /**
     * Calculates the highest intraday price for the given quotes
     *
     * @param quotes List of quotes
     *
     * @return max price
     */
    public double calculate(List<DailyQuoteBean> quotes) {
        return quotes.stream().map(DailyQuoteBean::getHigh).max(Comparator.naturalOrder()).orElse(0.0d);
    }

}
