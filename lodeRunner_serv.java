import java.io.*;
import objects.*;
import java.net.*;


public class LodeRunner_serv{
    public static boolean endGame = false;
    public static final int HEIGHT = 40;
    public static final int WIDTH = 100;
    public static final int PORT = 8080;
    public static final int MAX_CLI = 4;
    protected static PlayGround pg;
    protected static boolean start = false;

    public static void main(String Args[]){
        try{ 
            int nbPlayer = 0;
            int nbEnemy = 0;
            long timeStart = System.currentTimeMillis();
            ServerSocket servSoc = new ServerSocket(PORT);
            Connexion[] connexions = new Connexion[MAX_CLI];
            System.out.println("Server started " + servSoc);

            while(nbPlayer + nbEnemy < MAX_CLI && System.currentTimeMillis() - timeStart<10000){
                Socket client = servSoc.accept();
                Connexion connexion;
                if(nbPlayer<=nbEnemy){
                    connexion = new Connexion(nbPlayer,"player", client);
                    System.out.println("new player : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    nbPlayer++;
                }
                else{
                    connexion = new Connexion(nbEnemy,"enemy", client);
                    System.out.println("new enemy : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    nbEnemy++;
                }
                connexions[nbPlayer+nbEnemy-1] = connexion;
                connexion.start();
            }
            FileReader f = new FileReader("levels/multi/level1.txt");
            pg = new PlayGround(f,WIDTH,HEIGHT,nbPlayer,nbEnemy);
            f.close();
            start = true;
        }catch(Exception e){System.out.println(e);}
    }
    static class Connexion extends Thread{
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
        @Override
        public void run(){
            try{
                sisw.println("Matchmaking ...");
                while(!start){
                    try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
                }
                sisw.println("START");
                System.out.println("START");
                commands.start();
                while(!stop){
                    synchronized(pg){
                        sisw.println(pg.displayToSend());
                        sisw.println("END_LINE");
                    }
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

