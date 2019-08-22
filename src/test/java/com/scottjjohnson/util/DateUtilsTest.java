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

package com.scottjjohnson.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

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
    public void testAddYearsToDate() {

        Date inputDate = getTestDate(2017, Calendar.JULY, 19);

        Date calculatedAnswer = DateUtils.addYearsToDate(inputDate, 1);
        Date correctAnswer = getTestDate(2018, Calendar.JULY, 19);

        assertEquals(calculatedAnswer, correctAnswer);
    }

    @Test
    public void testAddYearsToDateOverLeapYear() {

        Date inputDate = getTestDate(2015, Calendar.JULY, 19);

        Date calculatedAnswer = DateUtils.addYearsToDate(inputDate, 1);
        Date correctAnswer = getTestDate(2016, Calendar.JULY, 19);

        assertEquals(calculatedAnswer, correctAnswer);
    }

    @Test
    public void testAddYearsToDateWithNegativeYears() {

        Date inputDate = getTestDate(2015, Calendar.JULY, 19);

        Date calculatedAnswer = DateUtils.addYearsToDate(inputDate, -1);
        Date correctAnswer = getTestDate(2014, Calendar.JULY, 19);

        assertEquals(calculatedAnswer, correctAnswer);
    }

    @Test
    public void testGetMidnightForDate() {

        Date inputDate = getTestDate(2014, Calendar.JULY, 18, 11, 23, 0);
        Date calculatedAnswer = DateUtils.getMidnightForDate(inputDate);
        Date correctAnswer = getTestDate(2014, Calendar.JULY, 18);

        assertEquals(correctAnswer, calculatedAnswer);
    }

    @Test
    public void testGetMidnightForDateAlreadyMidnight() {

        Date inputDate = getTestDate(2014, Calendar.JULY, 18);
        Date calculatedAnswer = DateUtils.getMidnightForDate(inputDate);
        Date correctAnswer = getTestDate(2014, Calendar.JULY, 18);

        assertEquals(correctAnswer, calculatedAnswer);
    }

    /**
     * Utility method to build a date object for midnight.
     *
     * @param year  year
     * @param month month
     * @param day   day
     *
     * @return Date representing the year/month/day
     */
    private Date getTestDate(int year, final int month, final int day) {
        return getTestDate(year, month, day, 0, 0, 0);
    }

    /**
     * Utility method to build a date object with hour/minute/seconds.
     *
     * @param year    year
     * @param month   month
     * @param day     day
     * @param hours   hours
     * @param minutes minutes
     * @param seconds seconds
     *
     * @return Date representing the year/month/day
     */
    private Date getTestDate(int year, final int month, final int day, final int hours, final int minutes,
            final int seconds) {
        return new GregorianCalendar(year, month, day, hours, minutes, seconds).getTime();
    }
}
