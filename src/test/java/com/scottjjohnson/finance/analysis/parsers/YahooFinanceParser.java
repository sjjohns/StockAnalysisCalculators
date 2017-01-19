/*
 * Copyright 2015 Scott J. Johnson (http://scottjjohnson.com)
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

package com.scottjjohnson.finance.analysis.parsers;

import com.google.gson.Gson;
import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.beans.QueryBean;
import com.scottjjohnson.finance.analysis.beans.QueryResultsBean;
import com.scottjjohnson.finance.analysis.beans.QuoteHistoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a stock quote response from the Yahoo Finance API.
 */
public class YahooFinanceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(YahooFinanceParser.class);

    public final String json;

    /**
     * @param json YQL response body in json format
     */
    public YahooFinanceParser(final String json) {
        if (json == null) {
            throw new IllegalArgumentException("json must not be null");
        }

        this.json = json;
    }

    /**
     * Extracts quote data from the YQL JSON response.
     *
     * @return list of quote beans
     */
    public List<DailyQuoteBean> parse() {

        List<DailyQuoteBean> quote = null;
        QuoteHistoryBean quoteHistory = new Gson().fromJson(json, QuoteHistoryBean.class);

        // unwrap the beans. We need only the list of quotes.
        if (quoteHistory != null) {
            QueryBean query = quoteHistory.getQuery();
            if (query != null) {
                if (query.getCount() > 0) {
                    QueryResultsBean results = query.getResults();
                    if (results != null) {
                        quote = results.getQuotes();
                        if (quote != null && !quote.isEmpty()) {

                            LOGGER.trace("Quote size = {}, list = {}.", quote.size(), quote);

                            // verify the first bean has a date. Sometimes Yahoo
                            // is returning incorrect column names resulting in
                            // GSON being unable to populate the beans.
                            DailyQuoteBean firstQuote = quote.get(0);
                            if (firstQuote == null || firstQuote.getDate() == null || firstQuote.getVolume() == 0) {
                                quote = null; // in the event of a problem, destroy the list
                            }
                        }
                    }
                } else {
                    quote = new ArrayList<>(); // if Yahoo returned no quotes, return an empty list.
                }
            }
        }

        if (quote == null) {
            LOGGER.warn("Unable to extract quotes from JSON.");
        }

        return quote;
    }
}
