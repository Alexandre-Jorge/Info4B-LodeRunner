package objects;

public class floor extends object{
    public floor(){
        super('#');
    }
    public floor(int x, int y){
        super('#', x, y);
    }
    public floor(int x, int y, boolean hide){
        super('#', x, y, hide);
    }
}