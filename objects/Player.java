package objects;

public class Player extends Character{
    //attributs
    private String name;
    private int nbGold, lives;
    //private boolean end = false;
    
    //constructeurs
    //
    //fonction init
    private void init(){
        this.nbGold    = 0;
        this.lives     = 5;
    }
    //par defaut
    public Player(PlayGround pg, boolean soloMode){
        super('O', pg,soloMode);
        init();
    }
    //standard 1
    public Player(String name, PlayGround pg, boolean soloMode){
        super('O', pg,soloMode);
        init();
        this.name = name;
    }
    //standard 2
    public Player(String name, int posX, int posY, PlayGround pg, boolean soloMode){
        super('O',posX,posY, pg,soloMode);
        init();
        this.name = name;
    }
    //methodes
    //
    //getteurs
    public              String  getName()  {return this.name;}
    public              int     getGold()  {return this.nbGold;}
    public synchronized int     getLives() {return this.lives;}
    //public              boolean isEnded()  {return this.end;}
    
    //setteurs
    public              void setName(String s) {this.name = s;}
    public              void setGold(int i)    {this.nbGold = Math.abs(i);}
    public synchronized void setLives(int i)   {this.lives = Math.abs(i);}
    //public              void setEnd(boolean b) {this.end = b;}
    //others
    public boolean isCaught(){
        for(int i=0;i<getPlayGround().getEnemys().size();i++){
            if(getX()==getPlayGround().getEnemy(i).getX() && getY()==getPlayGround().getEnemy(i).getY())return true;
        }
        return false;
    }
    public void updatePlayer(){
        updateCharacter();
        int X = getX();
        int Y = getY();
        if(Y<39 && X>0){
            if(getPlayGround().getDisplayTab()[X-1][Y+1].getAvailableType()=='#'){
                setDiggableL(true);
                getPlayGround().updateHole((Floor)getPlayGround().getDisplayTab()[X-1][Y+1]);
            }
            else setDiggableL(false);
        }
        if(Y<39 && X<99){
            if(getPlayGround().getDisplayTab()[X+1][Y+1].getAvailableType()=='#'){
                setDiggableR(true);
                getPlayGround().updateHole((Floor)getPlayGround().getDisplayTab()[X+1][Y+1]);
            }
            else setDiggableR(false);
        }
            
        if(getPlayGround().getDisplayTab()[X][Y].getAvailableType()=='$' && !getPlayGround().getDisplayTab()[X][Y].isHidden()){
            setGold(getGold()+1);
            getPlayGround().getDisplayTab()[X][Y].setHidden(true);
            if(getGold()==getPlayGround().getGolds()){
                getPlayGround().showExit(true);
            }
        }
        else if( isCaught() || getPlayGround().getDisplayTab()[X][Y].getAvailableType()=='#'){
            setLives(getLives()-1);
            getPlayGround().resetPos();
        }
        if(Y==1){
            getPlayGround().setEndGame(true);
            System.out.println(getName()+" won");
        }
        if(this.getLives()<=0){
            getPlayGround().setEndGame(true);
        }
    }
    //run
    @Override
    public void run(){
            Control ctrl = new Control();
            ctrl.start();
        while(!getPlayGround().isEndGame()){
            updatePlayer();
        }
    }
    class Control extends Thread{
        @Override
        public void run(){
            while(!getPlayGround().isEndGame()){
                if(getLeft())goLeft();
                else if(getRight())goRight();
                else if(getUp())goUp();
                else if(getDown())goDown();
                try{Thread.sleep(70);}catch(InterruptedException e){System.out.println(e);}
            }
        }
    }
    
}
