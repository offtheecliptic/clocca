//// ======================================================================================
//// Simple date and time library.  This basically sits on top of java.time, but 
//// but only exposes the very basics.
//// ======================================================================================
@file:JvmName("CloccaImpls") 

package offtheecliptic.clocca.impls

import offtheecliptic.clocca.interfaces.*

// import java.time.*
// import java.time.format.DateTimeFormatter
// import java.time.format.ResolverStyle
// import kotlin.math.abs
// import kotlin.math.sign

/// --------------------------------------------------------------------------------------
/// Type Aliases

// typealias ThisDuration   = offtheecliptic.clocca.interfaces.Duration
// typealias RawDuration    = java.time.Duration
// typealias RawInstant     = java.time.Instant

/// --------------------------------------------------------------------------------------
/// SimpleDate RECORD

data class SimpleDate(
    val year: Int?,
    val monthOfYear: Int?,
    val dayOfMonth: Int?, 
    val timeZone: TimeZone?
): Date {
    init {
        if (monthOfYear == null && dayOfMonth != null) {
            throw RuntimeException("Cannot specify day if month is not also specified.")
        }
        if (monthOfYear == null && year != null) {
            throw RuntimeException("Cannot specify year if month is not also specified.")
        }
        // if (timeZone != null && (monthOfYear == null || dayOfMonth == null)) {
        //     throw RuntimeException("Cannot specify time zone unless all date components specified.")
        // }
    }
    /** Copy constructor. */
    constructor(date: Date): 
        this(date.year(), date.monthOfYear(), date.dayOfMonth(), date.timeZone()) {
    }

    override fun year():        Int?   = year
    override fun monthOfYear(): Int?   = monthOfYear
    override fun dayOfMonth():  Int?   = dayOfMonth
    override fun timeZone(): TimeZone? = timeZone
    override fun isZoned():   Boolean  = timeZone == null    // If true, date is absolute (bound to time zone)
    override fun isPartial(): Boolean  = dayOfMonth() == null || year == null // If true, only year, or year+month, are specified
}

/// --------------------------------------------------------------------------------------
/// SimpleTime RECORD

data class SimpleTime(
    val hour:   Int? = 0,
    val minute: Int? = 0,
    val second: Int? = 0,
    val nanosecond: Int? = 0,
    val timeZone: TimeZone?
): Time {
    init {
        if (minute == null && second != null) {
            throw RuntimeException("Cannot specify seconds if minutes is not also specified.")
        }
        if (minute == null && hour != null) {
            throw RuntimeException("Cannot specify hours if minutes is not also specified.")
        }
    }
    /** Copy constructor. */
    constructor(time: Time):
        this(time.hour(), time.minute(), time.second(), time.nanosecond(), time.timeZone()) {
    }

    override fun hour():   Int?  = hour
    override fun minute(): Int?  = minute
    override fun second(): Int?  = second
    override fun millisecond(): Int?  = if (nanosecond != null) { (nanosecond / 1000000).toInt() } else null
    override fun microsecond(): Int?  = if (nanosecond != null) { (nanosecond / 1000).toInt() } else null
    override fun nanosecond():  Int?  = nanosecond
    override fun timeZone(): TimeZone? = timeZone
    override fun isZoned():   Boolean  = timeZone == null    // If true, date is absolute (bound to time zone)
    override fun isPartial(): Boolean  = hour == null || second == null // If true, only year, or year+month, are specified
}

/// --------------------------------------------------------------------------------------
/// SIMPLE DATE-TIME

private fun setTimeZone(date: Date?, time: Time?): TimeZone? {
    if (date != null && date.timeZone() != null) {
        if (time != null) {
            if (time.timeZone() != date.timeZone()) {
               throw RuntimeException("Date has timezone but time timezone does not match.")
            }
            return date.timeZone()
        }
    }
    if (time != null && time.timeZone() != null) {
        if (date != null) {
            if (date.timeZone() != time.timeZone()) {
               throw RuntimeException("Time has timezone but date timezone does not match.")
            }
            return time.timeZone()
        }
    }
    return null
}

data class SimpleDateTime(
    val date: Date?,
    val time: Time?,
    val timeZone: TimeZone? = setTimeZone(date,time)
): DateTime {
    init {
    }
    /** Copy constructor. */
    constructor(dateTime: DateTime): 
        this(dateTime.date(), dateTime.time(), dateTime.timeZone()) {    
    }

    override fun date(): Date? = date 
    override fun time(): Time? = time
    override fun timeZone(): TimeZone?  = timeZone
    override fun isZoned():   Boolean  = timeZone == null    // If true, date and time are absolute (bound to time zone)
    override fun isPartial(): Boolean  = date == null || time == null // If true, only date and/or time, are specified
                                      || date.isPartial() || time.isPartial() 
}

/// --------------------------------------------------------------------------------------
/// TIME ZONE

data class SimpleTimeZone(
    val timeZoneName: TimeZoneName, 
    val primeMeridianOffset: Duration = SimpleDuration(0,0)
): TimeZone {
    override fun timeZoneName(): TimeZoneName = timeZoneName
    override fun primeMeridianOffset(): Duration = primeMeridianOffset  // Duration in the form of hours and minutes (can be seconds too)
}

/// --------------------------------------------------------------------------------------
/// SimpleDuration RECORD

data class SimpleDuration(
    val totalSeconds: Long?,
    val secondNanos:  Int? = 0
): Duration {
    init {
    }
    override fun totalSeconds(): Long? = totalSeconds
    override fun secondNanos():  Int?  = secondNanos
}

/// --------------------------------------------------------------------------------------
/// INSTANT

data class SimpleInstant(
    val epochSecond: Long?,
    val secondNano:  Int?
): Instant {
    override fun epochSecond(): Long? = epochSecond
    override fun secondNano():   Int? = secondNano
}
// static Instant	ofEpochMilli​(long epochMilli)	
// Obtains an instance of Instant using milliseconds from the epoch of 1970-01-01T00:00:00Z.
// static Instant	ofEpochSecond​(long epochSecond)	
// Obtains an instance of Instant using seconds from the epoch of 1970-01-01T00:00:00Z.
// static Instant	ofEpochSecond​(long epochSecond, long nanoAdjustment)

    // object Factory {
    //     fun now(): Instant = java.time.Instant.now()
    //     // Obtains an instance of Instant using milliseconds from the epoch of
    //     // 1970-01-01T00:00:00Z.
    //     fun fromEpochMilli(epochMilli: Long): Instant 
    //         = java.time.Instant.ofEpochMilli​(epochMilli)
    //     // Obtains an instance of Instant using seconds from the epoch of 
    //     // 1970-01-01T00:00:00Z.
    //     fun fromEpochSecond(epochSecond: Long): Instant 
    //         = java.time.Instant.ofEpochSecond​(epochSecond)	
    //     // Obtains an instance of Instant using seconds from the epoch of 
    //     // 1970-01-01T00:00:00Z and nanosecond fraction of second.
    //     fun fromEpoch(epochSecond: Long, secondNano: Long): Instant 
    //         = java.time.Instant.ofEpochSecond​(epochSecond, secondNano)
    // }

