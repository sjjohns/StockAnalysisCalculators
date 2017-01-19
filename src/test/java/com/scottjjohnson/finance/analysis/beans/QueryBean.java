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
 * Holds a bean containing YQL results. This bean is instantiated and populated by GSON when parsing the YQL response
 * JSON. Bean hierarchy: quote history > query > results > daily quote list.
 */
public class QueryBean {

    /**
     * bean containing the YQL query results
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UWF_NULL_FIELD", justification = "results field is set by gson using reflection.")
    private final QueryResultsBean results = null;
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "SS_SHOULD_BE_STATIC", justification = "count is logically an instance value.")
    private int count = 0;

    /**
     * @return results bean containing YQL query results
     */
    public QueryResultsBean getResults() {
        return results;
    }

    /**
     * @return number of quotes in the YQL response
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "QueryBean [results=" + results + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((results == null) ? 0 : results.hashCode());
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
        QueryBean other = (QueryBean) obj;

        if (results == null) {

            if (other.results != null)
                return false;
        } else if (!results.equals(other.results))
            return false;
        return true;
    }
}
