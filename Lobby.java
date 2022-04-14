import java.net.*;


import objects.*;
import java.io.*;

public class Lobby extends Thread{
    private final int HEIGHT = 40;//hauteur du terrain
    private final int WIDTH = 100;//largeur du terrain
    private int numLobby;//numero de la partie sur le serveur
    private int nbPlayer = 0;//nombre de Player dans la partie
    private int nbEnemy = 0;//nombre d'ennemi dans la partie
    private Connexion[] connexionCli;//tableau ou se trouvent les clients
    private PlayGround pg;//terrain de jeu
    private boolean start = false;//booleen qui permet de savoir si la partie est lancée
    protected String out1, out2;//string qui contienent les données à envoyer aux clients
    protected long startTime;//moment ou le lobby a demarrer
    //contructor
    //
    //standard
    public Lobby(int nbClient, int numLobby){
        this.numLobby = numLobby;
        this.connexionCli = new Connexion[nbClient];
    }
    //methods
    //getter
    public Connexion[] getConnexionCli(){return this.connexionCli;}
    public PlayGround getPg(){return this.pg;}
    public boolean getStart(){return this.start;}
    public int getNbPlayer(){return this.nbPlayer;}
    public int getNbEnemy(){return this.nbEnemy;}
    //setter
    public void setConnexionCli(Connexion[] cc){this.connexionCli = cc;}
    public void setStart(boolean s){this.start = s;}
    //others
    public void addConnexion(String type, Socket soc){//ajoute un client au lobby
        if(type.equals("player")){//si c'est un player
            this.connexionCli[nbEnemy+nbPlayer] = new Connexion( nbPlayer,type, soc);//ajoute le client au lobby
            this.nbPlayer++;//incremente le nombre de player
        }
        else{//si c'est un ennemi
            this.connexionCli[nbEnemy+nbPlayer] = new Connexion( nbEnemy,type, soc);//ajoute le client au lobby
            this.nbEnemy++;//incremente le nombre d'ennemi
        }
        this.connexionCli[nbEnemy+nbPlayer-1].start();//lance le thread du client
    }

    @Override
    public void run(){
        startTime = System.currentTimeMillis();//recupere le moment ou le lobby a demarrer
        while(!getStart()){//tant que la partie n'est pas lancée
            System.out.print("Matchmaking... "+(nbEnemy+nbPlayer)+"/"+connexionCli.length+"\r");//affiche que le matchmaking est en cours avec le nombre de joueur connecté
            if(getNbPlayer() + getNbEnemy() == connexionCli.length || System.currentTimeMillis() - startTime > 30000){//si le lobby est plein ou que le matchmaking a durée 30 secondes
                setStart(true);//on lance la partie
            }
            try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}//attend un peu pour ne pas faire trop de calcul
        }
        System.out.println("Lobby number " + numLobby + " started");//affiche que la partie a commencé
        try{
            FileReader f = new FileReader("levels/multi/level1.txt");//ouvre le fichier de niveau
            this.pg = new PlayGround(f,WIDTH,HEIGHT,nbPlayer,nbEnemy);//initialise le terrain de jeu
            f.close();//ferme le fichier
        }catch(Exception e){System.out.println(e);}
        while(!pg.isEndGame()){//tant que la partie n'est pas finie
            this.out1 = "GAMEPLAY\n"+pg.displayToSend();//la premiere string a envoyer aux clients est le terrain de jeu
            for(int i=0;i<connexionCli.length;i++){//pour chaque client
                if(connexionCli[i] != null){//si le client existe bien
                    connexionCli[i].send(out1);//on lui envoie le terrain de jeu
                    if(connexionCli[i].getType().equals("player")){//si c'est un player
                        this.out2 = "INFO\n"+pg.infoToSend(pg.getPlayer(connexionCli[i].getID()));//on lui envoie les informations du player
                    }
                    else{//si c'est un ennemi
                        this.out2 = "INFO\n"+pg.infoToSend(pg.getEnemy(connexionCli[i].getID()));//on lui envoie les informations de l'ennemi
                    }
                    connexionCli[i].send(out2);//on lui envoie les informations
                }
            }
        }
        //une fois ici c'est que la partie est finie
        for(int i=0;i<connexionCli.length;i++){//pour chaque client
            if(connexionCli[i] != null){//si le client existe bien
                connexionCli[i].send("END_GAME");//on le previent que la partie est finie
            }
        }
    }


    class Connexion extends Thread{//classe qui gères les clients
        protected int id;//id du client
        protected String type;//type du client (player ou enemy)
        protected Socket soc;//socket du client
        protected BufferedReader sisr;//buffer du flux entrant
        protected PrintWriter sisw;//writer du flux sortant
        protected Commands commands;//classe qui gère les commandes
        protected boolean stop = false;//flag de fin de partie
        public Connexion(int id, String type, Socket soc){
            this.id = id;
            this.type = type;
            this.soc = soc;
            commands = new Commands();
            try{
                sisr = new BufferedReader(new InputStreamReader(soc.getInputStream()));//le buffer est initialisé avec le flux entrant du socket
                sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);//le writer du flux sortant est initialisé avec le flux sortant du socket
            }catch(Exception e){System.out.println(e + "class = Connexion method = constructor");}
        }

        //methods
        public void send(String s){//methode pour envoyer les datas au client
            sisw.println(s);//on envoie le message
            sisw.println("END");//on envoie un marqueur de fin de message
        }
        public String getType(){return this.type;}
        public int getID(){return this.id;}
        @Override
        public void run(){
            try{
                while(!start){//tant que la partie n'est pas lancée
                    sisw.println("Matchmaking ..."+(getNbEnemy()+getNbPlayer())+"/"+getConnexionCli().length + "\ttime left : "+((30000-(System.currentTimeMillis()-startTime))/1000));//on envoie au client que le matchmaking est en cours avec le nombre de joueur connecté et le temps restant avant le debut
                    try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}//on attend un peu pour ne pas surcharger ni le processeur ni le reseaux
                }
                sisw.println("START");//on envoie au client que la partie est lancée
                System.out.println("START");//on affiche que la partie est lancée
                commands.start();//on lance le thread de commande
                sleep(1000);//on attend un peu le temps que les calculs soient finis
                while(!stop){//tant que la partie n'est pas finie
                    onSpinWait();//methode pour attendre tout en economisant du processeur
                }
                System.out.println("STOP");//on affiche que la partie est finie
                //on ferme les flux et le serveur
                sisr.close();
                sisw.close();
                soc.close();
            }catch(Exception ex){System.out.println(ex + "class Connexion method run");}
        }

        class Commands extends Thread{//classe qui gère les commandes
            @Override
            public void run(){
                try{
                    String[] commands;//tableau de string qui contient les commandes
                    while(!stop){//tant que la partie n'est pas finie
                        commands = sisr.readLine().split(" ");//on lit la commande (du type "SET_UP TRUE")
                        switch(commands[0]){//si la premiere partie de la commande
                            case "STOP" :{//est STOP
                                stop = true;//on arrete de lire les commandes
                                break;
                            }
                            case "DIG":{//est DIG
                                if(type.equals("player"))//on verifie que le client est un player
                                    pg.getPlayer(id).dig(commands[1].trim().charAt(0));//on creuse du côté indiquer par la deuxieme partie de la commande
                                break;
                            }
                            case "SET_UP":{//est SET_UP
                                if(type.equals("player"))//si le client est un player
                                    pg.getPlayer(id).setUp(Boolean.parseBoolean(commands[1].trim()));//on recupere le player controlé par ce thread dans le playGround et on le fait aller ou non en haut selon la deuxieme partie de la commande
                                else
                                    pg.getEnemy(id).setUp(Boolean.parseBoolean(commands[1].trim()));//idem pour un ennemi
                                break;
                            }
                            case "SET_DOWN":{//idem mais en bas
                                if(type.equals("player"))
                                    pg.getPlayer(id).setDown(Boolean.parseBoolean(commands[1].trim()));
                                else
                                    pg.getEnemy(id).setDown(Boolean.parseBoolean(commands[1].trim()));
                                break;
                            }
                            case "SET_LEFT":{//idem mais a gauche
                                if(type.equals("player")){
                                    pg.getPlayer(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                                }
                                else{
                                    pg.getEnemy(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                                }
                                break;
                            }
                            case "SET_RIGHT":{//idem mais a droite
                                if(type.equals("player")){
                                    pg.getPlayer(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                                }
                                else{
                                    pg.getEnemy(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                                }
                                break;
                            }
                            case "SET_DIG_L":{//est SET_DIG_L
                                if(type.equals("player"))//on verifie que le client est un player
                                    pg.getPlayer(id).setDigL(Boolean.parseBoolean(commands[1].trim()));//la difference avec dig(char side) c'est que cette methode n'effectue pas de verification, elle doit etre utilisé seulement pour mettre le booleen a false (ce n'est effectivement pas très optimisé)
                                break;
                            }
                            case "SET_DIG_R":{//idem mais a droite
                                if(type.equals("player"))
                                    pg.getPlayer(id).setDigR(Boolean.parseBoolean(commands[1].trim()));
                                break;
                            }
                        }
                    }
                }catch(Exception ex){System.out.println(ex + "class Commands method run");}
            }
        }
    }
}