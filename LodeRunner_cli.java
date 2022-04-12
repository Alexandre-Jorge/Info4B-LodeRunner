import java.io.*;
import objects.*;
import java.net.*;


import java.awt.*;
import java.awt.event.*;


public class LodeRunner_cli{
    //attributs
    protected static final int HEIGHT = 40;
    protected static final int WIDTH = 100;
    protected static final int PORT = 8080;
    protected static String ADDR;
    protected static Socket clientSoc;
    protected static BufferedReader sisr;
    protected static PrintWriter sisw;

    public static boolean endGame = false;

    private static SendKeyListener skl;
    private static Frame frame;
    private static TextArea gamePlay, info;
    
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
            if(Args.length > 1){
                ADDR = Args[1];
            }
            else{
                ADDR = "localhost";
            }
            if(Args[0].equals("solo")){
                FileReader f = new FileReader("levels/solo/level1.txt");
                PlayGround pg = new PlayGround(f,WIDTH,HEIGHT);
                while(pg.getThPlayer(0).isAlive()){///////!\\\\\ A MODIFIER !!!
                    pg.display();
                }
                f.close();
            }
            else if(Args[0].equals("multi")){

                //init
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

                clientSoc = new Socket(ADDR, PORT);
                sisr = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
                sisw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSoc.getOutputStream())), true);
                String resp;
                while(!(resp = sisr.readLine()).equals("START")){
                    getGamePlay().setText(resp);
                }
                System.out.println("START");
                sisw.println("GO");
                String tmp;
                while(!endGame){///////!\\\\\ A MODIFIER !!!
                    resp=sisr.readLine();
                    if(resp.equals("GAMEPLAY")){
                        resp="";
                        while(!(tmp = sisr.readLine()).equals("END")){
                            resp+=tmp+'\n';
                        }
                        getGamePlay().setText(resp);
                    }
                    else if(resp.equals("INFO")){
                        resp="";
                        while(!(tmp = sisr.readLine()).equals("END")){
                            resp+=tmp+'\n';
                        }
                        getInfo().setText(resp);
                    }
                    else if(resp.equals("END_GAME")){
                        endGame = true;
                    }
                }
                sisr.close();
                sisw.close();
                clientSoc.close();
            }
            else{
                System.out.println("Usage : java LodeRunner_cli [solo|multi] [ip]");
            }
        }catch(Exception e){System.out.println(e);}
    }

    static class SendKeyListener implements KeyListener{
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
