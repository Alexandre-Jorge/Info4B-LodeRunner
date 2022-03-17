import java.io.*;
import java.util.concurrent.TimeUnit;


public class lodeRunner{
    public static boolean endGame = false;
    public static void main(String Args[]){
        try{
            FileReader f = new FileReader("levels/level1.txt");
            playGround pg = new playGround(f,100,40);
            while(pg.getThPlayer1().isAlive()){
                pg.display();
                try{TimeUnit.MILLISECONDS.sleep(1000/50);}catch(InterruptedException e){System.out.println(e);}//50 fps
            }
        }catch(Exception e){System.out.println(e);}
    }
}