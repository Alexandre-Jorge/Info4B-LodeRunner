import java.io.*;
import objects.*;
import java.net.*;


public class lodeRunner_serv{
    public static boolean endGame = false;
    public static final int HEIGHT = 40;
    public static final int WIDTH = 100;
    public static final int PORT = 8080;
    public static final int MAX_CLI = 4;
    protected static playGround pg;

    public static void main(String Args[]){
        try{
            int nbPlayer = 0;
            int nbEnemy = 0;
            long timeStart = System.currentTimeMillis();
            DatagramPacket requestPacket;
            DatagramSocket serverSoc = new DatagramSocket(PORT);
            String command = "";
            while(!"solo".equals(command) && !"multi".equals(command)){
                requestPacket = getPacket(serverSoc, 256);
                command = new String(requestPacket.getData()).trim();
                System.out.println(command);
            }
            if("solo".equals(command)){
                System.out.println("one player on solo mode");
                serverSoc.close();
            }
            else if("multi".equals(command)){
                InetAddress[] adresses = new InetAddress[4];
                int[] ports = new int[4];
                Connexion[] connexions = new Connexion[4];
                while(nbPlayer + nbEnemy < MAX_CLI && System.currentTimeMillis() - timeStart<10000){
                    requestPacket = getPacket(serverSoc,256);
                    Connexion connexion;
                    if(nbPlayer<=nbEnemy){
                        connexion = new Connexion(nbPlayer,"player",/*requestPacket.getAddress(),requestPacket.getPort(),*/pg, serverSoc);
                        adresses[nbEnemy+nbPlayer] = requestPacket.getAddress();
                        ports[nbEnemy+nbPlayer] = requestPacket.getPort();
                        connexions[nbEnemy+nbPlayer] = connexion;
                        System.out.println("new player : ip [" + requestPacket.getAddress() + "] port :" + requestPacket.getPort());
                        nbPlayer++;
                    }
                    else{
                        connexion = new Connexion(nbEnemy,"enemy",/*requestPacket.getAddress(),requestPacket.getPort(),*/pg, serverSoc);
                        adresses[nbEnemy+nbPlayer] = requestPacket.getAddress();
                        ports[nbEnemy+nbPlayer] = requestPacket.getPort();
                        connexions[nbEnemy+nbPlayer] = connexion;
                        System.out.println("new enemy : ip [" + requestPacket.getAddress() + "] port :" + requestPacket.getPort());
                        nbEnemy++;
                    }
                    connexion.start();
                }
                FileReader f = new FileReader("levels/multi/level1.txt");
                pg = new playGround(f,WIDTH,HEIGHT,nbPlayer,nbEnemy);
                for(int i=0;i<nbEnemy+nbPlayer;i++)
                    connexions[i].setPlayGround(pg);
                while(pg.getThPlayer(0).isAlive()){///////!\\\\\ A MODIFIER !!!
                    //requestPacket = getPacket(serverSoc, 65535);
                    
                    for(int i=0;i<nbEnemy+nbPlayer;i++)
                        sendPacket(serverSoc, pg.displayToSend().getBytes(), adresses[i], ports[i]);
                }
                f.close();
                serverSoc.close();
            }
            
        }catch(Exception e){System.out.println(e);}
    }
    public static DatagramPacket getPacket(DatagramSocket socket, int i) throws IOException{
        byte[] data = new byte[i];
        DatagramPacket pkt = new DatagramPacket(data, data.length);
        socket.receive(pkt);
        return pkt;
    }
    public static void sendPacket(DatagramSocket socket, byte[] data, InetAddress addr, int port)throws IOException{
        DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
        socket.send(packet);
    }

    
}
class Connexion extends Thread{
    private int id;
    // private InetAddress cli_addr;
    // private int cli_port;
    private String type;
    private playGround pg;
    private DatagramSocket soc;
    public Connexion(int id, String type,/* InetAddress ina, int port,*/ playGround pg, DatagramSocket soc){
        this.id = id;
        // this.cli_addr = ina;
        // this.cli_port = port;
        this.type = type;
        this.pg = pg;
        this.soc = soc;
    }
    public void setPlayGround(playGround pg){
        this.pg = pg;
    }
    @Override
    public void run(){
        try{
            DatagramPacket commandPacket;
            String[] commands;
            do{
                commandPacket = lodeRunner_serv.getPacket(soc, 256);
                commands = new String(commandPacket.getData()).split(" ");
                //System.out.println(commands[0]+commands[1]);
                switch(commands[0]){
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
                        System.out.println(commands[0]+" "+commands[1]);
                        if(type.equals("player")){
                            pg.getPlayer(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                            System.out.println("PLAYER :: RIGHT = "+pg.getPlayer(id).getRight()+"\nLEFT = "+pg.getPlayer(id).getLeft());
                        }
                        else{
                            pg.getEnemy(id).setLeft(Boolean.parseBoolean(commands[1].trim()));
                            System.out.println("ENEMY :: RIGHT = "+pg.getEnemy(id).getRight()+"\nLEFT = "+pg.getEnemy(id).getLeft());
                        }
                        break;
                    }
                    case "SET_RIGHT":{
                        if(type.equals("player")){
                            pg.getPlayer(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                            System.out.println("PLAYER :: RIGHT = "+pg.getPlayer(id).getRight()+"\nLEFT = "+pg.getPlayer(id).getLeft());
                        }
                        else{
                            pg.getEnemy(id).setRight(Boolean.parseBoolean(commands[1].trim()));
                            System.out.println("ENEMY :: RIGHT = "+pg.getEnemy(id).getRight()+"\nLEFT = "+pg.getEnemy(id).getLeft());
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
            }while(!commands[0].equals("END"));
        }catch(Exception ex){System.out.println(ex + "class Connexion method run");}
    }
}
