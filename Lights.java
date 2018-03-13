import java.util.ArrayList;
import java.util.Random;

public class Lights implements Runnable{
    private boolean isVerticalRed;
    private int interval = 15;
    //Q(s, a) table
    private ArrayList<Q> table = new ArrayList<>();
    //discount factor:
    double gamma = 0.9;
    //learning rate:
    double alpha = 0.1;
    //epsilon value(0.1-epsilon greedy;0-softmax):
    double epsilon;
    //stategy(0-random;1-non random):
    int stategy = 1;

    public Lights(boolean verticalRed){
        isVerticalRed = verticalRed;
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                for(int k=0;k<4;k++){
                    Q q0 = new Q(i, j, true, k);
                    Q q1 = new Q(i, j, false, k);
                    table.add(q0);
                    table.add(q1);
                }
            }
        }
    }

    public int getClosestPosition(ArrayList<Car> road){
        if(road.size()==0){
            return 9;
        }
        int temp = 36;
        for(int i=0;i<road.size();i++){
            if(road.get(i).getPosition()>=37 && road.get(i).getPosition()<=45 && road.get(i).getPosition()>temp){
                temp = road.get(i).getPosition();
            }
        }
        if(temp==36){
            return 9;
        }else{
            return 45-temp;
        }
    }

    public int reward(){
        if(Simulator.panel.leftToRightRoad.size()!=0){
            for(int i=0;i<Simulator.panel.leftToRightRoad.size();i++){
                if(!Simulator.panel.leftToRightRoad.get(i).ifMoving()){
                    return -1;
                }
            }
        }
        if(Simulator.panel.topToBottomRoad.size()!=0){
            for(int i=0;i<Simulator.panel.topToBottomRoad.size();i++){
                if(!Simulator.panel.topToBottomRoad.get(i).ifMoving()){
                    return -1;
                }
            }
        }
        return 0;
    }


    public boolean ifVerticalRed(){
        return isVerticalRed;
    }

    public void switchLights(){
        if(isVerticalRed){
            isVerticalRed = false;
        }else{
            isVerticalRed = true;
        }
    }

    public Q observeNext(Q current, int action){
        int hrzt = current.closestPosHrzt;
        int vtc = current.closestPosVtc;
        boolean s;
        int d = 0;
        if(current.setting){
            if(action==0){
                s = true;
                if(current.delay!=0){
                    d = current.delay - 1;
                }
            }else{
                s = false;
                d = 3;
            }
        }else{
            if(action==1){
                s = true;
                d = 3;
            }else{
                s = false;
                if(current.delay!=0){
                    d = current.delay - 1;
                }
            }
        }
        for(int i=0;i<table.size();i++){
            if(table.get(i).equals(hrzt, vtc, s, d)){
                return table.get(i);
            }
        }
        return null;
    }

    public void setInterval(int newInterval){
        interval = newInterval;
    }



    public void run(){
        int count = 0;
        int[] stoppedCars = new int[2];
        stoppedCars[0] = 0;
        stoppedCars[1] = 0;
        int delay = 0;
        int temp = 0;
        ArrayList<Integer> intervals = new ArrayList<>();
        while(true){
            int switchOrNot = 0;
            if(count<1000){
                for(int i=0;i<Simulator.panel.leftToRightRoad.size();i++){
                    if(!Simulator.panel.leftToRightRoad.get(i).ifMoving()){
                        stoppedCars[0]++;
                    }
                }
                for(int i=0;i<Simulator.panel.topToBottomRoad.size();i++){
                    if(!Simulator.panel.topToBottomRoad.get(i).ifMoving()){
                        stoppedCars[0]++;
                    }
                }
                if(count%15==0){
                    switchOrNot = 1;
                }
                for(int i=0;i<table.size();i++){
                    if(table.get(i).equals(getClosestPosition(Simulator.panel.leftToRightRoad), getClosestPosition(Simulator.panel.topToBottomRoad), isVerticalRed, delay)){
                        //Q = Q + alpha * (r + gamma * max(Q') - Q)
                        table.get(i).setQ(switchOrNot, table.get(i).getQ(switchOrNot) + alpha * (reward() + gamma * Math.max(observeNext(table.get(i), switchOrNot).getQ(0), observeNext(table.get(i), switchOrNot).getQ(1)) - table.get(i).getQ(switchOrNot)));
                        break;
                    }
                }
                if(switchOrNot==1){
                    switchLights();
                    delay = 3;
                    Simulator.panel.repaint();
                }

                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }else if(count==2000){
                for(int i=0;i<table.size();i++){
                    System.out.println("0: "+table.get(i).getQ(0)+"\t1: "+table.get(i).getQ(1));

                }
                System.out.println(stoppedCars[0]+"$$$$$"+stoppedCars[1]);
                int s = 0;
                for(int i=0;i<intervals.size();i++){
                    s += intervals.get(i);
                }
                System.out.println((double)s/intervals.size());
                break;
            }else if(count>=1000 && count<2000){
                for(int i=0;i<Simulator.panel.leftToRightRoad.size();i++){
                    if(!Simulator.panel.leftToRightRoad.get(i).ifMoving()){
                        stoppedCars[1]++;
                    }
                }
                for(int i=0;i<Simulator.panel.topToBottomRoad.size();i++){
                    if(!Simulator.panel.topToBottomRoad.get(i).ifMoving()){
                        stoppedCars[1]++;
                    }
                }

                for(int i=0;i<table.size();i++){
                    if(table.get(i).equals(getClosestPosition(Simulator.panel.leftToRightRoad), getClosestPosition(Simulator.panel.topToBottomRoad), isVerticalRed, delay)){
                        //epsilon greedy(e=0.1):
                        Random r = new Random();
                        if(r.nextInt(9)==0){
                            switchOrNot = r.nextInt(1);
                        }else{
                            if(table.get(i).getQ(0)>table.get(i).getQ(1)){
                                switchOrNot = 0;
                            }else{
                                switchOrNot = 1;
                            }
                        }
                        /*
                        random exploration:
                        Random r = new Random();
                        switchOrNot = r.nextInt(1);
                         */

                        /*
                        softmax exploration:
                        if(table.get(i).getQ(0)>table.get(i).getQ(1)){
                            switchOrNot = 0;
                        }else{
                            switchOrNot = 1;
                        }
                         */

                        //Q = Q + alpha * (r + gamma * max(Q') - Q)
                        table.get(i).setQ(switchOrNot, table.get(i).getQ(switchOrNot) + alpha * (reward() + gamma * Math.max(observeNext(table.get(i), switchOrNot).getQ(0), observeNext(table.get(i), switchOrNot).getQ(1)) - table.get(i).getQ(switchOrNot)));
                        break;
                    }
                }
                if(switchOrNot==1 && delay==0){
                    switchLights();
                    intervals.add(count - temp);
                    temp = count;
                    delay = 3;
                    Simulator.panel.repaint();
                }

                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            if(delay!=0){
                delay--;
            }
            count++;
        }
    }


    public void random(){

        Random r = new Random();
        if(r.nextInt(2)==0){

        }else{
            switchLights();
        }

    }


}
