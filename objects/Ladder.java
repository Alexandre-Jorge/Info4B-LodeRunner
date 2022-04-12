package objects;


public class Ladder extends Object {
    public Ladder(){
        super('H');
    }
    public Ladder(int x, int y){
        super('H', x, y);
    }
    public Ladder(int x, int y, boolean hide){
        super('H', x, y, hide);
    }
}
