package objects;


public class zipline extends object{
    public zipline(){
        super('_');
    }
    public zipline(int x, int y){
        super('_', x, y);
    }
    public zipline(int x, int y, boolean hide){
        super('_', x, y, hide);
    }
}
