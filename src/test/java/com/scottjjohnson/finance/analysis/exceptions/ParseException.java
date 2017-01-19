/*
 * Copyright 2015 Scott J. Johnson (http://scottjjohnson.com)
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

package com.scottjjohnson.finance.analysis.exceptions;

/**
 * Custom exception to hold the details of any parse failure.
 */
public class ParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5430750784242665175L;

    public ParseException(Exception e) {
        super(e);
    }

    public ParseException(String string) {
        super(string);
    }

    public ParseException(String string, Exception e) {
        super(string, e);
    }
}
