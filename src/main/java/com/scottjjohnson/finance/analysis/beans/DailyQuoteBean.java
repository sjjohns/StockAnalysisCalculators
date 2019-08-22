/*
 * Copyright 2019 Scott J. Johnson (https://scottjjohnson.com)
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scottjjohnson.util.DateUtils;

/**
 * Holds a quote details for a single day.
 * <p>
 * Note: open/high/low/close/volume values are adjusted for splits but not dividends. "Adjusted" values are adjusted
 * for both splits and dividends. (There is no "Adjusted Volume" since dividends have no effect on volume.)
 */
public class DailyQuoteBean implements Serializable {

    private static final long serialVersionUID = -7900749416610148463L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyQuoteBean.class);

    // SimpleDateFormat is not thread-safe so use a ThreadLocal to give each thread it's own copy
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_WRAPPER = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "US")));

    private String symbol;
    private String date;
    private Double open;
    private Double high;
    private Double low;
    private Double close = -9999d;
    private Double change = -9999d;
    private Double percentChange = -9999d;
    private Long volume = -9999L;
    private Date dateObj;
    private Long timestamp;

    public String getSymbol() {
        return symbol;
    }

    public Date getDate() {

        if (dateObj == null) {
            if (timestamp != null) {
                dateObj = DateUtils.getMidnightForDate(new Date(timestamp));
            } else if (date != null) {

                try {
                    dateObj = SIMPLE_DATE_FORMAT_WRAPPER.get().parse(date);
                } catch (ParseException e) {
                    LOGGER.warn("Failed to parse quote date. Full quote bean = {}.", this.toString());
                }
            }
        }

        return (dateObj == null ? null : new Date(dateObj.getTime()));
    }

    public double getOpen() {
        return (open == null ? getClose() : open);
    }

    public double getHigh() {
        return (high == null ? getClose() : high);
    }

    public double getLow() {
        return (low == null ? getClose() : low);
    }

    public double getClose() {
        return close;
    }

    public double getChange() {
        return change;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public long getVolume() {
        return volume;
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

    public void setChange(final double change) {
        this.change = change;
    }

    public void setPercentChange(final double percentChange) {
        this.percentChange = percentChange;
    }

    public void setVolume(final long volume) {
        this.volume = volume;
    }

    public void setDateObj(final Date dateObj) {
        this.dateObj = new Date(dateObj.getTime());
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DailyQuoteBean{" + "symbol='" + symbol + '\'' + ", date='" + date + '\'' + ", open=" + open + ", high="
                + high + ", low=" + low + ", close=" + close + ", change=" + change + ", percentChange=" + percentChange
                + ", volume=" + volume + ", dateObj=" + dateObj + ", timestamp=" + timestamp + '}';
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
