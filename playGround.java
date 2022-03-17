import java.io.*;
import java.util.*;
import java.awt.*;
import objects.*;

class playGround{
    //attributs
    private object displayTab[][];
    private int sizeX, sizeY, golds=0;
    private player player1;
    private enemy enemy1;
    private Runnable RunPlayer1, RunEnemy1;
    private Thread ThPlayer1, ThEnemy1;
    private Frame frame = new Frame("Lode Runner");
    private TextArea txt, info;
    private ArrayList<int[]> exitPos;
    private ArrayList<int[]> goldsPos;
    //constructeurs
    //
    //init
    public void init(){
        this.txt.setFocusable(false);
        this.txt.setFont(new Font("Monospaced",Font.BOLD, 12));
        this.info.setFocusable(false);
        this.frame.setLayout(new FlowLayout());
        this.frame.add(this.txt);
        this.frame.add(this.info);
        this.frame.setVisible(true);
        this.frame.addKeyListener(this.player1.getKeyListener());
        this.enemy1.setTarget(this.player1);
    }
    //standard
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new object[sizeX][sizeY];
        this.exitPos = new ArrayList<int[]>();
        this.goldsPos = new ArrayList<int[]>();
        this.txt = new TextArea(sizeY, sizeX);
        this.info = new TextArea(2,20);
        this.frame.setSize(1000,800);
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    switch(line.charAt(i)){
                        case 'O':{
                            this.player1 = new player("player1",i,j);
                            this.RunPlayer1 = this.player1;
                            this.ThPlayer1 = new Thread(this.RunPlayer1);
                            displayTab[i][j] = new object(' ');
                            break;
                        }
                        case '$':{
                            displayTab[i][j] = new gold(i,j);
                            this.golds++;
                            int[] tmp = {i,j};
                            this.goldsPos.add(tmp);
                            break;
                        }
                        case 'X':{
                            this.enemy1 = new enemy(i,j,true);
                            this.RunEnemy1 = this.enemy1;
                            this.ThEnemy1 = new Thread(this.RunEnemy1);
                            displayTab[i][j] = new object(' ');
                            break;
                        }
                        case 'H':{
                            displayTab[i][j] = new ladder(i,j);
                            break;
                        }
                        case 'h':{
                            displayTab[i][j] = new ladder(i,j,true);
                            int[] tmp = {i,j};
                            this.exitPos.add(tmp);
                            break;
                        }
                        case '#':{
                            displayTab[i][j] = new floor(i,j);
                            break;
                        }
                        case '_':{
                            displayTab[i][j] = new zipline(i,j);
                            break;
                        }    
                        default : {displayTab[i][j] = new object(line.charAt(i));}
                    }
                }
                j++;
            }
            init();
            this.ThPlayer1.start();
            this.ThEnemy1.start();
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    //
    //getteurs
    public object[][]   getDisplayTab(){return this.displayTab;}
    public int          getSizeX(){return this.sizeX;}
    public int          getSizeY(){return this.sizeY;}
    public int          getGolds(){return this.golds;}
    public player       getPlayer1(){return this.player1;}
    public enemy        getEnemy1(){return this.enemy1;}
    public Runnable     getRunPlayer1(){return this.RunPlayer1;} 
    public Runnable     getRunEnemy1(){return this.RunEnemy1;}
    public Thread       getThPlayer1(){return this.ThPlayer1;}
    public Thread       getThEnemy1(){return this.ThEnemy1;}
    public Frame        getFrame(){return this.frame;}
    public TextArea     getTxt(){return this.txt;}
    public TextArea     getInfo(){return this.info;}

    //others
    public void resetPos(){
        this.player1.goInit();
        this.player1.setGold(0);
        this.enemy1.goInit();
        for(int i=0;i<this.goldsPos.size();i++){
            this.displayTab[this.goldsPos.get(i)[0]][this.goldsPos.get(i)[1]].setHidden(false);
        }
        showExit(false);
    }
    public void showExit(boolean b){
        for(int i=0;i<this.exitPos.size();i++){
            this.displayTab[this.exitPos.get(i)[0]][this.exitPos.get(i)[1]].setHidden(!b);
        }
    }
    public void updateCharacter(character c){
        char pos0 = this.displayTab[c.getX()][c.getY()+1].getType();
        char pos1 = this.displayTab[c.getX()][c.getY()].getType();
        //deplacement
        if(pos0 =='#'|| pos0=='X') c.setOnFloor(true);
        else c.setOnFloor(false);
        if(pos1=='H') c.setOnLadder(true);
        else c.setOnLadder(false);
        if(pos1=='_') c.setOnZipLine(true);
        else c.setOnZipLine(false);
        if(pos1==' ' && pos0=='H') c.setOnTopOfLadder(true);
        else c.setOnTopOfLadder(false);
        c.fall();
    }
    public void updatePlayer(player p){
        updateCharacter(p);
        if(this.displayTab[p.getX()][p.getY()].getType()=='$' && !this.displayTab[p.getX()][p.getY()].isHidden()){
            p.setGold(p.getGold()+1);
            this.displayTab[p.getX()][p.getY()].setHidden(true);
            if(p.getGold()==this.golds){
                showExit(true);
            }
        }
        else if(p.getX()==this.enemy1.getX() && p.getY()==this.enemy1.getY()){
            p.setLives(p.getLives()-1);
            resetPos();
        }
        if(p.getY()==1){
            p.setEnd(true);
            this.info.setText("YOU WIN !");
            System.out.println(this.player1.getName()+" won");
        }
    }
    public void updateEnemy(enemy e){
        updateCharacter(e);
    }
    //toString
    public void display(){
        updatePlayer(this.player1);
        updateEnemy(this.enemy1);
        String res = "";
        for(int j=0;j<this.sizeY;j++){
            for(int i=0;i<this.sizeX;i++){
                if(this.player1.getX()==i && this.player1.getY()==j){
                    res+=this.player1;
                }
                else if(this.enemy1.getX()==i && this.enemy1.getY()==j){
                    res+=this.enemy1;
                }
                else res+=this.displayTab[i][j];
            }
            res+='\n';
        }
        this.txt.setText(res);
        this.info.setText("Lives = "+player1.getLives()+"\nGold = "+this.player1.getGold()+"/"+this.golds);
        // System.out.println("player pos = ("+this.player1.getX()+" , "+this.player1.getY()+") , onLadder :"+this.player1.isOnLadder()+" , topLadder : "+this.player1.isOnTopOfLadder());
    }
    
}