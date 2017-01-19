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

import com.scottjjohnson.finance.analysis.beans.{DailyQuoteBean, PriceRangeBean}

import scala.collection.JavaConversions._

/**
  * Calculates the minimum price and the maximum after that minimum. This is useful for finding stocks that have made
  * large moves and are possible short candidates.
  */
class PriceRangeCalculator {

    /**
      * Calculates the minimum price and subsequent max price
      *
      * @param quotes pre-sorted List of quotes
      *
      * @return bean containing the min/max price and dates when those events occurred
      */
    def calculate(quotes: java.util.List[DailyQuoteBean]): PriceRangeBean = {

        // I'm going through the list twice, but the extra time required is negligible. About 1-2ms. And the code is much simpler.
        val maxIntradayPriceBean = quotes.reduce((q1, q2) => {
            if (q1.getAdjustedHigh > q2.getAdjustedHigh) q1 else q2
        })
        val minIntradayPriceBean = quotes.reduce((q1, q2) => {
            if (q1.getAdjustedLow < q2.getAdjustedLow) q1 else q2
        })

        val result = new PriceRangeBean()
        result.setMaxPrice(maxIntradayPriceBean.getAdjustedHigh)
        result.setMaxPriceDate(maxIntradayPriceBean.getDate)
        result.setMinPrice(minIntradayPriceBean.getAdjustedLow)
        result.setMinPriceDate(minIntradayPriceBean.getDate)

        result
    }

}
