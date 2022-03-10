import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

class playGround{
    //attributs
    private object displayTab[][];
    private int sizeX;
    private int sizeY;
    private player player1;
    private Frame frame = new Frame("Lode Runner");
    private TextArea txt;
    private MykeyListener keylistener = new MykeyListener(); 
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
                    if(line.charAt(i)=='O'){
                        this.player1 = new player("player1");
                        displayTab[i][j] = this.player1;
                    }
                    else displayTab[i][j] = new object(""+line.charAt(i));
                }
                j++;
            }
        }catch(Exception e){System.out.println(e);}
    }
    //methodes
    //
    //getteurs
    public player getPlayer1(){return this.player1;}
    public void update(){
        
    }
    //toString
    public void display(){
        String res = "";
        for(int j=0;j<this.sizeY;j++){
            for(int i=0;i<this.sizeX;i++){
                res+=this.displayTab[i][j];
            }
            res+='\n';
        }
        this.txt.setText(res);
    }

}

class MykeyListener implements KeyListener{
    @Override
	public void keyTyped(KeyEvent e)
	{
		System.out.println("The key Typed was: " + e.getKeyChar());
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.isActionKey())
			System.exit(0);
		System.out.println("The key Pressed was: " + e.getKeyChar());
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
		System.out.println("The key Released was: " + e.getKeyChar());
	}
}