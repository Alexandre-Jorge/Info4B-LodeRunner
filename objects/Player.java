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
        updateCharacter();//appel la methode qui met a jour les attributs commun aux differents types de personnage 
        //on fixe les positions dans des variables pour ne pas les recaluculer a chaque fois ce qui crée des valeur differentes
        int X = getX();
        int Y = getY();
        if(Y<39 && X>0){//si le joueur n'est pas en bas de la map et pas coller a gauche
            if(getPlayGround().getDisplayTab()[X-1][Y+1].getAvailableType()=='#'){//si la case en bas et a gauche du joueur est un mur
                setDiggableL(true);//on dit que la case est possible a creuser
                getPlayGround().updateHole((Floor)getPlayGround().getDisplayTab()[X-1][Y+1]);//on appel le methode de la classe PlayGround qui verifie si le joueur creuse effectivment et va ou non cacher le sol
            }
            else setDiggableL(false);//sinon on dit que la case est impossible a creuser
        }
        if(Y<39 && X<99){//idem mais a droite
            if(getPlayGround().getDisplayTab()[X+1][Y+1].getAvailableType()=='#'){
                setDiggableR(true);
                getPlayGround().updateHole((Floor)getPlayGround().getDisplayTab()[X+1][Y+1]);
            }
            else setDiggableR(false);
        }
            
        if(getPlayGround().getDisplayTab()[X][Y].getAvailableType()=='$' && !getPlayGround().getDisplayTab()[X][Y].isHidden()){//si le joueur est sur une piece d'or et qu'elle n'est pas caché (pas deja ramasser)
            setGold(getGold()+1);//on ajoute 1 au nombre de gold du joueur
            getPlayGround().getDisplayTab()[X][Y].setHidden(true);//on cache la piece d'or
            if(getGold()==getPlayGround().getGolds()){//si le nombre de gold ramasser est egale au nombre de gold total
                getPlayGround().showExit(true);//on affiche la sortie
            }
        }
        else if( isCaught() || getPlayGround().getDisplayTab()[X][Y].getAvailableType()=='#'){//si le joueur est attrapé ou que le trou dans lequel il etait s'est rebouché
            setLives(getLives()-1);//on enleve une vie au joueur
            getPlayGround().resetPos();//on remet la postion initiale du jeux
        }
        if(Y==1){//si le joueur atteint le sommet de la map
            getPlayGround().setEndGame(true);//on dit que le jeux est fini
            System.out.println(getName()+" won");//on affiche que le joueur a gagné
        }
        if(this.getLives()<=0){//si le joueur n'a plus de vie
            getPlayGround().setEndGame(true);//on dit que le jeux est fini
            System.out.println(getName()+" lost");//on affiche que le joueur a perdu
        }
    }
    //run
    @Override
    public void run(){
            Control ctrl = new Control();//on instancie un objet de type Control
            ctrl.start();//on lance le thread
        while(!getPlayGround().isEndGame()){//tant que le jeux n'est pas fini
            updatePlayer();//on met a jour les attributs du joueur
        }
    }
    
}
