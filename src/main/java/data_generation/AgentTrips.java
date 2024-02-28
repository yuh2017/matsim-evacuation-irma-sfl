package data_generation;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class AgentTrips {
    private String separator = ",";
    private Charset charset = Charset.forName("UTF-8");

    public AgentTrips() {

    }
    public static void printArray(String[] rows) {
        for (int i = 0; i < rows.length; i++) {
            System.out.println(i + " , " +rows[i] + '\n');
        }
        System.out.println("\nlength of the array is "+ rows.length);
    }

    public List<AgentInfo> readFile(String inFile){
        List<AgentInfo> entries = new ArrayList<AgentInfo>();
        int idx = 1;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(inFile);
            isr = new InputStreamReader(fis, charset);
            br = new BufferedReader(isr);
            // skip first Line
            br.readLine();
            String line;
            while((line = br.readLine()) != null){
                String[] cols = line.split(separator);
                if( parseDouble(cols[24])  > 0.0 ) {
                    AgentInfo HouseholdEntry = new AgentInfo();
//                System.out.println("Person trips information: ");
//				printArray(cols);
                    HouseholdEntry.id_person = (int) Double.parseDouble(cols[9]);
//				HouseholdEntry.TAZ = (int) Double.parseDouble( cols[13] );
                    HouseholdEntry.h_x = parseDouble(cols[12]);
                    HouseholdEntry.h_y = parseDouble(cols[13]);
                    HouseholdEntry.s_x = parseDouble(cols[12]);
                    HouseholdEntry.s_y = parseDouble(cols[13]);
                    HouseholdEntry.d_x = parseDouble(cols[20]);
                    HouseholdEntry.d_y = parseDouble(cols[21]);
                    /** Time is in seconds **/
                    HouseholdEntry.starttime    = (int) (Double.parseDouble(cols[22]))* 60;
                    HouseholdEntry.endtime      = HouseholdEntry.starttime + (int) (Double.parseDouble(cols[ 25 ])*10/1.5) ;
                    HouseholdEntry.tripmode     = 3;
                    int tripMode = (int) Double.parseDouble(cols[4]);
                    if (tripMode == 1 || tripMode == 7) {
                        HouseholdEntry.trippurpose = 1;
                    } else if (tripMode == 4) {
                        HouseholdEntry.trippurpose = 1;
                    } else if (tripMode == 2) {
                        HouseholdEntry.trippurpose = 1;
                    } else if (tripMode == 3) {
                        HouseholdEntry.trippurpose = 1;
                    } else {
                        HouseholdEntry.trippurpose = 1;
                    }
                    HouseholdEntry.tripdistance = Math.sqrt(Math.pow(HouseholdEntry.s_x - HouseholdEntry.d_x, 2) + Math.pow(HouseholdEntry.s_y - HouseholdEntry.d_y, 2));
                    HouseholdEntry.tripduration = Double.parseDouble(cols[25]) * 10/1.25;
                    HouseholdEntry.id_tour = idx;
                    idx = idx + 1;
                    entries.add(HouseholdEntry);
                }
            }
            br.close();
            isr.close();
            fis.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return entries;
    }
    private int parseInteger(String string)
    {
        if (string == null) return 0;
        else if (string.trim().isEmpty()) return 0;
        else return Integer.valueOf(string);
    }

    private double parseDouble(String string){
        if (string == null) return 0.0;
        else if (string.trim().isEmpty()) return 0.0;
        else return Double.valueOf(string);
    }
}
