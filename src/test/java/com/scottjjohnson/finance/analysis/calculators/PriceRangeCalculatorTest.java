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
import com.scottjjohnson.finance.analysis.beans.PriceRangeBean;
import com.scottjjohnson.finance.analysis.helpers.QuotesHelper;
import com.scottjjohnson.finance.analysis.parsers.YahooFinanceParser;
import com.scottjjohnson.finance.analysis.testdata.FinanceTestData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceRangeCalculatorTest {

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

        double allowableError = 0.0000001d;
        double correctAnswerMaxPrice = 98.9447205d;
        Date correctAnswerMaxPriceDate = new Date(1406606400000L); // Tue Jul 29 00:00:00 EDT 2014

        double correctAnswerMinPrice = 85.8694024d;
        Date correctAnswerMinPriceDate = new Date(1400731200000L); // Thu May 22 00:00:00 EDT 2014

        PriceRangeBean calculatedAnswer = new PriceRangeCalculator().calculate(quotes);

        double deviationMaxPrice = Math.abs(correctAnswerMaxPrice - calculatedAnswer.getMaxPrice());
        double deviationMinPrice = Math.abs(correctAnswerMinPrice - calculatedAnswer.getMinPrice());

        assertTrue("calculated max price should be " + correctAnswerMaxPrice + " +/- " + allowableError + ", but was "
                        + calculatedAnswer.getMaxPrice() + " for a deviation of " + deviationMaxPrice + ".",
                deviationMaxPrice <= allowableError);

        assertEquals("calculated max price date should be " + correctAnswerMaxPriceDate + ", but was "
                        + calculatedAnswer.getMaxPriceDate() + ".", calculatedAnswer.getMaxPriceDate(),
                correctAnswerMaxPriceDate);

        assertTrue("calculated min price should be " + correctAnswerMinPrice + " +/- " + allowableError + ", but was "
                        + calculatedAnswer.getMinPrice() + " for a deviation of " + deviationMinPrice + ".",
                deviationMinPrice <= allowableError);

        assertEquals("calculated min price date should be " + correctAnswerMinPriceDate + ", but was "
                        + calculatedAnswer.getMinPriceDate() + ".", calculatedAnswer.getMinPriceDate(),
                correctAnswerMinPriceDate);
    }

}
