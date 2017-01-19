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
 * Holds data describing stock price and volume over a 1 week period.
 */
public class WeeklyQuoteBean {

    private String symbol;
    private Date weekEndingDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    /**
     * weekly high stock price adjusted for any stock splits and dividends
     */
    private double adjustedHigh = 0d;

    /**
     * weekly low stock price adjusted for any stock splits and dividends
     */
    private double adjustedLow = Double.MAX_VALUE;

    /**
     * weekly closing price adjusted for any stock splits and dividends
     */
    private double adjustedClose = 0d;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public Date getWeekEndingDate() {
        return (weekEndingDate == null ? null : new Date(weekEndingDate.getTime()));
    }

    public void setWeekEndingDate(final Date weekEndingDate) {
        this.weekEndingDate = (weekEndingDate == null ? null : new Date(weekEndingDate.getTime()));
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(final double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(final double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(final double low) {
        this.low = low;
    }

    public double getClose() {
        return this.close;
    }

    public void setClose(final double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(final long volume) {
        this.volume = volume;
    }

    /**
     * @return highest stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public double getAdjustedHigh() {
        return adjustedHigh;
    }

    /**
     * Sets the adjusted weekly high stock price.
     *
     * @param adjustedHigh highest stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public void setAdjustedHigh(final double adjustedHigh) {
        this.adjustedHigh = adjustedHigh;
    }

    /**
     * @return lowest stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public double getAdjustedLow() {
        return adjustedLow;
    }

    /**
     * Sets the adjusted weekly low stock price.
     *
     * @param adjustedLow lowest stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public void setAdjustedLow(final double adjustedLow) {
        this.adjustedLow = adjustedLow;
    }

    /**
     * @return closing stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public double getAdjustedClose() {
        return adjustedClose;
    }

    /**
     * Sets the adjusted week closing stock price.
     *
     * @param adjustedClose highest stock price during the week adjusted for any subsequent stock splits and dividends
     */
    public void setAdjustedClose(final double adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    @Override
    public String toString() {
        return "WeeklyQuoteBean [symbol=" + symbol + ", weekEndingDate=" + weekEndingDate + ", open=" + open + ", high="
                + high + ", low=" + low + ", close=" + close + ", volume=" + volume + ", adjustedHigh=" + adjustedHigh
                + ", adjustedLow=" + adjustedLow + ", adjustedClose=" + adjustedClose + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        result = prime * result + ((weekEndingDate == null) ? 0 : weekEndingDate.hashCode());
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
        WeeklyQuoteBean other = (WeeklyQuoteBean) obj;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        if (weekEndingDate == null) {
            if (other.weekEndingDate != null)
                return false;
        } else if (!weekEndingDate.equals(other.weekEndingDate))
            return false;
        return true;
    }
}
