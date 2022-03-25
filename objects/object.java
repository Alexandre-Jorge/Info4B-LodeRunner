package objects;

import java.io.Serializable;

public class object implements Serializable{
    //attributs
    private char type;
    private int posX, posY, initPosX, initPosY;
    private boolean hidden;
    //constructeur
    //
    //standard1
    public object(char type, int x, int y, boolean b){
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.initPosX = x;
        this.initPosY = y;
        this.hidden = b;
    }
    //standard2
    public object(char type, int x, int y){
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.initPosX = x;
        this.initPosY = y;
        this.hidden = false;
    }
    //standard3
    public object(char type){
        this.type = type;
        this.posX = 0;
        this.posY = 0;
        this.initPosX = 0;
        this.initPosY = 0;
        this.hidden = false;
    }
    //méthodes
    //
    //getteurs
    public synchronized int     getX()              {return this.posX;}
    public synchronized int     getY()              {return this.posY;}
    public              int     getInitX()          {return this.initPosX;}
    public              int     getInitY()          {return this.initPosY;}
    public synchronized boolean isHidden()          {return this.hidden;}
    public synchronized char    getAvailableType()  {if(!this.hidden) return this.type;else return ' ';}
    public synchronized char    getType()           {return this.type;}
    //setteurs
    public synchronized void setX(int i)          {this.posX = Math.abs(i);}
    public synchronized void setY(int i)          {this.posY = Math.abs(i);}
    public synchronized void setHidden(boolean b) {this.hidden = b;}
    public synchronized void setType(char c)      {this.type = c;}
    //toString
    public String toString(){
        if(!this.isHidden()) return ""+this.getType();
        else return " ";
    }
}