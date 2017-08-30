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

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.util.ParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Parses a stock quote response from the Google Finance API.
 */
public class GoogleFinanceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleFinanceParser.class);

    private final String csv;
    private final String symbol;

    /**
     * @param symbol Stock ticker symbol
     * @param csv    Google Finance response body in csv format
     */
    public GoogleFinanceParser(final String symbol, final String csv) {
        if (csv == null || symbol == null) {
            throw new IllegalArgumentException("symbol and csv must not be null");
        }

        this.symbol = symbol;
        this.csv = csv;
    }

    /**
     * Extracts quote data from the Google CSV http response body.
     *
     * @return list of quote beans
     *
     * @throws ParseException if unable to parse the date columns
     */
    public List<DailyQuoteBean> parse() throws ParseException {

        List<DailyQuoteBean> quotes = new ArrayList<>();
        SimpleDateFormat googleDateFormat = new SimpleDateFormat("dd-MMM-yy", new Locale("en", "US"));

        // I'm manually parsing the CSV rather than using Jackson or OpenCSV since this data is very simple and never
        // escaped. This approach will be faster to implement.

        StringTokenizer lineTokenizer = new StringTokenizer(csv, "\n", false);
        if (lineTokenizer.hasMoreTokens()) {
            lineTokenizer.nextToken(); // throw away the first line which is the column names
        }

        while (lineTokenizer.hasMoreTokens()) {

            int position = 0;
            DailyQuoteBean quote = new DailyQuoteBean();
            quote.setSymbol(this.symbol);

            StringTokenizer columnTokenizer = new StringTokenizer(lineTokenizer.nextToken(), ",", false);
            try {
                while (columnTokenizer.hasMoreTokens()) {
                    String value = columnTokenizer.nextToken();

                    switch (position) {
                    case 0:
                        quote.setDateObj(googleDateFormat.parse(value));
                        break;

                    case 1:
                        quote.setOpen(ParseUtils.safeParseDouble(value, -9999f));
                        break;

                    case 2:
                        quote.setHigh(ParseUtils.safeParseDouble(value, -9999f));
                        break;

                    case 3:
                        quote.setLow(ParseUtils.safeParseDouble(value, -9999f));
                        break;

                    case 4:
                        // Google data is split adjusted, but not dividend adjusted. And there is no AdjustedClose value
                        // to use to derive the dividend adjustment.
                        quote.setClose(Double.parseDouble(value));
                        quote.setAdjustedClose(Double.parseDouble(value));
                        break;

                    case 5:
                        quote.setVolume(Long.parseLong(value));
                        break;

                    default:
                        throw new RuntimeException("Found more than 6 columns in the Google Finance quote data");
                    }

                    position++;
                }

                LOGGER.trace("quote bean = %s", quote);

                quotes.add(quote);

            } catch (NumberFormatException e) {
                // I "safe parse" the open/high/low since they aren't critical. If I can't parse the closing price or
                // volume, then skip this day's quote.
                // As it turns out, Google frequently doesn't have open/high/low but does the have the closing price.
                LOGGER.error(
                        "Encountered exception parsing 1 quote record for {}. Skipping that day's quote. Quote bean prior to exception = {}",
                        symbol, quote);
            }
        }

        if (quotes.isEmpty()) {
            LOGGER.warn("Unable to extract quotes from the csv string.");
        }

        return quotes;
    }
}
