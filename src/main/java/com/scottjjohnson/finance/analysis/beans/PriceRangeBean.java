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

import java.util.Date;

/**
 * Container to hold the details of a stock's price range over time.
 */
public class PriceRangeBean {

    private String symbol;
    private double maxPrice = 0d;
    private Date maxPriceDate = null;
    private double minPrice = Double.MAX_VALUE;
    private Date minPriceDate = null;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(final double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Date getMaxPriceDate() {
        return (this.maxPriceDate == null ? null : new Date(this.maxPriceDate.getTime()));
    }

    public void setMaxPriceDate(final Date maxPriceDate) {
        this.maxPriceDate = (maxPriceDate == null ? null : new Date(maxPriceDate.getTime()));
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(final double minPrice) {
        this.minPrice = minPrice;
    }

    public Date getMinPriceDate() {
        return (this.minPriceDate == null ? null : new Date(minPriceDate.getTime()));
    }

    public void setMinPriceDate(final Date minPriceDate) {
        this.minPriceDate = (minPriceDate == null ? null : new Date(minPriceDate.getTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
        PriceRangeBean other = (PriceRangeBean) obj;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PriceRangeBean [symbol=" + symbol + ", maxPrice=" + maxPrice + ", maxPriceDate=" + maxPriceDate
                + ", minPrice=" + minPrice + ", minPriceDate=" + minPriceDate + "]";
    }
}
