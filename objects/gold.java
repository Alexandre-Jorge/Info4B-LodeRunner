package objects;

public class gold extends object{
    public gold(){
        super('$');
    }
    public gold(int x, int y){
        super('$', x, y);
    }
    public gold(int x, int y, boolean hide){
        super('$', x, y, hide);
    }
}
