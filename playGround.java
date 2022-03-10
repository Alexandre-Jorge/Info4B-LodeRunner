import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class playGround{
    //attributs
    private object display[][];
    private int sizeX;
    private int sizeY;
    //constructeurs
    //
    //standard
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.display = new object[sizeX][sizeY];
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    if(line.charAt(i)=='O')display[i][j] = new player();
                    else display[i][j] = new object(""+line.charAt(i));
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