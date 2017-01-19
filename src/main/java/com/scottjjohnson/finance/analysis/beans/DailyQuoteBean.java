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
 * Holds a quote details for a single day. This bean is instantiated and populated by GSON when parsing the YQL response
 * JSON. No setter methods are required. Bean hierarchy: quote history > query > results > daily quote list.
 */
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "NM_FIELD_NAMING_CONVENTION", justification = "names must match YQL response.")
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

    // These fields don't meet naming standards because they are named based on Yahoo Finance XML elements. That's
    // necessary for Gson to be able to automatically populate the bean.
    private String Symbol;
    private String Date;
    private Double Open;
    private Double High;
    private Double Low;
    private Double Close;
    private Long Volume;

    /**
     * closing price adjusted for any stock splits
     */
    private Double Adj_Close;

    private Date dateObj;

    public String getSymbol() {
        return Symbol;
    }

    public Date getDate() {

        if (Date != null && dateObj == null) {

            try {
                dateObj = SIMPLE_DATE_FORMAT_WRAPPER.get().parse(Date);
            } catch (ParseException e) {
                LOGGER.warn("Failed to parse quote date. Full quote bean = {}.", this.toString());
            }
        }

        return (dateObj == null ? null : new Date(dateObj.getTime()));
    }

    public double getOpen() {
        return (Open == null ? getAdjustedClose() : Open);
    }

    public double getHigh() {
        return (High == null ? getAdjustedClose() : High);
    }

    public double getLow() {
        return (Low == null ? getAdjustedClose() : Low);
    }

    public double getClose() {
        return (Close == null ? -9999f : Close);
    }

    public long getVolume() {
        return (Volume == null ? -9999L : Volume);
    }

    /**
     * Intraday highest stock price for this day adjusted for any subsequent stock splits. (The adjustment also takes
     * into account dividends which we don't want, but the difference is small for the stocks I'm tracking.)
     *
     * @return adjusted high stock price
     */
    public double getAdjustedHigh() {
        if (Adj_Close != null && Close != null && High != null) {
            return (Adj_Close / Close * High);
        } else if (Adj_Close != null && Close != null) {
            return (Adj_Close); // sometimes Google has only the close, so use it if necessary.
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
        if (Adj_Close != null && Close != null && Low != null) {
            return (Adj_Close / Close * Low);
        } else if (Adj_Close != null && Close != null) {
            return (Adj_Close); // sometimes Google has only the close, so use it if necessary.
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
        if (Adj_Close != null) {
            return Adj_Close;
        } else {
            return -9999d;
        }
    }

    public void setSymbol(final String symbol) {
        Symbol = symbol;
    }

    public void setDate(final String date) {
        Date = date;
    }

    public void setOpen(final double open) {
        Open = open;
    }

    public void setHigh(final double high) {
        High = high;
    }

    public void setLow(final double low) {
        Low = low;
    }

    public void setClose(final double close) {
        Close = close;
    }

    public void setVolume(final long volume) {
        Volume = volume;
    }

    public void setAdjustedClose(final double adj_Close) {
        Adj_Close = adj_Close;
    }

    public void setDateObj(final Date dateObj) {
        this.dateObj = new Date(dateObj.getTime());
    }

    @Override
    public String toString() {
        return "DailyQuoteBean [Symbol=" + Symbol + ", Date=" + Date + ", Open=" + Open + ", High=" + High + ", Low="
                + Low + ", Close=" + Close + ", Volume=" + Volume + ", Adj_Close=" + Adj_Close + ", dateObj=" + dateObj
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        Date d = getDate();

        result = prime * result + ((Symbol == null) ? 0 : Symbol.hashCode());
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
        if (Symbol == null) {
            if (other.Symbol != null)
                return false;
        } else if (!Symbol.equals(other.Symbol))
            return false;
        if (d == null) {
            if (other.getDate() != null)
                return false;
        } else if (!d.equals(other.getDate()))
            return false;
        return true;
    }

}
