/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */


package eu.europa.ec.fisheries.uvms.commons.date;

import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.regex.Pattern;

public class DateUtils extends XMLDateUtils {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DateUtils.class);


    public static final String DATE_TIME_UI_FORMAT = DateFormats.Strings.DATE_TIME_PATTERN_UTC_WITH_T;

    public static String dateToHumanReadableString(Instant date) {
        String dateString = null;
        if (date != null) {
            dateString = date.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateFormats.DATE_TIME_PATTERN.getFormat()));
        }
        return dateString;
    }

    public static String dateToEpochMilliseconds(Instant date){
        return "" + date.toEpochMilli();
    }

    public static Instant nowUTCMinusHours(final Instant now, final int hours)  {
        return now.minus(hours, ChronoUnit.HOURS );
    }

    public static Instant stringToDate(String dateString){
        if(dateString == null){
            return null;
        }
        if(Pattern.matches("\\d{10}", dateString)){
            return parseEpochSecondsTimestamp(dateString);
        }
        if(Pattern.matches("\\d{13}", dateString)){
            return parseEpochMillisecondsTimestamp(dateString);
        }
        if(dateString.length() < 20){    //if there is no offset info, assume UTC and add it
            dateString = dateString.concat(" Z");
        }
        for (DateFormats format : DateFormats.values()) {
            Instant date = convertDateWithPattern(dateString, format.getFormat());
            if (date != null) {
                return date;
            }
        }
        LOG.error("Could not parse dateTimeInUTC: " + dateString + " with pattern any defined pattern.");
        return null;
    }

    public static Instant parseEpochSecondsTimestamp(String epochSeconds){
        return Instant.ofEpochSecond(Long.parseLong(epochSeconds));
    }

    public static Instant parseEpochMillisecondsTimestamp(String epochMilliseconds){
        return Instant.ofEpochMilli(Long.parseLong(epochMilliseconds));
    }

    public static Instant convertDateWithPattern(String dateString, String pattern){
        if (dateString != null) {
            try {
                return ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)).toInstant();   //goes via ZonedDateTime to make sure that it can handle formats other then ISO_INSTANT, for example formats other then 2011-12-03T10:15:30Z and does not cry in pain from a zone
            } catch (DateTimeParseException e) {
                LOG.info("Could not parse dateTimeInUTC: " + dateString + " with pattern: " + pattern + ". Error: " + e.getMessage() + ". Trying next pattern");
            }
        }
        return null;
    }

}
