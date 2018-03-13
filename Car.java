
public class Car implements Runnable{
    private boolean isVertical;
    private boolean isLeftToRight_Or_TopToBottom;
    private boolean isMoving = false;
    private int position = 0;

    public Car(boolean vertical, boolean inverse){
        isVertical = vertical;
        isLeftToRight_Or_TopToBottom = inverse;
    }

    public boolean ifVertical(){
        return isVertical;
    }

    public boolean ifLeftToRight_Or_TopToBottom(){
        return isLeftToRight_Or_TopToBottom;
    }

    public boolean ifMoving(){
        return isMoving;
    }

    public int getPosition(){
        return position;
    }

    public void move(){
        isMoving = true;
    }

    public void stop(){
        isMoving = false;
    }

    public void setPosition(int pos){
        position = pos;
    }

    public boolean hasStoppingCarInFront(){
        if(ifVertical()){
            if(Simulator.panel.topToBottomRoad.size()==0)return false;
            for(int i=0;i<Simulator.panel.topToBottomRoad.size();i++){
                if(ifLeftToRight_Or_TopToBottom()==Simulator.panel.topToBottomRoad.get(i).ifLeftToRight_Or_TopToBottom() && getPosition()+1==Simulator.panel.topToBottomRoad.get(i).getPosition() && !Simulator.panel.topToBottomRoad.get(i).ifMoving()){
                    return true;
                }
            }
        }else{
            if(Simulator.panel.leftToRightRoad.size()==0)return false;
            for(int i=0;i<Simulator.panel.leftToRightRoad.size();i++){
                if(ifLeftToRight_Or_TopToBottom()==Simulator.panel.leftToRightRoad.get(i).ifLeftToRight_Or_TopToBottom() && getPosition()+1==Simulator.panel.leftToRightRoad.get(i).getPosition() && !Simulator.panel.leftToRightRoad.get(i).ifMoving()){
                    return true;
                }
            }
        }
        return false;
    }


    public void run(){
        while(true){
            if(getPosition()>100){
                if(ifVertical()){
                    Simulator.panel.topToBottomRoad.remove(this);

                }else{
                    Simulator.panel.leftToRightRoad.remove(this);
                }
                break;
            }

            if(Simulator.panel.lights.ifVerticalRed() && ifVertical() && getPosition()==45) {
                stop();
            }else if(!Simulator.panel.lights.ifVerticalRed() && !ifVertical() && getPosition()==45) {
                stop();
            }else if(hasStoppingCarInFront()){
                stop();
            }else{
                move();
            }

            if(ifMoving()){
                setPosition(getPosition()+1);
            }
            Simulator.panel.repaint();
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(5000);
            Thread.interrupted();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
