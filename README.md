# Library Clocca

## Introduction

This is a simple library for managing time and date and related concepts such as duration and timezone.

Why is it called 'clocca'?  Because it is the Latin word for bell, which in earlier times was used to indicate to townspeople the passage of time. Large clocks were also the first means of determining longitudinal distance on long ocean voyages; the longitudinal meridians form the basis for time zones, and hence our date reckoning. 

## Motivation

Why another library for dates and times? Especially when the Java/Kotlin world has settled on java.time? For a couple reasons.  One is that java.time and other date/time libraries were designed to handle *every* use case related to time and calendar data management; this library is intended for simple applications that have very simple needs. The other, arguably primary, reason is that this library is a playground for experimenting with design and code architecture in Kotlin -- specifically for exploring patterns for API-SPI-Plugin interactions.

## Design Philosophy

Everything is interface-based. So the code is organized into pairs of files for interfaces and implementations.

The core data model -- date, time, datetime, timezone, duration, instant -- is nearly 100% decoupled from operations involving concrete types.  The exception is that the default implementation of the basic data types provides a default constructor to set the fields of each of them.

Serialization and deserialization, and general construction -- all of which require possibly in-depth knowledge of calendars and time-keeping and such -- are delegated to a *provider*, through a service provider interface (SPI).  

The default provider is the java.time library.  However, another library could be used in its place.

No operations are provided for date and time arithmetic.  As mentioned in the Introduction, this library is intended for very simple use cases.  However, if one wanted some additional operations, a recommended approach is provided below.

## Overview of Code Organization

There are four distinct parts to this library:
* the core data model
* the operation set extension framework
* the operation set interfaces, containing API and SPI classes for each kind of operation supported
* the operation set provider, containing implementations for the API and SPI operation sets

The core data model is very simple.  It consists of interfaces and base implementations for a set of date- and time-related concepts: Date, Time, DateTime, Duration, Instant, and TimeZone.  This part of the library can stand alone; i.e., it can be imported and used independently of the rest of the library.

An operation set provide a set of classes and functions that perform useful operations on the core data model.  Operations typically require in-depth knowledge of how calendars and clocks work, often taking into account locales (time zones) and rules such as daylight savings time and leap years and seconds. That is why they are separated from the core model -- so that they can be delegated to a provider that has that in-depth knowledge codified.

The operation set framework defines a set of interface and base classes that enable the definition of operation set interfaces, and the subsequent implementation of those interfaces by a service provider.

The two operation set interfaces are provided out-of-the-box: Serde and Factory, for serialization/deserialization and construction operations, respectively. 

A provider provides the implementations for an operation set, using a library or set of libraries that provide the necessary logic for the operation set API functions.  

There is one provider for the two out-of-the-box operation sets: the java.time library.

 
## Files

### Core Model

There are two files that hold the core data model.
* clocca_interfaces
* clocca_impls - these have concrete implementations, independent of provider

### Operation Set Extension Framework

* xpi/interfaces
* xpi/base
* api/interfaces - extend their counterparts in xpi
* api/base - extend the xpi base
* spi/interfaces - extend their counterparts in xpi
* spi/base - extend the xpi base
* api/builder - instantiates ...
* spi/builder - instantiates ...

### Operation Sets

#### Serde
All operations that involve object-to-string conversions (serialization) or string-to-object conversions (deserialization) reside in three files in the 'serde' directory. None of these have ties to specific provider logic.
* serde/clocca_serde_interfaces - define all serialization/deserialization methods
* serde/clocca_serde_enums - provide formatting strings for parse and format ops
* serde/clocca_serde_impls - delegates to provider-supplied operations

#### Factory
Similarly, construction operations reside in two files in the 'factory' directory.
* factory/clocca_factory_interfaces - define all construction methods
* factory/clocca_factory_impls - delegates to provider-supplied operations

There is one file defining the service provider interface, in the spi directory:
* spi/clocca_spi

Finally, there is one provider provided with the core library: the java.time library.  This provides all the logic for the serde and factory impls.
* provider/javatime/clocca_provider_javatime

Note that technically the java.time provider should not be bundled in this library, and should instead be provided as an auxiliary jar. However, since java.time use is so pervasive in JVM-hosted languages -- Java, Kotlin, Clojure, Scala -- it is more convenient to bundled this one provider with the main library.  If other providers are defined though, they should be bundled separately.

## Architecture

Clocca consists of a very simple core model that is 100% independent of all complexities and nuances of calendars, clocks, and time zones, and the various ways to construct, serialize, compare, and manipulate those constructs.  In addition, Clocca provides auxiliary functionality -- i.e., those complexities and nuances mentioned above -- as 'operations' that can be added into the main library. 

### Core Data Model

### Operations Module

An operations module is organized as an extensible component with an API exposing the functionality and an SPI exposing interfaces to be implemented by a provider.  The component consists of the following subsystems:

   1. API - in clocca.api.{operation-name} - what consumers of library use
   2. SPI - in clocca.spi.{operation-name} - what providers are to implement
   3. Provider - in clocca.provider.{provider-name}.{operation-name} - provider impls

Each of those have interfaces, impls, and optionally, enums and examples.


## Usage

Maven coordinates: [offtheecliptic/clocca 0.2.0]

### Import Statements

# Core data model
    import offtheecliptic.clocca.interfaces.*
    import offtheecliptic.clocca.impls.*
# Serialization-deserialization operations
    import offtheecliptic.clocca.serde.interfaces.*
    import offtheecliptic.clocca.serde.enums.*
    import offtheecliptic.clocca.serde.impls.*
# Construction operations
    import offtheecliptic.clocca.factory.interfaces.*
    import offtheecliptic.clocca.factory.impls.*
# Service provider interfaces for operation packages
    import offtheecliptic.clocca.spi.serde.*
    import offtheecliptic.clocca.spi.factory.*
# Default provider implementations for operation packages
    import offtheecliptic.clocca.provider.javatime.serde.*
    import offtheecliptic.clocca.provider.javatime.factory.*

## Concepts

These are the key concepts the library deals with.

* **Date**         - Defines a date in a calendar. Can be local or based on a time zone.

* **Time**         - Defines a time on a clock. Can be local or based on a time zone.

* **DateTime**     - Defines a combination of a date and a time, which, when each is fully specified, defines a single point on a calendar timeline. Can be local or based on a time zone.

* **TimeZone**     - Defines a time zone for binding dates, times, and date-times to a specific longitudinal region of Earth. A time zone consists of two things: an identifier, in the form of a TimeZoneName, and an offset from the Prime Meridian.  The latter is defined in terms of a Duration, with positive values...

* **TimeZoneName** - An abstraction for dealing with time zones, consisting of a name, an abbreviation, and an optional set of locations in which the time zone is applicable.  [Maybe that should be in Time Zone, not name.]

* **Duration**     - 

* **Instant**      - A specific point in time, measured from a reference point. The typical reference point is January 1, 1970, as measured from the Prime Meridian. This is the Unix reference point.

### Special Considerations

#### Dates

* **Partial date** - A date that does not have all the date components (year, month, day).  If any of the components are not provided, the missing one or ones must be at one end of the component sequence.  

* **Zoned date**   - A date that is bound to a specific timezone.

#### Times

* **24-hour clock vs 12-hour clock**

**Partial time**    - A time that does not have all the time components (hour, minute, second, nanosecond).  If any of the components are not provided, the missing one or ones must be at one end of the component sequence.  For example, hour and minute can be missing, but minute alone cannot.  Similarly, second can be missing, as can second and minute, but again, minute cannot be missing on its own.   

* **Zoned time**      - A time that is bound to a specific timezone.
    
### DateTimes

* **Partial date-time**

* **Zoned date-time**


### Correspondence to java.time concepts

| Clocca Concept | java.time Concept             | Notes                              |
| -------------- | ------------------------------| -----------------------------------|
| Date           | LocalDate, ZonedDate          |   |
|                | Year, YearMonth, MonthDay     |   |
| Time           | LocalTime, ZonedDate          |   |
| DateTime       | LocalDateTime, ZonedDateTime  |   |
| TimeZone       | TimeZone                      |   |
| TimeZoneName   | ZoneId                        |   |
| Duration       | Duration                      |   |
| Instant        |                               | Tied in tighter with DateTime      |
|                |                               |                                    |

_________________________________________________________________________________________________________________
## Examples

### Example 1 - Default Constructors

    import offtheecliptic.clocca.interfaces.*
    import offtheecliptic.clocca.impls.*

    //These create local dates, times, and date-times...
    val date: Date = SimpleDate(2021,6,30)
    val time: Time = SimpleTime(12,30,40)
    val dateTime: DateTime = SimpleDateTime(date,time)

### Example 2 - Define Serde Singletons

These are used whenever you need to convert core data model instances to strings (serialization), or to extract core data model instances from strings (deserialization).  
    import offtheecliptic.clocca.serde.interfaces.*
    import offtheecliptic.clocca.serde.enums.*
    import offtheecliptic.clocca.serde.impls.*
    
    import offtheecliptic.clocca.spi.*
    import offtheecliptic.clocca.provider.javatime.*
    
    val cloccaProvider  = CloccaProviderJavaTime()
    val DATE_SERDE      = SimpleDateSerde.getInstance(cloccaProvider)
    val TIME_SERDE      = SimpleTimeSerde.getInstance(cloccaProvider)
    val DATE_TIME_SERDE = SimpleDateTimeSerde.getInstance(cloccaProvider)

### Example 3 - Deserialize Date/Time Instances from Strings

    val dateString = "2021-06-30"
    val date = DATE_SERDE.fromString(dateString) // equals SimpleDate(2021,6,30)
    val timeString = "12:30:40"
    val time = TIME_SERDE.fromString(timeString) // equals SimpleTime(12,30,40)

    val dateTimeString = "2021-06-30T12:30:40"
    val dateTime = DATE_TIME_SERDE.fromString(dateTimeString) // equals SimpleDateTime(date,time)
                     
### Example 4 - Serialize Date/Time Instances to Strings

### Example 5 - Define Factory Singletons for Construction

These are used whenever you need to construct core data model instances from the environment, from provider data concepts, or from data field combinations other than those exposed through the default constructors. Note that 'creation' of instances from strings is not considered to be a construction operation; instead it is a deserialization operation, available through the serde singletons.

    import offtheecliptic.clocca.factory.interfaces.*
    import offtheecliptic.clocca.factory.impls.*
    
    import offtheecliptic.clocca.spi.*
    import offtheecliptic.clocca.provider.javatime.*
    
    val cloccaProvider    = CloccaProviderJavaTime()
    val DATE_FACTORY      = SimpleDateFactory.getInstance(cloccaProvider)
    val TIME_FACTORY      = SimpleTimeFactory.getInstance(cloccaProvider)
    val DATE_TIME_FACTORY = SimpleDateTimeFactory.getInstance(cloccaProvider)

### Example 6 - Construct Date Instances 
### Example 7 - Construct Time Instances 
### Example 8 - Construct DateTime Instances 

### Example 9 - Instants

### Example 10 - Durations

### Example 11 - Time Zones

### Example 12 - Zoned Dates, Times, Date-Times

### Example 13 - Partial Dates - Years, Year-Months, and Month-Days

### Example 14 - Correspondence Between DateTimes and Instants
### Example 15 - 

_________________________________________________________________________________________________________________
## Library Developers - Basics

### Maven 

To compile:
   mvn clean
   mvn compile
   mvn package

## Test

### Maven 

You can run the test cases using Maven.

To run, can do either of these:

java  -jar target/clocca-0.2.0-jar-with-dependencies.jar

kotlin -cp target/clocca-0.2.0-jar-with-dependencies.jar ....

_________________________________________________________________________________________________________________
## Library Developers - Extending Clocca

There are two ways you can extend the functionality of this library:
1. Add operations to the existing library
2. Add a new provider

### Adding New Operations to Clocca

This library is 'minimalist' in terms of exposed functionality.  As such, it does not provide much of the functionality available in libraries such as java.time.  If you need that functionality, you have two choices: (1) use java.time; or (2) add additional functionality that build off this library.  We will discuss the second option, as the first is self-explanatory.

New functionality is organized and managed in terms of 'operation sets'.  An operation set has functions that perform one type of operation to all the constructs in the core model.  For example, two operation sets are supplied with the library at present:
* Serde - for serialization from core model to string, and deserialization from string to model
* Factory - for creating new instance of the core model constructs



There are two separate roles and activities involved in adding a new operation ...

### Operation Set Provider

Operations should be added in a separate 'auxiliary' library.  Their package names can be compatible with those of the core Clocca library, but they should be packaged separately.  For example, these are some libraries that people might want:
* clocca.compare.interfaces - compare dates, times, etc for relational operators (>, <, etc)
* clocca.arithmetic.interfaces - perform date/time-based arithmetic on instances
* clocca.native.interfaces - expose the native constructs of the provider, to directly leverage the provider library

For each of these operation libraries, the following should accompany the interfaces:
* clocca.{op-lib}.impls
* clocca.spi.interfaces.{op-lib}
* clocca.provider.{provider}.{op-lib}

_________________________________________________________________________________________________________________
## Test Cases


### Imports

#### Simple Test Cases


#### Mapping Test Cases


_________________________________________________________________________________________________________________
## Contributions


_________________________________________________________________________________________________________________
## License

Copyright © 2021-2022 OffTheEcliptic

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at https://www.gnu.org/software/classpath/license.html.

