//// =====================================================================================
//// Clocca, a simple date and time library.  
////
//// Core data model, interfaces.
////
//// The core model is 100% independent of any logic involving calendars, clocks,
//// time zones, and other intricacies of "the hard stuff".  All of that logic is
//// delegated to the operations APIs, for performing, for example, serializations 
//// and deserializations, and for constructing the objects in ways besides those 
//// offerred y thje core impls.
//// =====================================================================================
package offtheecliptic.clocca.interfaces

/// --------------------------------------------------------------------------------------
/// INTERFACES

/**
 * A date is a representation of a point -- or a period -- on a calendar.  
 * 
 * A date is represented by 3 numbers: the year, the month number in the year, and
  the day in the month.
 *
 * A partial date can be defined by eliminating one or more date components from one
 * end or the other.  What this essentially does is to peg a period on a calendar,
 * rather than a point on the calendar.  For example, by leaving out the day component, 
 * you can specify a month in a year, such as "January 2020".  Or by eliminating both 
 * the month and day, you can specifiy only a year, such as "2020", the year of the 
 * great pandemic. On the flip side, leaving out the year allows one to specify a month
 * and a day -- e.g., a birth date or a recurring holiday, sans year -- such as "July 4".  
 * Similarly, leaving out the year and month, you can specify a day number independent 
 * of month -- e.g., the "1st of the month".
 *
 * Dates are independent of time zone -- aka 'local' -- unless a time zone is explicitly 
 * specified.  
 */
interface Date {
    fun year(): Int?             // year number
    fun month(): Int?      // 1..12
    fun day():  Int?      // 1..31
    fun timeZone(): TimeZone?
    fun isZoned():   Boolean = timeZone() == null    // If true, date is absolute (bound to time zone)
    fun isPartial(): Boolean = false    // If true, only year, or year+month, are specified
}

/**
 * A time is a representation of an point -- or a period -- on a clock.  Since
 * a clock cycles every 24 hours, a time by itself cannot be represented on a 
 * temporal line (a 'time line').  
 * 
 * Time is described by 3 numbers -- the hopur, the minute in the hour, and the seconds
 * in the minute. Since seconds can be fractional, it is split into two integral numbers, 
 * representing the whole number of seconds and the fractional number of seconds.  So,
 * this interface represents time with 4 numbers: the hour, the minute in the hour, the
 * second in the minute (whole number), and the nanosecond part of the second.
 * 
 * The finest granularity of the units of time gets down to the nanosecond level.
 * However, the default behavior is that nanoseconds are set to 0, not that they
 * are unset (null), which implies a precision of 1 second, whereas the default
 * behavior implies a precision of 1 nanosecond, which happens to always be 0 if
 * not explicitly set. 
 * 
 * A 'period' of time can be represented by leaving some of the lower-order units
 * unset.  For example, leaving seconds unset (which implies that nanoseconds is
 * also unset) means the time specified will cover a period of 1 minute on a clock.
 * Similarly, leaving minutes unset (which implies that seconds and nanoseconds are 
 * also unset) means 
 */
interface Time {
    fun hour():   Int           // 0..23
    fun minute(): Int           // 0..59
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
    //fun locations(): Set<String>?
    //fun matches(value: String, partial: Boolean = false): Boolean     // True if value matches name or abbreviation
}

interface Duration {
    fun totalSeconds(): Long?
    fun secondNanos():  Int? 
    // Do these through 'introspect' operation set
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
