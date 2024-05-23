package data_generation;

public class TransNodes_newsce {
    public int    NodeID ;
    public double    x , y;


    public TransNodes_newsce() {

    }

    public void setCoords(String[] dataline) {
        this.NodeID = (int)Double.parseDouble(dataline[0]);
        this.x = Double.parseDouble(dataline[1] );
        this.y = Double.parseDouble(dataline[2]);
    }
}
