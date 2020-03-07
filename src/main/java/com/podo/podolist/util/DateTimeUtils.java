package com.podo.podolist.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static ZoneId getZonedIdOfSeoul() {
        return ZoneId.ofOffset("UTC", ZoneOffset.of("+09:00"));
    }

    public static ZonedDateTime getZonedDateTimeFromEpochSecond(long epochSecond) {
        return ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(epochSecond),
                DateTimeUtils.getZonedIdOfSeoul()
        );
    }

    public static ZonedDateTime getStartTimeOfDay(ZonedDateTime sometime) {
        Assert.notNull(sometime, "'sometime' should not be null");

        return ZonedDateTime.of(sometime.getYear(), sometime.getMonthValue(), sometime.getDayOfMonth(),
                0, 0, 0, 0, sometime.getOffset());
    }

    public static ZonedDateTime getEndTimeOfDay(ZonedDateTime sometime) {
        Assert.notNull(sometime, "'sometime' should not be null");

        return ZonedDateTime.of(sometime.getYear(), sometime.getMonthValue(), sometime.getDayOfMonth(),
                23, 59, 59, 999999999, sometime.getOffset());
    }

    /**
     * yyyyMMdd 형식의 문자열을 시간으로 변환합니다.
     * @param dateString yyyyMMdd 형식의 문자열
     * @return 서울 기준 zonedDateTime
     */
    public static ZonedDateTime parseDate(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return DateTimeUtils.getStartTimeOfDay(ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul()));
        }
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(4, 6));
        int day = Integer.parseInt(dateString.substring(6, 8));
        return ZonedDateTime.of(year, month, day, 0, 0, 0, 0, getZonedIdOfSeoul());
    }
}
