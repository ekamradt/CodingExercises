# Interview Coding Exercise

We receive CSV's from out data pater that often have errors in them that can break downstream
 processes. As the first step in our process we need to prepare these CSV's for our data pipeline
 with the following rules:

- Discard blank rows and log the number of blank rows
- Discard rows with duplicate ids and log the number duplicates
- If multiple columns with the same name are encountered, we need to include the first one
    and drop any other columns and log the number of duplicate columns
- If non ascii characters are encountered, we want to strip them out but otherwise retain the row,
    and log the number of rows with invalid ascii characters.

The output should be a cleaned csv file ready for processing and a log of errors encountered along with
    your solution. Your solution can use any programming language of your choosing, but should be flexible.
    Meaning that you should be able to run it with any input file.


#### Compile and Run program 
 
javac ParseCsv.java

java ParseCsv sample_input.csv 

#### Output 
* filtered file to STDOUT
* Statistic report to STDERR

#### Note
* Written in core java, no libraries like lombok.
* Java 11
 