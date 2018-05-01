import java.util.Objects;

public class Couple implements Comparable{

    private int ligne;
    private int colonne;

    public Couple(int ligne, int colonne){
        this.ligne = ligne;
        this.colonne=colonne;
    }

    public int getLigne(){
        return this.ligne;
    }

    public int getColonne(){
        return this.colonne;
    }

    public int compareTo(Object o){
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        Couple other = (Couple) o;
        if(other.ligne < this.ligne)return 1;
        if(ligne == other.ligne) {
            if (other.colonne < colonne)
                return 1;
            else
                return (other.colonne == colonne ? 0 : -1);
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple couple = (Couple) o;
        return ligne == couple.ligne &&
                colonne == couple.colonne;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ligne, colonne);
    }

    @Override
    public String toString(){
        return "("+this.ligne+","+this.colonne+")";
    }
}
