package cave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CAVE {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int mode_opt = 0;// define value for two different modes 1 | 2
        //By default start and End cavern is -1
        int start_cavern = -1;
        int end_cavern = -1;
        System.out.print("Choose mode from below options : ");
        System.out.print("Press \"1\" : ");
        System.out.print("Press \"2\" : ");
        String input = scanner.nextLine();// Scan user input to get value for mode
        switch (input) {
            case "1": System.out.println("First Mode Started"); mode_opt=1;break;
            case "2": System.out.println("Second Mode Started");mode_opt=2;break;
        }
        
        
        
        // Open input.cav
        BufferedReader br = new BufferedReader(new FileReader("input1.cav"));

        //Read the line of comma separated text from the file
        String buffer = br.readLine();
        System.out.println("Raw data : " + buffer);

        br.close();

        //Convert the data to an array
        String[] data = buffer.split(",");

        //Now extract data from the array - note that we need to convert from String to int as we go
        int noOfCaves = Integer.parseInt(data[0]);
        System.out.println("There are " + noOfCaves + " caves.");
        
        System.out.println("Choose value from 1 to" + noOfCaves + " for start and end caverns.");
        
        
        
        System.out.print("Enter Start Cavern : ");
        int start = scanner.nextInt();// Scan user input to get start cavern
        start_cavern = start;
        System.out.print("Enter End Cavern : ");
        int end = scanner.nextInt();// Scan user input to get end cavern
        end_cavern = end;


        //Get coordinates
        for (int count = 1; count < ((noOfCaves * 2) + 1); count = count + 2) {
            System.out.println("Cave at " + data[count] + "," + data[count + 1]);
        }

        //Build connectivity matrix
        //Declare the array
        boolean[][] connected = new boolean[noOfCaves][];

        for (int row = 0; row < noOfCaves; row++) {
            connected[row] = new boolean[noOfCaves];
        }
        //Now read in the data - the starting point in the array is after the coordinates 
        int col = 0;
        int row = 0;

        for (int point = (noOfCaves * 2) + 1; point < data.length; point++) {
            //Work through the array

            if (data[point].equals("1")) {
                connected[row][col] = true;
            } else {
                connected[row][col] = false;
            }

            row++;
            if (row == noOfCaves) {
                row = 0;
                col++;
            }
        }
        //Connected now has the adjacency matrix within it 
        for (boolean[] row_connected : connected) {
        System.out.println(Arrays.toString(row_connected));
            
        }


        ArrayList<Integer> cavePath = new ArrayList<Integer>();
        cavePath.add(start_cavern);//set start cavern to path
        int curr_cave = start_cavern;//set current cavern from user input
        double tot_distance = 0;//set default total distance to 0
        boolean isNextAvailable = true;//set default true for next available cavern
        //find next connected and shortest Cave
        //While having any available cavern loop continues
        while (isNextAvailable) {
            //if mode is second then user have to press enter to proceed further
            if(mode_opt==2){
                System.out.println("Press Enter to proceed:");
                String next = scanner.nextLine();
                while(!next.trim().equals("")){
                    next = scanner.nextLine();
                }
            }
            System.out.println("Current Cavern is :" + curr_cave);
            System.out.println(Arrays.toString(connected[curr_cave - 1]));
            boolean[] curr_row = connected[curr_cave - 1];
            ArrayList<Integer> availableNextNode = new ArrayList<Integer>();
            for (int i = 0; i < curr_row.length; i++) {
                if(curr_row[i]){
                    availableNextNode.add(i+1);
                }
            }
            System.out.println("Available Next Caverns "+(availableNextNode.size()==1?"is":"are")+": "+availableNextNode);
            ArrayList<Integer> pathMapKey = new ArrayList<Integer>();
            ArrayList<Double> pathMapValue = new ArrayList<Double>();
            
            for (int i = 0; i < availableNextNode.size(); i++) {
                //check if connected cavern are not traversed yet
                if(cavePath.indexOf(availableNextNode.get(i))<0){
                    int next_avail_cave = availableNextNode.get(i);
                    System.out.println("Checking for next available connected cavern :" + curr_cave + " ==> " + availableNextNode.get(i));
                    //Calculating distance between any two caverns
                    int x2 = 0, x1 = 0, y2 = 0, y1 = 0;// set all co-ordinates to 0
                    x2 = Integer.parseInt(data[next_avail_cave + next_avail_cave - 1]);//Extract x co-ordinate from data array for next available connected cavern
                    x1 = Integer.parseInt(data[curr_cave + curr_cave - 1]);//Extract x co-ordinate from data array for current cavern
                    y2 = Integer.parseInt(data[next_avail_cave + next_avail_cave]);//Extract y co-ordinate from data array for next available connected cavern
                    y1 = Integer.parseInt(data[curr_cave + curr_cave]);//Extract y co-ordinate from data array for current cavern
                    System.out.println("Co-ordinates are: ("+x1+","+y1+"), ("+x2+","+y2+")");
                    double distPath = Math.sqrt(((x2 - x1) * (x2 - x1)) + (y2 - y1) * (y2 - y1));// Calculate Euclidean distance between the two coordinates
                    pathMapKey.add(next_avail_cave);// Store distance 
                    pathMapValue.add(distPath);// Store distance for this cavern
                    System.out.println("Euclidean distance between the two coordinates is : " + distPath+" between cavern "+curr_cave+" ==> "+next_avail_cave);
                    System.out.println(">>" + pathMapValue);
                    System.out.println(">>" + pathMapKey);
                }
                    
                    //Check for last available connected cavern distance from current cavern is calculated
                if (availableNextNode.size()==1 || i==(availableNextNode.size()-1)) {
                    double minDist = -1;//set default for min distance 
                    int minkey = -1;//set default for min distance's cavern 
                    if (pathMapValue.size() > 1) {//check distance list size is greater than 1
                        if (pathMapKey.indexOf(end_cavern) >= 0) {//check end cavern is in @pathMapKey available connected cavern list
                            minDist = pathMapValue.get(pathMapKey.indexOf(end_cavern));// distance with end cavern is final distan
                        } else {//check end cavern is not in @pathMapKey available connected cavern list
                            //find next available min distance cavern 
                            for (int ii = 1; ii < pathMapValue.size(); ii++) {
                                minDist = (pathMapValue.get(ii - 1) < pathMapValue.get(ii) ? pathMapValue.get(ii - 1) : pathMapValue.get(ii));
                            }
                        }
                    } else {//distance list size is <=1
                        minDist = pathMapValue.get(0);
                        tot_distance += minDist;
                    }
                    if (pathMapValue.size() > 1) {//check distance list size is greater than 1
                        if (pathMapKey.indexOf(end_cavern) >= 0) {//check end cavern is in @pathMapKey available connected cavern list
                            minkey = end_cavern;//set end cavern to min key
                        } else {//check end cavern is not in @pathMapKey available connected cavern list
                            //find next available min distance cavern 
                            for (int ii = 0; ii < pathMapValue.size(); ii++) {
                                if (minDist == pathMapValue.get(ii)) {//check which connected cavern is having min distance with current
                                    tot_distance += minDist;//add distance
                                    minkey = pathMapKey.get(ii);//get min key for settin new current cavern
                                }
                            }
                        }
                    } else {//distance list size is <=1
                        minkey = pathMapKey.get(0);
                    }
                    System.out.println("Next Available Node:" + minkey);
                    cavePath.add(minkey);
                    System.out.println("Path:" + cavePath);
                    curr_cave = (minkey);
                    System.out.println("New Current Cave:" + curr_cave);
                    System.out.println("======================Final Output==============================");
                    System.out.println("Path : " + cavePath);
                    System.out.println("Distance : " + tot_distance);
                    if (curr_cave == end_cavern) {
                        System.exit(0);
                    }
                }
            }
        }

    }

}
