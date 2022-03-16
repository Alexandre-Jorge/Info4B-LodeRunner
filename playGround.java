import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

class playGround{
    //attributs
    protected object displayTab[][];
    protected int sizeX;
    protected int sizeY;
    protected player player1;
    protected Frame frame = new Frame("Lode Runner");
    protected TextArea txt;
    protected MykeyListener keylistener = new MykeyListener(); 
    //constructeurs
    //
    public void init(){
        this.txt.setFocusable(false);
        this.txt.setFont(new Font("Monospaced",Font.BOLD, 12));
        this.frame.setLayout(new FlowLayout());
        this.frame.add(this.txt);
        this.frame.setVisible(true);
        this.frame.addKeyListener(this.keylistener);
    }
    //standard
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new object[sizeX][sizeY];
        this.txt = new TextArea(sizeY, sizeX);
        this.frame.setSize(850,650);
        init();
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    switch(line.charAt(i)){
                        case 'O':{
                            this.player1 = new player("player1",i,j);
                            displayTab[i][j] = new object(" ");
                            break;
                        }
                        /*case 'X':{
                            break;
                        }
                        case 'H':{
                            break;
                        }
                        case '$':{
                            break;
                        }*/
                        default : {displayTab[i][j] = new object(""+line.charAt(i));}
                    }
                }
                j++;
            }
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    //
    //getteurs
    public player getPlayer1(){return this.player1;}
    public void updatePlayer1(){
        char pos0 = this.displayTab[this.player1.getX()][this.player1.getY()+1].getChar();
        char pos1 = this.displayTab[this.player1.getX()][this.player1.getY()].getChar();
        if(pos0 =='#'|| pos0=='X') this.player1.setOnFloor(true);
        else this.player1.setOnFloor(false);
        if(pos1=='H' || pos0=='H') this.player1.setOnLadder(true);
        else this.player1.setOnLadder(false);
        if(pos1=='_') this.player1.setOnZipLine(true);
        else this.player1.setOnZipLine(false);
        player1.fall();
    }
    //toString
    public void display(){
        updatePlayer1();
        String res = "";
        for(int j=0;j<this.sizeY;j++){
            for(int i=0;i<this.sizeX;i++){
                if(this.player1.getX()==i && this.player1.getY()==j){
                    res+=this.player1;
                }
                else res+=this.displayTab[i][j];
            }
            res+='\n';
        }
        this.txt.setText(res);
        System.out.println("player pos = ("+this.player1.getX()+" , "+this.player1.getY()+") , onLadder :"+this.player1.isOnLadder()+" , onFloor : "+this.player1.isOnFloor());
    }
    class MykeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e)
        {
            switch(e.getKeyChar()){
                case 27 : {System.exit(0);break;}
                case 'z': {player1.goUp();break;}
                case 'q': {player1.goRight();break;}
                case 's': {player1.goDown();break;}
                case 'd': {player1.goLeft();break;}
            }
        }
        @Override
        public void keyPressed(KeyEvent e)
        {
            /*switch(e.getKeyChar()){
                case 27 : {System.exit(0);break;}
                case 'z': {player1.goUp();break;}
                case 'q': {player1.goRight();break;}
                case 's': {player1.goDown();break;}
                case 'd': {player1.goLeft();break;}
            }*/
            // System.out.println("The key Pressed was: " + e.getKeyChar());
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            // System.out.println("The key Released was: " + e.getKeyChar());
        }
    }
}