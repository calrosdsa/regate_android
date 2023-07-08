/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.regate.util

import android.os.Build
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.core.os.ConfigurationCompat
import app.regate.inject.ActivityScope
import java.time.LocalDateTime as JavaLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.Temporal
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject
import java.text.SimpleDateFormat
import java.time.format.DecimalStyle
import java.util.Calendar

@ActivityScope
@Inject
class AppDateFormatter(
    private val locale: Locale,
) {
    private val utcTimeZoneId: ZoneId = ZoneId.of("UTC")
    private val utcTimeZone: java.util.TimeZone = java.util.TimeZone.getTimeZone("UTC")
    private val shortDate: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(locale)
    }
    private val shortTime: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedTime(FormatStyle.SHORT)
            .withLocale(locale)
    }
    private val shortDateTime: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(locale)
    }
    private val mediumDate: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(locale)
    }
    private val mediumDateTime: DateTimeFormatter by lazy {
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(locale)
    }


//   A date format skeleton used to format the date picker's year selection menu button (e.g. "March 2021")
    private val yearMonthSkeleton: String = "yMMMM"


//    A date format skeleton used to format a selected date (e.g. "Mar 27, 2021")
    val yearAbbrMonthDaySkeleton: String = "yMMMd"

    val monthDaySkeleton: String = "MMMd"

    //  A date format skeleton used to format a selected date to be used as content description for screen
//  readers (e.g. "Saturday, March 27, 2021")
    private val yearMonthWeekdayDaySkeleton: String = "yMMMMEEEEd"

    private fun Instant.toTemporal(): Temporal {
        return JavaLocalDateTime.ofInstant(toJavaInstant(), utcTimeZone.toZoneId())
    }

    private fun Instant.toTemporalDefaultZoneId(): Temporal {
        return JavaLocalDateTime.ofInstant(toJavaInstant(), ZoneId.systemDefault())
    }



    fun formatShortDate(instant: Instant): String {
        return shortDate.format(instant.toTemporal())
    }

    fun formatShortDate(date: LocalDate): String {
        return shortDate.format(date.toJavaLocalDate())
    }

    fun formatMediumDate(instant: Instant): String {
        return mediumDate.format(instant.toTemporal())
    }

    fun formatMediumDateTime(instant: Instant): String {
        return mediumDateTime.format(instant.toTemporalDefaultZoneId())
    }
    fun formatShortDateTime(instant: Instant): String {
        return shortTime.format(instant.toTemporal())
    }
//    fun formatShortTime(localTime: LocalTime): String {
//        return shortTime.format(localTime.toJavaLocalTime())
//    }
    fun formatShortTime(instant: Instant): String {
        return instant.toLocalDateTime(TimeZone.UTC).time.toString()
//        return shortTime.format(instant.toTemporal())
    }



    fun formatShortRelativeTime(dateTime: Instant): String {
        val nowInstant = kotlinx.datetime.Clock.System.now()
        val now = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        val localDateTime = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())

        return if (dateTime < nowInstant) {
            if (localDateTime.year == now.year || dateTime > (nowInstant - 7.days)) {
                // Within the past week
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toEpochMilliseconds(),
                    nowInstant.toEpochMilliseconds(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE,
                ).toString()
            } else {
                // More than 7 days ago
                formatShortDate(dateTime)
            }
        } else {
            if (localDateTime.year == now.year || dateTime > (nowInstant - 14.days)) {
                // In the near future (next 2 weeks)
                DateUtils.getRelativeTimeSpanString(
                    dateTime.toEpochMilliseconds(),
                    nowInstant.toEpochMilliseconds(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE,
                ).toString()
            } else {
                // In the far future
                formatShortDate(dateTime)
            }
        }
    }
    fun formatWithSkeleton(
        utcTimeMillis: Long,
        skeleton: String,
    ): String {
//        val isa =Instant.fromEpochMilliseconds()
        val pattern = DateFormat.getBestDateTimePattern(locale, skeleton)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatWithPattern(utcTimeMillis,pattern,locale)
        } else {
            formatWithPattern2(utcTimeMillis,pattern,locale)
        }
    }

    private fun formatWithPattern(utcTimeMillis: Long, pattern: String, locale: Locale): String {
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern(pattern, locale)
                .withDecimalStyle(DecimalStyle.of(locale))
        return java.time.Instant
            .ofEpochMilli(utcTimeMillis)
            .atZone(utcTimeZoneId)
            .toLocalDate()
            .format(formatter)
    }

    private fun formatWithPattern2(utcTimeMillis: Long, pattern: String, locale: Locale): String {
        val dateFormat = SimpleDateFormat(pattern, locale)
        dateFormat.timeZone = utcTimeZone
        val calendar = Calendar.getInstance(utcTimeZone)
        calendar.timeInMillis = utcTimeMillis
        return dateFormat.format(calendar.timeInMillis)
    }

    fun formatDateString(str:String): String {
        val pattern = DateFormat.getBestDateTimePattern(locale, yearMonthSkeleton)
        val dateFormat = SimpleDateFormat(pattern, locale)
        dateFormat.timeZone = utcTimeZone
//        val calendar = Calendar.getInstance(utcTimeZone)
//        calendar.timeInMillis = utcTimeMillis
        return str.toLocalDateTime().toString()
    }

}
