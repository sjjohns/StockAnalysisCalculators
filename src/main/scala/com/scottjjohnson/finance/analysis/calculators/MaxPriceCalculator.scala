/*
 * Copyright 2016 Scott J. Johnson (http://scottjjohnson.com)
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

package com.scottjjohnson.finance.analysis.calculators

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean

import scala.collection.JavaConversions._

/**
  * Calculates the maximum intraday price over the range of quotes
  */
class MaxPriceCalculator {

    /**
      * Calculates the highest intraday price for the given quotes
      *
      * @param quotes List of quotes
      *
      * @return max price
      */
    def calculate(quotes: java.util.List[DailyQuoteBean]): Double = {

        quotes.reduce(maxIntradayPrice).getAdjustedHigh
    }

    private def maxIntradayPrice(q1: DailyQuoteBean, q2: DailyQuoteBean): DailyQuoteBean = if (q1.getAdjustedHigh > q2.getAdjustedHigh) q1 else q2

}
