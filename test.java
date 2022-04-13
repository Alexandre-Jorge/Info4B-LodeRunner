import java.lang.annotation.Target;
import java.util.*;

public class test {
    public static int[] target = {1,4};
    public static void main(String Args[]){
        int[] pos = {3,3};
        System.out.println("final = "+tryMove('D',pos,3));   
    }
    private static Double tryMove(char dir, int tryPos1[],int depth){
        int[] tryPos = {tryPos1[0],tryPos1[1]};
        Double res = Math.sqrt(Math.pow(target[0]-tryPos[0],2)+Math.pow(target[1]-tryPos[1],2));
        for(int i=3;i>depth;i--)System.out.print("|  ");
        System.out.println(""+dir+" -> "+res);
        //boolean[] tmpArrayOfBooleans = updateCharacter(tryPos);
        //[onFloor, onTopOfLadder, onLadder, onZipline, wallOnLeft, wallOnRight]
        //if(res==0)System.out.println("res = 0");
        if(!(depth < 1 || res == 0)){
            if(dir=='L'){
                //if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[4]){
                    tryPos[0]--;
                    Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                    List<Double> trys = Arrays.asList(array);
                    res = Collections.min(trys);
                //}
            }
            else if(dir=='R'){
                //if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[5]){
                    tryPos[0]++;
                    Double[] array = {tryMove('R', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                    List<Double> trys = Arrays.asList(array);
                    res = Collections.min(trys);
                //}
            }
            else if(dir=='U'){
                //if(tmpArrayOfBooleans[2] && tryPos[1]>0){
                    tryPos[1]--;
                    Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('R',tryPos,depth-1)};
                    List<Double> trys = Arrays.asList(array);
                    res = Collections.min(trys);
                //}
            }
            else if(dir=='D'){
                //if(((tmpArrayOfBooleans[2] && !tmpArrayOfBooleans[0]) || tmpArrayOfBooleans[3] || tmpArrayOfBooleans[1]) && tryPos[1]<39){
                    tryPos[1]++;
                    Double[] array = {tryMove('L', tryPos,depth-1),tryMove('R',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                    List<Double> trys = Arrays.asList(array);
                    res = Collections.min(trys);
                //}
            }
        }
        return res;
    }
}
