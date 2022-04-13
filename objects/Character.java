package objects;

import java.awt.event.*;

//objet représentant les personnages, cette classe doit être etendue par les joueur ou les enemis
public class Character extends Object implements Runnable{
    //attributs
    private boolean onLadder, onFloor, onZipline, inHole, onTopOfLadder, wallOnLeft, wallOnRight, diggableR, diggableL;//booleens pour savoir si le personnage est sur une echelle, sur le sol, dans un trou ...
    protected MykeyListener keylistener;//listener pour les touches du clavier
    protected boolean left, right, up, down, digL, digR;//booleen pour les deplacments du personnage
    private PlayGround pg;//la grille de jeu
    //constructeurs
    //
    //fonction init
    private void init(boolean human){
        this.onLadder      = false;
        this.onFloor       = false;
        this.onZipline     = false;
        this.inHole        = false;
        this.onTopOfLadder = false;
        if(human)
            this.keylistener   = new MykeyListener();//on ajoute un listener que si le joueur est humain
    }
    //standard 1
    public Character(char type, PlayGround pg, boolean human){
        super(type);
        this.pg = pg;
        init(human);
    }
    //standard 2
    public Character(char type, int x, int y, PlayGround pg, boolean human){
        super(type, x, y);
        this.pg = pg;
        init(human);
    }
    //methodes
    //
    //getteurs
    public synchronized boolean          isOnLadder()     {return this.onLadder;}
    public synchronized boolean          isOnFloor()      {return this.onFloor;}
    public synchronized boolean          isOnZipline()    {return this.onZipline;}
    public synchronized boolean          isInHole()       {return this.inHole;}
    public synchronized boolean          isOnTopOfLadder(){return this.onTopOfLadder;}
    public synchronized boolean          isDiggableL()    {return this.diggableL;}
    public synchronized boolean          isDiggableR()    {return this.diggableR;}
    public synchronized MykeyListener    getKeyListener() {return this.keylistener;}
    public synchronized boolean          getLeft()        {return this.left;}
    public synchronized boolean          getRight()       {return this.right;}
    public synchronized boolean          getUp()          {return this.up;}
    public synchronized boolean          getDown()        {return this.down;}
    public synchronized boolean          getDigR()        {return this.digR;}
    public synchronized boolean          getDigL()        {return this.digL;}
    public synchronized PlayGround       getPlayGround()  {return this.pg;}
    public synchronized boolean          getWallOnLeft()  {return this.wallOnLeft;}
    public synchronized boolean          getWallOnRight() {return this.wallOnRight;}
    //setteurs
    public synchronized void setOnLadder(boolean b)          {this.onLadder = b;}
    public synchronized void setOnFloor(boolean b)           {this.onFloor = b;}
    public synchronized void setOnZipLine(boolean b)         {this.onZipline = b;}
    public synchronized void setInHole(boolean b)            {this.inHole = b;}
    public synchronized void setOnTopOfLadder(boolean b)     {this.onTopOfLadder = b;}
    public synchronized void setDiggableL(boolean b)         {this.diggableL = b;}
    public synchronized void setDiggableR(boolean b)         {this.diggableR = b;}
    public synchronized void setDigL(boolean b)              {this.digL = b;}
    public synchronized void setDigR(boolean b)              {this.digR = b;}
    public synchronized void setKeyListener(MykeyListener kl){this.keylistener = kl;}
    public synchronized void setUp(boolean b)                {this.up = b;}
    public synchronized void setDown(boolean b)              {this.down = b;}
    public synchronized void setLeft(boolean b)              {this.left = b;}
    public synchronized void setRight(boolean b)             {this.right = b;}
    public synchronized void setWallOnLeft(boolean b)        {this.wallOnLeft = b;}
    public synchronized void setWallOnRight(boolean b)       {this.wallOnRight = b;}
    
    //deplacements

    public void goInit() {setX(getInitX());setY(getInitY());}//retourne a la position initiale

    public boolean goUp(){//fait aller le personnage vers le haut
        boolean res;
        if(isOnLadder() && getY()>0){//verifie que le personnage puisse bien aller vers le haut
            setY(getY()-1);
            res=true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}//attend 50ms car il y a plus de case en largeur qu'en hauteur, sans ca le personnage monte ou descend trop vite
        return res;//retourne vrai ou faut selon si le personnage a pu aller vers le haut
    }
    public boolean goDown(){//fait aller le personnage vers le bas
        boolean res;
        if(((isOnLadder() && !isOnFloor()) || isOnZipline() || isOnTopOfLadder()) && getY()<39){//verifie que le personnage puisse bien aller vers le bas
            setY(getY()+1);
            res = true;
        }
        else res = false;
        try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e);}//attend 50ms car il y a plus de case en largeur qu'en hauteur, sans ca le personnage monte ou descend trop vite
        return res;//retourne vrai ou faut selon si le personnage a pu aller vers le bas
    }
    public boolean goLeft(){//fait aller le personnage vers la gauche
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && !getWallOnLeft()){//verifie que le personnage puisse bien aller vers la gauche
            setX(getX()-1);
            return true;
        }
        else return false;
        //retourne vrai ou faut selon si le personnage a pu aller vers la gauche
    }
    public boolean goRight(){//fait aller le personnage vers la droite
        if((isOnLadder() || isOnFloor() || isOnZipline() || isOnTopOfLadder()) && !getWallOnRight()){//verifie que le personnage puisse bien aller vers la droite
            setX(getX()+1);
            return true;
        }
        else return false;
        //retourne vrai ou faut selon si le personnage a pu aller vers la droite
    }
    public void fall(){//fait tomber le personnage
        if(!isOnLadder() && !isOnFloor() && !isOnZipline() && !isOnTopOfLadder() && !isInHole() && getY()<39){//verifie que le personnage puisse bien tomber
            setY(getY()+1);
            try{Thread.sleep(50);}catch(InterruptedException e){System.out.println(e + "class character method fall");}//attend 50ms pour ne pas que le personnage tombe trop vite
        } 
    }

    public boolean dig(char side){//fait creuser le mur a gauche ou a droite
        if(this.getAvailableType()=='O'){//verifie que le personnage est un Player et pas Enemy
            if(side=='R' && isDiggableR()){//verifie que le personnage peut bien creuser a droite
                setDigR(true);
                return true;
            }
            else if(side=='L' && isDiggableL()){//verifie que le personnage peut bien creuser a gauche
                setDigL(true);
                return true;
            }
            else return false;
        }
        else return false;
        //retourne vrai ou faut selon si le personnage a pu creuser
    }
    public boolean isOnEnemy(){//verifie si le personnage est sur un Enemy
        for(int i=0;i<getPlayGround().getEnemys().size();i++){//parcourt la liste des ennemis
            if(getX()==getPlayGround().getEnemy(i).getX() && getY()==getPlayGround().getEnemy(i).getY()-1) return true;//si le personnage jsute sous lui est un Enemy retourne vrai
        }
        return false;//sinon retourne faux
    }
    public boolean isOnEnemy(int pos[]){//verifie si la position en paramètre est sur un Enemy (utile seulement pour l'IA)
        for(int i=0;i<getPlayGround().getEnemys().size();i++){
            if(pos[0]==getPlayGround().getEnemy(i).getX() && pos[1]==getPlayGround().getEnemy(i).getY()-1) return true;
        }
        return false;
    }
    public void updateCharacter(){//met a jour les attributs du personnage (appelé a chaque frame)
        char posC = getPlayGround().getDisplayTab()[getX()][getY()].getAvailableType();//recupere le type de la case sur laquelle se trouve le personnage
        char leftPosC = getPlayGround().getDisplayTab()[getX()-1][getY()].getAvailableType();//recupere le type de la case a gauche du personnage
        char rightPosC = getPlayGround().getDisplayTab()[getX()+1][getY()].getAvailableType();//recupere le type de la case a droite du personnage
        char UnderPosC;
        //deplacement
        if(getY()<39){
            UnderPosC = getPlayGround().getDisplayTab()[getX()][getY()+1].getAvailableType();//recupere le type de la case sous le personnage
            if(isOnEnemy() || UnderPosC =='#') setOnFloor(true);//si le personnage est sur un Enemy ou que la case sous le personnage est un sol
            else setOnFloor(false);
            if(posC==' ' && UnderPosC=='H') setOnTopOfLadder(true);//si  le personnage est sur un case vide et que la case sous le personnage est une echelle
            else setOnTopOfLadder(false);
        }
        if(posC=='H') setOnLadder(true);//si le personnage est sur une echelle
        else setOnLadder(false);
        if(posC=='_') setOnZipLine(true);//si le personnage est sur une tyrolienne
        else setOnZipLine(false);
        if(leftPosC=='#') setWallOnLeft(true);//si la case a gauche du personnage est un mur
        else setWallOnLeft(false);
        if(rightPosC=='#') setWallOnRight(true);//si la case a droite du personnage est un mur
        else setWallOnRight(false);
        
        fall();//fait tomber le personnage (on fait tomber le personnage a chaque frame, mais si les condition dans la fonction fall() ne sont pas vrai il ne tombera pas);
    }
    public boolean[] updateCharacter(int pos[]){//renvoi les meme booleen que la fonction updateCharacter() mais sous forme de tableau et la position pris en compte est passé en paramètre (utile seulement pour l'IA)
        char posC = getPlayGround().getDisplayTab()[pos[0]][pos[1]].getAvailableType();
        char leftPosC = getPlayGround().getDisplayTab()[pos[0]-1][pos[1]].getAvailableType();
        char rightPosC = getPlayGround().getDisplayTab()[pos[0]+1][pos[1]].getAvailableType();
        char UnderPosC;
        boolean[] out = new boolean[6];
        //deplacement
        if(getY()<39){
            UnderPosC = getPlayGround().getDisplayTab()[pos[0]][pos[1]+1].getAvailableType();
            if(isOnEnemy(pos) || UnderPosC =='#') out[0] = true;
            else out[0] = false;
            if(posC==' ' && UnderPosC=='H') out[1] = true;
            else out[1] = false;
        }
        if(posC=='H') out[2] = true;
        else out[2] = false;
        if(posC=='_') out[3] = true;
        else out[3] = false;
        if(leftPosC=='#') out[4] = true;
        else out[4] = false;
        if(rightPosC=='#') out[5] = true;
        else out[5] = false;
        return out;
    }

    //run
    @Override
    public void run(){
        //run vide mais doit nécessairement être implémenté pour être surchargé
    }

    class MykeyListener implements KeyListener{//class qui gère les évènements clavier
        private boolean haveDigged = false;//booleen qui permet de savoir si le personnage a déjà creuser (corrige un bug où le personnage pouvait creuser constamment)
        @Override
        public void keyTyped(KeyEvent e)
        {
          //methode vide mais doit être implémenté
        }
        @Override
        public void keyPressed(KeyEvent e)//methode appelé lors de l'appui sur une touche
        {
            switch(e.getKeyChar()){
                case 'a': {//si la touche a est appuyé
                    if(!haveDigged){//si le personnage n'a pas déjà creuser
                        dig('L');//creuse a gauche
                        try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}//attend 10ms le temps que les calculs externe se fassent
                        setDigL(false);//met le booleen lu par le PlayGround pour savoir si le personnage creuse à gauche à faux
                        this.haveDigged = true;//indique que le personnage a déjà creuser
                    }
                    break;
                }
                case 'e': {//si la touche e est appuyé
                    if(!haveDigged){//si le personnage n'a pas déjà creuser
                        dig('R');//creuse a droite
                        try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}//attend 10ms le temps que les calculs externe se fassent
                        setDigR(false);//met le booleen lu par le PlayGround pour savoir si le personnage creuse à droite à faux
                        this.haveDigged = true;//indique que le personnage a déjà creuser
                    }
                    break;
                }
                case 'z': {setUp(true);break;}//si la touche z est appuyé, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage monte à vrai
                case 'q': {setLeft(true);break;}//si la touche q est appuyé, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage va à gauche à vrai
                case 's': {setDown(true);break;}//si la touche s est appuyé, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage descend à vrai
                case 'd': {setRight(true);break;}//si la touche d est appuyé, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage va à droite à vrai
                case 27 : {System.exit(0);break;}//si la touche echap est appuyé, quitte le programme
                

            }
        }
        @Override
        public void keyReleased(KeyEvent e)//methode appelé lors de la relachement d'une touche
        {
            switch(e.getKeyChar()){
                case 'z': {setUp(false);break;}//si la touche z est relaché, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage monte à faux
                case 'q': {setLeft(false);break;}//si la touche q est relaché, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage va à gauche à faux
                case 's': {setDown(false);break;}//si la touche s est relaché, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage descend à faux
                case 'd': {setRight(false);break;}//si la touche d est relaché, met le booleen lu par le run de la sous-classe Controle pour savoir si le personnage va à droite à faux
                case 'a': {//si la touche a est relaché
                    setDigL(false);//met le booleen lu par le PlayGround pour savoir si le personnage creuse à gauche à faux
                    this.haveDigged = false;//indique que le personnage n'a pas encore creuser
                    break;
                }
                case 'e': {//si la touche e est relaché
                    setDigR(false);//met le booleen lu par le PlayGround pour savoir si le personnage creuse à droite à faux
                    this.haveDigged = false;//indique que le personnage n'a pas encore creuser
                    break;
                }
            }
        }
    }

    class Control extends Thread{//class qui gère les mouvements du personnage
        @Override
        public void run(){
            while(!getPlayGround().isEndGame()){//tant que le jeu n'est pas fini
                if(getLeft())goLeft();//si on a demander d'aller à gauche, le personnage va à gauche
                else if(getRight())goRight();//si on a demander d'aller à droite, le personnage va à droite
                else if(getUp())goUp();//si on a demander d'aller en haut, le personnage monte
                else if(getDown())goDown();//si on a demander d'aller en bas, le personnage descend
                try{Thread.sleep(70);}catch(InterruptedException e){System.out.println(e);}//attend 70ms, (règle la vitesse du personnage)
            }
        }
    }
}
