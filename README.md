#  Heuristic-Search
## CS452

## Author(s):

Lucas Rappette

## Date:

3/16/18


## Description:

A program that implements dijkstra's shortest path algorithm with a heuristic.
This program searches for the shortest path from one city to another from city to city
In this case the heuristic is the shortest arc length from one city to another on the globe.
There is an additional readme for this project, which is the original assignment description.

This file is called _README.pdf_.


## How to build the software

Add this project to any Java IDE, it will automatically compile.
If this does not work execute the command below on the command line to build the project.

```
javac -d bin -sourcepath src src/*
```


## How to use the software

Execute the command below on a command line in the directory, or run from the 
IDE with runtime arguments.

```
java -cp bin; Search x
```

_Valid Arguments:_

- The first arg is required, __x__ is the city data file with extension included.
- File formats are described in _README.pdf_.


## How the software was tested

Testing was completed by using outlier-like input arguments in order to stress
test the algorithm's capabilities.


## Known bugs and problem areas

None.