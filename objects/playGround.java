package objects;

import java.io.*;
import java.util.*;


import java.awt.*;

public class playGround implements Serializable{
    //attributs
    private object displayTab[][];
    private int sizeX, sizeY, golds=0;
    private ArrayList<player> players;
    private ArrayList<enemy> enemys;
    private ArrayList<Runnable> runPlayers;
    private ArrayList<Runnable> runEnemys;
    private ArrayList<Thread> thPlayers;
    private ArrayList<Thread> thEnemys;
    private Frame frame = new Frame("Lode Runner");
    private TextArea gamePlay, info;
    private ArrayList<int[]> exitPos;
    private ArrayList<int[]> goldsPos;

    //constructeurs
    //
    //init
    public void init(){
        this.gamePlay.setFocusable(false);
        this.gamePlay.setFont(new Font("Monospaced",Font.BOLD, 12));
        this.info.setFocusable(false);
        this.frame.setLayout(new FlowLayout());
        this.frame.add(this.gamePlay);
        this.frame.add(this.info);
        this.frame.setVisible(true);
        for(int i=0;i<this.players.size();i++)
            this.frame.addKeyListener(this.players.get(i).getKeyListener());
        for(int i=0;i<this.enemys.size();i++)
            this.enemys.get(i).setTarget(this.players.get(i%this.players.size()));
    }
    //standard mode solo
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new object[sizeX][sizeY];
        this.exitPos = new ArrayList<int[]>();
        this.goldsPos = new ArrayList<int[]>();
        this.enemys = new ArrayList<enemy>();
        this.players = new ArrayList<player>();
        this.runEnemys = new ArrayList<Runnable>();
        this.runPlayers = new ArrayList<Runnable>();
        this.thEnemys = new ArrayList<Thread>();
        this.thPlayers = new ArrayList<Thread>();
        this.gamePlay = new TextArea(sizeY, sizeX);
        this.info = new TextArea(2,20);
        this.frame.setSize(1000,800);
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    switch(line.charAt(i)){
                        case 'O':{
                            this.players.add(new player("player",i,j, this, true));
                            this.runPlayers.add(this.players.get(this.players.size()-1));
                            this.thPlayers.add(new Thread(this.runPlayers.get(this.runPlayers.size()-1)));
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
                            this.enemys.add(new enemy(i,j,true,this,true));
                            this.runEnemys.add(this.enemys.get(this.enemys.size()-1));
                            this.thEnemys.add(new Thread(this.runEnemys.get(this.runEnemys.size()-1)));
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
            for(int i=0;i<this.thPlayers.size();i++)
                this.thPlayers.get(i).start();
            for(int i=0;i<this.thEnemys.size();i++)
                this.thEnemys.get(i).start();
        }catch(Exception e){System.out.println(e);}
    }
    //standard mode multi
    public playGround(FileReader f, int sizeX, int sizeY,int nbPlayer, int nbEnemy){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new object[sizeX][sizeY];
        this.exitPos = new ArrayList<int[]>();
        this.goldsPos = new ArrayList<int[]>();
        this.enemys = new ArrayList<enemy>();
        this.players = new ArrayList<player>();
        this.runEnemys = new ArrayList<Runnable>();
        this.runPlayers = new ArrayList<Runnable>();
        this.thEnemys = new ArrayList<Thread>();
        this.thPlayers = new ArrayList<Thread>();
        this.gamePlay = new TextArea(sizeY, sizeX);
        this.info = new TextArea(2,20);
        this.frame.setSize(1000,800);
        try{
            BufferedReader buf = new BufferedReader(f) ;
            String line;
            while((line = buf.readLine()) != null){
                for(int i=0;i<sizeX;i++){
                    switch(line.charAt(i)){
                        case 'O':{
                            System.out.println("players.size < nbPlayer => "+this.players.size()+" < "+nbPlayer);
                            if(this.players.size()<nbPlayer){
                                this.players.add(new player("player",i,j, this,false));
                                this.runPlayers.add(this.players.get(this.players.size()-1));
                                this.thPlayers.add(new Thread(this.runPlayers.get(this.runPlayers.size()-1)));
                                displayTab[i][j] = new object(' ');
                            }
                            else{
                                displayTab[i][j] = new object(' ');
                            }
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
                            System.out.println("enemys.size < nbEnemy => "+this.enemys.size()+" < "+nbEnemy);
                            if(this.enemys.size()<nbEnemy){
                                this.enemys.add(new enemy(i,j,false,this,false));
                            }
                            else{
                                this.enemys.add(new enemy(i,j,true,this,false));
                            }
                            this.runEnemys.add(this.enemys.get(this.enemys.size()-1));
                            this.thEnemys.add(new Thread(this.runEnemys.get(this.runEnemys.size()-1)));
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
            for(int i=0;i<this.thPlayers.size();i++)
                this.thPlayers.get(i).start();
            for(int i=0;i<this.thEnemys.size();i++)
                this.thEnemys.get(i).start();
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    //
    //getteurs
    public object[][]           getDisplayTab()    {return this.displayTab;}
    public int                  getSizeX()         {return this.sizeX;}
    public int                  getSizeY()         {return this.sizeY;}
    public int                  getGolds()         {return this.golds;}
    public player               getPlayer(int i)   {return this.players.get(i);}
    public enemy                getEnemy(int i)    {return this.enemys.get(i);}
    public ArrayList<player>    getPlayers()       {return this.players;}
    public ArrayList<enemy>     getEnemys()        {return this.enemys;}
    public Runnable             getRunPlayer(int i){return this.runPlayers.get(i);}
    public Runnable             getRunEnemy(int i) {return this.runEnemys.get(i);}
    public ArrayList<Runnable>  getRunPlayers()    {return this.runPlayers;} 
    public ArrayList<Runnable>  getRunEnemys()     {return this.runEnemys;}
    public Thread               getThPlayer(int i) {return this.thPlayers.get(i);}
    public Thread               getThEnemy(int i)  {return this.thEnemys.get(i);}
    public ArrayList<Thread>    getThPlayers()     {return this.thPlayers;}
    public ArrayList<Thread>    getThEnemys()      {return this.thEnemys;}
    public Frame                getFrame()         {return this.frame;}
    public TextArea             getGamePlay()      {return this.gamePlay;}
    public TextArea             getInfo()          {return this.info;}
    public int[]                getGoldPos(int i)  {return this.goldsPos.get(i);}
    public int[]                getExitPos(int i)  {return this.exitPos.get(i);}
    public ArrayList<int[]>     getGoldsPos()      {return this.goldsPos;}
    public ArrayList<int[]>     getExitPos()       {return this.exitPos;}

    //others
    public void resetPos(){
        for(int i=0;i<getPlayers().size();i++){
            getPlayer(i).goInit();
            getPlayer(i).setGold(0);
        }
        for(int i=0;i<getEnemys().size();i++)
            getEnemy(i).goInit();
        for(int i=0;i<getGoldsPos().size();i++){
            getDisplayTab()[getGoldPos(i)[0]][getGoldPos(i)[1]].setHidden(false);
        }
        showExit(false);
    }
    
    public void showExit(boolean b){
        for(int i=0;i<getExitPos().size();i++){
            getDisplayTab()[getExitPos(i)[0]][getExitPos(i)[1]].setHidden(!b);
        }
    }
    
    public void escapeFromHole(enemy e){
        chronoToEscape cte = new chronoToEscape(e, 3500);
        cte.start();
    }
    public void updateHole(floor f){
        for(int i=0;i<getPlayers().size();i++){
            if((getPlayer(i).getDigL() && (getPlayer(i).getX()==f.getX()+1 && getPlayer(i).getY()==f.getY()-1)) || (getPlayer(i).getDigR() && (getPlayer(i).getX()==f.getX()-1 && getPlayer(i).getY()==f.getY()-1))){
                f.setHidden(true);
                resealHole(f);
            }
        }
    }
    public void resealHole(floor f){
        chronoToShow cts = new chronoToShow(f,8000);
        cts.start();
    }
    
    public boolean enemyHere(int x, int y){
        for(int i=0;i<getEnemys().size();i++){
            if(getEnemy(i).getX()==x && getEnemy(i).getY()==y)return true;
        }
        return false;
    }
    public boolean playerHere(int x, int y){
        for(int i=0;i<getPlayers().size();i++){
            if(getPlayer(i).getX()==x && getPlayer(i).getY()==y)return true;
        }
        return false;
    }
    //toString
    String res = "";
    public void display(){
        //if(getPlayer1().isEnded())return;
        res = "";
        for(int j=0;j<getSizeY();j++){
            for(int i=0;i<getSizeX();i++){
                if(playerHere(i, j)){
                    res+=getPlayer(0).toString();//just concat the toString of a player don't matter if it's the good player
                }
                else if(enemyHere(i, j)){
                    res+=getEnemy(0).toString();//just concat the toString of a enemy don't matter if it's the good enemy
                }
                else res+=getDisplayTab()[i][j];
            }
            res+='\n';
        }
        getGamePlay().setText(res);
        getInfo().setText("t");
        for(int i=0;i<getPlayers().size();i++){
            getInfo().append("player : "+getPlayer(i)+"\n");
            getInfo().append("Lives = "+getPlayer(i).getLives()+"\nGold = "+getPlayer(i).getGold()+"/"+getGolds()+"\n");
        }
    }
    public String displayToSend(){
        res = "";
        for(int j=0;j<getSizeY();j++){
            for(int i=0;i<getSizeX();i++){
                if(playerHere(i, j)){
                    res+=getPlayer(0).toString();//just concat the toString of a player don't matter if it's the good player
                }
                else if(enemyHere(i, j)){
                    res+=getEnemy(0).toString();//just concat the toString of a enemy don't matter if it's the good enemy
                }
                else res+=getDisplayTab()[i][j];
            }
            res+='\n';
        }
        return res;
    }
    class chrono extends Thread{
        protected object o;
        protected int  time;
        public chrono(object o){
            this.o = o;
            time = 4000;
        }
        public chrono(object o, int t){
            this.o = o;
            this.time = t;
        }
        public object getO(){return this.o;}
        public int getTime(){return this.time;}

        public void setO(object o){this.o = o;}
        public void setTime(int i){this.time = i;}
        @Override
        public void run(){};
        
    }
    class chronoToShow extends chrono{
        public chronoToShow(object o){
            super(o);
        }
        public chronoToShow(object o, int t){
            super(o,t);
        }

        @Override
        public void run(){
            try{Thread.sleep(getTime());}catch(InterruptedException e){System.out.println(e + "class chronoToShow, methode run");}
            this.o.setHidden(false);
            for(int i=0;i<getEnemys().size();i++){
                if(getEnemy(i).getX()==o.getX() && getEnemy(i).getY()==o.getY())
                    getEnemy(i).die();
            }
        }
    }
    class chronoToEscape extends chrono{
        enemy e;
        public chronoToEscape(enemy e){
            super(e);
            this.e = e;
        }
        public chronoToEscape(enemy e, int t){
            super(e,t);
            this.e = e;
        }

        @Override
        public void run(){
            try{Thread.sleep(getTime());}catch(InterruptedException e){System.out.println(e + "class chronoToEscape, methode run");}
            if(e.getTarget().getX()<e.getX()){
                e.setY(e.getY()-1);
                e.setX(e.getX()-1);
            }
            else{
                e.setY(e.getY()-1);
                e.setX(e.getX()+1);
            }
            this.e.setInHole(false);
        }
    }
    
    
}