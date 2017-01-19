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

package com.scottjjohnson.finance.analysis.helpers;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.beans.WeeklyQuoteBean;
import com.scottjjohnson.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Contains methods to manipulate quote beans.
 */
public class QuotesHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesHelper.class);

    /**
     * Builds a list of weekly quotes from daily quotes. Requires the list of daily quote beans to be sorted in date
     * ascending order.
     *
     * @param quotes list of daily quotes
     *
     * @return list of weekly quotes
     */
    public static List<WeeklyQuoteBean> findWeeklyQuotes(final List<DailyQuoteBean> quotes) {
        Map<Date, WeeklyQuoteBean> map = new TreeMap<>();

        // for each entry in list
        for (DailyQuoteBean quote : quotes) {

            // find following Friday
            Date fri = DateUtils.getNextFriday(quote.getDate());

            // see if a bean exists for that Friday, if not create one and add
            // it to the map
            WeeklyQuoteBean weeklyQuote = null;
            if (map.containsKey(fri)) {
                weeklyQuote = map.get(fri);
            } else {
                weeklyQuote = new WeeklyQuoteBean();
                weeklyQuote.setSymbol(quote.getSymbol());
                weeklyQuote.setWeekEndingDate(fri);
                map.put(fri, weeklyQuote);
            }

            // calculate the quote values and populate the bean
            weeklyQuote.setOpen(quote.getOpen());
            weeklyQuote.setHigh(Math.max(weeklyQuote.getHigh(), quote.getHigh()));
            weeklyQuote.setLow(Math.min(weeklyQuote.getLow(), quote.getLow()));
            weeklyQuote.setClose(quote.getClose());
            weeklyQuote.setAdjustedHigh(Math.max(weeklyQuote.getAdjustedHigh(), quote.getAdjustedHigh()));
            weeklyQuote.setAdjustedLow(Math.min(weeklyQuote.getAdjustedLow(), quote.getAdjustedLow()));
            weeklyQuote.setAdjustedClose(quote.getAdjustedClose());
        }

        LOGGER.trace("Weekly quotes map = {}.", map.toString());

        return new ArrayList<>(map.values());
    }

    /**
     * Sorts a list of quotes by date.
     *
     * @param quotes list of quotes
     */
    public static void sortQuoteListByDate(final List<DailyQuoteBean> quotes) {
        // sort to ensure we're reading the quotes in the right order
        Collections.sort(quotes, (q1, q2) -> q1.getDate().compareTo(q2.getDate()));
    }
}
