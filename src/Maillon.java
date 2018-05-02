/**
 * Class du Maillon générique
 * <p>Un Maillon est caractérisé par :
 * <ul>
 *     <li>Son info, élément contenue dans le Maillon</li>
 *     <li>Son suiv, le prochain Maillon</li>
 * </ul>
 * </p>
 * @author Rozovyk A. Caillot M.
 * @param <T> généricité
 */


public class Maillon<T extends Comparable<T>> implements Comparable {
    /**
     * L'élément contenu dans le Maillon, non modifiable
     *
     * @see Maillon#getInfo()
     */
    private T info;

    /**
     * le Maillon suivant
     *
     * @see Maillon#getSuiv()
     * @see Maillon#setSuivant(Maillon)
     */
    private Maillon suiv;

    /**
     * Constructeur du Maillon
     * <p>Le Maillon suivant est mis a null à sa création</p>
     * @param info élément du Maillon
     *
     * @see Maillon#info
     * @see Maillon#suiv
     */
    public Maillon(T info){
        this.info=info;
        suiv = null;
    }

    /**
     * Permet de modifier le Maillon suivant
     * @param i Le nouveau Maillon suivant
     */
    public void setSuivant(Maillon i){
        this.suiv = i;
    }

    /**
     * Renvoie l'élément contenue dans le Maillon
     * @return renvoie l'élément du Maillon de class T
     */
    public T getInfo() {
        return info;
    }

    /**
     * Renvoie le Maillon suivant
     * @return renvoie le Maillon suivant
     */
    public Maillon getSuiv() {
        return suiv;
    }

    /**
     * Permet de comparer deux Maillon
     * <p>Appelle la fonction compareTo de l'élément contenue dans le Maillon</p>
     * @param o Maillon avec lequel on compare le Maillon a comparé(en objet)
     * @return renvoie un entier : 0,1 ou -1
     */
    @Override
    public int compareTo(Object o) {

        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        return info.compareTo(((Maillon<T>)o).getInfo());
    }
}
