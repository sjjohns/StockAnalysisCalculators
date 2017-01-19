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

package com.scottjjohnson.finance.analysis.testdata;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Map;

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

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(getComparisonFileLocationForReading()))) {
            comparisonQuoteMap = unsafeCast(is.readObject());
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("failed to get test data", e);
        }

        return comparisonQuoteMap;
    }

    /**
     * This method creates the comparison data map and saves it in the test resources directory. So it must run on the
     * dev machine. The next build will automatically copy the file into the target directory where it can be read by
     * the test. The method is run once when the data needs to be updated and tests read from the local file.
     */
    /*
    public static void saveTestData() {

        Map<Date, DailyQuoteBean> comparisonQuoteMap = ComparisonQuotesHelper.getComparisonQuoteMap(3); // 3 years

        try (ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(getComparisonFileLocationForWriting()))) {
            os.writeObject(comparisonQuoteMap);
        } catch (IOException e) {
            LOGGER.error("failed to get test data", e);
        }
    }
    */
    private static String getComparisonFileLocationForReading() {

        return Thread.currentThread().getContextClassLoader().getResource(COMPARISON_QUOTES_FILENAME).getPath();
    }

    private static String getComparisonFileLocationForWriting() {

        return "src/test/resources/" + COMPARISON_QUOTES_FILENAME;
    }

    @SuppressWarnings("unchecked")
    private static Map<Date, DailyQuoteBean> unsafeCast(final Object o) {
        if (o instanceof Map<?, ?>) {
            return (Map<Date, DailyQuoteBean>) o;
        } else {
            return null;
        }
    }

    /*
    public static void main(String[] args) {
        saveTestData();
    }
    */
}
