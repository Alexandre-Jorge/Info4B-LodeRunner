import java.io.*;
import java.awt.*;
import objects.*;

class playGround{
    //attributs
    private object displayTab[][];
    private int sizeX, sizeY;
    private player player1;
    private enemy enemy1;
    private Runnable RunPlayer1, RunEnemy1;
    private Thread ThPlayer1, ThEnemy1;
    private Frame frame = new Frame("Lode Runner");
    private TextArea txt, info;
    //protected MykeyListener keylistener = new MykeyListener(); 
    //constructeurs
    //
    public void init(){
        this.txt.setFocusable(false);
        this.txt.setFont(new Font("Monospaced",Font.BOLD, 12));
        this.info.setFocusable(false);
        this.frame.setLayout(new FlowLayout());
        this.frame.add(this.txt);
        this.frame.add(this.info);
        this.frame.setVisible(true);
        this.frame.addKeyListener(this.player1.getKeyListener());
    }
    //standard
    public playGround(FileReader f, int sizeX, int sizeY){
        int j=0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.displayTab = new object[sizeX][sizeY];
        this.txt = new TextArea(sizeY, sizeX);
        this.info = new TextArea(2,20);
        this.frame.setSize(850,670);
        //init();
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
                            break;
                        }
                        case 'X':{
                            this.enemy1 = new enemy(i,j,false);
                            this.RunEnemy1 = this.player1;
                            this.ThEnemy1 = new Thread(this.RunEnemy1);
                            displayTab[i][j] = new object(' ');
                            break;
                        }
                        case 'H':{
                            displayTab[i][j] = new ladder(i,j);
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
        this.enemy1.goInit();
    }
    public void updatePlayer1(){
        char pos0 = this.displayTab[this.player1.getX()][this.player1.getY()+1].getType();
        char pos1 = this.displayTab[this.player1.getX()][this.player1.getY()].getType();
        //deplacement
        if(pos0 =='#'|| pos0=='X') this.player1.setOnFloor(true);
        else this.player1.setOnFloor(false);
        if(pos1=='H' || pos0=='H') this.player1.setOnLadder(true);
        else this.player1.setOnLadder(false);
        if(pos1=='_') this.player1.setOnZipLine(true);
        else this.player1.setOnZipLine(false);
        player1.fall();

        //interraction
        if(pos1=='$' && !this.displayTab[this.player1.getX()][this.player1.getY()].isHidden()){
            this.player1.setGold(this.player1.getGold()+1);
            this.displayTab[this.player1.getX()][this.player1.getY()].setHidden(true);
        }
        else if(this.player1.getX()==this.enemy1.getX() && this.player1.getY()==this.enemy1.getY()){
            this.player1.setLives(this.player1.getLives()-1);
            resetPos();
        }
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
                else if(this.enemy1.getX()==i && this.enemy1.getY()==j){
                    res+=this.enemy1;
                }
                else res+=this.displayTab[i][j];
            }
            res+='\n';
        }
        this.txt.setText(res);
        this.info.setText("Lives = "+player1.getLives()+"\nGold = "+this.player1.getGold());
        //System.out.println("player pos = ("+this.player1.getX()+" , "+this.player1.getY()+") , onLadder :"+this.player1.isOnLadder()+" , onFloor : "+this.player1.isOnFloor());
    }
    
}