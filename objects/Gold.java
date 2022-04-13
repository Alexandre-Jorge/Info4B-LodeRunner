package objects;

//objet représentant les pieces ou tresors a ramasser
public class Gold extends Object {
    public Gold(){
        super('$');
    }
    public Gold(int x, int y){
        super('$', x, y);
    }
    public Gold(int x, int y, boolean hide){
        super('$', x, y, hide);
    }
}
