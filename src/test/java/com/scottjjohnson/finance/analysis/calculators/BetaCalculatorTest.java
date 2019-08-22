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

package com.scottjjohnson.finance.analysis.calculators;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.testdata.ComparisonQuotesTestData;
import com.scottjjohnson.finance.analysis.testdata.FinanceQuotesTestData;
import com.scottjjohnson.util.DateUtils;

import static org.junit.Assert.assertEquals;

public class BetaCalculatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BetaCalculatorTest.class);

    private static List<DailyQuoteBean> quotes = null;
    private static Map<Date, DailyQuoteBean> comparisonQuotes = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        quotes = FinanceQuotesTestData.getTestData();
        comparisonQuotes = ComparisonQuotesTestData.getTestData();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        quotes = null;
        comparisonQuotes = null;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCalculate() {

        Date lastQuoteDate = quotes.get(quotes.size() - 1).getDate();
        Date oneYearBeforeLastQuoteDate = DateUtils.addYearsToDate(lastQuoteDate, -1);

        List<DailyQuoteBean> lastYearOfQuotes = quotes.stream()
                                                      .filter(q -> q.getDate().after(oneYearBeforeLastQuoteDate))
                                                      .collect(Collectors.toList());

        double allowableError = 0.00002d;
        double correctAnswer = 1.51303d;
        double calculatedAnswer = new BetaCalculator().calculate(lastYearOfQuotes, comparisonQuotes);

        assertEquals(correctAnswer, calculatedAnswer, allowableError);
    }
}
