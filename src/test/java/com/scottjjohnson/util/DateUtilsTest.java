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

package com.scottjjohnson.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

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
    public void testGetNextFridayFromSunday() {
        assertTrue("getNextFriday handles Sunday input.",
                getTestDate(2014, 7, 25).equals(DateUtils.getNextFriday(getTestDate(2014, 7, 20))));
    }

    @Test
    public void testGetNextFridayFromThursday() {
        assertTrue("getNextFriday handles Thursday input.",
                getTestDate(2014, 7, 25).equals(DateUtils.getNextFriday(getTestDate(2014, 7, 24))));
    }

    @Test
    public void testGetNextFridayFromFriday() {
        assertTrue("getNextFriday handles Friday input.",
                getTestDate(2014, 7, 25).equals(DateUtils.getNextFriday(getTestDate(2014, 7, 25))));
    }

    @Test
    public void testGetNextFridayFromSaturday() {
        assertTrue("getNextFriday handles Saturday input.",
                getTestDate(2014, 7, 25).equals(DateUtils.getNextFriday(getTestDate(2014, 7, 19))));
    }

    @Test
    public void testGetPreviousFridayFromSunday() {
        assertTrue("getPreviousFriday handles Sunday input.",
                getTestDate(2014, 7, 18).equals(DateUtils.getPreviousFriday(getTestDate(2014, 7, 20))));
    }

    @Test
    public void testGetPreviousFridayFromThursday() {
        assertTrue("getPreviousFriday handles Thursday input.",
                getTestDate(2014, 7, 18).equals(DateUtils.getPreviousFriday(getTestDate(2014, 7, 24))));
    }

    @Test
    public void testGetPreviousFridayFromFriday() {
        assertTrue("getPreviousFriday handles Friday input.",
                getTestDate(2014, 7, 18).equals(DateUtils.getPreviousFriday(getTestDate(2014, 7, 18))));
    }

    @Test
    public void testGetPreviousFridayFromSaturday() {
        assertTrue("getPreviousFriday handles Saturday input.",
                getTestDate(2014, 7, 18).equals(DateUtils.getPreviousFriday(getTestDate(2014, 7, 19))));
    }

    /**
     * Utility method to build a date object.
     *
     * @param year  year
     * @param month month
     * @param day   day
     *
     * @return Date representing the year/month/day
     */
    public Date getTestDate(int year, final int month, final int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }
}
