import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class player extends Thread{
    //attributs
    private String name;
    private int posX, posY, nbGold, lives;
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
        init();
        this.posX = 0;
        this.posY = 0;
        
    }
    //standard 1
    public player(String name){
        init();
        this.name = name;
        this.posX = 0;
        this.posY = 0;
    }
    //standard 2
    public player(String name, int posX, int posY){
        init();
        this.name = name;
        this.posX = posX;
        this.posY = posY;
    }
    //par clonage
    public player(player p){
        this.name      = p.getName();
        this.posX      = p.getX();
        this.posY      = p.getY();
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
    public int      getX()          {return this.posX;}
    public int      getY()          {return this.posY;}
    public int      getGold()       {return this.nbGold;}
    public int      getLives()      {return this.lives;}
    public boolean  isOnLadder()    {return this.onLadder;}
    public boolean  isOnFloor()     {return this.onFloor;}
    public boolean  isOnZipline()  {return this.onZipline;}
    public boolean  isInHole()      {return this.inHole;}
    //setteurs
    public void setName(String s)       {this.name = s;}
    public void setX(int i)             {this.posX = Math.abs(i);}
    public void setY(int i)             {this.posY = Math.abs(i);}
    public void setGold(int i)          {this.nbGold = Math.abs(i);}
    public void setLives(int i)         {this.lives = Math.abs(i);}
    public void setOnLadder(boolean b)  {this.onLadder = b;}
    public void setOnFloor(boolean b)   {this.onFloor = b;}
    public void setOnZipLine(boolean b){this.onZipline = b;}
    public void setInHole(boolean b)    {this.inHole = b;}
    //deplacements
    public void goUp()      {if(this.isOnLadder)this.posY--;}
    public void goDown()    {if(this.isOnLadder)this.posY++;}
    public void goLeft()    {if(this.onLadder || this.onFloor || this.onZipline)this.posX--;}
    public void goRight()   {if(this.onLadder || this.onFloor || this.onZipline)this.posX++;}
    public void fall(){
        while(!this.onLadder && !this.onFloor && !this.onZipline){
            this.posY++;
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
    //toString
    public String toString(){
        return "o";
    }
}