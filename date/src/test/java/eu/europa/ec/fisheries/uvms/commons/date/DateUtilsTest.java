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

import com.fasterxml.jackson.annotation.JsonFormat;
import junit.framework.TestCase;

import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtilsTest extends TestCase {

    public void testDateConversion() {
        Instant date = Instant.ofEpochMilli(1441065600000l);
        Instant result = DateUtils.stringToDate(DateUtils.dateToString(date));
        assertEquals(date, result);
    }

    public void testTDateString(){
        Instant date = DateUtils.stringToDate("2019-09-10T13:14:39");

        String dateString = DateUtils.dateToString(date);

        Instant date2 = DateUtils.stringToDate(dateString);

        assertEquals(date,date2);

    }


    public void testGetDateFromString() throws ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X");


        Instant testDate = OffsetDateTime.of(2018, 3, 9, 11, 26, 30, 0, ZoneOffset.ofHours(2)).toInstant();
        //System.out.println(sdf.format(testDate.getTime()));

        //Formats are in DateFormats.java
        //yyyy-MM-dd HH:mm:ss Z      					  2018-03-09 09:26:30 +0100
        Instant timestamp = DateUtils.stringToDate("2018-03-09 10:26:30 +0100");
        assertTrue(testDate.equals(timestamp));

        timestamp = DateUtils.stringToDate("2018-03-09 09:26:30 Z");
        assertTrue(testDate.equals(timestamp));

        timestamp = DateUtils.stringToDate("2018-03-09 04:26:30 -0500");
        assertTrue(testDate.equals(timestamp));

        //EEE MMM dd HH:mm:ss z yyyy			Fri Mar 09 08:26:30 CET 2018
        timestamp = DateUtils.stringToDate("Fri Mar 09 10:26:30 CET 2018");
        assertTrue(testDate.equals(timestamp));

        timestamp = DateUtils.stringToDate("Fri Mar 09 18:26:30 JST 2018");
        assertTrue(testDate.equals(timestamp));

        //yyyy-MM-dd HH:mm:ss X
        timestamp = DateUtils.stringToDate("2018-03-09 10:26:30 +01");
        assertTrue(testDate.equals(timestamp));

        timestamp = DateUtils.stringToDate("2018-03-09 12:26:30 +03");
        assertTrue(testDate.equals(timestamp));

        //yyyy-MM-dd HH:mm:ss
        timestamp = DateUtils.stringToDate("2018-03-09 09:26:30");
        assertTrue(testDate.equals(timestamp));


        //fails to come

        timestamp = DateUtils.stringToDate("9:26:30");
        assertNull("Only time should not work", timestamp);



        timestamp = DateUtils.stringToDate("2018-02-31");
        assertNull("Only date should not work", timestamp);

        timestamp = DateUtils.stringToDate("2018-02-31 10:26");
        assertNull("Missing seconds", timestamp);

        timestamp = DateUtils.stringToDate(null);
        assertNull("Null", timestamp);


    }

    public void testParseUTCDateToString() {
        Instant testDate = OffsetDateTime.of(2018, 3, 9, 11, 26, 30, 0, ZoneOffset.ofHours(2)).toInstant();

        String formatedDate = DateUtils.parseUTCDateToString(testDate);
        assertTrue(formatedDate, formatedDate.contentEquals("2018-03-09 09:26:30 Z"));

        testDate = OffsetDateTime.of(2018,3, 9, 3, 26, 30, 00, ZoneOffset.ofHours(2)).toInstant();
        ZonedDateTime zonedTestDate = ZonedDateTime.of(2018,3, 9, 10, 26, 30, 00, ZoneId.of("CET"));			//Lets hop that it understand that this is supposed to be summer time internally
        zonedTestDate = zonedTestDate.withZoneSameInstant(ZoneId.of("CST", ZoneId.SHORT_IDS));

        formatedDate = DateUtils.parseUTCDateToString(zonedTestDate.toInstant());
        assertTrue(formatedDate, formatedDate.contentEquals("2018-03-09 09:26:30 Z"));

        formatedDate = DateUtils.parseUTCDateToString(null);
        assertNull(formatedDate);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_TIME_UI_FORMAT)
    public void testAnnotation() {
        // A test to make sure the (useless) annotation can be compiled. Values has to be constant expressions.
    }
}