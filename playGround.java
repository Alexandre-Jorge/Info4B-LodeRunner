import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class playGround{
    //attributs
    private char display[][];
    private int sizeX;
    private int sizeY;
    //constructeurs
    //
    //standard
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.display = new char[sizeX][sizeY];
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    display[i][j] = line.charAt(i);
                }
                j++;
            }
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    
    //toString
    public String toString(){
        String res = "";
        for(int j=0;j<this.sizeY;j++){
            for(int i=0;i<this.sizeX;i++){
                res+=this.display[i][j];
            }
            res+='\n';
        }
        return res;
    }

}