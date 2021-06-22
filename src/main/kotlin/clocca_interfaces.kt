//// ======================================================================================
//// Simple date and time library.  This basically sits on top of java.time, but 
//// but only exposes the very basics.
//// ======================================================================================
@file:JvmName("CloccaInterfaces") 

package offtheecliptic.clocca.interfaces

/// --------------------------------------------------------------------------------------
/// INTERFACES

/**
 * A date is a representation of a point -- or a period -- on a calendar.
 *
 * A partial date can be defined by eliminting one or more date components from one
 * end or the other.  What this essentially does is to peg a period on a calendar,
 * rather tham a point on the calendar.  For example, by leaving out the day component, 
 * you can specify a month in a year.  Or by eliminating both the month and day, you 
 * can specifiy only a year. On the flip side, leaving out the year allows one to 
 * specify a month and a day -- e.g., a birth date, sans year.  Similarly, leaving 
 * out the year and month, you can specify a day number independent of month -- e.g.,
 * the "1st of the month".
 *
 * Dates are local -- independent of time zone -- unless a time zone is explicitly 
 * specified.  
 */
interface Date {
    fun year(): Int?             // year number
    fun monthOfYear(): Int?      // 1..12
    fun dayOfMonth():  Int?      // 1..31
    fun timeZone(): TimeZone?
    fun isZoned():   Boolean = timeZone() == null    // If true, date is absolute (bound to time zone)
    fun isPartial(): Boolean = false    // If true, only year, or year+month, are specified
}

interface Time {
    fun hour():   Int?          // 0..23
    fun minute(): Int?          // 0..59
    fun second(): Int?          // 0..59     (0..60 for leap-second)
    fun millisecond(): Int?     // 0..999    = 1st 3 digits of nanoseconds, rounded
    fun microsecond(): Int?     // 0..999999 = 1st 6 digits of nanoseconds, rounded
    fun nanosecond():  Int?     // 0..999999999
    fun timeZone(): TimeZone?
    fun isZoned():   Boolean = timeZone() == null    // If true, time is absolute (bound to time zone)
    fun isPartial(): Boolean = false    // If true, only hour, or hour+minute, are specified
}

interface DateTime {
    fun date(): Date?
    fun time(): Time?
    fun timeZone(): TimeZone?
    fun isZoned():   Boolean = timeZone() == null    // If true, date and time are absolute (bound to time zone)
    fun isPartial(): Boolean = false    // If true, only date and/or time are partially specified
}

interface TimeZone {
    fun timeZoneName(): TimeZoneName
    fun primeMeridianOffset(): Duration   // Duration in the form of hours and minutes (can be seconds too)
}

interface TimeZoneName {
    fun name(): String?
    fun abbrev(): String?
    fun locations(): Set<String>?
    fun matches(value: String, partial: Boolean = false): Boolean     // True if value matches name or abbreviation
}

interface Duration {
    fun totalSeconds(): Long?
    fun secondNanos():  Int? 
    // fun dayComponent():    Int?   // 0..Inf
    // fun hourComponent():   Int?   // 0..23         - number of hours
    // fun minuteComponent(): Int?   // 0..59         - number of minutes within the last hour
    // fun secondComponent(): Int?   // 0..59         - number of seconds within the last minute
    // fun milliComponent():  Int?   // 0..999        - number of milliseconds within the last second
    // fun microComponent():  Int?   // 0..999999     - number of microseconds within the last second
    // fun nanoComponent():   Int?   // 0..999999999  - number of nanoseconds  within the last second
}

interface Instant {
    fun epochSecond(): Long?
    fun secondNano(): Int?
}
