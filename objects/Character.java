package objects;

import java.awt.event.*;

public class Character extends Object implements Runnable{
    //attributs
    private boolean onLadder, onFloor, onZipline, inHole, onTopOfLadder, wallOnLeft, wallOnRight, diggableR, diggableL;
    protected MykeyListener keylistener;
    protected boolean left, right, up, down, digL, digR;
    private PlayGround pg;
    //constructeurs
    //
    //fonction init
    private void init(boolean solo){
        this.onLadder      = false;
        this.onFloor       = false;
        this.onZipline     = false;
        this.inHole        = false;
        this.onTopOfLadder = false;
        if(solo)
            this.keylistener   = new MykeyListener();
    }
    //standard 1
    public Character(char type, PlayGround pg, boolean soloMode){
        super(type);
        this.pg = pg;
        init(soloMode);
    }
    //standard 2
    public Character(char type, int x, int y, PlayGround pg, boolean soloMode){
        super(type, x, y);
        this.pg = pg;
        init(soloMode);
    }
    //methodes
    //
    //getteurs
    public synchronized boolean          isOnLadder()     {return this.onLadder;}
    public synchronized boolean          isOnFloor()      {return this.onFloor;}
    public synchronized boolean          isOnZipline()    {return this.onZipline;}
    public synchronized boolean          isInHole()       {return this.inHole;}
    public synchronized boolean          isOnTopOfLadder(){return this.onTopOfLadder;}
    public synchronized boolean          isDiggableL()    {return this.diggableL;}
    public synchronized boolean          isDiggableR()    {return this.diggableR;}
    public synchronized MykeyListener    getKeyListener() {return this.keylistener;}
    public synchronized boolean          getLeft()        {return this.left;}
    public synchronized boolean          getRight()       {return this.right;}
    public synchronized boolean          getUp()          {return this.up;}
    public synchronized boolean          getDown()        {return this.down;}
    public synchronized boolean          getDigR()        {return this.digR;}
    public synchronized boolean          getDigL()        {return this.digL;}
    public synchronized PlayGround       getPlayGround()  {return this.pg;}
    public synchronized boolean          getWallOnLeft()   {return this.wallOnLeft;}
    public synchronized boolean          getWallOnRight()  {return this.wallOnRight;}
    //setteurs
    public synchronized void setOnLadder(boolean b)          {this.onLadder = b;}
    public synchronized void setOnFloor(boolean b)           {this.onFloor = b;}
    public synchronized void setOnZipLine(boolean b)         {this.onZipline = b;}
    public synchronized void setInHole(boolean b)            {this.inHole = b;}
    public synchronized void setOnTopOfLadder(boolean b)     {this.onTopOfLadder = b;}
    public synchronized void setDiggableL(boolean b)         {this.diggableL = b;}
    public synchronized void setDiggableR(boolean b)         {this.diggableR = b;}
    public synchronized void setDigL(boolean b)              {this.digL = b;}
    public synchronized void setDigR(boolean b)              {this.digR = b;}
    public synchronized void setKeyListener(MykeyListener kl){this.keylistener = kl;}
    public synchronized void setUp(boolean b)                {this.up = b;}
    public synchronized void setDown(boolean b)              {this.down = b;}
    public synchronized void setLeft(boolean b)              {this.left = b;}
    public synchronized void setRight(boolean b)             {this.right = b;}
    public synchronized void setWallOnLeft(boolean b)        {this.wallOnLeft = b;}
    public synchronized void setWallOnRight(boolean b)       {this.wallOnRight = b;}
    //deplacements
    public void goInit() {setX(getInitX());setY(getInitY());}
    public boolean goUp(){
        boolean res;
        if(isOnLadder() && getY()>0){
            setY(getY()-1);
            res=true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}
        return res;
    }
    public boolean goDown(){
        boolean res;
        if(((isOnLadder() && !isOnFloor()) || isOnZipline() || isOnTopOfLadder()) && getY()<39){
            setY(getY()+1);
            res = true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}
        return res;
    }
    public boolean goLeft(){
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && !getWallOnLeft()){
            setX(getX()-1);
            return true;
        }
        else return false;
    }
    public boolean goRight(){
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && !getWallOnRight()){
            setX(getX()+1);
            return true;
        }
        else return false;
    }
    public void fall(){
        if(!isOnLadder() && !isOnFloor() && !isOnZipline() && !isOnTopOfLadder() && !isInHole() && getY()<39){
            setY(getY()+1);
            try{Thread.sleep(70);}catch(InterruptedException e){System.out.println(e + "class character method fall");}
        } 
    }

    public boolean dig(char side){
        if(this.getAvailableType()=='O'){
            if(side=='R' && isDiggableR()){
                setDigR(true);
                return true;
            }
            else if(side=='L' && isDiggableL()){
                setDigL(true);
                return true;
            }
            else return false;
        }
        else return false;
    }
    public boolean isOnEnemy(){
        for(int i=0;i<getPlayGround().getEnemys().size();i++){
            if(getX()==getPlayGround().getEnemy(i).getX() && getY()==getPlayGround().getEnemy(i).getY()-1) return true;
        }
        return false;
    }
    public boolean isOnEnemy(int pos[]){
        for(int i=0;i<getPlayGround().getEnemys().size();i++){
            if(pos[0]==getPlayGround().getEnemy(i).getX() && pos[1]==getPlayGround().getEnemy(i).getY()-1) return true;
        }
        return false;
    }
    public void updateCharacter(){
        char posC = getPlayGround().getDisplayTab()[getX()][getY()].getAvailableType();
        char leftPosC = getPlayGround().getDisplayTab()[getX()-1][getY()].getAvailableType();
        char rightPosC = getPlayGround().getDisplayTab()[getX()+1][getY()].getAvailableType();
        char UnderPosC;
        //deplacement
        if(getY()<39){
            UnderPosC = getPlayGround().getDisplayTab()[getX()][getY()+1].getAvailableType();
            if(isOnEnemy() || UnderPosC =='#') setOnFloor(true);
            else setOnFloor(false);
            if(posC==' ' && UnderPosC=='H') setOnTopOfLadder(true);
            else setOnTopOfLadder(false);
        }
        if(posC=='H') setOnLadder(true);
        else setOnLadder(false);
        if(posC=='_') setOnZipLine(true);
        else setOnZipLine(false);
        if(leftPosC=='#') setWallOnLeft(true);
        else setWallOnLeft(false);
        if(rightPosC=='#') setWallOnRight(true);
        else setWallOnRight(false);
        
        fall();
    }
    public boolean[] updateCharacter(int pos[]){
        char posC = getPlayGround().getDisplayTab()[pos[0]][pos[1]].getAvailableType();
        char leftPosC = getPlayGround().getDisplayTab()[pos[0]-1][pos[1]].getAvailableType();
        char rightPosC = getPlayGround().getDisplayTab()[pos[0]+1][pos[1]].getAvailableType();
        char UnderPosC;
        boolean[] out = new boolean[6];
        //deplacement
        if(getY()<39){
            UnderPosC = getPlayGround().getDisplayTab()[pos[0]][pos[1]+1].getAvailableType();
            if(isOnEnemy(pos) || UnderPosC =='#') out[0] = true;
            else out[0] = false;
            if(posC==' ' && UnderPosC=='H') out[1] = true;
            else out[1] = false;
        }
        if(posC=='H') out[2] = true;
        else out[2] = false;
        if(posC=='_') out[3] = true;
        else out[3] = false;
        if(leftPosC=='#') out[4] = true;
        else out[4] = false;
        if(rightPosC=='#') out[5] = true;
        else out[5] = false;
        return out;
    }

    //run
    @Override
    public void run(){
        
    }

    class MykeyListener implements KeyListener{
        private boolean haveDigged = false;
        @Override
        public void keyTyped(KeyEvent e)
        {
          
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 'a': {
                    if(!haveDigged){
                        dig('L');
                        try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                        setDigL(false);
                        this.haveDigged = true;
                    }
                    break;
                }
                case 'e': {
                    if(!haveDigged){
                        dig('R');
                        try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                        setDigR(false);
                        this.haveDigged = true;
                    }
                    break;
                }
                case 'z': {setUp(true);break;}
                case 'q': {setLeft(true);break;}
                case 's': {setDown(true);break;}
                case 'd': {setRight(true);break;}
                case 27 : {System.exit(0);break;}
                

            }
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 'z': {setUp(false);break;}
                case 'q': {setLeft(false);break;}
                case 's': {setDown(false);break;}
                case 'd': {setRight(false);break;}
                case 'a': {
                    setDigL(false);
                    this.haveDigged = false;
                    break;
                }
                case 'e': {
                    setDigR(false);
                    this.haveDigged = false;
                    break;
                }
            }
        }
    }
}
