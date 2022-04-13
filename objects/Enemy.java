package objects;
import java.util.*;

public class Enemy extends Character{
    //attributs
    private Boolean bot;
    private Player target;
    //constructeurs
    //
    //par defaut
    public Enemy(PlayGround pg, boolean soloMode){
        super('X',pg,soloMode);
        this.bot = true;
    }
    //standard 1
    public Enemy(int x, int y, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = true;
    }
    //standard 2
    public Enemy(int x, int y, boolean b, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = b;
    }
    //standard 3
    public Enemy(int x, int y, Player p, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = true;
        this.target = p;
    }
    //standard 4
    public Enemy(int x, int y, boolean b, Player p, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = b;
        this.target = p;
    }
    //methodes
    //
    //getteurs
    public boolean isBot()    {return this.bot;}
    public Player  getTarget(){return this.target;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
    public void setTarget(Player p) {this.target = p;}
    //others
    public void die(){
        setInHole(false);
        setY(1);
        setX((int)(Math.random()*98+1));
    }
    public void updateEnemy(){
        updateCharacter();
        if(getPlayGround().getDisplayTab()[getX()][getY()].getType()=='#' && !isInHole()){
            setInHole(true);
            getPlayGround().escapeFromHole(this);
        }
    }
    
    @Override
    public void run(){
        if(isBot()){
            System.out.println("enemy control by AI");
            AI ai = new AI(15);
            ai.start();
        }
        else {
            System.out.println("enemy control by player");
            Control ctrl = new Control();
            ctrl.start();
        }
        while(!getPlayGround().isEndGame()){
            updateEnemy();
        }
    }
    class AI extends Thread{
        int depth;
        public AI(int depth){
            this.depth = depth;
        }
        private Double tryMove(char dir, int pos[],int depth){
            int[] tryPos = {pos[0],pos[1]};
            Double res = Math.sqrt(Math.pow(getTarget().getX()-tryPos[0],2)+Math.pow(getTarget().getY()-tryPos[1],2));
            boolean[] tmpArrayOfBooleans = updateCharacter(tryPos);
            //[onFloor, onTopOfLadder, onLadder, onZipline, wallOnLeft, wallOnRight]
            if(!(depth < 1 || res == 0)){
                if(dir=='L'){
                    if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[4]){
                        tryPos[0]--;
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        res = Collections.min(trys);
                    }
                }
                else if(dir=='R'){
                    if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[5]){
                        tryPos[0]++;
                        Double[] array = {tryMove('R', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        res = Collections.min(trys);
                    }
                }
                else if(dir=='U'){
                    if(tmpArrayOfBooleans[2] && tryPos[1]>0){
                        tryPos[1]--;
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('R',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        res = Collections.min(trys);
                    }
                }
                else if(dir=='D'){
                    if(((tmpArrayOfBooleans[2] && !tmpArrayOfBooleans[0]) || tmpArrayOfBooleans[3] || tmpArrayOfBooleans[1]) && tryPos[1]<39){
                        tryPos[1]++;
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('R',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        res = Collections.min(trys);
                    }
                }
            }
            return res;
        }
        @Override
        public void run(){
            /*boolean moved;
            while(getTarget().getLives()>0){
                try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
                moved = false;
                if(!isInHole()){
                    if (isOnFloor() || isOnTopOfLadder()){
                        if(isOnLadder()){
                            if (getY()<getTarget().getY()) {moved = goDown();}
                            else if (getY()>getTarget().getY()){moved = goUp();}
                        }
                        if((isOnFloor() || isOnTopOfLadder()) && !moved){
                            if(getX()<getTarget().getX()) {moved = goRight();}
                            else if (getX()>getTarget().getX()){moved = goLeft();}
                        }
                    }
                    if((isOnLadder() || isOnTopOfLadder())&& !moved){
                        if (getY()<getTarget().getY()) {moved = goDown();}
                        else if (getY()>getTarget().getY()){moved = goUp();}
                    }
                    if(isOnZipline() && !moved){
                        if(getX()<getTarget().getX()) {moved = goRight();}
                        else if (getX()>getTarget().getX()){moved = goLeft();}
                        else if(getY()<getTarget().getY()){moved = goDown();}
                    }
                }
            }*/
            boolean moved = false;
            while(getTarget().getLives()>0){
                try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
                int[] tryPos = {getX(),getY()};
                Double[] array = {tryMove('L', tryPos,this.depth),tryMove('R',tryPos,this.depth),tryMove('D',tryPos,this.depth), tryMove('U', tryPos,this.depth)};
                ArrayList<Double> trys =new ArrayList<>(Arrays.asList(array));
                moved = false;
                while(!moved){
                    int toDo = trys.indexOf(Collections.min(trys));
                    switch(toDo) {
                        case 0:{
                            moved = goLeft();
                            trys.set(0, 1000.0);//pour être sur que l'élément soit le plus grand
                            break;
                        }
                        case 1:{
                            moved = goRight();
                            trys.set(1, 1000.0);
                            break;
                        }
                        case 2:{
                            moved = goDown();
                            trys.set(2, 1000.0);
                            break;
                        }
                        case 3:{
                            moved = goUp();
                            trys.set(3, 1000.0);
                            break;
                        }
                    }
                }
            }
        }
    }
    class Control extends Thread{
        @Override
        public void run(){
            while(!getPlayGround().isEndGame()){
                if(getLeft())goLeft();
                else if(getRight())goRight();
                else if(getUp())goUp();
                else if(getDown())goDown();
                try{Thread.sleep(70);}catch(InterruptedException e){System.out.println(e);}
            }
        }
    }
}
