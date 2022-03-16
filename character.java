import java.io.*;
import java.awt.event.*;

public class character extends object implements Runnable, Serializable{
    //attributs
    private boolean onLadder, onFloor, onZipline, inHole;
    protected MykeyListener keylistener;
    //constructeurs
    //
    //fonction init
    private void init(){
        this.onLadder  = false;
        this.onFloor   = false;
        this.onZipline = false;
        this.inHole    = false;
        this.keylistener  = new MykeyListener();
    }
    //standard 1
    public character(String type){
        super(type);
        init();
    }
    //standard 2
    public character(String type, int x, int y){
        super(type, x, y);
        init();
    }
    //methodes
    //
    //getteurs
    public boolean          isOnLadder()    {return this.onLadder;}
    public boolean          isOnFloor()     {return this.onFloor;}
    public boolean          isOnZipline()   {return this.onZipline;}
    public boolean          isInHole()      {return this.inHole;}
    public MykeyListener    getKeyListener(){return this.keylistener;}
    //setteurs
    public void setOnLadder(boolean b)          {this.onLadder = b;}
    public void setOnFloor(boolean b)           {this.onFloor = b;}
    public void setOnZipLine(boolean b)         {this.onZipline = b;}
    public void setInHole(boolean b)            {this.inHole = b;}
    public void setKeyListener(MykeyListener kl){this.keylistener = kl;}
    //deplacements
    public void goInit()    {setX(getInitX());setY(getInitY());}
    public void goUp()      {if(this.onLadder)setY(getY()-1);}
    public void goDown()    {if((this.onLadder && !this.onFloor) || this.onZipline)setY(getY()+1);}
    public void goLeft()    {if(this.onLadder || this.onFloor || this.onZipline)setX(getX()+1);}
    public void goRight()   {if(this.onLadder || this.onFloor || this.onZipline)setX(getX()-1);}
    public void fall()      {if(!this.onLadder && !this.onFloor && !this.onZipline) setY(getY()+1);}

    //run
    @Override
    public void run(){
        
    }

    class MykeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 27 : {System.exit(0);break;}
                case 'z': {goUp();break;}
                case 'q': {goRight();break;}
                case 's': {goDown();break;}
                case 'd': {goLeft();break;}
            }
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            /*switch(e.getKeyChar()){
                case 27 : {System.exit(0);break;}
                case 'z': {player1.goUp();break;}
                case 'q': {player1.goRight();break;}
                case 's': {player1.goDown();break;}
                case 'd': {player1.goLeft();break;}
            }*/
            // System.out.println("The key Pressed was: " + e.getKeyChar());
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            // System.out.println("The key Released was: " + e.getKeyChar());
        }
    }
}
