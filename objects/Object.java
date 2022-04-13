package objects;

import java.io.Serializable;

public class Object implements Serializable{
    //attributs
    private char type;//savoir si c'est un objet ou un personnage
    private int posX, posY, initPosX, initPosY;//les coordonnées de l'objet (courante ou initiale)
    private boolean hidden;//savoir si l'objet est caché
    //constructeur
    //
    //standard1
    public Object(char type, int x, int y, boolean b){
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.initPosX = x;
        this.initPosY = y;
        this.hidden = b;
    }
    //standard2
    public Object(char type, int x, int y){
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.initPosX = x;
        this.initPosY = y;
        this.hidden = false;
    }
    //standard3
    public Object(char type){
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
    public synchronized char    getAvailableType()  {if(!this.hidden) return this.type;else return ' ';}//renvoie le type de l'objet si il est visible
    public synchronized char    getType()           {return this.type;}//renvoie le type de l'objet peut importe si il est visible ou non
    //setteurs
    public synchronized void setX(int i)          {this.posX = Math.abs(i);}
    public synchronized void setY(int i)          {this.posY = Math.abs(i);}
    public synchronized void setHidden(boolean b) {this.hidden = b;}
    public synchronized void setType(char c)      {this.type = c;}
    //toString
    public String toString(){
        if(!this.isHidden()) return ""+this.getType();//affiche le type de l'objet, le type étant représenté par son caractère d'affichage ('X', 'O', '#'...)
        else return " ";
    }
}
