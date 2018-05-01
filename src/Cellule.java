/**
 * La classe Cellule qui correspond à une cellule vivante dans le Jeu
 */
public class Cellule implements Comparable<Cellule> {

    private int ligne;
    private int colonne;
    private int nbvois;

    /**
     * Constructeur par défaut
     * @param ligne
     * @param colonne
     */
    public Cellule(int ligne, int colonne){
        this.ligne = ligne;
        this.colonne = colonne;
        this.nbvois = 1;
    }

    public Cellule(int ligne, int colonne,int nbVoisins){
        this(ligne,colonne);
        this.nbvois = nbVoisins;
    }

    public int getLigne(){
        return ligne;
    }

    public int getColonne(){
        return colonne;
    }

    @Override
    public String toString(){
        return "("+this.ligne+","+this.colonne+","+this.nbvois+")";
    }

    public int getNbvois() {
        return nbvois;
    }

    public void setNbvois(int nbvois) {
        this.nbvois = nbvois;
    }

    @Override
    public int compareTo(Cellule o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        if(o.getLigne() < this.ligne)return 1;
        if(ligne == o.getLigne()) {
            if (o.getColonne() < colonne)
                return 1;
            else
                return (o.getColonne() == colonne ? 0 : -1);
        }
        return -1;
    }
}
