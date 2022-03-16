package objects;

public class enemy extends character{
    //attributs
    private Boolean bot;
    //constructeurs
    //
    //par defaut
    public enemy(){
        super('X');
        this.bot = false;
    }
    //standard 1
    public enemy(int x, int y){
        super('X', x, y);
        this.bot = false;
    }
    //standard 2
    public enemy(int x, int y, boolean b){
        super('X', x, y);
        this.bot = b;
    }
    //methodes
    //
    //getteurs
    public boolean isBot() {return this.bot;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
}
