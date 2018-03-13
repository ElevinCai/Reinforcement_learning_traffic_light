import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Simulator extends JFrame{
    static MyPanel panel = new MyPanel();

    public Simulator(){


        this.add(panel);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //new Thread(panel).start();
    }

    public static void main(String[] args) {
        new Simulator();
    }


}

class MyPanel extends JPanel implements Runnable{
    Lights lights = new Lights(true);
    ArrayList<Car> leftToRightRoad = new ArrayList<Car>();
    ArrayList<Car> topToBottomRoad = new ArrayList<Car>();

    public MyPanel(){
        new Thread(lights).start();
        try{
            Thread.sleep(50);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void paint(Graphics g){

        super.paint(g);
        //draw two roads
        g.setColor(Color.darkGray);
        g.fillRect(0, 450, 1000, 5);
        g.fillRect(0, 545, 1000, 5);
        g.fillRect(450, 0, 5, 1000);
        g.fillRect(545, 0, 5, 1000);
        g.setColor(Color.gray);
        g.fillRect(0, 499, 1000, 2);
        g.fillRect(499, 0, 2, 1000);
        //draw lights
        g.setColor(Color.black);
        g.fillRect(455, 435, 35, 15);
        g.fillRect(435, 510, 15, 35);
        g.fillRect(510, 550, 35, 15);
        g.fillRect(550, 455, 15, 35);

        if(lights.ifVerticalRed()){
            g.setColor(Color.red);
            g.fillOval(459, 439, 10, 10);
            g.fillOval(514, 554, 10, 10);
            g.setColor(Color.green);
            g.fillOval(439, 514, 10, 10);
            g.fillOval(554, 459, 10, 10);
        }else {
            g.setColor(Color.green);
            g.fillOval(459, 439, 10, 10);
            g.fillOval(514, 554, 10, 10);
            g.setColor(Color.red);
            g.fillOval(439, 514, 10, 10);
            g.fillOval(554, 459, 10, 10);
        }
        //draw cars
        //System.out.println(leftToRightRoad.size());
        //System.out.println(topToBottomRoad.size());
        for(int i=0;i<leftToRightRoad.size();i++){
            Car car = leftToRightRoad.get(i);
            g.setColor(Color.blue);
            if(car.ifLeftToRight_Or_TopToBottom()){
                g.fillRect(car.getPosition()*10-10, 455, 10, 8);
            }else{
                g.fillRect(1000-car.getPosition()*10, 537, 10, 8);
            }
        }
        for(int i=0;i<topToBottomRoad.size();i++){
            Car car = topToBottomRoad.get(i);
            g.setColor(Color.blue);
            if(car.ifLeftToRight_Or_TopToBottom()){
                g.fillRect(455, car.getPosition()*10-10, 8, 10);
            }else{
                g.fillRect(537, 1000-car.getPosition()*10, 8, 10);
            }
        }



    }




    @Override
    public void run() {
        Car car = null;
        // TODO Auto-generated method stub
        while(true){
            Random rd = new Random();
            switch(rd.nextInt(4)){
                case 0:
                    car = new Car(true, true);
                    topToBottomRoad.add(car);
                    break;
                case 1:
                    car = new Car(false, true);
                    leftToRightRoad.add(car);
                    break;
                case 2:
                    car = new Car(true, false);
                    topToBottomRoad.add(car);
                    break;
                case 3:
                    car = new Car(false, false);
                    leftToRightRoad.add(car);
                    break;
            }
            new Thread(car).start();
            try {
                Random rt = new Random(10);
                Thread.sleep((rt.nextInt(10)+5)*100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}