/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

class TestApp {

    /**
     * Take a rectangular grid of numbers and find the length
     * of the longest sub-sequence.
     * @return the length as an integer.
     */
    
    // Store objects in a list so they are uniquely accessable
    static List<NumberObject> objects = new ArrayList<NumberObject>();
    static HashMap<List, Integer> longestList;
    static int longestSequence = 0;
    
    public static int longestSequence(int[][] grid) {
        // TODO: implement this function
        // Create objects that will hold a unique ID & the value
        for(int row=0; row < grid.length; row++){
            for (int col=0; col < grid[row].length; col++){
                String array = Integer.toString(row) + Integer.toString(col);
                objects.add(new NumberObject(array, grid[row][col]));
            }
        }
        
        //Call the function to input all possible connections for each NumberObject
        pathfinderBot2000(grid);

       for(NumberObject s: objects){
            List<NumberObject> newList = new ArrayList<NumberObject>();
            longestList = new HashMap<List, Integer>();
            recursive(s, newList);
            
            //Choose the largest value in longestList and set it to longSequenceLength in NumberObject
            //Choose the longest list in longestList and set it to longList in NumberObject
            if (!longestList.isEmpty()){
                Integer large = Collections.max(longestList.values());
                s.setLongSequence(large);
                for (Entry<List, Integer> entry : longestList.entrySet()){
                    if (entry.getValue().equals(large)){
                        s.setLongList(entry.getKey());
                        break;
                    }
                }
            }
            else{
               s.setLongSequence(0);
            }
            
            
            if (s.getLongSequence() > longestSequence){
                longestSequence = s.getLongSequence();
            }
       }
        return longestSequence;
    }
    
    //Add all connections to each NumberObjects
    //I recognize that this violates encapsulation. I'm sorry. 
    public static void pathfinderBot2000(int[][] grid){
        for (NumberObject numObject: objects){
            String[] digits1 = numObject.getId().split("(?<=.)");
            int i = Integer.parseInt(digits1[0]);
            int x = Integer.parseInt(digits1[1]);
        // Test to see if row above to test against. Checking vertical up
        if ((i - 1 >= 0) && (Math.abs(grid[i - 1][x] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i - 1, x);
            numObject.setConnections(connection);
        }
        //Checking vertical down
        if ((i + 1 < grid.length) && (Math.abs(grid[i + 1][x] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i + 1, x);
            numObject.setConnections(connection);
        }
        //Check if horizontal left
        if ((x - 1 >= 0) && (Math.abs(grid[i][x - 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i, x - 1);
            numObject.setConnections(connection);
        }
        //Check if horizontal right
        if ((x + 1 < grid[i].length) && (Math.abs(grid[i][x + 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i, x + 1);
            numObject.setConnections(connection);
        }
        //Check if diagonal up left
        if ((i - 1 >= 0) && (x - 1 >= 0) && (Math.abs(grid[i - 1][x - 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i - 1, x - 1);
            numObject.setConnections(connection);
        }
        //Check if diagnoal up right
        if ((i - 1 >= 0) && (x + 1 < grid[i].length) && (Math.abs(grid[i - 1][x + 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i - 1, x + 1);
            numObject.setConnections(connection);
        }
        //Check if diagonal down left
        if ((i + 1 < grid.length) && (x - 1 >= 0) && (Math.abs(grid[i + 1][x - 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i + 1, x - 1);
            numObject.setConnections(connection);
        }
        //Check if diagonal down right
        if ((i + 1 < grid.length) && (x + 1 < grid[i].length) && (Math.abs(grid[i + 1][x + 1] - grid[i][x]) > 3)) {
            NumberObject connection = matchingBot(i + 1, x + 1);
            numObject.setConnections(connection);
        }
        }
    }
    
    //Match the location in the grid to the correct NumberObject
    public static NumberObject matchingBot(int row, int col){
        for (NumberObject x: objects){
            String idValue = Integer.toString(row) + Integer.toString(col);
            if (x.getId().equals(idValue)){
                return x;
            }
        }
        return null;
    }

    public static void recursive(NumberObject object, List<NumberObject> alterList){
        //This is where we are adding another 3
        if (!alterList.contains(object) && isUnique(object, alterList)) {
            alterList.add(object);
            
//          Get rid of duplicate objects values in alterList
//            for (int i=0; i < alterList.size(); i++){
//                   if (alterList.get(i).getValue() == object.getValue()){
//                       alterList.remove(object);
//                       return;
//                   }
//               }

            if (object.hasConnections()) {
                List<NumberObject> connections = object.getConnections();
                
                
                for (NumberObject y : connections) {
                    recursive(y, alterList);
                }
            } 
            // This does run, but not like I need it to
            else {
                if (object.getLongSequence() < alterList.size()){
                    object.setLongSequence(alterList.size());
                };
                if (alterList.size() > 0) {
                    alterList.remove(alterList.size() - 1);
                }
                return;
            }
        }
        else {
           //if an object has a long sequence, then it has its longest possible sequence of characters.
           //This would be an object we have already seen before. So we don't want to alter its longest list/sequence, just the one it's attached to.


           if (object.getLongSequence() > 0){
               // Even if we've already encountered the numbers in the longest string for 8, it doesn't care. It just reads "4" and adds it in.
               // TODO: Save string itself with largest value in each number object. If any values in the old number object exist in the new number object, we can't perform the addition.
            List<NumberObject> tempList = object.getLongList();
            int a = 0;
            for(NumberObject s: alterList){
                //if the tempList contains any variables from alterList, then the result is no good and we need to leave
                if (tempList.contains(s)){
                    a += 1;
                    break;
                }
            }
            if (a == 1){
                return;
            }
            //Else, if we see that no numbers in tempList exist in alterList, we can combine them together and 
            else {
                List<NumberObject> newList = new ArrayList<NumberObject>();
                newList.addAll(object.getLongList());
                newList.addAll(alterList);
                
                int size = object.getLongSequence() + alterList.size();
                
                longestList.put(newList, size);
            }
           }
           //If an object doesn't have a longestSequence, then we can put it in our longestList to determine which string is the longest.
           else {
           longestList.put(alterList, alterList.size());
           }
        return;
        }
    }
    
    public static boolean isUnique(NumberObject object, List<NumberObject> alterList){
        for (NumberObject x: alterList){
            if (x.getValue() == object.getValue())
                return false;
        }
        return true;
    }
    

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int numRows = 0;
        int numCols = 0;
        String[] firstLine = reader.readLine().split("\\s+");
        numRows = Integer.parseInt(firstLine[0]);
        numCols = Integer.parseInt(firstLine[1]);

        int[][] grid = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            String[] inputRow = reader.readLine().split("\\s+");

            for (int col = 0; col < numCols; col++) {
                grid[row][col] = Integer.parseInt(inputRow[col]);
            }
        }
        int length = longestSequence(grid);
        System.out.println(length);
    }
}

class NumberObject {
    private String id;
    private int value;
    private List<NumberObject> connections = new ArrayList<NumberObject>();
    private int longSequenceLength = 0;
    private List<NumberObject> longList = new ArrayList<NumberObject>();
    
    //Constructor
    public NumberObject(String id, int value){
        this.id = id;
        this.value = value;
    }
    //print statement
    public String toString(){
        return ("NumberOject: Id = " + id + "\nValue = " + value);
    }
    //Check if it has connections
    public boolean hasConnections(){
        if (connections == null){
            return false;
        }
        else if (connections.size() != 0){
            return true;
        }
        else 
            return false;
    }
    //Return the connections it has
    public List<NumberObject> getConnections(){
        return connections;
    }
    
    public void setConnections(NumberObject connection){
        connections.add(connection);
    }

    
    public void setLongSequence(int x) {
        longSequenceLength = x;
    }
    
    public int getLongSequence(){
        return longSequenceLength;
    }
    
    public String getId(){
        return id;
    }
    public int getValue(){
        return value;
    }
    
    public List<NumberObject> getLongList(){
        return longList;
    }
    
    public void setLongList(List<NumberObject> x){
        longList = x;
    }
}
