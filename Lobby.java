import java.net.*;


import objects.*;
import java.io.*;

public class Lobby extends Thread{
    private final int HEIGHT = 40;
    private final int WIDTH = 100;
    private int numLobby;
    private boolean endGame = false;
    private int nbPlayer = 0;
    private int nbEnemy = 0;
    private Connexion[] connexionCli;
    private PlayGround pg;
    private boolean start = false;
    protected String out;
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
    public void addConnexion(String type, Socket soc){
        if(type.equals("player")){
            this.connexionCli[nbEnemy+nbPlayer] = new Connexion( nbPlayer,type, soc);
            this.nbPlayer++;
        }
        else{
            this.connexionCli[nbEnemy+nbPlayer] = new Connexion( nbEnemy,type, soc);
            this.nbEnemy++;
        }
        this.connexionCli[nbEnemy+nbPlayer-1].start();
    }

    @Override
    public void run(){
        while(!getStart()){
            System.out.print("Matchmaking... "+(nbEnemy+nbPlayer)+"/"+connexionCli.length+"\r");
            try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
        }
        System.out.println("Lobby number " + numLobby + " started");
        try{
            FileReader f = new FileReader("levels/multi/level1.txt");
            this.pg = new PlayGround(f,WIDTH,HEIGHT,nbPlayer,nbEnemy);
            f.close();
        }catch(Exception e){System.out.println(e);}
        while(!endGame){
            this.out = pg.displayToSend();
            for(int i=0;i<connexionCli.length;i++){
                if(connexionCli[i] != null){
                    connexionCli[i].send(out);
                }
            }
        }
    }


    class Connexion extends Thread{
        protected int id;
        protected String type;
        protected Socket soc;
        protected BufferedReader sisr;
        protected PrintWriter sisw;
        protected Commands commands;
        protected boolean stop = false;
        public Connexion(int id, String type, Socket soc){
            this.id = id;
            this.type = type;
            this.soc = soc;
            commands = new Commands();
            try{
                sisr = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
            }catch(Exception e){System.out.println(e + "class = Connexion method = constructor");}
        }

        //methods
        public void send(String s){
            sisw.println(s);
            sisw.println("END_DISPLAY");
        }
        
        @Override
        public void run(){
            try{
                while(!start){
                    sisw.println("Matchmaking ..."+(getNbEnemy()+getNbPlayer())+"/"+getConnexionCli().length);
                    try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
                }
                sisw.println("START");
                System.out.println("START");
                commands.start();
                sleep(1000);
                while(!stop){
                    sleep(10);
                }
                System.out.println("STOP");
                sisr.close();
                sisw.close();
                soc.close();
            }catch(Exception ex){System.out.println(ex + "class Connexion method run");}
        }

        class Commands extends Thread{
            @Override
            public void run(){
                try{
                    String[] commands;
                    while(!stop){
                        commands = sisr.readLine().split(" ");
                        switch(commands[0]){
                            case "STOP" :{
                                stop = true;
                                break;
                            }
                            case "DIG":{
                                if(type.equals("player"))
                                    pg.getPlayer(id).dig(commands[1].trim().charAt(0));
                                break;
                            }
                            case "SET_UP":{
                                if(type.equals("player"))
                                    pg.getPlayer(id).setUp(Boolean.parseBoolean(commands[1].trim()));
                                else
                                    pg.getEnemy(id).setUp(Boolean.parseBoolean(commands[1].trim()));
                                break;
                            }
                            case "SET_DOWN":{
                                if(type.equals("player"))
                                    pg.getPlayer(id).setDown(Boolean.parseBoolean(commands[1].trim()));
                                else
                                    pg.getEnemy(id).setDown(Boolean.parseBoolean(commands[1].trim()));
                                break;
                            }
                            case "SET_LEFT":{
                                if(type.equals("player")){
                                    pg.getPlayer(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                                }
                                else{
                                    pg.getEnemy(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                                }
                                break;
                            }
                            case "SET_RIGHT":{
                                if(type.equals("player")){
                                    pg.getPlayer(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                                }
                                else{
                                    pg.getEnemy(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                                }
                                break;
                            }
                            case "SET_DIG_L":{
                                if(type.equals("player"))
                                    pg.getPlayer(id).setDigL(Boolean.parseBoolean(commands[1].trim()));
                                break;
                            }
                            case "SET_DIG_R":{
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

