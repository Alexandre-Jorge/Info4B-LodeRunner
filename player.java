import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class player extends object implements Runnable{
    //attributs
    private String name;
    private int nbGold, lives;
    private boolean onLadder, onFloor, onZipline, inHole;
    //constructeurs
    //
    //fonction init
    public void init(){
        this.nbGold    = 0;
        this.lives     = 3;
        this.onLadder  = false;
        this.onFloor   = false;
        this.onZipline = false;
        this.inHole    = false;
    }
    //par defaut
    public player(){
        super("O");
        init();
    }
    //standard 1
    public player(String name){
        super("O");
        init();
        this.name = name;
    }
    //standard 2
    public player(String name, int posX, int posY){
        super("O",posX,posY);
        init();
        this.name = name;
    }
    //par clonage
    public player(player p){
        super("O",p.getX(),p.getY());
        this.name      = p.getName();
        this.nbGold    = p.getGold();
        this.lives     = p.getLives();
        this.onLadder  = p.isOnLadder();
        this.onFloor   = p.isOnFloor();
        this.onZipline = p.isOnZipline();
        this.inHole    = p.isInHole();
    }
    //methodes
    //
    //getteurs
    public String   getName()       {return this.name;}
    public int      getGold()       {return this.nbGold;}
    public int      getLives()      {return this.lives;}
    public boolean  isOnLadder()    {return this.onLadder;}
    public boolean  isOnFloor()     {return this.onFloor;}
    public boolean  isOnZipline()  {return this.onZipline;}
    public boolean  isInHole()      {return this.inHole;}
    //setteurs
    public void setName(String s)       {this.name = s;}
    public void setGold(int i)          {this.nbGold = Math.abs(i);}
    public void setLives(int i)         {this.lives = Math.abs(i);}
    public void setOnLadder(boolean b)  {this.onLadder = b;}
    public void setOnFloor(boolean b)   {this.onFloor = b;}
    public void setOnZipLine(boolean b){this.onZipline = b;}
    public void setInHole(boolean b)    {this.inHole = b;}
    //deplacements
    public void goUp()      {if(this.onLadder)setY(getY()-1);}
    public void goDown()    {if(this.onLadder)setY(getY()+1);}
    public void goLeft()    {if(this.onLadder || this.onFloor || this.onZipline)setX(getX()-1);}
    public void goRight()   {if(this.onLadder || this.onFloor || this.onZipline)setX(getX()+1);}
    public void fall(){
        while(!this.onLadder && !this.onFloor && !this.onZipline){
            setY(getY()+1);
            try{TimeUnit.MILLISECONDS.sleep(10);}catch(InterruptedException e){System.out.println(e);}
        }
    }

    //run
    @Override
    public void run(){
        while(!lodeRunner.endGame){
            try{
                wait();
                System.out.println("OK");
            }catch(InterruptedException e){System.out.println(e);}
        }
    }
}