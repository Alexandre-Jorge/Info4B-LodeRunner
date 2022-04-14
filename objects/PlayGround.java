package objects;

import java.io.*;
import java.util.*;


import java.awt.*;

public class PlayGround implements Serializable{
    //attributs
    private Object displayTab[][];//tableau qui contient les differents objets de la map
    private int sizeX, sizeY, golds=0;//taille de la map en x et y et le nombre d'or
    private ArrayList<Player> players;//liste des joueurs
    private ArrayList<Enemy> enemys;//liste des enemis
    private ArrayList<Runnable> runPlayers;//liste des runnable des joueurs
    private ArrayList<Runnable> runEnemys;//liste des runnable des enemis
    private ArrayList<Thread> thPlayers;//liste des threads des joueurs
    private ArrayList<Thread> thEnemys;//liste des threads des enemis
    private Frame frame = new Frame("Lode Runner");//fenetre du jeu
    private TextArea gamePlay, info;//zone de jeu et zone d'info
    private ArrayList<int[]> exitPos;//liste des positions des sorties
    private ArrayList<int[]> goldsPos;//liste des positions des golds
    private boolean endGame = false;//booleen qui indique si la partie est finie

    protected boolean chronoForceStop = false;//booleen qui indique si on doit arrêter le chrono de force

    //constructeurs
    //
    //standard mode solo
    public PlayGround(FileReader f, int sizeX, int sizeY){
        int j=0;//compteur pour les lignes
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new Object[sizeX][sizeY];
        this.exitPos = new ArrayList<int[]>();
        this.goldsPos = new ArrayList<int[]>();
        this.enemys = new ArrayList<Enemy>();
        this.players = new ArrayList<Player>();
        this.runEnemys = new ArrayList<Runnable>();
        this.runPlayers = new ArrayList<Runnable>();
        this.thEnemys = new ArrayList<Thread>();
        this.thPlayers = new ArrayList<Thread>();
        this.gamePlay = new TextArea(sizeY+1, sizeX+2);//on ajoute 1 et 2 au dimension pour avoir un affichage plus confortable
        this.info = new TextArea(4,20);
        this.frame.setSize(900,800);
        try{
            BufferedReader buf = new BufferedReader(f) ;//buffer pour lire le fichier txt ou est représenté la map
            String line;
            while((line = buf.readLine()) != null){//on lit ligne par ligne
                for(int i=0;i<sizeX;i++){//on lit chaque caractere de la ligne
                    switch(line.charAt(i)){
                        case 'O':{//si on trouve un 'O', c'est un joueur
                            this.players.add(new Player("player",i,j, this, true));//on créé un joueur que l'on ajoute a la liste des joueurs
                            this.runPlayers.add(this.players.get(this.players.size()-1));//on créé un runnable que l'on ajoute a la liste des runnable des joueurs
                            this.thPlayers.add(new Thread(this.runPlayers.get(this.runPlayers.size()-1)));//on créé un thread que l'on ajoute a la liste des threads des joueurs
                            displayTab[i][j] = new Object(' ');//on met dans la grille du jeux un espace vide (les joueurs et les enemys ne sont pas représenter car ils se déplacent)
                            break;
                        }
                        case '$':{//si on trouve un '$', c'est un gold
                            displayTab[i][j] = new Gold(i,j);//on met dans la grille du jeux un gold
                            this.golds++;//on ajoute un gold au nombre de golds total
                            int[] tmp = {i,j};//on crée un tableau temporaire qui contient les coordonnées du gold
                            this.goldsPos.add(tmp);//on ajoute le tableau temporaire a la liste des postions des golds
                            break;
                        }
                        case 'X':{//si on trouve un 'X', c'est un enemi
                            this.enemys.add(new Enemy(i,j,this,false));//on créé un enemi que l'on ajoute a la liste des enemis
                            this.runEnemys.add(this.enemys.get(this.enemys.size()-1));//on créé un runnable que l'on ajoute a la liste des runnable des enemis
                            this.thEnemys.add(new Thread(this.runEnemys.get(this.runEnemys.size()-1)));//on créé un thread que l'on ajoute a la liste des threads des enemis
                            displayTab[i][j] = new Object(' ');//on met dans la grille du jeux un espace vide (les joueurs et les enemys ne sont pas représenter car ils se déplacent)
                            break;
                        }
                        case 'H':{//si on trouve un 'H', c'est une echelle
                            displayTab[i][j] = new Ladder(i,j);//on met dans la grille du jeux une echelle
                            break;
                        }
                        case 'h':{//si on trouve un 'h', c'est une echelle caché car elle représente l'echelle de la sortie
                            displayTab[i][j] = new Ladder(i,j,true);//on met dans la grille du jeux une echelle caché
                            int[] tmp = {i,j};//on crée un tableau temporaire qui contient les coordonnées de l'echelle
                            this.exitPos.add(tmp);//on ajoute le tableau temporaire a la liste des postions des echelles
                            break;
                        }
                        case '#':{//si on trouve un '#', c'est un sol/mur
                            displayTab[i][j] = new Floor(i,j);//on met dans la grille du jeux un sol/mur
                            break;
                        }
                        case '_':{//si on trouve un '_', c'est une tyrolienne
                            displayTab[i][j] = new Zipline(i,j);//on met dans la grille du jeux une tyrolienne
                            break;
                        }    
                        default : {displayTab[i][j] = new Object(line.charAt(i));}//par defaut on place un objet qui a pour type/affichage le caractère lu
                    }
                }
                j++;//on passe a la ligne suivante
            }
            this.gamePlay.setFocusable(false);//on rend le terrain de jeux non focusable, pour ne pas pouvoir ecrire dans la textArea
            this.gamePlay.setFont(new Font("Monospaced",Font.BOLD, 12));//on met la police de caractère du terrain de jeux en monospace pour avoir les espace de la meme taille que les caractères
            this.info.setFocusable(false);//idem pour les infos
            this.frame.setLayout(new FlowLayout());//on met le layout du frame en flowLayout
            this.frame.add(this.gamePlay);//on ajoute le terrain de jeux a la frame
            this.frame.add(this.info);//on ajoute les infos a la frame
            this.frame.setVisible(true);//on rend la frame visible
                
            for(int i=0;i<this.enemys.size();i++){//on parcours la liste des enemis
                this.enemys.get(i).setTarget(this.players.get(i%this.players.size()));//on affecte a chaque enemi un joueur different a suivre
                this.thEnemys.get(i).start();//on lance le thread de l'enemi
            }
            for(int i=0;i<this.players.size();i++){//on parcours la liste des joueurs
                this.frame.addKeyListener(this.players.get(i).getKeyListener());//on ajoute un keyListener a chaque joueur
                this.thPlayers.get(i).start();//on lance le thread du joueur
            }
        }catch(Exception e){System.out.println(e);}
    }
    //standard mode multi
    public PlayGround(FileReader f, int sizeX, int sizeY,int nbPlayer, int nbEnemy){
        //constructeur quasiment identique sauf ce qui sera commenté
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new Object[sizeX][sizeY];
        this.exitPos = new ArrayList<int[]>();
        this.goldsPos = new ArrayList<int[]>();
        this.enemys = new ArrayList<Enemy>();
        this.players = new ArrayList<Player>();
        this.runEnemys = new ArrayList<Runnable>();
        this.runPlayers = new ArrayList<Runnable>();
        this.thEnemys = new ArrayList<Thread>();
        this.thPlayers = new ArrayList<Thread>();
        //ici pas besoin de frame, le PlayGround tourne sur le serveur
        
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    switch(line.charAt(i)){
                        case 'O':{
                            if(this.players.size()<nbPlayer){//si le nombre de joueur sur sur plateau est inferieur au nombre de joueur connecté au serveur
                                this.players.add(new Player("player",i,j, this,true));
                                this.runPlayers.add(this.players.get(this.players.size()-1));
                                this.thPlayers.add(new Thread(this.runPlayers.get(this.runPlayers.size()-1)));
                                displayTab[i][j] = new Object(' ');
                            }
                            else{//sinon le joueur n'est pas pris en compte
                                displayTab[i][j] = new Object(' ');
                            }
                            break;
                        }
                        case '$':{
                            displayTab[i][j] = new Gold(i,j);
                            this.golds++;
                            int[] tmp = {i,j};
                            this.goldsPos.add(tmp);
                            break;
                        }
                        case 'X':{
                            if(this.enemys.size()<nbEnemy && nbEnemy>=1){//si le nombre d'enemis sur le plateau est inferieur au nombre d'enemis connecté au serveur et que le nombre d'ennemis est au moins 1
                                this.enemys.add(new Enemy(i,j,this,true));//on ajoute un enemi controlé par un humain au tableau
                            }
                            else{
                                this.enemys.add(new Enemy(i,j,this,false));//sinon on ajoute un enemi controlé par l'IA au tableau
                            }
                            this.runEnemys.add(this.enemys.get(this.enemys.size()-1));
                            this.thEnemys.add(new Thread(this.runEnemys.get(this.runEnemys.size()-1)));
                            displayTab[i][j] = new Object(' ');
                            break;
                        }
                        case 'H':{
                            displayTab[i][j] = new Ladder(i,j);
                            break;
                        }
                        case 'h':{
                            displayTab[i][j] = new Ladder(i,j,true);
                            int[] tmp = {i,j};
                            this.exitPos.add(tmp);
                            break;
                        }
                        case '#':{
                            displayTab[i][j] = new Floor(i,j);
                            break;
                        }
                        case '_':{
                            displayTab[i][j] = new Zipline(i,j);
                            break;
                        }    
                        default : {displayTab[i][j] = new Object(line.charAt(i));}
                    }
                }
                j++;
            }
            for(int i=0;i<this.thEnemys.size();i++){
                this.enemys.get(i).setTarget(this.players.get(i%this.players.size()));
                this.thEnemys.get(i).start();
            }
            for(int i=0;i<this.thPlayers.size();i++)
                this.thPlayers.get(i).start();
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    //
    //getteurs
    public Object[][]           getDisplayTab()      {return this.displayTab;}
    public int                  getSizeX()           {return this.sizeX;}
    public int                  getSizeY()           {return this.sizeY;}
    public int                  getGolds()           {return this.golds;}
    public Player               getPlayer(int i)     {return this.players.get(i);}
    public Enemy                getEnemy(int i)      {return this.enemys.get(i);}
    public ArrayList<Player>    getPlayers()         {return this.players;}
    public ArrayList<Enemy>     getEnemys()          {return this.enemys;}
    public Runnable             getRunPlayer(int i)  {return this.runPlayers.get(i);}
    public Runnable             getRunEnemy(int i)   {return this.runEnemys.get(i);}
    public ArrayList<Runnable>  getRunPlayers()      {return this.runPlayers;} 
    public ArrayList<Runnable>  getRunEnemys()       {return this.runEnemys;}
    public Thread               getThPlayer(int i)   {return this.thPlayers.get(i);}
    public Thread               getThEnemy(int i)    {return this.thEnemys.get(i);}
    public ArrayList<Thread>    getThPlayers()       {return this.thPlayers;}
    public ArrayList<Thread>    getThEnemys()        {return this.thEnemys;}
    public Frame                getFrame()           {return this.frame;}
    public TextArea             getGamePlay()        {return this.gamePlay;}
    public TextArea             getInfo()            {return this.info;}
    public int[]                getGoldPos(int i)    {return this.goldsPos.get(i);}
    public int[]                getExitPos(int i)    {return this.exitPos.get(i);}
    public ArrayList<int[]>     getGoldsPos()        {return this.goldsPos;}
    public ArrayList<int[]>     getExitPos()         {return this.exitPos;}
    public boolean              isEndGame()          {return this.endGame;}
    //setteurs
    public void                 setEndGame(boolean b){this.endGame = b;}

    //others
    public void resetPos(){//remet le plateau dans la position initiale
        for(int i=0;i<getPlayers().size();i++){//on parcourt la liste des joueurs
            getPlayer(i).goInit();//on les remet dans leur position initiale
            getPlayer(i).setGold(0);//on remet leur nombre de gold a 0
        }
        for(int i=0;i<getEnemys().size();i++)//on parcourt la liste des enemis
            getEnemy(i).goInit();//on les remet dans leur position initiale
        for(int i=0;i<getGoldsPos().size();i++){//on parcourt la liste des golds
            getDisplayTab()[getGoldPos(i)[0]][getGoldPos(i)[1]].setHidden(false);//on dit qu'ils sont a nouveau visibles
        }
        showExit(false);//on cache la sortie
        this.chronoForceStop = true;//on force le chrono a s'arreter (cela permet de reboucher les trous)
    }
    
    public void showExit(boolean b){//methode pour montrer ou non la sortie
        for(int i=0;i<getExitPos().size();i++){//on parcourt la liste des echelles formanant la sortie
            getDisplayTab()[getExitPos(i)[0]][getExitPos(i)[1]].setHidden(!b);//affiche ou non l'echelle
        }
    }
    
    public void escapeFromHole(Enemy e){//methode pour sortir d'un trou
        ChronoToEscape cte = new ChronoToEscape(e, 3500);//on lance un chrono (Thread) qui va attendre 3500 millisecondes puis faire sortir l'enemi
        cte.start();//on lance le thread
    }
    public void updateHole(Floor f){//methode pour mettre a jour l'etat d'un sol/mur
        for(int i=0;i<getPlayers().size();i++){//on parcourt la liste des joueurs
            if((getPlayer(i).getDigL() && (getPlayer(i).getX()==f.getX()+1 && getPlayer(i).getY()==f.getY()-1)) || (getPlayer(i).getDigR() && (getPlayer(i).getX()==f.getX()-1 && getPlayer(i).getY()==f.getY()-1))){
                //si (le joueur est en haut a droite du mur et qu'il creuse a gauche) ou (le joueur est en haut a gauche du mur et qu'il creuse a droite)
                f.setHidden(true);//on cache le mur
                resealHole(f);//on appel la methode qui rebouche le trou
            }
        }
    }
    public void resealHole(Floor f){//methode pour reboucher un trou
        ChronoToShow cts = new ChronoToShow(f,8000);//on lance un chrono (Thread) qui va attendre 8000 millisecondes puis faire réapparaitre le mur
        this.chronoForceStop = false;//on met le booleen pour forcer l'arrêt a faux
        cts.start();//on lance le thread
    }
    
    public boolean enemyHere(int x, int y){//methode pour savoir si un enemi est a cet endroit
        for(int i=0;i<getEnemys().size();i++){//on parcourt la liste des enemis
            if(getEnemy(i).getX()==x && getEnemy(i).getY()==y)return true;//si un enemi se trouve au coordonées demandées, on retourne vrai
        }
        return false;//sinon on retourne faux
    }
    public boolean playerHere(int x, int y){//methode pour savoir si un joueur est a cet endroit (idem enemyHere)
        for(int i=0;i<getPlayers().size();i++){
            if(getPlayer(i).getX()==x && getPlayer(i).getY()==y)return true;
        }
        return false;
    }
    //affichage
    String res;//variable qui va contenir le texte a afficher
    public void display(){
        res = "";//on remet la variable a vide
        for(int j=0;j<getSizeY();j++){//on parcourt les lignes
            for(int i=0;i<getSizeX();i++){//on parcourt les colonnes
                if(playerHere(i, j)){//si un joueur se trouve a cet endroit
                    res += 'O';//on affiche un O
                }
                else if(enemyHere(i, j)){//si un enemi se trouve a cet endroit
                    res += 'X';//on affiche un X
                }
                else res+=getDisplayTab()[i][j];//sinon on affiche le caractere contenu dans le tableau de jeux
            }
            res+='\n';//on ajoute un retour a la ligne
        }
        getGamePlay().setText(res);//on affiche le plateau dans la textArea
        res = "";//on remet la variable a vide
        if(getPlayer(0).getY()==1){ //si le joueur est au sommet de la map (mode solo donc getPlayer(0) suffit)
            res += "YOU WIN !!";//on affiche "YOU WIN !!"
        }
        else if(getPlayer(0).getLives()==0){// sinon si le nombre de vies du joueur est nul (mode solo donc getPlayer(0) suffit)
            res += "GAME OVER";//on affiche "GAME OVER"
        }
        else{//sinon
            res += "player : "+getPlayer(0).getName()+"\n";//on affiche le nom du joueur
            res += "Lives = "+getPlayer(0).getLives()+"\nGold = "+getPlayer(0).getGold()+"/"+getGolds()+"\n";//ainsi que ses vies et son nombre de gold/nombre de golds total
        }
        getInfo().setText(res);//on affiche les informations dans la textArea
    }

    public String displayToSend(){//methode similaire a display mais qui a vocation à être envoyer aux clients
        res = "";
        for(int j=0;j<getSizeY();j++){
            for(int i=0;i<getSizeX();i++){
                if(playerHere(i, j)){
                    res += 'O';
                }
                else if(enemyHere(i, j)){
                    res += 'X';
                }
                else res+=getDisplayTab()[i][j];
            }
            res+='\n';
        }
        return res;//retourne uniquement le plateau de jeux
    }

    public String infoToSend(Character c){// méthode similaire à displayToSend mais qui envoie les infos du personnage passé en paramètre
        if(c.getType()=='O'){//si le personnage est un joueur
            if(c.getY()==1)return "YOU WIN !!";//si le joueur est au sommet de la map, on affiche "YOU WIN !!"
            else if(((Player)c).getLives()==0)return "GAME OVER";//sinon si le nombre de vies du joueur est nul, on affiche "GAME OVER"
            else{//sinon
                res = "player : "+((Player)c).getName()+"\n";//on affiche le nom du joueur
                res+="Lives = "+((Player)c).getLives()+"\nGold = "+((Player)c).getGold()+"/"+getGolds()+"\n";//ainsi que ses vies et son nombre de gold/nombre de golds total
                return res;//on retourne les infos
            }
        }
        else if(c.getType()=='X'){//si le personnage est un enemi
            for(int i=0;i<getPlayers().size();i++){//on parcourt la liste des joueurs
                if(getPlayer(i).getY()==1){return "GAME OVER";}//si un joueur est au sommet de la map, on affiche "GAME OVER"
                if(getPlayer(i).getLives()>0){//si un joueur a encore des vies
                    return "";//on ne renvoie rien
                }
            }
            return "YOU WIN !!";//sinon on affiche "YOU WIN !!"
        }
        return "";//si le personnage n'est pas identifié, on ne renvoie rien
    }
    class Chrono extends Thread{//classe qui va gérer les chronos (elle doit être étendue)
        protected Object o;//objet qui va être modifié a la fin du chrono
        protected int  time;//durée du chrono
        public Chrono(Object o){
            this.o = o;
            time = 4000;
        }
        public Chrono(Object o, int t){
            this.o = o;
            this.time = t;
        }
        public Object getO(){return this.o;}
        public int getTime(){return this.time;}

        public void setO(Object o){this.o = o;}
        public void setTime(int i){this.time = i;}
        @Override
        public void run(){};//methode run qui va être surchargée
        
    }
    class ChronoToShow extends Chrono{//chrono qui va réafficher les trous une fois terminé
        public ChronoToShow(Object o){
            super(o);
        }
        public ChronoToShow(Object o, int t){
            super(o,t);
        }

        @Override
        public void run(){
            long startTime = System.currentTimeMillis();//on récupère le temps actuel pour savoir quand a commencé le chrono
            while(System.currentTimeMillis()-startTime<getTime() && !chronoForceStop){//tant que le chrono n'est pas fini et que le chrono n'a pas été stoppé de force
                onSpinWait();//cette méthode permet d'economiser du processeur, (remarque : on fait une boucle while et pas un sleep comme dans ChronoToEscape car on veut pouvoir forcé l'arrêt)
            }
            this.o.setHidden(false);//on reaffiche l'objet
            for(int i=0;i<getEnemys().size();i++){//on parcourt la liste des enemis
                if(getEnemy(i).getX()==o.getX() && getEnemy(i).getY()==o.getY())//si un enemi est sur le même endroit que l'objet
                    getEnemy(i).die();//on le tue
            }
        }
    }
    class ChronoToEscape extends Chrono{//chrono qui va faire s'échapper un enemi d'un trou une fois terminé
        Enemy e;
        public ChronoToEscape(Enemy e){
            super(e);
            this.e = e;
        }
        public ChronoToEscape(Enemy e, int t){
            super(e,t);
            this.e = e;
        }

        @Override
        public void run(){
            try{sleep(getTime());}catch(InterruptedException e){System.out.println(e + " class chronoToEscape, methode run");}//on attend le temps du chrono
            if(e.getTarget().getX()<e.getX()){//si le joueur que l'on suit est a gauche de l'enemi
                e.setY(e.getY()-1);//on fait sortir l'enemi du trou
                e.setX(e.getX()-1);//par la gauche
            }
            else{//si le joueur que l'on suit est a droite de l'enemi
                e.setY(e.getY()-1);//on fait sortir l'enemi du trou
                e.setX(e.getX()+1);//par la droite
            }
            this.e.setInHole(false);//on indique que l'enemi n'est plus dans un trou
        }
    }
    
    
}
