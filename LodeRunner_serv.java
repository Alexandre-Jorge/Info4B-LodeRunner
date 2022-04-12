import java.net.*;


public class LodeRunner_serv{
    public static final int PORT = 8080;
    public static final int MAX_LOBBY = 4;
    public static final int MAX_PLAYER = 6;
    public static void main(String Args[]){
        try{
            int nbLobby = 0;
            ServerSocket servSoc = new ServerSocket(PORT);
            Lobby[] lobbys = new Lobby[MAX_LOBBY];
            System.out.println("Server started " + servSoc);
            while(nbLobby < MAX_LOBBY){
                Socket client = servSoc.accept();
                for(int i = 0; i < lobbys.length; i++){
                    if(lobbys[i] != null){
                        if(!lobbys[i].isAlive()){
                            nbLobby--;
                        }
                    }
                }
                if(nbLobby < 1 || lobbys[nbLobby-1].getStart()){
                    lobbys[nbLobby] = new Lobby(MAX_PLAYER, nbLobby);
                    nbLobby++;
                    lobbys[nbLobby-1].start();
                }
                if(lobbys[nbLobby-1].getNbEnemy()+lobbys[nbLobby-1].getNbPlayer() < MAX_PLAYER){
                    if((lobbys[nbLobby-1].getNbEnemy()+lobbys[nbLobby-1].getNbPlayer())%2 == 1){
                        lobbys[nbLobby-1].addConnexion("enemy", client);
                        System.out.println("new enemy : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    }
                    else{
                        lobbys[nbLobby-1].addConnexion("player", client);
                        System.out.println("new player : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    }
                }
            }
            for(int i = 0; i < lobbys.length; i++){
                lobbys[i].join();
            }
            servSoc.close();
        }catch(Exception e){System.out.println(e);}
    }

}

