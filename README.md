# Flight-Plan-Project
Project for Data Structures &amp; Algorithms Course


Suyesh Shrestha

5/7/2023

PROJECT 2 - Graph Algorithm

- This program is able to determine the best possible flight plans for a person wishing to travel between two 
  different cities serviced by an airline (assuming a path exists) based on either cost or time

- This program is able to write to a output file as well as output into the terminal.
  - the output in the terminal displays:
    - all the flight legs read from the flight_data.txt file
    - all the requested flights from the requested_flights.txt file
    - a visualization of my adjacency graph
    - the top 3 best flight paths depending on cost or time
  - the output in the outputfile only displays the top 3 best flight paths depending on cost or time for each requested flight

 - This project utilizes a Depth First Search algorithm as well as an adjacency list

**was not able to print error message if an impossible flight plan was requested**
**all files must be entered into the flightPlan.java itself, it does not take parameters**
  - flight data file must be entered at line 279
  - requested flights file must be entered at line 291
  - output file must be entered at 394
