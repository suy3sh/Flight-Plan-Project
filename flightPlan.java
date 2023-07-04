/* OBJECTIVE
   ---------
- Determine all possible flight plans for a person wishing to travel between two 
  different cities serviced by an airline (assuming a path exists) 

- Calculate the total cost incurred for all parts of the trip

- Use information from two different input files in order to calculate the trip plan and total cost
*/

import java.util.*;
import java.io.*;

//Flight Leg Object Class
class Flight{
   String origin;
   String destination;
   int cost;
   int time;

   public Flight(String o, String d, int c, int t){
      origin = o;
      destination = d;
      cost = c;
      time = t;
   }

   public String toString() {
      return String.format("%s | %s | %d | %d", origin, destination, cost, time);
   }
}

//Requested Flight Plan Object Class
class Plan{
   String origin;
   String destination;
   char sort;

   public Plan(String o, String d, char s){
      origin = o;
      destination = d;
      sort = s;
   }

   public String toString() {
      return String.format("%s | %s | %c", origin, destination, sort);
   }

}

//Path Object Class
class Path{
   List<String> path;
   int totalCost;
   int totalTime;

   public Path(List<String> p, int c, int t){
      path = p;
      totalCost = c;
      totalTime = t;
   }

   public void print() {
      System.out.print("Path: ");
      System.out.print(path.get(0));
      for (int i = 1; i < path.size(); i++){
         System.out.print(" -> " + path.get(i));
      }
      System.out.println("\nTotal Cost: " + totalCost);
      System.out.println("Total TIme: " + totalTime);   
   }

}

//class to sort by cost
class SortbyCost implements Comparator<Path>{
   public int compare(Path a, Path b){
      return a.totalCost - b.totalCost;
   }
}

//class to sorty by time
class SortbyTime implements Comparator<Path>{
   public int compare(Path a, Path b){
      return a.totalTime - b.totalTime;
   }
}

//Graph Class to create undirected graph
class Graph<T> {
   //Hashmap to store edges
   public static Map<String, LinkedList<String>> edges = new HashMap<>();

   //adds vertex to graph
   public void addVertex(String s){
      edges.put(s, new LinkedList<String>());
   }

   //adds edge
   public void addEdge(String src, String dest){
      if(!edges.containsKey(src)){
         addVertex(src);
      }
      
      if(!edges.containsKey(dest)){
         addVertex(dest);
      }

      edges.get(src).add(dest);
      edges.get(dest).add(src);   

   }

   //print adj list
   public String toString(){
      StringBuilder builder = new StringBuilder();

      for (String v : edges.keySet()){
         builder.append(v.toString() + ": ");
         for (String w : edges.get(v)){
            builder.append(w.toString() + " ");
         }
         builder.append("\n");
      }

      return builder.toString();
   }

   //gets all paths between two specified vertices (uses DFS)
   public List<List<String>> getAllPaths(String src, String dest) {
      List<List<String>> allPaths = new ArrayList<>();
      boolean[] visited = new boolean[edges.size()];
      List<String> path = new ArrayList<>();

      path.add(src);

      dfs(src, dest, visited, path, allPaths);

      return allPaths;
   }

   //Depth First Search
   public void dfs(String current, String destination, boolean[] visited, List<String> path, List<List<String>> allPaths){
      visited[getIndex(current)] = true;

      if (current.equals(destination)){
         allPaths.add(new ArrayList<>(path));
      } else {
         for (String neighbor : edges.get(current)){
            if (!visited[getIndex(neighbor)]){
               path.add(neighbor);
               dfs(neighbor, destination, visited, path, allPaths);
               path.remove(path.size() - 1);
            }
         }
      }

      visited[getIndex(current)] = false;
   }

   //gets the index of a vertex in Map<String, LinkedList<String>> edges = new HashMap<>();
   private int getIndex(String vertex) {
      int index = 0;
      for (String key : edges.keySet()) {
          if (key.equals(vertex))
              return index;
          index++;
      }
      return -1;
  }
}

public class flightPlan{

   //read flight data file
   static ArrayList<Flight> readFlightData(String file) throws IOException {
      
      //read file
      File flightDataFile = new File(file);
      Scanner sc = new Scanner(flightDataFile);

      //number of legs
      int numFlights = Integer.parseInt(sc.nextLine());

      // //Array holding all legs
      ArrayList<Flight> flightsLegs = new ArrayList<Flight>();

      //fill the array
      for (int i = 0; i < numFlights; i++){
         String line = sc.nextLine();
         String[] parts = line.split("\\|");
         String origin = parts[0];
         String destination = parts[1];
         int cost = Integer.parseInt(parts[2]);
         int time = Integer.parseInt(parts[3]);

         Flight flight = new Flight(origin, destination, cost, time);

         flightsLegs.add(flight);
      }

      //close file
      sc.close();

      return flightsLegs;
   }

   //read flight plan data file
   static ArrayList<Plan> readFlightPlans(String file) throws IOException {

      //read file
      File requestedFlightsFile = new File(file);
      Scanner sc = new Scanner(requestedFlightsFile);

      //number of requests
      int numPlans = Integer.parseInt(sc.nextLine());

      //Array holding all the requested flights
      ArrayList<Plan> requestedFlights = new ArrayList<Plan>();

      //fill the array
      for (int i = 0; i < numPlans; i++){
         String line = sc.nextLine();
         String[] parts = line.split("\\|");
         String origin = parts[0];
         String destination = parts[1];
         char sort = parts[2].charAt(0);

         Plan plan = new Plan(origin, destination, sort);

         requestedFlights.add(plan);
      }

      //close the file
      sc.close();

      return requestedFlights;
   }

   //writes flight plans to output file
   static void writeToOutput(String file, ArrayList<Plan> reqFlights, List<Path> paths){
      try{
      
         FileWriter myWriter = new FileWriter(file);
      
         for (int i = 0; i < reqFlights.size(); i++){
            myWriter.write("Flight " + (i + 1) + ": " + reqFlights.get(i).origin + " to " + reqFlights.get(i).destination + " (" + reqFlights.get(i).sort + ")");
            myWriter.write("\n---------------------------------------------------\n");

            for (int j = 0; j < paths.size(); j++){
               myWriter.write("Path: ");
               myWriter.write(paths.get(0).path.get(0));
               for (int k = 1; k < paths.get(j).path.size(); k++){
                  myWriter.write(" -> " + paths.get(j).path.get(k));
               }
               myWriter.write("\nTotal Cost: " + paths.get(j).totalCost + "\n");
               myWriter.write("Total Time: " + paths.get(j).totalTime + "\n\n"); 

               if (j == 2)
                  break;
            }

            myWriter.write("\n");
            
         } 

         myWriter.close();
         System.out.println("Successfully wrote to the file.");

      } catch (IOException e){
         System.out.println("ERROR WRITING TO FILE");
      }
   }

   public static void main(String[] args) throws IOException {

      /*
       * READ BOTH INPUT FILE AND PUT DATA INTO AN ARRAY
       */

      //Array of flight leg objects (EDGES OF THE GRAPH)
      ArrayList<Flight> flightLegsArray = readFlightData("flight_data.txt");

      //print flight legs array
      System.out.println("FLIGHT LEGS");
      System.out.println("-----------");
      for (int i = 0; i < flightLegsArray.size(); i++){
         System.out.println(flightLegsArray.get(i));
      }

      System.out.print("\n");

      //Array of requested flight plans (REQUESTED PATH IN GRAPH)
      ArrayList<Plan> requestedFlightsArray = readFlightPlans("requested_flights.txt");

      //print requested flights array
      System.out.println("REQUESTED FLIGHTS");
      System.out.println("-----------------");
      for (int i = 0; i < requestedFlightsArray.size(); i++){
         System.out.println(requestedFlightsArray.get(i));
      }

      System.out.print("\n");


      /*
      * CREATE A UNDIRECTED GRAPH BASED OFF FLIGHT LEGS
      */

      //Hashmap to store edges
      Graph<String> legs = new Graph<String>();

      //add all the legs to the graph
      for (int i = 0; i < flightLegsArray.size(); i++){
         legs.addEdge(flightLegsArray.get(i).origin, flightLegsArray.get(i).destination);
      }

      //print adj list
      System.out.println("GRAPH:");
      System.out.println("------");
      System.out.println(legs.toString() + "\n");
   

      /*
      * FIND ALL THE POSSIBLE REQUESTED FLIGHT PLANS BASED OFF OF THE FLIGHT LEGS
      */

       /*
      * CALCULATE THE TOTAL COST AND TIME FOR POSSIBLE FLIGHT PLANS
      */

      List<Path> flightPaths = new ArrayList<Path>();

      
      for (int i = 0; i < requestedFlightsArray.size(); i++){
         //initialize both LISTS

         //List of PATHS that will be sorted and displayed
         flightPaths = new ArrayList<Path>();

         //List that just holds all the individual lists of strings representing paths
         List<List<String>> allPaths = new ArrayList<>();

         allPaths = legs.getAllPaths(requestedFlightsArray.get(i).origin, requestedFlightsArray.get(i).destination);
         
         int totalCost;
         int totalTime;

         //claculates all possible paths and their weight in cost and time
         for (List<String> path : allPaths) {
            totalCost = 0;
            totalTime = 0;

            for (int j = 0; j < path.size() - 1; j++){
               String origin = path.get(j);
               String destination = path.get(j + 1);

               for (Flight flight : flightLegsArray){
                  if (flight.origin.equals(origin) && flight.destination.equals(destination)  || flight.origin.equals(destination) && flight.destination.equals(origin)){
                     totalCost = totalCost + flight.cost;
                     totalTime = totalTime + flight.time;
                     break;
                  }
               }
            }

            //creates a PATH object using list of strings representing path and calculated weights
            flightPaths.add(new Path(path, totalCost, totalTime));
         }    

         //sort based on the TIME or COST
         if (requestedFlightsArray.get(i).sort == 'T'){
            Collections.sort(flightPaths, new SortbyTime());
         } 
         else if (requestedFlightsArray.get(i).sort == 'C'){
            Collections.sort(flightPaths, new SortbyCost());
         }


         //Print requested flight and their respective PATHS
         System.out.println("\nFlight " + (i + 1) + ": " + requestedFlightsArray.get(i).origin + " to " + requestedFlightsArray.get(i).destination + " (" + requestedFlightsArray.get(i).sort + ")");
         System.out.println("---------------------------------------------------");
         for (int z = 0; z < flightPaths.size(); z++){
           flightPaths.get(z).print();
           System.out.println();
           if (z == 2){
            break;
           }
         }

   
         System.out.println("\n");

      }

      /*
      * WRITE RESULTS INTO THE OUTPUT FILE
      */

      writeToOutput("output.txt", requestedFlightsArray, flightPaths);
      

   }
}


