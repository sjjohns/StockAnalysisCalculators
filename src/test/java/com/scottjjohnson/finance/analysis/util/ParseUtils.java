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

package com.scottjjohnson.finance.analysis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String parsing utility methods.
 */
public class ParseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtils.class);

    /**
     * Parses an integer from a string. Number signs, commas or percent symbols are removed prior to parsing.
     *
     * @param s            String to be parsed
     * @param defaultValue default value returned if the String is null or cannot be parsed
     *
     * @return integer value
     */
    public static int safeParseInt(final String s, final int defaultValue) {
        int result = defaultValue;

        try {
            if (s != null) {
                result = Integer.parseInt(s.replaceAll("[#,%A-Za-z\\$ ]", ""));
            }
        } catch (NumberFormatException e) {
            // do nothing
            LOGGER.debug("Parsing integer from '" + s + "' failed.", e);
        }

        return result;
    }

    /**
     * Parses an long integer from a string. Number signs, commas or percent symbols are removed prior to parsing.
     *
     * @param s            String to be parsed
     * @param defaultValue default value returned if the String is null or cannot be parsed
     *
     * @return long value
     */
    public static long safeParseLong(final String s, final long defaultValue) {
        long result = defaultValue;

        try {
            if (s != null) {
                result = Long.parseLong(s.replaceAll("[#,%A-Za-z\\$ ]", ""));
            }
        } catch (NumberFormatException e) {
            // do nothing
            LOGGER.debug("Parsing long from '" + s + "' failed.", e);
        }

        return result;
    }

    /**
     * Parses an double from a string. Number signs, commas or percent symbols are removed prior to parsing
     *
     * @param s            String to be parsed
     * @param defaultValue default value returned if the String is null or cannot be parsed
     *
     * @return double value
     */
    public static double safeParseDouble(final String s, final double defaultValue) {
        double result = defaultValue;

        try {
            if (s != null) {
                result = Double.parseDouble(s.replaceAll("[#,%A-Za-z\\/$ ]", ""));
            }
        } catch (NumberFormatException e) {
            // do nothing
            LOGGER.debug("Parsing double from '" + s + "' failed.", e);
        }

        return result;
    }

    /**
     * Parses an date from a string.
     *
     * @param d            String to be parsed
     * @param dateFormat   Expected format of the date string
     * @param defaultValue default value returned if the String is null or cannot be parsed
     *
     * @return double value
     */
    public static Date safeParseDate(final String d, final String dateFormat, final Date defaultValue) {
        Date result = defaultValue;

        try {
            if (d != null && dateFormat != null) {
                DateFormat df = new SimpleDateFormat(dateFormat);
                result = df.parse(d);
            }
        } catch (ParseException e) {
            // do nothing
            LOGGER.debug("Parsing date from '" + d + "' using format '" + dateFormat + "' failed.", e);
        }

        return result;
    }

}
