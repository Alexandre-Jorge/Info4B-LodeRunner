package objects;

//objet représentant les sols et les mur du jeu
public class Floor extends Object {
    public Floor(){
        super('#');
    }
    public Floor(int x, int y){
        super('#', x, y);
    }
    public Floor(int x, int y, boolean hide){
        super('#', x, y, hide);
    }
}
