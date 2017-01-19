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
  * Calculates a stock's up/down volume ratio. It's the sum of the volume on up days divided by the volume on down (or
  * unchanged) days based on the stock's closing price.
  */
class UpDownVolumeRatioCalculator {

    /**
      * Calculate the up/down volume
      *
      * @param quotes pre-sorted list of quotes
      * @param days   number of days to look back
      *
      * @return up/down volume ratio
      */
    def calculate(quotes: java.util.List[DailyQuoteBean], days: Int): Double = {

        val daysToLookBack = math.min(quotes.size - 1, days)
        val rangeForCalculation = quotes.subList(quotes.size - daysToLookBack - 1, quotes.size)
        var previousQuote = rangeForCalculation.remove(0); // grab the first bean as "previous"

        var sumOfUpVolume = 0l
        var sumOfDownVolume = 0l

        rangeForCalculation.foreach { quote => {
            if (quote.getAdjustedClose > previousQuote.getAdjustedClose)
                sumOfUpVolume += quote.getVolume
            else
                sumOfDownVolume += quote.getVolume

            previousQuote = quote
        }
        }

        if (sumOfDownVolume > 0l)
            sumOfUpVolume.toFloat / sumOfDownVolume
        else
            Double.MaxValue
    }

}
