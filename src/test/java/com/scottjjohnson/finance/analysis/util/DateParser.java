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
import java.util.Locale;

public class DateParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateParser.class);

    public static Date parseDateString(final String dateString) {

        Date d = null;
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", new Locale("en", "US"));
        try {
            d = df.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("failed to parse date string ", e);
        }
        return d;
    }

}
