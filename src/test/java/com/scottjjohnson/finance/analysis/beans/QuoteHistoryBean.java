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

/**
 * Bean to hold YQL query results after being parsed by GSON. This is the top-level bean. Bean hierarchy: quote history
 * > query > results > daily quote list.
 */
public class QuoteHistoryBean {

    /**
     * holds metadata parsed from YQL response describing the query results
     */
    private final QueryBean query = null;

    public QueryBean getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "QuoteHistoryBean [query=" + query + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((query == null) ? 0 : query.hashCode());
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
        QuoteHistoryBean other = (QuoteHistoryBean) obj;

        if (query == null) {

            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        return true;
    }
}
