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

package com.scottjjohnson.finance.analysis.calculators;

import com.scottjjohnson.finance.analysis.beans.DailyQuoteBean;
import com.scottjjohnson.finance.analysis.beans.WeeklyQuoteBean;
import com.scottjjohnson.finance.analysis.beans.WeeksTightBean;
import com.scottjjohnson.finance.analysis.helpers.QuotesHelper;
import com.scottjjohnson.finance.analysis.parsers.GoogleFinanceParser;
import com.scottjjohnson.finance.analysis.testdata.FinanceTestData;
import com.scottjjohnson.finance.analysis.util.DateParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WeeksTightCalculatorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testScanQuotesForWeeksTightLastWeek() {

        int correctLength = 3;
        double allowableError = 0.005d;
        double correctBP = 686.15d;
        double deviation = 0.0d;

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_CMG_WITH_NEW_WT);

        WeeksTightBean wt = new WeeksTightCalculator().scanForWeeksTight(quotes, 100);

        assertNotNull("Should find weeks tight for given test data.", wt);

        Date d = DateParser.parseDateString("Fri Aug 08 00:00:00 EDT 2014");
        assertEquals("WT end date", d, wt.getPatternEndingDate());

        deviation = Math.abs(wt.getBuyPoint() - correctBP);
        assertTrue("Buy point on WT should be " + correctBP + " +/- " + allowableError + ", but was " + wt.getBuyPoint()
                + ".", deviation < allowableError);

        assertEquals("WT length should be " + correctLength + ", but was " + wt.getWeeksTightLength() + ".",
                correctLength, wt.getWeeksTightLength());
    }

    @Test
    public void testScanQuotesForWeeksTightPreviousWeeks() {

        int correctLength = 3;
        double allowableError = 0.005d;
        double correctBP = 86.85d;
        double deviation = 0.0d;

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_WLK_WITH_OLDER_WT);

        WeeksTightBean wt = new WeeksTightCalculator().scanForWeeksTight(quotes, 100);

        assertNotNull("Should find weeks tight for given test data.", wt);

        Date d = DateParser.parseDateString("Fri Jul 18 00:00:00 EDT 2014");
        assertEquals("WT end date", d, wt.getPatternEndingDate());

        deviation = Math.abs(wt.getBuyPoint() - correctBP);
        assertTrue("Buy point on WT should be " + correctBP + " +/- " + allowableError + ", but was " + wt.getBuyPoint()
                + ".", deviation < allowableError);

        assertEquals("WT length should be " + correctLength + ", but was " + wt.getWeeksTightLength() + ".",
                correctLength, wt.getWeeksTightLength());
    }

    @Test
    public void testScanQuotesForWeeksTightVeryOldWT() {

        int correctLength = 3;
        double allowableError = 0.005d;
        double correctBP = 166.42d;
        double deviation = 0.0d;

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_AGN_WITH_VERY_OLD_WT);

        WeeksTightBean wt = new WeeksTightCalculator().scanForWeeksTight(quotes, 100);

        assertNotNull("Should find weeks tight for given test data.", wt);

        Date d = DateParser.parseDateString("Fri Nov 29 00:00:00 EST 2013");
        assertEquals("WT end date", d, wt.getPatternEndingDate());

        deviation = Math.abs(wt.getBuyPoint() - correctBP);
        assertTrue("Buy point on WT should be " + correctBP + " +/- " + allowableError + ", but was " + wt.getBuyPoint()
                + ".", deviation < allowableError);

        assertEquals("WT length should be " + correctLength + ", but was " + wt.getWeeksTightLength() + ".",
                correctLength, wt.getWeeksTightLength());
    }

    @Test
    public void testScanQuotesForWeeksTightFourWeeksLong() {

        int correctLength = 4;
        double allowableError = 0.005d;
        double correctBP = 222.17d;
        double deviation = 0.0d;

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_AGN_WITH_4_WT);

        WeeksTightBean wt = new WeeksTightCalculator().scanForWeeksTight(quotes, 100);

        assertNotNull("Should find weeks tight for given test data.", wt);

        Date d = DateParser.parseDateString("Fri Aug 01 00:00:00 EDT 2014");
        assertEquals("WT end date", d, wt.getPatternEndingDate());

        deviation = Math.abs(wt.getBuyPoint() - correctBP);
        assertTrue("Buy point on WT should be " + correctBP + " +/- " + allowableError + ", but was " + wt.getBuyPoint()
                + ".", deviation < allowableError);

        assertEquals("WT length should be " + correctLength + ", but was " + wt.getWeeksTightLength() + ".",
                correctLength, wt.getWeeksTightLength());
    }

    @Test
    public void testScanQuotesForWeeksTightNoWT() {

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_AAPL_WITH_SPLIT1);

        quotes = quotes.subList(2, quotes.size());

        assertNull("Shouldn't find weeks tight for given test data.",
                new WeeksTightCalculator().scanForWeeksTight(quotes, 4));

    }

    @Test
    public void testScanQuotesForWeeksTightPriorToMaxAge() {

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_AAPL_WITH_SPLIT2);

        assertNull("Shouldn't find weeks tight for given test data.",
                new WeeksTightCalculator().scanForWeeksTight(quotes, 3));

    }

    @Test
    public void testScanQuotesForWeeksTightWithSLXPRule() {

        int correctLength = 3;
        double allowableError = 0.001d;
        double correctBP = 136.72d;
        double deviation = 0.0d;

        List<WeeklyQuoteBean> quotes = parse(FinanceTestData.GOOGLE_QUOTES_CSV_SLXP_SPECIAL_RULE);

        WeeksTightBean wt = new WeeksTightCalculator().scanForWeeksTight(quotes, 6);

        assertNotNull("Should find weeks tight for given test data.", wt);

        Date d = DateParser.parseDateString("Fri Jul 25 00:00:00 EDT 2014");
        assertEquals("WT end date", d, wt.getPatternEndingDate());

        deviation = Math.abs(wt.getBuyPoint() - correctBP);
        assertTrue("Buy point on WT should be " + correctBP + " +/- " + allowableError + ", but was " + wt.getBuyPoint()
                + ".", deviation < allowableError);

        assertEquals("WT length should be " + correctLength + ", but was " + wt.getWeeksTightLength() + ".",
                correctLength, wt.getWeeksTightLength());
    }

    private static List<WeeklyQuoteBean> parse(final String csv) {

        List<DailyQuoteBean> dailyQuotes;

        try {
            dailyQuotes = new GoogleFinanceParser("XXX", csv).parse();
        } catch (ParseException e) {
            throw new RuntimeException(e);  // TODO: Fix
        }

        QuotesHelper.sortQuoteListByDate(dailyQuotes);

        return QuotesHelper.findWeeklyQuotes(dailyQuotes);
    }

}
