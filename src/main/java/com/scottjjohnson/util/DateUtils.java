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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Date utility methods
 */
public final class DateUtils {

    private static final TimeZone exchangeTZ = TimeZone.getTimeZone("America/New_York");

    /**
     * Private constructor to avoid instantiation
     */
    private DateUtils() {
    }

    /**
     * Gets the current time in New York
     *
     * @return date object representing the time in NY
     */
    public static Date getCurrentDate() {

        Calendar cal = getStockExchangeCalendar();

        return cal.getTime();
    }

    /**
     * Finds the Friday following the date. If the date is already a Friday the original date is returned. Time
     * properties in the Date object are not modified.
     *
     * @param d date to be evaluated
     *
     * @return date adjusted to the following Friday
     */
    public static Date getNextFriday(final Date d) {

        Calendar cal = getStockExchangeCalendar();
        cal.setTime(d);

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_WEEK, (13 - dow) % 7);

        return cal.getTime();
    }

    /**
     * Finds the Friday preceding the date. If the date is already a Friday, the original date is returned. Time
     * properties in the Date object are not modified.
     *
     * @param d date to be evaluated
     *
     * @return date adjusted to the previous Friday
     */
    public static Date getPreviousFriday(final Date d) {

        Calendar cal = getStockExchangeCalendar();
        cal.setTime(d);

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_WEEK, -(dow + 1) % 7);

        return cal.getTime();
    }

    /**
     * Adds days to a Date.
     *
     * @param d    date to be adjusted
     * @param days number of days
     *
     * @return adjusted date
     */
    public static Date addDaysToDate(final Date d, final int days) {

        Calendar cal = getStockExchangeCalendar();
        cal.setTime(d);

        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    /**
     * Adds years to a Date.
     *
     * @param d     date to be adjusted
     * @param years number of years
     *
     * @return adjusted date
     */
    public static Date addYearsToDate(final Date d, final int years) {

        Calendar cal = getStockExchangeCalendar();
        cal.setTime(d);

        cal.add(Calendar.YEAR, years);

        return cal.getTime();
    }

    /**
     * Calculates the difference between 2 date objects and returns the result in the requested time units.
     *
     * @param d1       first date
     * @param d2       second date
     * @param timeUnit time unit desired. Example: TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MILLISECONDS
     *
     * @return difference between the 2 dates
     */
    public static long getDateDiff(final Date d1, final Date d2, final TimeUnit timeUnit) {

        long differenceInMilliseconds = d2.getTime() - d1.getTime();
        return timeUnit.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets a reference to a Calendar object with the right time zone for the NYSE.
     *
     * @return Calendar in the stock exchange time zone
     */
    public static Calendar getStockExchangeCalendar() {
        return new GregorianCalendar(exchangeTZ);
    }
}
