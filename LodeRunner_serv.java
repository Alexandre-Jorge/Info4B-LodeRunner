import java.net.*;


public class LodeRunner_serv{
    public static final int PORT = 8080;//port du serveur
    public static final int MAX_LOBBY = 4;//nombre de lobby sur le serveur
    public static final int MAX_PLAYER = 6;//nombre de joueurs/lobby
    public static void main(String Args[]){
        try{
            int nbLobby = 0;//nombre de lobby actuel
            ServerSocket servSoc = new ServerSocket(PORT);//création du serveur
            Lobby[] lobbys = new Lobby[MAX_LOBBY];//tableau contenant les lobbys
            System.out.println("Server started " + servSoc);//on affiche que le serveur est lancé
            while(nbLobby < MAX_LOBBY){//tant que le nombre max de lobby n'est pas atteint
                Socket client = servSoc.accept();//on accepte un client (bloquant)
                for(int i=0;i<lobbys.length;i++){//on parcours tous les lobbys
                    if(lobbys[i] == null){//si le lobby n'a pas été créé
                        lobbys[i] = new Lobby(MAX_PLAYER, i);//on en créé un
                        nbLobby++;//on incrémente le nombre de lobby
                        lobbys[i].start();//on lance le lobby
                    }
                    else if(!lobbys[i].isAlive()){//sinon si un lobby a déjà été créé mais qu'il est terminé
                        lobbys[i] = new Lobby(MAX_PLAYER, i);//on en créé un nouveau
                        lobbys[i].start();//on le lance (pas besoin d'incrémenter le nombre de lobby car on ne fait qu'en remplacer un)
                    }
                    //une fois ici on a soit un nouveau lobby créé, soit c'est que le lobby est en cour matchmaking ou qu'une partie est deja lancée
                    if(lobbys[i].isAlive() && !lobbys[i].getStart()){//si le lobby est en cours de matchmaking
                        if(lobbys[i].getNbEnemy()+lobbys[i].getNbPlayer() < MAX_PLAYER){//si le lobby n'a pas atteint le nombre max de joueurs
                            if((lobbys[i].getNbEnemy()+lobbys[i].getNbPlayer())%2 == 1){//si le nombre de joueurs est impair
                                lobbys[i].addConnexion("enemy", client);//on ajoute un ennemi
                                System.out.println("new enemy : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());//on affiche qu'un ennemi a été ajouté et qui va le controler
                            }
                            else{//si le nombre de joueurs est pair
                                lobbys[i].addConnexion("player", client);//on ajoute un Player
                                System.out.println("new player : IP = ["+client.getInetAddress()+"] PORT = "+client.getPort());//on affiche qu'un joueur a été ajouté et qui va le controler
                            }
                        }
                        i = lobbys.length;//on force la sorti de la boucle
                    }
                }
                
            }
            //on arrive ici si le nombre max de lobby a été atteint
            for(int i = 0; i < lobbys.length; i++){//on parcours tous les lobbys
                lobbys[i].join();//on attend que le lobby finisse
            }
            servSoc.close();//on ferme le serveur
        }catch(Exception e){System.out.println(e);}
    }

}

