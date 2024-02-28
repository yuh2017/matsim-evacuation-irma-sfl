package data_generation;

public class TransEdges {

    public int   EndID, BeginID, edgeID, LANES, TWOWAY;
    public double  DISTANCE, traveltime;
    public double  beginx , beginy, endx, endy, Speed_limit;


    public TransEdges(int i) {
        this.edgeID = i;
    }

    public void setEdge(String[] dataline) {

        this.BeginID = (int) Double.parseDouble(dataline[0]);
        this.EndID = (int) Double.parseDouble(dataline[1]);

        this.beginx = Double.parseDouble(dataline[103]);
        this.beginy = Double.parseDouble(dataline[104]);
        this.endx = Double.parseDouble(dataline[105]);
        this.endy = Double.parseDouble(dataline[106]);

        this.DISTANCE = Double.parseDouble(dataline[3]);
        this.traveltime = Double.parseDouble(dataline[4]);
        this.LANES = (int)Double.parseDouble(dataline[6]);

        this.edgeID = (int)Double.parseDouble(dataline[107]);
        this.Speed_limit = Double.parseDouble(dataline[16]) ; //

        if(this.Speed_limit < 4.47){
            this.Speed_limit = 0;
        }

        if(this.LANES < 1){
            this.LANES = 0;
        }

        this.TWOWAY = (int)Double.parseDouble(dataline[8]);

    }
}
