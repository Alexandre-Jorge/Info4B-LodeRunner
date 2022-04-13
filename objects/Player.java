package objects;

public class Player extends Character{
    //attributs
    private String name;//nom du joueur
    private int nbGold, lives;//nombre d'or et de vie
    
    //constructeurs
    //
    //fonction init
    private void init(){
        this.nbGold    = 0;
        this.lives     = 5;
    }
    //stadard 1
    public Player(PlayGround pg, boolean human){
        super('O', pg, human);
        init();
        this.name = "Player";
    }
    //standard 2
    public Player(String name, PlayGround pg, boolean human){
        super('O', pg, human);
        init();
        this.name = name;
    }
    //standard 3
    public Player(String name, int posX, int posY, PlayGround pg, boolean human){
        super('O',posX,posY, pg, human);
        init();
        this.name = name;
    }
    //methodes
    //
    //getteurs
    public              String  getName()  {return this.name;}
    public              int     getGold()  {return this.nbGold;}
    public synchronized int     getLives() {return this.lives;}
    
    //setteurs
    public              void setName(String s) {this.name = s;}
    public              void setGold(int i)    {this.nbGold = Math.abs(i);}
    public synchronized void setLives(int i)   {this.lives = Math.abs(i);}
    //others

    public boolean isCaught(){//verifie si le joueur est attrapé par un enemi
        for(int i=0;i<getPlayGround().getEnemys().size();i++){//parcours de la liste des enemis
            if(getX()==getPlayGround().getEnemy(i).getX() && getY()==getPlayGround().getEnemy(i).getY())return true;//si le joueur est sur la meme position que l'enemi retourne vrai
        }
        return false;//sinon retourne faux
    }
    public void updatePlayer(){//met a jour les attributs du joueur
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
    
}
