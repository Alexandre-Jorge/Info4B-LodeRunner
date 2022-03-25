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
    public player  getTarget(){return this.target;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
    public void setTarget(player p) {this.target = p;}
    //others
    public void die(){
        setInHole(false);
        setY(1);
        setX((int)(Math.random()*98+1));
    }
    //function AI
    public void AIscript(){
        boolean moved;
        while(getTarget().getLives()>0){
            try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
            moved = false;
            if(!isInHole()){
                if (isOnFloor() || isOnTopOfLadder()){
                    if(isOnLadder()){
                        if (getY()<getTarget().getY()) {moved = goDown();}
                        else if (getY()>getTarget().getY()){moved = goUp();}
                    }
                    if((isOnFloor() || isOnTopOfLadder()) && !moved){
                        if(getX()<getTarget().getX()) {moved = goLeft();}
                        else if (getX()>getTarget().getX()){moved = goRight();}
                    }
                }
                if((isOnLadder() || isOnTopOfLadder())&& !moved){
                    if (getY()<getTarget().getY()) {moved = goDown();}
                    else if (getY()>getTarget().getY()){moved = goUp();}
                }
                if(isOnZipline() && !moved){
                    if(getX()<getTarget().getX()) {moved = goLeft();}
                    else if (getX()>getTarget().getX()){moved = goRight();}
                    else if(getY()<getTarget().getY()){moved = goDown();}
                }
            }
        }
    }
    //
    @Override
    public void run(){
        if(isBot()){
            System.out.println("enemy control by AI");
            AIscript();
        }
        else System.out.println("enemy control by player");
    }
}
