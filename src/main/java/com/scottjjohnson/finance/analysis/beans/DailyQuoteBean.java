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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Holds a quote details for a single day.
 * <p>
 * Note: open/high/low/close/volume values are adjusted for splits but not dividends. "Adjusted" values are adjusted
 * for both splits and dividends. (There is no "Adjusted Volume" since dividends have no effect on volume.)
 */
public class DailyQuoteBean implements java.io.Serializable {

    private static final long serialVersionUID = -7900749416610148463L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyQuoteBean.class);

    // SimpleDateFormat is not thread-safe so use a ThreadLocal to give each thread it's own copy
    protected static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_WRAPPER = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "US"));
        }
    };

    private String symbol;
    private String date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;

    /**
     * closing price adjusted for any stock splits
     */
    private Double adjClose;

    private Date dateObj;

    public String getSymbol() {
        return symbol;
    }

    public Date getDate() {

        if (date != null && dateObj == null) {

            try {
                dateObj = SIMPLE_DATE_FORMAT_WRAPPER.get().parse(date);
            } catch (ParseException e) {
                LOGGER.warn("Failed to parse quote date. Full quote bean = {}.", this.toString());
            }
        }

        return (dateObj == null ? null : new Date(dateObj.getTime()));
    }

    public double getOpen() {
        return (open == null ? getAdjustedClose() : open);
    }

    public double getHigh() {
        return (high == null ? getAdjustedClose() : high);
    }

    public double getLow() {
        return (low == null ? getAdjustedClose() : low);
    }

    public double getClose() {
        return (close == null ? -9999f : close);
    }

    public long getVolume() {
        return (volume == null ? -9999L : volume);
    }

    /**
     * Intraday highest stock price for this day adjusted for any subsequent stock splits. (The adjustment also takes
     * into account dividends which we don't want, but the difference is small for the stocks I'm tracking.)
     *
     * @return adjusted high stock price
     */
    public double getAdjustedHigh() {
        if (adjClose != null && close != null && high != null) {
            return (adjClose / close * high);
        } else if (adjClose != null && close != null) {
            return (adjClose); // sometimes Google has only the close, so use it if necessary.
        } else {
            return -9999d;
        }
    }

    /**
     * Intraday lowest stock price for this day adjusted for any subsequent stock splits. (The adjustment also takes
     * into account dividends which we don't want, but the difference is small for the stocks I'm tracking.)
     *
     * @return adjusted low stock price
     */
    public double getAdjustedLow() {
        if (adjClose != null && close != null && low != null) {
            return (adjClose / close * low);
        } else if (adjClose != null && close != null) {
            return (adjClose); // sometimes Google has only the close, so use it if necessary.
        } else {
            return -9999d;
        }
    }

    /**
     * Closing stock price for this day adjusted for any subsequent stock splits. (The adjustment also takes into
     * account dividends which we don't want, but the difference is small for the stocks I'm tracking.)
     *
     * @return adjusted closing stock price
     */
    public double getAdjustedClose() {
        if (adjClose != null) {
            return adjClose;
        } else {
            return -9999d;
        }
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setOpen(final double open) {
        this.open = open;
    }

    public void setHigh(final double high) {
        this.high = high;
    }

    public void setLow(final double low) {
        this.low = low;
    }

    public void setClose(final double close) {
        this.close = close;
    }

    public void setVolume(final long volume) {
        this.volume = volume;
    }

    public void setAdjustedClose(final double adjClose) {
        this.adjClose = adjClose;
    }

    public void setDateObj(final Date dateObj) {
        this.dateObj = new Date(dateObj.getTime());
    }

    @Override
    public String toString() {
        return "DailyQuoteBean [symbol=" + symbol + ", date=" + date + ", open=" + open + ", high=" + high + ", low="
                + low + ", close=" + close + ", volume=" + volume + ", adjClose=" + adjClose + ", dateObj=" + dateObj
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        Date d = getDate();

        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        result = prime * result + ((d == null) ? 0 : d.hashCode());
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
        DailyQuoteBean other = (DailyQuoteBean) obj;
        Date d = getDate();
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        if (d == null) {
            if (other.getDate() != null)
                return false;
        } else if (!d.equals(other.getDate()))
            return false;
        return true;
    }

}
