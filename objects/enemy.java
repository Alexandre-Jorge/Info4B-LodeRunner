package objects;

public class enemy extends character{
    //attributs
    private Boolean bot;
    private player target;
    //constructeurs
    //
    //par defaut
    public enemy(){
        super('X');
        this.bot = true;
    }
    //standard 1
    public enemy(int x, int y){
        super('X', x, y);
        this.bot = true;
    }
    //standard 2
    public enemy(int x, int y, boolean b){
        super('X', x, y);
        this.bot = b;
    }
    //standard 3
    public enemy(int x, int y, player p){
        super('X', x, y);
        this.bot = true;
        this.target = p;
    }
    //standard 4
    public enemy(int x, int y, boolean b, player p){
        super('X', x, y);
        this.bot = b;
        this.target = p;
    }
    //methodes
    //
    //getteurs
    public boolean isBot()    {return this.bot;}
    public player getTarget() {return this.target;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
    public void setTarget(player p) {this.target = p;}
    //function AI
    public void AIscript(){
        boolean moved;
        while(target.getLives()>0){
            try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
            moved = false;
            if (isOnFloor() || isOnTopOfLadder()){
                if(isOnLadder()){
                    if (getY()<this.target.getY()) {moved = this.goDown();}
                    else if (getY()>this.target.getY()){moved = this.goUp();}
                }
                if((isOnFloor() || isOnTopOfLadder()) && !moved){
                    if(getX()<this.target.getX()) {moved = this.goLeft();}
                    else if (getX()>this.target.getX()){moved = this.goRight();}
                }
            }
            if(isOnLadder() && !moved){
                if (getY()<this.target.getY()) {moved = this.goDown();}
                else if (getY()>this.target.getY()){moved = this.goUp();}
            }
            if(isOnZipline() && !moved){
                if(getX()<this.target.getX()) {moved = this.goLeft();}
                else if (getX()>this.target.getX()){moved = this.goRight();}
                else if(getY()<this.target.getY()){moved = this.goDown();}
            }
        }
    }
    //
    @Override
    public void run(){
        if(this.bot){
            System.out.println("enemy control by AI");
            AIscript();
        }
        else System.out.println("enemy control by player");
    }
}
