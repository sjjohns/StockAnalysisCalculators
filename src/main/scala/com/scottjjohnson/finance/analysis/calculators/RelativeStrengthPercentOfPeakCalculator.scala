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

import java.util.Date

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean
import com.scottjjohnson.util.DateUtils

import scala.collection.JavaConversions._
import scala.language.postfixOps

/**
  * Calculates a stock's relative strength (RS). This calculator calculates a ratio of a stock's closing price to that of
  * another stock or index (e.g. SPX) over a period of days. The calculator returns the current ratio as a percent of the
  * highest value. This is useful in technical analysis as stocks with a high RS often continue to outperform the market.
  */
class RelativeStrengthPercentOfPeakCalculator {

    /**
      * Calculates the ratio of current relative strength vs peak relative strength
      *
      * @param quotes           pre-sorted List of quotes
      * @param comparisonQuotes pre-sorted List of quotes for the stock/ETF/index that the stock should be compared to
      * @param years            number of years in the past for which to calculate the ratio
      *
      * @return
      */
    def calculate(quotes: java.util.List[DailyQuoteBean], comparisonQuotes: java.util.Map[Date, DailyQuoteBean], years: Int): Double = {

        var maxRSRatio = 0.0
        var currentRSRatio = 0.0
        val numberOfQuotes = quotes.size()

        val filterDate = DateUtils.addYearsToDate(new Date(), -years)

        if (numberOfQuotes > 0) {

            val currentQuote = quotes(numberOfQuotes - 1)
            currentRSRatio = calculateRSRatio(currentQuote, comparisonQuotes.getOrElse(currentQuote.getDate, null))

            quotes.filter(_.getDate.after(filterDate)).foreach { quote => {
                maxRSRatio = math.max(maxRSRatio, calculateRSRatio(quote, comparisonQuotes.getOrElse(quote.getDate, null)))
            }
            }

        }

        if (maxRSRatio != 0)
            (currentRSRatio / maxRSRatio) toFloat
        else
            0
    }

    private def calculateRSRatio(quote: DailyQuoteBean, comparisonQuote: DailyQuoteBean): Double = {

        if (comparisonQuote != null) {
            quote.getAdjustedClose / comparisonQuote.getAdjustedClose * 100
        } else {
            0.0d
        }
    }

}

