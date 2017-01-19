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

package com.scottjjohnson.finance.analysis.beans;

import java.util.List;

/**
 * Bean to hold the information from the YQL query response. Instantiated and populated by GSON when parsing the YQL
 * response JSON. Bean hierarchy: quote history > query > results > daily quote list.
 */
public class QueryResultsBean {

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UWF_NULL_FIELD", justification = "quote field is set by gson using reflection.")
    private final List<DailyQuoteBean> quote = null;

    public List<DailyQuoteBean> getQuotes() {
        return quote;
    }

    @Override
    public String toString() {
        return "QueryResultsBean [quote=" + quote + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((quote == null) ? 0 : quote.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryResultsBean other = (QueryResultsBean) obj;

        if (quote == null) {

            if (other.quote != null)
                return false;
        } else if (!quote.equals(other.quote))
            return false;
        return true;
    }

}
