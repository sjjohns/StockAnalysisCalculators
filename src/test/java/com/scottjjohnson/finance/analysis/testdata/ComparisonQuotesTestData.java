/*
 * Copyright 2019 Scott J. Johnson (https://scottjjohnson.com)
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

package com.scottjjohnson.finance.analysis.testdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;

/**
 * Reads/writes SPX index data suitable for unit testing Pudding calculators.
 */
public class ComparisonQuotesTestData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonQuotesTestData.class);

    private static final String COMPARISON_QUOTES_FILENAME = "ComparisonQuoteMapTestData.dat";

    /**
     * Retrieves test data from a local file. Called by unit tests.
     *
     * @return Map containing a map of calendar dates to quote beans
     */
    public static Map<Date, DailyQuoteBean> getTestData() {

        Map<Date, DailyQuoteBean> comparisonQuoteMap = null;

        String inputFilenameWithPath = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResource(COMPARISON_QUOTES_FILENAME)
                                             .getPath();

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(inputFilenameWithPath))) {
            comparisonQuoteMap = unsafeCastToMap(is.readObject());
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Failed to read comparison quote test data from input file", e);
        }

        return Collections.unmodifiableMap(comparisonQuoteMap);
    }

    @SuppressWarnings("unchecked")
    private static Map<Date, DailyQuoteBean> unsafeCastToMap(final Object o) {
        if (o instanceof Map<?, ?>) {
            return (Map<Date, DailyQuoteBean>) o;
        } else {
            return null;
        }
    }

}
