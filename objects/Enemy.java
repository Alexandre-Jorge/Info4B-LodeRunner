package objects;

public class Enemy extends Character{
    //attributs
    private Boolean bot;
    private Player target;
    //constructeurs
    //
    //par defaut
    public Enemy(PlayGround pg, boolean soloMode){
        super('X',pg,soloMode);
        this.bot = true;
    }
    //standard 1
    public Enemy(int x, int y, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = true;
    }
    //standard 2
    public Enemy(int x, int y, boolean b, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = b;
    }
    //standard 3
    public Enemy(int x, int y, Player p, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = true;
        this.target = p;
    }
    //standard 4
    public Enemy(int x, int y, boolean b, Player p, PlayGround pg, boolean soloMode){
        super('X', x, y, pg,soloMode);
        this.bot = b;
        this.target = p;
    }
    //methodes
    //
    //getteurs
    public boolean isBot()    {return this.bot;}
    public Player  getTarget(){return this.target;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
    public void setTarget(Player p) {this.target = p;}
    //others
    public void die(){
        setInHole(false);
        setY(1);
        setX((int)(Math.random()*98+1));
    }
    public void updateEnemy(){
        updateCharacter();
        if(getPlayGround().getDisplayTab()[getX()][getY()].getType()=='#' && !isInHole()){
            setInHole(true);
            getPlayGround().escapeFromHole(this);
        }
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
                        if(getX()<getTarget().getX()) {moved = goRight();}
                        else if (getX()>getTarget().getX()){moved = goLeft();}
                    }
                }
                if((isOnLadder() || isOnTopOfLadder())&& !moved){
                    if (getY()<getTarget().getY()) {moved = goDown();}
                    else if (getY()>getTarget().getY()){moved = goUp();}
                }
                if(isOnZipline() && !moved){
                    if(getX()<getTarget().getX()) {moved = goRight();}
                    else if (getX()>getTarget().getX()){moved = goLeft();}
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
            AI ai = new AI();
            ai.start();
        }
        else {
            System.out.println("enemy control by player");
            Control ctrl = new Control();
            ctrl.start();
        }
        while(getPlayGround().getThPlayer(0).isAlive()){/////!\\\\ A MODIFIER !!!
            updateEnemy();
        }
    }
    class AI extends Thread{
        @Override
        public void run(){
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
                            if(getX()<getTarget().getX()) {moved = goRight();}
                            else if (getX()>getTarget().getX()){moved = goLeft();}
                        }
                    }
                    if((isOnLadder() || isOnTopOfLadder())&& !moved){
                        if (getY()<getTarget().getY()) {moved = goDown();}
                        else if (getY()>getTarget().getY()){moved = goUp();}
                    }
                    if(isOnZipline() && !moved){
                        if(getX()<getTarget().getX()) {moved = goRight();}
                        else if (getX()>getTarget().getX()){moved = goLeft();}
                        else if(getY()<getTarget().getY()){moved = goDown();}
                    }
                }
            }
        }
    }
    class Control extends Thread{
        @Override
        public void run(){
            while(this.isAlive()){
                if(getLeft())goLeft();
                else if(getRight())goRight();
                else if(getUp())goUp();
                else if(getDown())goDown();
                try{Thread.sleep(70);}catch(InterruptedException e){System.out.println(e);}
            }
        }
    }
}
