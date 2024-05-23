package data_generation;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.NetworkWriter;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigInteger;

public class createNetwork {
    private static final long CAP_FIRST_LAST = 3600; // [veh/h]
    // capacity at all other links
    private static final long CAP_MAIN = 1800; // [veh/h]

    // link length for all other links
    private static final long LINK_LENGTH = 200; // [m]
    // travel time for links that all agents have to use
    private static final double MINIMAL_LINK_TT = 1; // [s]

    public static void printArray(String[] rows) {
        for (int i = 0; i < rows.length; i++) {
            System.out.println(i + " , " +rows[i]);
            System.out.print('\n');
        }
        System.out.println("\nlength of the array is "+ rows.length);
    }

    public static ArrayList<TransNodes> readNodes(String csvFile) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<TransNodes> AllNodes = new ArrayList<TransNodes>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] dataline = line.split(cvsSplitBy);

//                System.out.println("Print out node information");
//                printArray(dataline) ;

                TransNodes nodei = new TransNodes();
                nodei.setCoords(dataline) ;
                AllNodes.add(nodei);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finished reading Nodes!");
        return AllNodes;
    }

    public static ArrayList<TransEdges> readEdges(String csvFile) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<TransEdges> AllEdges = new ArrayList<TransEdges>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            int idx = 0;
            while ((line = br.readLine()) != null) {
                String[] dataline = line.split(cvsSplitBy);
//                System.out.println("Print out edge information: \n");
//				printArray(dataline) ;
                TransEdges edgei = new TransEdges(idx);
                edgei.setEdge(dataline);
                idx += 1;
//                householdi.displayhousehold();
                AllEdges.add(edgei);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Finished reading Edges!");
        return AllEdges;
    }


    public static void main(String[] args) {
        /**   **/

//		String NodePth = "C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\sflNodes.csv";
//        String edgePth = "C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\sflEdges.csv";


        String NodePth = "C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\sflNodes_flood.csv";
        String edgePth = "C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\sflEdges_flood.csv";
//
//        String NodePth2 = "C:\\Users\\yuh46\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiNodes_100_slr.csv";
//        String edgePth2 = "C:\\Users\\yuh46\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiEdges_100_slr.csv";
//
//        String NodePth3 = "C:\\Users\\yuh46\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiNodes_500_slr.csv";
//        String edgePth3 = "C:\\Users\\yuh46\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiEdges_500_slr.csv";


        ArrayList<TransNodes> SFLNodes = readNodes(NodePth);
        ArrayList<TransEdges> SFLEdges = readEdges(edgePth);

        Config config = ConfigUtils.createConfig();
        Scenario scenario = ScenarioUtils.createScenario(config);
        Network net = scenario.getNetwork();
        NetworkFactory fac = net.getFactory();

        // create nodes

        HashMap<Integer, Node> NodeMap = new HashMap<Integer, Node>() ;
        for (TransNodes entry : SFLNodes) {
            Node n0 = fac.createNode(Id.createNodeId(entry.NodeID), new Coord(entry.x, entry.y));
            net.addNode(n0);
            NodeMap.put(entry.NodeID, n0) ;
//            """if ( NodeMap.containsKey(  entry.NodeID ) ){
//                System.out.println( "Transport node with id " + entry.NodeID + "exist" );
//            }else{
//                net.addNode(n0);
//                NodeMap.put(entry.NodeID, n0) ;
//            }"""
        }

        // create links
        ArrayList<String> linkIDs = new ArrayList<String>();
        for (TransEdges entry2 : SFLEdges) {
            int Capacity;
            if( entry2.Speed_limit > 0 & entry2.LANES  > 0){
                if( entry2.DISTANCE > entry2.Speed_limit ){
                    Capacity = (int) ( Math.round( entry2.DISTANCE / entry2.Speed_limit  )  * entry2.LANES  );
                }else{
                    Capacity = entry2.LANES  ;
                }


                if( Capacity == 0){
                    System.out.println( "Capacity 0 I " + entry2.DISTANCE + " " + entry2.Speed_limit + " " + entry2.LANES);
                }

            }else if( entry2.Speed_limit <= 0 ) {
                Capacity = 0;

            }else{
                Capacity = (int) ( Math.round( entry2.DISTANCE   )   );
                if( Capacity == 0){
                    System.out.println( "Capacity 0 III " + entry2.DISTANCE + " " + entry2.Speed_limit + " " + entry2.LANES);
                }
            }

            double speed = entry2.Speed_limit ;
            if( Capacity <= 0 ){
                System.out.println( "Transport Capacity is " + Capacity );
                System.out.println( "Distance " + entry2.DISTANCE + " Speed limit " + entry2.Speed_limit + " Lanes " + entry2.LANES);
            }
            String newStr = entry2.BeginID + "_" + entry2.EndID;
            String newStr2 = entry2.EndID + "_" + entry2.BeginID;

            if( entry2.BeginID != entry2.EndID ){

                linkIDs.add(newStr);
                Link l = fac.createLink(Id.createLinkId(newStr), NodeMap.get(entry2.BeginID), NodeMap.get(entry2.EndID));
                setLinkAttributes(l, Capacity, entry2.DISTANCE , entry2.LANES, speed);
                net.addLink(l);

            }

//				linkIDs.add(newStr2);
//				Link l2 = fac.createLink(Id.createLinkId(newStr2), NodeMap.get(entry2.EndID), NodeMap.get(entry2.BeginID));
//				setLinkAttributes(l2, Capacity, entry2.DISTANCE * 1609.34, entry2.LANES, speed);
//				net.addLink(l2)

        }
        System.out.println( "Finish reading data" );
        // write network
//        new NetworkWriter(net).write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario1\\SflNetwork.xml");
//        new NetworkWriter(net).write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario2\\SflNetwork.xml");

        new NetworkWriter(net).write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario3\\SflNetwork_flooded2.xml");
        new NetworkWriter(net).write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario4\\SflNetwork_flooded2.xml");
        System.out.println("Net size "+SFLEdges.size());
    }


    private static void setLinkAttributes(Link link, double capacity, double length, int lanes, double speed) {
        link.setCapacity(capacity);
        link.setLength(length);
        link.setNumberOfLanes(lanes);

        // agents have to reach the end of the link before the time step ends to
        // be able to travel forward in the next time step (matsim time step logic)
        link.setFreespeed( speed );
    }

}
