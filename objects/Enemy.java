package objects;
import java.util.*;

//objet representant un enemi
public class Enemy extends Character{
    //attributs
    private Boolean bot;//si l'enemi est un bot
    private Player target;//definie quelle joueur l'enemi va poursuire (si c'est un bot)
    //constructeurs
    //
    //standard 1
    public Enemy(int x, int y, PlayGround pg, boolean human){
        super('X', x, y, pg,human);
        this.bot = !human;
    }
    //standard 3
    public Enemy(int x, int y, Player p, PlayGround pg, boolean human){
        super('X', x, y, pg, human);
        this.bot = !human;
        this.target = p;
    }
    //methodes
    //
    //getteurs
    public boolean isBot()    {return this.bot;}
    public Player  getTarget(){return this.target;}
    //setteurs
    public void setIsBot(boolean b) {this.bot = b;}
    public void setTarget(Player p) {this.target = p;}
    //others
    public void die(){//fait mourir l'enemi
        setInHole(false);//on dit qu'il n'est plus dans un trou
        setY(1);//on le place tout en haut du PlayGround
        setX((int)(Math.random()*98+1));//on le place a aleatoirement en x
    }
    public void updateEnemy(){//met a jour les attributs de l'enemi (appeler a chaque frame)
        updateCharacter();//appel la methode qui met a jour les attributs commun aux differents types de personnage
        if(getPlayGround().getDisplayTab()[getX()][getY()].getType()=='#' && !isInHole()){//si on est pas dans deja dans un trou et que la case ou le se trouve est de type '#' (sol)
            setInHole(true);//on dit qu'on est dans un trou
            getPlayGround().escapeFromHole(this);//on appelle la methode qui fait sortir l'enemi du trou (cette methode fais appel a un timer)
        }
    }
    
    @Override
    public void run(){
        if(isBot()){//si c'est un bot
            System.out.println("enemy controlled by an AI");//on affiche que l'enemi est controlé par l'IA
            AI ai = new AI(40);//on instancie l'IA avec une profondeur de 40 (l'IA calcul 4*(3^40) possibilitées (au maximum, c'est souvent moins))
            ai.start();//on lance l'IA
        }
        else {//si c'est un humain
            System.out.println("enemy controlled by a player");//on affiche que l'enemi est controlé par un vrai joueur
            Control ctrl = new Control();//on instancie la class control (qui gère les deplacement de l'enemi)
            ctrl.start();//on lance le control
        }
        while(!getPlayGround().isEndGame()){//tant que le jeu n'est pas fini
            updateEnemy();//on met a jour l'enemi
        }
    }
    class AI extends Thread{//classe qui gere l'IA
        int depth;//profondeur de l'IA
        public AI(int depth){
            this.depth = depth;
        }
        private Double tryMove(char dir, int pos[],int depth){//fonction recursive qui test autant de deplacement que la profondeur demande
            int[] tryPos = {pos[0],pos[1]};//on cree un tableau qui contient les coordonnes de la case ou l'on va tester (utiliser directement pos[] crée un bug, car modifie directment la postion)
            Double dist = Math.sqrt(Math.pow(getTarget().getX()-tryPos[0],2)+Math.pow(getTarget().getY()-tryPos[1],2));//on calcule la distance entre la position de l'enemi et la position du joueur qu'il suit
            boolean[] tmpArrayOfBooleans = updateCharacter(tryPos);//tableau de booleen pour savoir si la position pos[] est sur le sol, une echelle ... (ordre des booleens comme ci-dessous)
            //[onFloor, onTopOfLadder, onLadder, onZipline, wallOnLeft, wallOnRight]
            if(!(depth < 1 || dist == 0)){//si on est pas encore a la profondeur demande ou si on est pas deja sur joueur que l'on suit
                if(dir=='L'){//si la direction testé est à gauche
                    if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[4]){//on verifie que le deplacement à gauche est possible
                        tryPos[0]--;//on va à gauche
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};//on test tout les deplacements sauf celui d'où on vient avec une profondeur -1
                        //on place le resultat dans un tableau
                        List<Double> trys = Arrays.asList(array);//convertit le tableau en liste pour acceder certaines methodes
                        dist = Collections.min(trys);//on prend le minimum des resultats
                    }
                }
                else if(dir=='R'){//idem mais a droite
                    if((tmpArrayOfBooleans[0] || tmpArrayOfBooleans[1] || tmpArrayOfBooleans[2] || tmpArrayOfBooleans[3]) && !tmpArrayOfBooleans[5]){
                        tryPos[0]++;
                        Double[] array = {tryMove('R', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        dist = Collections.min(trys);
                    }
                }
                else if(dir=='U'){//idem mais en haut
                    if(tmpArrayOfBooleans[2] && tryPos[1]>0){
                        tryPos[1]--;
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('U',tryPos,depth-1),tryMove('R',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        dist = Collections.min(trys);
                    }
                }
                else if(dir=='D'){//idem mais en bas
                    if(((tmpArrayOfBooleans[2] && !tmpArrayOfBooleans[0]) || tmpArrayOfBooleans[3] || tmpArrayOfBooleans[1]) && tryPos[1]<39){
                        tryPos[1]++;
                        Double[] array = {tryMove('L', tryPos,depth-1),tryMove('R',tryPos,depth-1),tryMove('D',tryPos,depth-1)};
                        List<Double> trys = Arrays.asList(array);
                        dist = Collections.min(trys);
                    }
                }
            }
            return dist;//on revoie la distance courante si on est a la profondeur demande ou si on est sur le joueur que l'on suit
            //sinon cela renvoie la plus petite distance trouvée sur la direction demandée
        }
        @Override
        public void run(){
            /*boolean moved;
            while(getTarget().getLives()>0){
                try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}
                moved = false;
                if(!isInHole()){
                    if (isOnFloor() || isOnTopOfLadder()){
                        if(isOnLadder()){
                            if (getY()<getTarget().getY()) {moved = goDown();}
                            else if (getY()>getTarget().getY()){moved = goUp();}
                        }
                        if((isOnFloor() || isOnTopOfLadder()) && !moved){
                            if(getX()<getTarget().getX()) {moved = goRight();}
                            else if (getX()>getTarget().getX()){moved = goLeft();}
                        }
                    }
                    if((isOnLadder() || isOnTopOfLadder())&& !moved){
                        if (getY()<getTarget().getY()) {moved = goDown();}
                        else if (getY()>getTarget().getY()){moved = goUp();}
                    }
                    if(isOnZipline() && !moved){
                        if(getX()<getTarget().getX()) {moved = goRight();}
                        else if (getX()>getTarget().getX()){moved = goLeft();}
                        else if(getY()<getTarget().getY()){moved = goDown();}
                    }
                }
            }*/
            boolean moved;//booleen qui indique qu'un mouvement a deja ete effectue ou non
            while(!getPlayGround().isEndGame()){//tant que le jeu n'est pas fini
                try{Thread.sleep(100);}catch(InterruptedException e){System.out.println(e);}//on attend 100ms, definie la vitesse de l'IA (inferieur à celle d'un Player)
                int[] tryPos = {getX(),getY()};//on cree un tableau qui contient les coordonnes de la positon de l'enemi
                Double[] array = {tryMove('L', tryPos,this.depth),tryMove('R',tryPos,this.depth),tryMove('D',tryPos,this.depth), tryMove('U', tryPos,this.depth)};//on test tout les deplacements avec une profondeur de this.depth
                //on place le resultat dans un tableau
                ArrayList<Double> trys =new ArrayList<>(Arrays.asList(array));//convertit le tableau en ArrayList pour acceder certaines methodes
                moved = false;//on dit qu'aucun mouvement n'a ete effectue
                while(!moved){//tant que aucun mouvement n'a ete effectue
                    int toDo = trys.indexOf(Collections.min(trys));//on prend l'index du minimum des resultats des test
                    switch(toDo) {
                        case 0:{//si l'indice est 0, donc que le test était a gauche
                            moved = goLeft();//on va à gauche
                            trys.set(0, 1000.0);//pour être sur que l'élément soit le plus grand, dans le cas où goLeft() renvoie false
                            break;
                        }
                        case 1:{//idem mais a droite
                            moved = goRight();
                            trys.set(1, 1000.0);
                            break;
                        }
                        case 2:{//idem mais en bas
                            moved = goDown();
                            trys.set(2, 1000.0);
                            break;
                        }
                        case 3:{//idem mais en haut
                            moved = goUp();
                            trys.set(3, 1000.0);
                            break;
                        }
                    }
                }
            }
        }
    }
    
}
