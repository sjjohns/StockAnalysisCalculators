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
  * Calculates the average volume over the given period.
  */
class AverageShareVolumeCalculator {

    /**
      * Calculates the average volume
      *
      * @param quotes pre-sorted List of quotes
      * @param days   number of days in the past for which to calculate the average. Only days when the market is open are counted
      *
      * @return average daily share volume
      */
    def calculate(quotes: java.util.List[DailyQuoteBean], days: Int): Long = {

        val daysToLookBack = math.min(quotes.size, days)

        val sumOfVolumes = quotes.subList(quotes.size - daysToLookBack, quotes.size).map(_.getVolume).sum

        sumOfVolumes / daysToLookBack
    }

}
