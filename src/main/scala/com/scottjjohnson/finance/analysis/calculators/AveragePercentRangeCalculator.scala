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
import scala.language.postfixOps

/**
  * Calculates the average true range of a stock for a given period.
  *
  * @see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr
  *
  */
class AveragePercentRangeCalculator {

    /**
      * Calculates the average true range
      *
      * @param quotes pre-sorted List of quotes
      * @param days   number of days in the past for which to calculate the average. Only days when the market is open are counted
      *
      * @return average true range
      */
    def calculate(quotes: java.util.List[DailyQuoteBean], days: Int): Double = {

        @annotation.tailrec
        def recurse(p: DailyQuoteBean, q: List[DailyQuoteBean], accum: Double): Double = {
            q match {
                case head :: tail => recurse(head, tail, accum + calculateDayRange(head, p))
                case Nil => accum
            }
        }

        val daysToLookBack = math.min(quotes.size - 1, days)
        val rangeForCalculation = quotes.subList(quotes.size - daysToLookBack - 1, quotes.size).toList
        val sumOfPercentRanges = recurse(rangeForCalculation.head, rangeForCalculation.tail, 0)


        (sumOfPercentRanges / daysToLookBack) toFloat
    }

    private def calculateDayRange(quote: DailyQuoteBean, previousQuote: DailyQuoteBean): Double = {

        val highLowRange = quote.getAdjustedHigh - quote.getAdjustedLow
        val highPreviousCloseRange = quote.getAdjustedHigh - previousQuote.getAdjustedClose
        val lowPreviousCloseRange = quote.getAdjustedLow - previousQuote.getAdjustedClose

        math.max(highLowRange, math.max(highPreviousCloseRange, lowPreviousCloseRange)) / previousQuote.getAdjustedClose
    }

}

