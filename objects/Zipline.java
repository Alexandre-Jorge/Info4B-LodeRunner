package objects;

//objet représentant les tyrolennes
public class Zipline extends Object{
    public Zipline(){
        super('_');
    }
    public Zipline(int x, int y){
        super('_', x, y);
    }
    public Zipline(int x, int y, boolean hide){
        super('_', x, y, hide);
    }
}
