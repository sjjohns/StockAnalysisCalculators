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
 * Contains a description of a instance of a Weeks Tight chart pattern.
 */
public class WeeksTightBean {

    /**
     * date on which the chart pattern was completed
     */
    private Date patternEndingDate;

    /**
     * Stock ticker symbol
     */
    private String symbol;

    /**
     * number of weeks tight
     */
    private int length;

    /**
     * highest intra-day price in the pattern
     */
    private double highestPrice;

    /**
     * lowest intra-day price in the pattern
     */
    private double lowestPrice;

    /**
     * maximum percentage change in weekly price close inside the pattern
     */
    private double maxPriceRangePercent;

    /**
     * @return date when the WT pattern ended
     */
    public Date getPatternEndingDate() {
        return (patternEndingDate == null ? null : new Date(patternEndingDate.getTime()));
    }

    /**
     * Sets the date when the WT pattern ended.
     *
     * @param patternEndingDate date when the pattern ended
     */
    public void setPatternEndingDate(final Date patternEndingDate) {
        this.patternEndingDate = (patternEndingDate == null ? null : new Date(patternEndingDate.getTime()));
    }

    /**
     * @return stock ticker symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the stock ticker symbol.
     *
     * @param symbol stock ticker symbol
     */
    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    /**
     * Calculates the weeks tight pattern buy point.
     *
     * @return pattern buy point
     */
    public double getBuyPoint() {
        return highestPrice + 0.10d;
    }

    /**
     * @return length of the weeks tight pattern
     */
    public int getWeeksTightLength() {
        return length;
    }

    /**
     * @return highest stock price inside the pattern
     */
    public double getHighestPrice() {
        return highestPrice;
    }

    /**
     * Sets the highest stock price inside the pattern.
     *
     * @param highestPrice highest price during the pattern
     */
    public void setHighestPrice(final double highestPrice) {
        this.highestPrice = highestPrice;
    }

    /**
     * @return lowest stock price inside the pattern
     */
    public double getLowestPrice() {
        return lowestPrice;
    }

    /**
     * Sets the lowest stock price inside the pattern.
     *
     * @param lowestPrice lowest stock price inside the pattern
     */
    public void setLowestPrice(final double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    /**
     * Sets the length of the weeks tight pattern.
     *
     * @param weeksTightLength number of weeks in the pattern
     */
    public void setWeeksTightLength(final int weeksTightLength) {
        this.length = weeksTightLength;
    }

    /**
     * @return maximum percentage change in closing price inside the pattern
     */
    public double getMaxPriceRangePercent() {
        return maxPriceRangePercent;
    }

    /**
     * Sets the maximum percentage change in closing price inside the pattern
     *
     * @param weeksTightMaxPriceRangePercent maximum percentage change in closing price inside the pattern
     */
    public void setMaxPriceRangePercent(final double weeksTightMaxPriceRangePercent) {
        this.maxPriceRangePercent = weeksTightMaxPriceRangePercent;
    }

    @Override
    public String toString() {
        return "WeeksTightBean [patternEndingDate=" + patternEndingDate + ", symbol=" + symbol + ", length=" + length
                + ", highestPrice=" + highestPrice + ", lowestPrice=" + lowestPrice
                + ", weeksTightMaxPriceRangePercent=" + maxPriceRangePercent + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((patternEndingDate == null) ? 0 : patternEndingDate.hashCode());
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
        WeeksTightBean other = (WeeksTightBean) obj;
        if (patternEndingDate == null) {
            if (other.patternEndingDate != null)
                return false;
        } else if (!patternEndingDate.equals(other.patternEndingDate))
            return false;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        return true;
    }
}
