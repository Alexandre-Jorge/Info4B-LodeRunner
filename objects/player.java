package objects;

public class player extends character{
    //attributs
    private String name;
    private int nbGold, lives;
    private boolean end = false;
    
    //constructeurs
    //
    //fonction init
    private void init(){
        this.nbGold    = 0;
        this.lives     = 5;
    }
    //par defaut
    public player(){
        super('O');
        init();
    }
    //standard 1
    public player(String name){
        super('O');
        init();
        this.name = name;
    }
    //standard 2
    public player(String name, int posX, int posY){
        super('O',posX,posY);
        init();
        this.name = name;
    }
    //methodes
    //
    //getteurs
    public String  getName()  {return this.name;}
    public int     getGold()  {return this.nbGold;}
    public int     getLives() {return this.lives;}
    public boolean isEnded()  {return this.end;}
    
    //setteurs
    public void setName(String s) {this.name = s;}
    public void setGold(int i)    {this.nbGold = Math.abs(i);}
    public void setLives(int i)   {this.lives = Math.abs(i);}
    //run
    @Override
    public void run(){
        while(!end){
            // System.out.println("ok");
            if(getLives()<1){
                System.out.print("X = "+getX()+" , Y = "+getY()+"\r");
                System.out.println("\nplus de vie !");
                this.end = true;

            }
            else{
                System.out.print("X = "+getX()+" , Y = "+getY()+"\r");
            }
        }
    }
    
}