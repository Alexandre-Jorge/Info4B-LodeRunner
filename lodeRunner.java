import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class lodeRunner{
    public static void main(String Args[]){
        try{
            FileReader f = new FileReader("level2.txt");
            playGround pg = new playGround(f,100,40);
            System.out.println(""+pg);
            pg.test();
        }catch(Exception e){System.out.println(e);}
    }
}