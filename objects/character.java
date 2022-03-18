package objects;

import java.awt.event.*;

public class character extends object implements Runnable{
    //attributs
    private boolean onLadder, onFloor, onZipline, inHole, onTopOfLadder, diggableR, diggableL;
    protected MykeyListener keylistener;
    protected boolean left, right, up, down, digL, digR;
    //constructeurs
    //
    //fonction init
    private void init(){
        this.onLadder      = false;
        this.onFloor       = false;
        this.onZipline     = false;
        this.inHole        = false;
        this.onTopOfLadder = false;
        this.keylistener   = new MykeyListener();
    }
    //standard 1
    public character(char type){
        super(type);
        init();
    }
    //standard 2
    public character(char type, int x, int y){
        super(type, x, y);
        init();
    }
    //methodes
    //
    //getteurs
    public boolean          isOnLadder()     {return this.onLadder;}
    public boolean          isOnFloor()      {return this.onFloor;}
    public boolean          isOnZipline()    {return this.onZipline;}
    public boolean          isInHole()       {return this.inHole;}
    public boolean          isOnTopOfLadder(){return this.onTopOfLadder;}
    public MykeyListener    getKeyListener() {return this.keylistener;}
    public boolean          getLeft()        {return this.left;}
    public boolean          getRight()       {return this.right;}
    public boolean          getUp()          {return this.up;}
    public boolean          getDown()        {return this.down;}
    public boolean          getDigR()        {return this.digR;}
    public boolean          getDigL()        {return this.digL;}
    //setteurs
    public void setOnLadder(boolean b)          {this.onLadder = b;}
    public void setOnFloor(boolean b)           {this.onFloor = b;}
    public void setOnZipLine(boolean b)         {this.onZipline = b;}
    public void setInHole(boolean b)            {this.inHole = b;}
    public void setOnTopOfLadder(boolean b)     {this.onTopOfLadder = b;}
    public void setDiggableL(boolean b)         {this.diggableL = b;}
    public void setDiggableR(boolean b)         {this.diggableR = b;}
    public void setKeyListener(MykeyListener kl){this.keylistener = kl;}
    //deplacements
    public void goInit()    {setX(getInitX());setY(getInitY());}
    public boolean goUp(){
        boolean res;
        if(this.onLadder && getY()>0){
            setY(getY()-1);
            res=true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}
        return res;
    }
    public boolean goDown(){
        boolean res;
        if(((this.onLadder && !this.onFloor) || this.onZipline || this.onTopOfLadder) && getY()<39){
            setY(getY()+1);
            res = true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}
        return res;
    }
    public boolean goLeft(){
        if((this.onLadder || this.onFloor || this.onZipline || this.onTopOfLadder) && getX()>0){
            setX(getX()+1);
            return true;
        }
        else return false;
    }
    public boolean goRight(){
        if((this.onLadder || this.onFloor || this.onZipline || this.onTopOfLadder) && getX()<99){
            setX(getX()-1);
            return true;
        }
        else return false;
    }
    public void fall(){if(!this.onLadder && !this.onFloor && !this.onZipline && !onTopOfLadder && !inHole && getY()<39) setY(getY()+1);}

    public boolean dig(char side){
        if(this.getType()=='O'){
            if(side=='R' && this.diggableR){
                this.digR = true;
                return true;
            }
            else if(side=='L' && this.diggableL){
                this.digL = true;
                return true;
            }
            else return false;
        }
        else return false;
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
                case 27 : {System.exit(0);break;}
                case 'z': {up = true;break;}
                case 'q': {right = true;break;}
                case 's': {down = true;break;}
                case 'd': {left = true;break;}
                case 'a': {dig('L');break;}
                case 'e': {dig('R');break;}
            }
            // System.out.println("The key Pressed was: " + e.getKeyChar());
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 'z': {up = false;break;}
                case 'q': {right = false;break;}
                case 's': {down = false;break;}
                case 'd': {left = false;break;}
                case 'a': {digL = false;break;}
                case 'e': {digR = false;break;}
            }
            // System.out.println("The key Released was: " + e.getKeyChar());
        }
    }
}
