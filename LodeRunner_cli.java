import java.io.*;
import objects.*;
import java.net.*;


import java.awt.*;
import java.awt.event.*;


public class LodeRunner_cli{
    //attributs
    protected static final int HEIGHT = 40;//hauteur de la fenêtre
    protected static final int WIDTH = 100;//largeur de la fenêtre
    protected static final int PORT = 8080;//port du serveur
    protected static String ADDR;//adresse du serveur
    protected static Socket clientSoc;//socket du client
    protected static BufferedReader sisr;//buffer du flux entrant
    protected static PrintWriter sisw;//writer du flux sortant

    public static boolean endGame = false;//booleen qui permet de savoir si le jeu est fini

    private static SendKeyListener skl;//listener du clavier qui enverra les commandes au serveur
    private static Frame frame;//fenêtre du jeu
    private static TextArea gamePlay, info;//zone de jeu et zone d'information
    
    //methodes
    //
    //getter
    public static Frame    getFrame()   {return frame;}
    public static TextArea getGamePlay(){return gamePlay;}
    public static TextArea getInfo()    {return info;}

    //setter
    public static void setFrame(Frame f)       {frame = f;}
    public static void setGamePlay(TextArea g) {gamePlay = g;}
    public static void setInfo(TextArea i)     {info = i;}
    //main
    public static void main(String Args[]){
        
        try{
            if(Args.length > 1){//si il y a 2 argument ou plus
                ADDR = Args[1];//le deuxieme argument est l'adresse du serveur
            }
            else{
                ADDR = "localhost";//sinon on met l'adresse par défaut
            }
            //le premier argument représente le mode de jeu
            if(Args[0].equals("solo")){//si c'est solo
                //pas besoin de reseaux dans ce cas
                FileReader f = new FileReader("levels/solo/level1.txt");//on charge le premier niveau
                PlayGround pg = new PlayGround(f,WIDTH,HEIGHT);//on crée le terrain de jeu
                while(!pg.isEndGame()){//tant que le jeux n'est pas fini
                    pg.display();//on affiche le terrain de jeu
                }
                f.close();//on ferme le fichier
            }
            else if(Args[0].equals("multi")){//si c'est multi

                //initialisation de la fenetre et ses composants
                try{skl = new SendKeyListener();}catch(Exception ex){System.out.println(ex + "class lodeRunner_cli method init");}
                frame = new Frame("Lode Runner client");
                gamePlay = new TextArea(HEIGHT+1, WIDTH+2);
                info = new TextArea(4,20);
                gamePlay.setFocusable(false);
                gamePlay.setFont(new Font("Monospaced",Font.BOLD, 12));
                info.setFocusable(false);
                frame.setLayout(new FlowLayout());
                frame.add(gamePlay);
                frame.add(info);
                frame.addKeyListener(skl);
                frame.setVisible(true);
                frame.setSize(900, 800);
                //

                clientSoc = new Socket(ADDR, PORT);//on se connecte au serveur
                sisr = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));//initilaisation du buffer du flux entrant
                sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSoc.getOutputStream())), true);//initialisation du writer du flux sortant
                String resp;//string qui contiendra la réponse du serveur
                while(!(resp = sisr.readLine()).equals("START")){//tant que le serveur ne dit pas de demarrer
                    getGamePlay().setText(resp);//on affiche ce que nous envoie le serveur (matchmaking en cour etc...)
                }
                System.out.println("START");//on affiche que le jeux demarre
                String tmp;//string temporaire qui va lire les lignes de la reponse du serveur a certain moment
                while(!endGame){//tant que le jeux n'est pas fini
                    resp=sisr.readLine();//on attend une reponse du serveur
                    if(resp.equals("GAMEPLAY")){//si le marqeur de commande est GAMEPLAY
                        resp="";//on vide la string
                        while(!(tmp = sisr.readLine()).equals("END")){//tant que le marqeur de fin "END" n'est pas recu
                            resp+=tmp+'\n';//on ajoute la ligne recu a la reponse globale
                        }
                        getGamePlay().setText(resp);//on affiche le terrain de jeu recu
                    }
                    else if(resp.equals("INFO")){//si le marqeur de commande est INFO
                        resp="";//on vide la string
                        while(!(tmp = sisr.readLine()).equals("END")){//tant que le marqeur de fin "END" n'est pas recu
                            resp+=tmp+'\n';//on ajoute la ligne recu a la reponse globale
                        }
                        getInfo().setText(resp);//on affiche les informations recu
                    }
                    else if(resp.equals("END_GAME")){//si la commande est END_GAME
                        endGame = true;//on indique que le jeux est fini
                    }
                }
                sisw.println("STOP");//on previent le serveur que le jeux est fini
                //on ferme les flux et le socket
                sisr.close();
                sisw.close();
                clientSoc.close();
            }
            else{//sinon on affiche un message expliquant comment utiliser le programme
                System.out.println("Usage : java LodeRunner_cli [solo|multi] [ip]");
            }
        }catch(Exception e){System.out.println(e);}
    }

    static class SendKeyListener implements KeyListener{//listener du clavier qui enverra les commandes au serveur
        //class fonctionnant comme la class "MykeyListener" present dans la class "Character" mais celle ci envoie les commande au serveur et n'agit pas directement sur un PlayGround
        private boolean haveDigged = false;
        @Override
        public void keyTyped(KeyEvent e)
        {
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            try{
                switch(e.getKeyChar()){
                    case 'a': {
                        if(!haveDigged){
                            sisw.println("DIG L");
                            try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                            sisw.println("SET_DIG_L FALSE");
                            this.haveDigged = true;
                        }
                        break;
                    }
                    case 'e': {
                        if(!haveDigged){
                            sisw.println("DIG R");
                            try{Thread.sleep(10);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                            sisw.println("SET_DIG_R FALSE");
                            this.haveDigged = true;
                        }
                        break;
                    }
                    case 'z': {sisw.println("SET_UP TRUE");break;}
                    case 'q': {sisw.println("SET_LEFT TRUE");break;}
                    case 's': {sisw.println("SET_DOWN TRUE");break;}
                    case 'd': {sisw.println("SET_RIGHT TRUE");break;}
                    case 27 : {sisw.println("STOP");System.exit(0);endGame = true;break;}
                    

                }
            }catch(Exception ex){System.out.println(ex + "class SendKeyListener method keyPressed");}
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            try{
                switch(e.getKeyChar()){
                    case 'z': {sisw.println("SET_UP FALSE");break;}
                    case 'q': {sisw.println("SET_LEFT FALSE");break;}
                    case 's': {sisw.println("SET_DOWN FALSE");break;}
                    case 'd': {sisw.println("SET_RIGHT FALSE");break;}
                    case 'a': {
                        sisw.println("SET_DIG_L FALSE");
                        this.haveDigged = false;
                        break;
                    }
                    case 'e': {
                        sisw.println("SET_DIG_R FALSE");
                        this.haveDigged = false;
                        break;
                    }
                }
            }catch(Exception ex){System.out.println(ex + "class SendkeyListener method keyreleased");}
        }
    }
}
