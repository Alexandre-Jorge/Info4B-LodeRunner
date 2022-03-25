import java.io.*;
import objects.*;

public class lodeRunner{
    public static boolean endGame = false;
    public static final int HEIGHT = 40;
    public static final int WIDTH = 100;
    public static void main(String Args[]){
        try{
            FileReader f = new FileReader("levels/level1.txt");
            playGround pg = new playGround(f,WIDTH,HEIGHT);
            while(pg.getThPlayer1().isAlive()){
                pg.display();
                try{Thread.sleep(1000/50);}catch(InterruptedException e){System.out.println(e);}//50 fps
            }
            f.close();
        }catch(Exception e){System.out.println(e);}
    }
}