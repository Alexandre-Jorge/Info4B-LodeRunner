package objects;


public class ladder extends object{
    public ladder(){
        super('H');
    }
    public ladder(int x, int y){
        super('H', x, y);
    }
    public ladder(int x, int y, boolean hide){
        super('H', x, y, hide);
    }
}