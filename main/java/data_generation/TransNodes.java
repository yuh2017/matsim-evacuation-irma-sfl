package data_generation;
import java.math.BigInteger;

public class TransNodes {
    public int    NodeID ;
    public double    x , y;


    public TransNodes() {

    }

    public void setCoords(String[] dataline) {
        this.NodeID = Integer.parseInt(dataline[0]) ;
        this.x = Double.parseDouble(dataline[2] );
        this.y = Double.parseDouble(dataline[1]);
    }
}
