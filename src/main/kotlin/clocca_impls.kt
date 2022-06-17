//// =====================================================================================
//// Clocca, a simple date and time library.  
////
//// Core data model, implementations.
////
//// Since the data model is independent of the logic supplied by the provider,
//// these classes are all simple data holders, with no real logic except basic
//// validation.
//// =====================================================================================
package offtheecliptic.clocca.impls

import offtheecliptic.clocca.interfaces.*

/// --------------------------------------------------------------------------------------
/// SimpleDate Record

data class SimpleDate(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null, 
    val timeZone: TimeZone? = null
): Date {
    init {
        if (month == null && day != null) {
            throw RuntimeException("Cannot specify day if month is not also specified.")
        }
        if (month == null && year != null) {
            throw RuntimeException("Cannot specify year if month is not also specified.")
        }
        constraint("SimpleDate", "month", month, 1, 12)
        constraint("SimpleDate", "day", day, 1, 31)
        // if (timeZone != null && (month == null || day == null)) {
        //     throw RuntimeException("Cannot specify time zone unless all date components specified.")
        // }
    }
    /** Copy constructor. */
    constructor(date: Date): 
        this(date.year(), date.month(), date.day(), date.timeZone()) {
    }

    override fun year():        Int?   = year
    override fun month(): Int?   = month
    override fun day():  Int?   = day
    override fun timeZone(): TimeZone? = timeZone
    override fun isZoned():   Boolean  = timeZone == null    // If true, date is absolute (bound to time zone)
    override fun isPartial(): Boolean  = day == null || year == null // If true, only year, or year+month, are specified
}

/// --------------------------------------------------------------------------------------
/// SimpleTime Record

data class SimpleTime(
    val hour:   Int,
    val minute: Int,
    val second: Int? = 0,
    val nanosecond: Int? = 0,
    val timeZone: TimeZone? = null
): Time {
    init {
        constraint("SimpleTime", "hour", hour, 0, 23)
        constraint("SimpleTime", "hour", minute, 0, 59)
        constraint("SimpleTime", "second", second, 0, 60)   // 60 only for leap-second
        constraint("SimpleTime", "nanosecond", nanosecond, 0, 999999999)
    }
    /** Copy constructor. */
    constructor(time: Time):
        this(time.hour(), time.minute(), time.second(), time.nanosecond(), time.timeZone()) {
    }

    override fun hour():   Int   = hour
    override fun minute(): Int   = minute
    override fun second(): Int?  = second
    override fun millisecond(): Int?  = if (nanosecond != null) { (nanosecond / 1000000).toInt() } else null
    override fun microsecond(): Int?  = if (nanosecond != null) { (nanosecond / 1000).toInt() } else null
    override fun nanosecond():  Int?  = nanosecond
    override fun timeZone(): TimeZone? = timeZone
    override fun isZoned():   Boolean  = timeZone == null   // If true, date is absolute (bound to time zone)
    override fun isPartial(): Boolean  = second == null     // If true, seconds not specified
}

/// --------------------------------------------------------------------------------------
/// DateTime Record

data class SimpleDateTime(
    val date: Date? = null,
    val time: Time? = null,
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
    override fun timeZone(): TimeZone? = timeZone
    override fun isZoned():   Boolean  = timeZone == null   // If true, date and time are absolute (bound to time zone)
    override fun isPartial(): Boolean                       // If true, only date and/or time, are specified 
        = date == null || time == null || date.isPartial() || time.isPartial() 
}

/// --------------------------------------------------------------------------------------
/// Duration Record

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
/// Instant Record

data class SimpleInstant(
    val epochSecond: Long?,
    val secondNano:  Int?
): Instant {
    override fun epochSecond(): Long? = epochSecond
    override fun secondNano():   Int? = secondNano
}

/// --------------------------------------------------------------------------------------
/// TimeZone Record

data class SimpleTimeZone(
    val timeZoneName: TimeZoneName, 
    val primeMeridianOffset: Duration = SimpleDuration(0,0)
): TimeZone {
    override fun timeZoneName(): TimeZoneName = timeZoneName
    override fun primeMeridianOffset(): Duration = primeMeridianOffset  // Duration in the form of hours and minutes (can be seconds too)
}

/// --------------------------------------------------------------------------------------
/// Helper Functions

private fun constraint(objectType: String, fieldName: String, 
                       value: Int?, minValue: Int, maxValue: Int) {
    value?.let { 
        if (it < minValue || it > maxValue) {
            throw RuntimeException(
                "$objectType construction failed, $fieldName value $it outside bounds [$minValue,$maxValue].")
        }
    } ?: return
}

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