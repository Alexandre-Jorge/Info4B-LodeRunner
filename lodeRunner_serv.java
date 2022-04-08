import java.net.*;


public class LodeRunner_serv{
    public static final int PORT = 8080;
    public static final int MAX_LOBBY = 4;
    public static void main(String Args[]){
        try{
            int nbLobby = 0;
            ServerSocket servSoc = new ServerSocket(PORT);
            Lobby[] lobbys = new Lobby[MAX_LOBBY];
            System.out.println("Server started " + servSoc);
            while(nbLobby < MAX_LOBBY){
                Lobby lobby = new Lobby(4, nbLobby);
                lobbys[nbLobby] = lobby;
                lobby.start();
                long timeStart = System.currentTimeMillis();//!!!\\a modifier//!!\\
                while(lobby.getNbEnemy()+lobby.getNbPlayer() < 4 && System.currentTimeMillis() - timeStart <10000){
                    Socket client = servSoc.accept();
                    if((lobby.getNbEnemy()+lobby.getNbPlayer())%2 == 1){
                        lobby.addConnexion("enemy", client);
                        System.out.println("new enemy : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    }
                    else{
                        lobby.addConnexion("player", client);
                        System.out.println("new player : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());
                    }
                }
                lobby.setStart(true);
                nbLobby++;
            }
            servSoc.close();
        }catch(Exception e){System.out.println(e);}
    }
}

