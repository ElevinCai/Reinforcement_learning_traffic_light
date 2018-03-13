
public class Q {
    //closest car's position on roads(pos:0-8;no:9)
    int closestPosVtc;
    int closestPosHrzt;
    //light delay
    int delay;
    //same with isVertical in Lights
    boolean setting;
    //corresponding Q value
    double[] q = new double[2];

    /*
    2 more available state descriptions:
    (1)
    state =
    number of cars in 8 steps from intersection of horizontal road X
    number of cars in 8 steps from intersection of vertical road X
    light delay X
    light settings
    (2)
    state =
    number of pair of tightly following cars on horizontal road X
    number of pair of tightly following cars on vertical road X
    light delay X
    light settings
     */


    public Q(int vertical, int horizontal, boolean s, int d){
        closestPosVtc = vertical;
        closestPosHrzt = horizontal;
        setting = s;
        delay = d;
        q[0] = 0;
        q[1] = 0;
    }

    public int getDelay(){
        return delay;
    }

    public void setDelay(int newDelay){
        delay = newDelay;
    }

    public double getQ(int action){
        return q[action];
    }

    public void setQ(int action, double newQ){
        q[action] = newQ;
    }

    public boolean equals(int hrzt, int vtc, boolean s, int d){
        if(closestPosHrzt==hrzt && closestPosVtc==vtc && setting==s && delay==d){
            return true;
        }
        return false;
    }
}
