class object{
    //attributs
    private String type;
    private int posX, posY;
    private boolean hidden;
    //constructeur
    //
    //standard1
    public object(String type, int x, int y){
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.hidden = false;
    }
    //standard2
    public object(String type){
        this.type = type;
        this.posX = 0;
        this.posY = 0;
        this.hidden = false;
    }
    //méthodes
    //
    //getteurs
    public int     getX()     {return this.posX;}
    public int     getY()     {return this.posY;}
    public boolean isHidden() {return this.hidden;}
    //setteurs
    public void setX(int i)          {this.posX = Math.abs(i);}
    public void setY(int i)          {this.posY = Math.abs(i);}
    public void setHidden(boolean b) {this.hidden = b;}
    //toString
    public String toString(){
        if(!this.hidden) return this.type;
        else return " ";
    }
}