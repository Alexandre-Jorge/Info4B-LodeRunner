package objects;

import java.awt.event.*;

public class character extends object implements Runnable{
    //attributs
    private boolean onLadder, onFloor, onZipline, inHole, onTopOfLadder, diggableR, diggableL;
    protected MykeyListener keylistener;
    protected boolean left, right, up, down, digL, digR;
    private playGround pg;
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
    public character(char type, playGround pg, boolean soloMode){
        super(type);
        this.pg = pg;
        init(soloMode);
    }
    //standard 2
    public character(char type, int x, int y, playGround pg, boolean soloMode){
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
    public synchronized MykeyListener    getKeyListener() {return this.keylistener;}
    public synchronized boolean          getLeft()        {return this.left;}
    public synchronized boolean          getRight()       {return this.right;}
    public synchronized boolean          getUp()          {return this.up;}
    public synchronized boolean          getDown()        {return this.down;}
    public synchronized boolean          getDigR()        {return this.digR;}
    public synchronized boolean          getDigL()        {return this.digL;}
    public synchronized boolean          isDiggableL()    {return this.diggableL;}
    public synchronized boolean          isDiggableR()    {return this.diggableR;}
    public synchronized playGround       getPlayGround()  {return this.pg;}
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
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && getX()>0){
            setX(getX()-1);
            return true;
        }
        else return false;
    }
    public boolean goRight(){
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && getX()<99){
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
    public void updateCharacter(){
        char posC = getPlayGround().getDisplayTab()[getX()][getY()].getAvailableType();
        char UnderPosC;
        //deplacement
        if(getY()<39){
            UnderPosC = getPlayGround().getDisplayTab()[getX()][getY()+1].getAvailableType();
            if(UnderPosC =='#'|| isOnEnemy()) setOnFloor(true);
            else setOnFloor(false);
            if(posC==' ' && UnderPosC=='H') setOnTopOfLadder(true);
            else setOnTopOfLadder(false);
        }
        if(posC=='H') setOnLadder(true);
        else setOnLadder(false);
        if(posC=='_') setOnZipLine(true);
        else setOnZipLine(false);
        
        fall();
    }

    //run
    @Override
    public void run(){
        
    }

    class MykeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e)
        {
          
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 'a': {
                    dig('L');
                    try{Thread.sleep(30);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                    keyReleased(e);
                    break;
                }
                case 'e': {
                    dig('R');
                    try{Thread.sleep(30);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                    keyReleased(e);
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
                case 'a': {setDigL(false);break;}
                case 'e': {setDigR(false);break;}
            }
        }
    }
}
