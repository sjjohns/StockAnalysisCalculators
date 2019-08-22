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

import static org.junit.Assert.assertEquals;

public class RelativeStrengthPercentOfPeakCalculatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelativeStrengthPercentOfPeakCalculatorTest.class);

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

        int yearsToCalculate = 1;
        double allowableError = 0.02d;
        double correctAnswer = 63.3318764d;
        double calculatedAnswer = new RelativeStrengthPercentOfPeakCalculator().calculate(quotes, comparisonQuotes,
                yearsToCalculate);

        assertEquals(correctAnswer, calculatedAnswer, allowableError);
    }

}
