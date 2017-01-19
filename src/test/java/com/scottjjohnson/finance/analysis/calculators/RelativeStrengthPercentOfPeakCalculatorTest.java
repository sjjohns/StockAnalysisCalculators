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
import com.scottjjohnson.finance.analysis.helpers.QuotesHelper;
import com.scottjjohnson.finance.analysis.parsers.YahooFinanceParser;
import com.scottjjohnson.finance.analysis.testdata.ComparisonQuotesTestData;
import com.scottjjohnson.finance.analysis.testdata.FinanceTestData;
import com.scottjjohnson.finance.analysis.util.DateParser;
import com.scottjjohnson.util.DateUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class RelativeStrengthPercentOfPeakCalculatorTest {

    private List<DailyQuoteBean> quotes = null;
    private Map<Date, DailyQuoteBean> comparisonQuotes = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // ComparisonQuotesTestData.saveTestData();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        quotes = new YahooFinanceParser(FinanceTestData.JSON_WITH_SPLIT2).parse();
        QuotesHelper.sortQuoteListByDate(quotes);

        comparisonQuotes = ComparisonQuotesTestData.getTestData();
    }

    @After
    public void tearDown() throws Exception {
        quotes = null;
        comparisonQuotes = null;
    }

    @Test
    public void testCalculate() {

        int yearsToCalculate = 100; // for this test, use whatever data is available in the test array
        double allowableError = 0.0000001d;
        double correctAnswer = 0.9850902d;
        double calculatedAnswer = new RelativeStrengthPercentOfPeakCalculator().calculate(quotes, comparisonQuotes,
                yearsToCalculate);
        double deviation = Math.abs(correctAnswer - calculatedAnswer);

        assertTrue("calculated relative strength should be " + correctAnswer + " +/- " + allowableError + ", but was "
                + calculatedAnswer + " for a deviation of " + deviation + ".", deviation <= allowableError);
    }

    @Test
    public void testCalculateWithMissingComparisonQuote() {

        Date dateToRemoveFromComparisonQuotes = DateParser.parseDateString("Fri Jun 27 00:00:00 EDT 2014");
        comparisonQuotes.remove(dateToRemoveFromComparisonQuotes);

        int yearsToCalculate = 100; // for this test, use whatever data is available in the test array
        double allowableError = 0.0000001d;
        double correctAnswer = 0.9850902d;
        double calculatedAnswer = new RelativeStrengthPercentOfPeakCalculator().calculate(quotes, comparisonQuotes,
                yearsToCalculate);
        double deviation = Math.abs(correctAnswer - calculatedAnswer);

        assertTrue("calculated relative strength should be " + correctAnswer + " +/- " + allowableError + ", but was "
                + calculatedAnswer + " for a deviation of " + deviation + ".", deviation <= allowableError);
    }

    @Test
    public void testCalculateWithDateFilter() {

        final int weeksInAYear = 52;

        // chop the test data so the peak occurs on the first day. This ensures our filter changes the result.
        quotes = quotes.subList(12, quotes.size() - 25 - 1);

        // this test depends on the test data being just older than 1 year so I need to adjust the test bean dates.
        int dateOffset = determineQuoteDateOffsetForCalculateWithDateFilterTest(weeksInAYear + 1);

        // these methods modify the quote collections as a side effect, which is not normally a good design, but the
        // collections exist only for the duration of this test method. Rather than writing a set of methods to do a
        // deep clone, I'm modifying them in-place.
        adjustQuoteDatesForCalculateWithDateFilterTest(dateOffset);
        adjustComparisonQuoteDatesForCalculateWithDateFilterTest(dateOffset);

        int yearsToCalculate = 1;
        double allowableError = 0.0000001d;
        double correctAnswer = 0.9986170d;
        double calculatedAnswer = new RelativeStrengthPercentOfPeakCalculator().calculate(quotes, comparisonQuotes,
                yearsToCalculate);
        double deviation = Math.abs(correctAnswer - calculatedAnswer);

        assertTrue("calculated relative strength should be " + correctAnswer + " +/- " + allowableError + ", but was "
                + calculatedAnswer + " for a deviation of " + deviation + ".", deviation <= allowableError);
    }

    private int determineQuoteDateOffsetForCalculateWithDateFilterTest(int quotesShouldStartWeeksAgo) {
        final long daysInAWeek = 7;

        Calendar todayCalendar = DateUtils.getStockExchangeCalendar();
        todayCalendar.setTime(new Date());
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        Date todayMidnight = todayCalendar.getTime();

        long ageOfQuotesInDays = DateUtils.getDateDiff(quotes.get(0).getDate(), todayMidnight, TimeUnit.DAYS);

        // adjust so that offset is a multiple of a week. Monday must map to a Monday
        ageOfQuotesInDays -= ageOfQuotesInDays % daysInAWeek;

        return (int) (ageOfQuotesInDays - quotesShouldStartWeeksAgo * daysInAWeek);
    }

    private void adjustQuoteDatesForCalculateWithDateFilterTest(int dateOffset) {

        for (DailyQuoteBean q : quotes) {
            Date currentQuoteDate = q.getDate();
            q.setDate(null); // destroy this String since we aren't using it.
            q.setDateObj(DateUtils.addDaysToDate(currentQuoteDate, dateOffset));
        }
    }

    private void adjustComparisonQuoteDatesForCalculateWithDateFilterTest(int dateOffset) {

        Map<Date, DailyQuoteBean> newComparisonQuotes = new HashMap<>();

        for (Map.Entry<Date, DailyQuoteBean> e : comparisonQuotes.entrySet()) {
            DailyQuoteBean q = e.getValue();
            Date updatedComparisonQuoteDate = DateUtils.addDaysToDate(q.getDate(), dateOffset);

            q.setDate(null); // destroy this String since we aren't using it.
            q.setDateObj(updatedComparisonQuoteDate);

            newComparisonQuotes.put(updatedComparisonQuoteDate, q);
        }

        comparisonQuotes = newComparisonQuotes;
    }

}
