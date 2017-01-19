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

package com.scottjjohnson.finance.analysis.calculators;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.helpers.QuotesHelper;
import com.scottjjohnson.finance.analysis.parsers.YahooFinanceParser;
import com.scottjjohnson.finance.analysis.testdata.FinanceTestData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class MaxPriceCalculatorTest {

    private List<DailyQuoteBean> quotes = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        quotes = new YahooFinanceParser(FinanceTestData.JSON_WITH_SPLIT2).parse();
        QuotesHelper.sortQuoteListByDate(quotes);
    }

    @After
    public void tearDown() throws Exception {
        quotes = null;
    }

    @Test
    public void testCalculate() {

        double allowableError = 0.000001d;
        double correctAnswer = 98.944720d;
        double calculatedAnswer = new MaxPriceCalculator().calculate(quotes);
        double deviation = Math.abs(correctAnswer - calculatedAnswer);

        assertTrue("calculated max price should be " + correctAnswer + " +/- " + allowableError + ", but was "
                + calculatedAnswer + " for a deviation of " + deviation + ".", deviation <= allowableError);
    }

}
