# Library Clocca

## Introduction

This is a simple library for managing time and date and related concepts such as duration and timezone.

Why 'clocca'?  Because it is the Latin word for bell, which in earlier times was used to indicate to townspeople the passage of time.

## Motivation

Why another library for dates and times? Especially when the Java/Kotlin world has settled on java.time?  Because java.time and other date/time libraries were designed to handle *every* use case related to time and calendar data management This library is intended for applications that have very simple needs.

## Design Philosopy

Everything is interface-based.

The core data concepts -- date, time, datetime, duration, instant, and timezone -- do not have *any* ties to serialization or deserialization, or to construction (aside from default constructors).  Instead, all sewrde (serializtion-deserialization) is in a separate serde file, and all construction methods are in a separate factories file.

The default implementation is provided by the java.time library.  However, another library could be used in its place.

The serde operations can be used to get the raw objects that the implementation library provides.

No operations are provided for date and time arithmetic.  Deal with it.


## Usage

Maven coordinates: [offtheecliptic/clocca 0.2.0]

### Import Statements

import offtheecliptic.clocca.interfaces.*
import offtheecliptic.clocca.impls.*
import offtheecliptic.clocca.factories.*
import offtheecliptic.clocca.serde.*

## Concepts

There are three types of elements which can be composed into component networks.

    Date        - Defines a date in a calendar. 
    Time        - Defines 
    DateTime    - Defines a combination of a date and a time, which, when
        each is fully specified, defines a single point on a calendar timeline.
    TimeZone    - 

### Special Considerations

    Partial date    - A date that does not have all the date components 
        (year, month, day).  If any of the components are not provided, the missing one or ones must be at one end of the component sequence.  
    Zoned date      - A date that is bound to a specific timezone.
    Partial time    - A time that does not have all the time components (
        (hour, minute, second, nanosecond).  If any of the components are not provided, the missing one or ones must be at one end of the component sequence.  For example, hour and minute can be missing, but minute alone cannot.  Similarly, second can be missing, as can second and minute, but again, ninute cannot be missing on its own.   
    Zoned time      - A time that is bound to a specific timezone.
    

### Date


## Test Cases

To compile:
   mvn clean
   mvn compile
   mvn package
To run, can do either of these:
   java  -jar target/clocca-0.2.0-jar-with-dependencies.jar
   kotlin -cp target/clocca-0.2.0-jar-with-dependencies.jar .....


## Definitions


## Usage

## Example

## Build

To compile:
   mvn clean
   mvn compile
   mvn package

## Test

You can run the test cases using Maven.

To run, can do either of these:

java  -jar target/clocca-0.2.0-jar-with-dependencies.jar

kotlin -cp target/clocca-0.2.0-jar-with-dependencies.jar ....


### Imports

i
#### Simple Test Cases


#### Mapping Test Cases


## Contributions

## License

Copyright © 2021 OffTheEcliptic

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

