/**
 * Utilise comme vecteur
 */
public class Couple {

    private int y;
    private int x;

    /**
     * Initialise l'objet à partir de deux entiers
     * @param ligne
     * @param colonne
     */
    public Couple(int ligne, int colonne){
        this.y = ligne;
        this.x=colonne;
    }

    /**
     *
     * @return l'élément y
     */
    public int getLigne(){
        return this.y;
    }

    /**
     *
     * @return l'élément x
     */
    public int getColonne(){
        return this.x;
    }


    @Override
    public String toString(){
        return "("+this.y+","+this.x+")";
    }
}
