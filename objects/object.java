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
    public int     getX()     {return this.posX;}
    public int     getY()     {return this.posY;}
    public int     getInitX() {return this.initPosX;}
    public int     getInitY() {return this.initPosY;}
    public boolean isHidden() {return this.hidden;}
    public char    getType()  {if(!this.hidden) return this.type;else return ' ';}
    //setteurs
    public void setX(int i)          {this.posX = Math.abs(i);}
    public void setY(int i)          {this.posY = Math.abs(i);}
    public void setHidden(boolean b) {this.hidden = b;}
    public void setType(char c)      {this.type = c;}
    //toString
    public String toString(){
        if(!this.hidden) return ""+this.type;
        else return " ";
    }
}