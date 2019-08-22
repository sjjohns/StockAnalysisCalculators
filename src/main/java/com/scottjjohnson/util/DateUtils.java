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
import java.util.TimeZone;

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

    public static Date getMidnightForDate(final Date d) {

        Calendar todayCalendar = DateUtils.getStockExchangeCalendar();
        todayCalendar.setTime(d);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);

        return todayCalendar.getTime();
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
