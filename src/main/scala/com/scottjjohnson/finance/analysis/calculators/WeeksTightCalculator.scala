/*
 * Copyright 2014 Scott J. Johnson (http://scottjjohnson.com)
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

import com.scottjjohnson.finance.analysis.beans.{WeeklyQuoteBean, WeeksTightBean}
import com.scottjjohnson.util.DateUtils
import org.apache.commons.math3.stat.regression.SimpleRegression
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

/**
  * Implements an algorithm for finding a Weeks Tight chart pattern.
  *
  * A Weeks Tight is a chart pattern where a stock's closing price is within a range of about 1% for at least 3 weeks.
  *
  * A breakout from a Weeks Tight pattern is a lower risk opportunity to add to an existing stock position. For
  * aggressive investors it can be used to start a position.
  *
  * The buy point is 10 cents above the highest intraday stock price during the pattern. To be buyable, the breakout
  * should be on volume at least 40% greater than the 50 day average volume. Prior to the Weeks Tight, the stock should
  * have had a significant prior uptrend after breaking out of a proper base (for example, a cup with handle).
  *
  * This algorithm checks for closing price within 1.025%, that the buy point is above the 50 day SMA and that the stock
  * had a prior uptrend with a trendline with a 30 degree slope. Before buying any Weeks Tight reported by this program,
  * verify the stock has broken out of a proper base and made significant gains prior to the Weeks Tight forming.
  *
  * @see <a href="http://education.investors.com/investors-corner/629900-3-weeks-tight-can-bolster-your-returns.htm">3
  *      Weeks Tight Can Bolster Your Returns</a>
  *
  *      New rule: If the intra-week high for the final 2 weeks of a WT are 2.5% or more below the high for the previous week
  *      and the week after the WT is within that intra-week high, then ignore the previous week when calculating pivot.
  *      Example: SLXP in July 2014. 3WT with intra-week highs of 140.49, 136.62, and 134.24. High for the Week after WT
  *      completed was 136. So pivot is 136.62 not 140.49.
  * @see <a href="http://news.investors.com/investing-sector-leaders-review/072814-710645-salix-pharmaceuticals-forms-pattern.htm">
  *      More info.</a>
  *
  */
class WeeksTightCalculator {

    private val LOGGER = LoggerFactory.getLogger(classOf[WeeksTightCalculator])

    private val MIN_LENGTH_OF_A_WEEKS_TIGHT_IN_WEEKS = 3

    // Weeks Tight Tolerance is 1.5%
    private val WEEKS_TIGHT_TOLERANCE = 0.015d

    // ratio for ignoring deviations in high early in pattern.
    private val SLXP_RULE_DEVIATION = 1.025d

    // how steep of an uptrend is required prior to WT
    private val MINIMUM_SLOPE_OF_TRENDLINE_UPTREND = 0.03d

    // period over which there must be an uptrend for WT to be considered
    private val MINIMUM_UPTREND_PERIOD_IN_WEEKS = 6

    /**
      * Find a Weeks Tight pattern
      *
      * @param quotes           quotes list
      * @param maxAgeInWeeks    how far back the calculator should scan in weeks.  The most recent Friday counts as week 1
      * @param fiftyDaySMAPrice Current week's 50 day SMA of the stock price. Used to drop WT with buy point < than the 50 day SMA
      *
      * @return Weeks Tight bean  if one is found, otherwise null
      */
    def calculate(quotes: java.util.List[WeeklyQuoteBean], maxAgeInWeeks: Int, fiftyDaySMAPrice: Double): WeeksTightBean = {

        var wt: WeeksTightBean = null

        if (quotes != null && quotes.size > 0) {

            wt = scanForWeeksTight(quotes, maxAgeInWeeks)

            // if the buy point is below the 50 day SMA then drop it.
            if (wt != null && wt.getBuyPoint < fiftyDaySMAPrice) {
                wt = null
            }
        } else {
            LOGGER.warn("No weekly quotes are available for symbol {}", (if (quotes == null) null else quotes(0).getSymbol))
        }

        wt
    }

    /**
      * Iterates over the quote list looking for a Weeks Tight
      *
      * @param quotes list of stock quote beans
      *
      * @return tuple of Weeks Tight bean (if on is found else null) and max high price
      */
    protected def scanForWeeksTight(quotes: java.util.List[WeeklyQuoteBean], maxAgeInWeeks: Int): WeeksTightBean = {

        var maxHighPriceAfterCurrentWeek = Double.MinValue
        var minClosePriceAfterCurrentWeek = Double.MaxValue
        var wt: WeeksTightBean = null
        var i: Int = quotes.size

        val lastWeekToScan = math.max(MIN_LENGTH_OF_A_WEEKS_TIGHT_IN_WEEKS - 1, quotes.size - maxAgeInWeeks)

        // loop through each week in reverse order starting at the current week-ending.
        // stop at i=2 because minimum weeks tight is 3. Only look back MAX_AGE_IN_WEEKS_OF_WT weeks.
        while (wt == null && i > lastWeekToScan) {

            i -= 1

            val possibleWeekPatternCompleted = quotes(i)

            val results = checkWeekForWeeksTight(quotes, i, maxHighPriceAfterCurrentWeek)

            wt = results._1
            maxHighPriceAfterCurrentWeek = results._2

            if (wt == null) {

                // if we're not in a WT save the high for this week in case we need it for the special "SLXP" rule
                // mentioned in the Javadoc.
                maxHighPriceAfterCurrentWeek = possibleWeekPatternCompleted.getAdjustedHigh

                // also save the lowest close prior to the WT
                minClosePriceAfterCurrentWeek = math.min(minClosePriceAfterCurrentWeek, possibleWeekPatternCompleted.getAdjustedClose)
            }
        }


        if (wt != null && minClosePriceAfterCurrentWeek < wt.getLowestPrice()) {
            wt = null
        }

        wt
    }

    /**
      * Determines if a stock's weekly closing prices are trending up
      *
      * @param quotes list of stock quote beans
      *
      * @return true if the weekly closing prices are trending up, otherwise false
      */
    private def isStockInUptrend(quotes: java.util.List[WeeklyQuoteBean]): Boolean = {

        val r = new SimpleRegression(true)

        val numberOfQuotes = quotes.size

        // Normalize all of the prices relative to the first quote's closing price.
        val firstClosingPrice = quotes(0).getAdjustedClose

        var d = 0.0d
        quotes.foreach { quote =>

            r.addData(d / numberOfQuotes, quote.getAdjustedClose() / firstClosingPrice)
            d += 1.0
        }


        r.getSlope() >= MINIMUM_SLOPE_OF_TRENDLINE_UPTREND
    }

    /**
      * Checks the specified week and X prior weeks to see if they fit the criteria for a weeks tight pattern
      *
      * @param quotes                      list of stock quote beans
      * @param indexOfPossiblePatternStart index of where the check should begin
      * @param maxHighWeekAfterWT          maximum intraday high that the calculator has see so far as we move back in time
      *
      * @return true if the weekly closing prices are trending up, otherwise false
      */
    private def checkWeekForWeeksTight(quotes: java.util.List[WeeklyQuoteBean], indexOfPossiblePatternStart: Int, maxHighWeekAfterWT: Double): (WeeksTightBean, Double) = {

        var wt: WeeksTightBean = null
        val possibleWeekPatternCompleted = quotes(indexOfPossiblePatternStart)
        val today = DateUtils.getStockExchangeCalendar.getTime

        var j = indexOfPossiblePatternStart

        if (!possibleWeekPatternCompleted.getWeekEndingDate.after(today)) {

            var minClose = possibleWeekPatternCompleted.getAdjustedClose
            var maxClose = possibleWeekPatternCompleted.getAdjustedClose
            var maxHigh = possibleWeekPatternCompleted.getAdjustedHigh
            var minLow = possibleWeekPatternCompleted.getAdjustedLow

            var maxVariance = 0.0d
            var variance = 0.0d

            // for the week-ending, see how far back we can go and be within
            // tolerance for a weeks tight
            while (j > 0 && variance <= WEEKS_TIGHT_TOLERANCE) {
                j -= 1

                val priorWeek = quotes(j)

                minClose = math.min(minClose, priorWeek.getAdjustedClose)
                maxClose = math.max(maxClose, priorWeek.getAdjustedClose)

                variance = math.abs((minClose / maxClose) - 1.0d)
                maxVariance = math.max(maxVariance, variance)

                if (variance <= WEEKS_TIGHT_TOLERANCE) {
                    val patternLength = indexOfPossiblePatternStart - j + 1
                    minLow = math.min(minLow, priorWeek.getAdjustedLow)

                    // This is the special "SLXP" rule mentioned above. It ignores drops > 2.5% in the week high earlier
                    // than 2 weeks into the pattern.
                    if (!(patternLength >= 3
                            && priorWeek.getAdjustedHigh >= SLXP_RULE_DEVIATION * maxHigh
                            && maxHighWeekAfterWT < maxHigh)) {
                        maxHigh = math.max(maxHigh, priorWeek.getAdjustedHigh)
                    }

                    // make sure we're at least 3 weeks into the Weeks Tight
                    if (patternLength >= MIN_LENGTH_OF_A_WEEKS_TIGHT_IN_WEEKS &&
                            isStockInUptrend(quotes.subList(math.max(j - MINIMUM_UPTREND_PERIOD_IN_WEEKS, 0), j + 1))) {
                        wt = new WeeksTightBean()
                        wt.setPatternEndingDate(possibleWeekPatternCompleted.getWeekEndingDate)
                        wt.setSymbol(possibleWeekPatternCompleted.getSymbol)
                        wt.setWeeksTightLength(patternLength)
                        wt.setHighestPrice(maxHigh)
                        wt.setLowestPrice(minLow)
                        wt.setMaxPriceRangePercent(maxVariance * 100.0d)
                    }
                }
            }
        }

        (wt, maxHighWeekAfterWT)
    }
}

