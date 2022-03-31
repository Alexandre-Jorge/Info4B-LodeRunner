import java.io.*;
import objects.*;
import java.net.*;


import java.awt.*;
import java.awt.event.*;


public class lodeRunner_cli{
    //attributs
    protected static final int HEIGHT = 40;
    protected static final int WIDTH = 100;
    protected static final int PORT = 8080;
    protected static InetAddress ADDR;
    
    public static boolean endGame = false;
    private SendKeyListener skl;
    private static Frame frame;
    private static TextArea gamePlay, info;
    //constructeurs
    //init
    private void init(){
        gamePlay.setFocusable(false);
        gamePlay.setFont(new Font("Monospaced",Font.BOLD, 12));
        info.setFocusable(false);
        frame.setLayout(new FlowLayout());
        frame.add(gamePlay);
        frame.add(info);
        frame.addKeyListener(skl);
        frame.setVisible(true);
        frame.setSize(1000, 800);
    }
    public lodeRunner_cli(){
        try{this.skl = new SendKeyListener();}catch(Exception ex){System.out.println(ex + "class lodeRunner_cli method init");}
        frame = new Frame("Lode Runner client");
        gamePlay = new TextArea(HEIGHT, WIDTH);
        info = new TextArea(2,20);
        init();
    }
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
    //others
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
    //main
    public static void main(String Args[]){
        new lodeRunner_cli();
        try{
            if(Args.length != 0){
                ADDR = InetAddress.getByName(Args[0]);
            }
            else{
                ADDR = InetAddress.getByName("localhost");
            }
            DatagramPacket respPacket;
            DatagramSocket clientSoc = new DatagramSocket();
            sendPacket(clientSoc, Args[1].getBytes(), ADDR, PORT);
            if(Args[1].equals("solo")){
                FileReader f = new FileReader("levels/solo/level1.txt");
                playGround pg = new playGround(f,WIDTH,HEIGHT);
                while(pg.getThPlayer(0).isAlive()){///////!\\\\\ A MODIFIER !!!
                    pg.display();
                    try{Thread.sleep(1000/50);}catch(InterruptedException e){System.out.println(e);}//50 fps
                }
                f.close();
            }
            else if(Args[1].equals("multi")){
                //respPacket = getPacket(clientSoc, 65535);
                sendPacket(clientSoc, "join".getBytes(), ADDR, PORT);
                while(!endGame){///////!\\\\\ A MODIFIER !!!
                    respPacket = getPacket(clientSoc, 65535);
                    getGamePlay().setText(new String(respPacket.getData()));
                    /*respPacket = getPacket(clientSoc, 65535);
                    getInfo().setText(new String(respPacket.getData()));*/
                    try{Thread.sleep(1000/50);}catch(InterruptedException e){System.out.println(e);}//50 fps
                }
            }
        }catch(Exception e){System.out.println(e);}
    }


    class SendKeyListener implements KeyListener{
        private DatagramSocket clientSoc;
        public SendKeyListener() throws Exception{
            this.clientSoc = new DatagramSocket();
        }
        @Override
        public void keyTyped(KeyEvent e)
        {
          
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            System.out.println("Key pressed = "+e.getKeyChar());
            try{
                switch(e.getKeyChar()){
                    case 'a': {
                        //dig('L');
                        sendPacket(clientSoc, "DIG L".getBytes(), ADDR, PORT);
                        try{Thread.sleep(30);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                        keyReleased(e);
                        break;
                    }
                    case 'e': {
                        // dig('R');
                        sendPacket(clientSoc, "DIG R".getBytes(), ADDR, PORT);
                        try{Thread.sleep(30);}catch(InterruptedException i){System.out.println(i + " class MyKeyListener methods keypressed");}
                        keyReleased(e);
                        break;
                    }
                    case 'z': {/*setUp(true)*/sendPacket(clientSoc, "SET_UP TRUE".getBytes(), ADDR, PORT);break;}
                    case 'q': {/*setLeft(true)*/sendPacket(clientSoc, "SET_LEFT TRUE".getBytes(), ADDR, PORT);break;}
                    case 's': {/*setDown(true)*/sendPacket(clientSoc, "SET_DOWN TRUE".getBytes(), ADDR, PORT);break;}
                    case 'd': {/*setRight(true)*/sendPacket(clientSoc, "SET_RIGHT TRUE".getBytes(), ADDR, PORT);break;}
                    case 27 : {System.exit(0);break;}
                    

                }
            }catch(Exception ex){System.out.println(ex + "class SendKeyListener method keyPressed");}
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            try{
                switch(e.getKeyChar()){
                    case 'z': {/*setUp(false)*/sendPacket(clientSoc, "SET_UP FALSE".getBytes(), ADDR, PORT);break;}
                    case 'q': {/*setLeft(false)*/sendPacket(clientSoc, "SET_LEFT FALSE".getBytes(), ADDR, PORT);break;}
                    case 's': {/*setDown(false)*/sendPacket(clientSoc, "SET_DOWN FALSE".getBytes(), ADDR, PORT);break;}
                    case 'd': {/*setRight(false)*/sendPacket(clientSoc, "SET_RIGHT FALSE".getBytes(), ADDR, PORT);break;}
                    case 'a': {/*setDigL(false)*/sendPacket(clientSoc, "SET_DIG_L FALSE".getBytes(), ADDR, PORT);break;}
                    case 'e': {/*setDigR(false)*/sendPacket(clientSoc, "SET_DIG_R FALSE".getBytes(), ADDR, PORT);break;}
                }
            }catch(Exception ex){System.out.println(ex + "class SendkeyListener method keyreleased");}
        }
    }
}