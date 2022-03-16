import java.io.*;

public class gold extends object implements Serializable{
    public gold(){
        super("$");
    }
    public gold(int x, int y){
        super("$", x, y);
    }
}
