/**
 * La Class de la Liste générique
 * <p>Une List est caractérisé par :
 * <ul>
 *     <li>Sa tête, premier Maillon de la List</li>
 * </ul>
 * </p>
 *
 * @param <T> la class des éléments de la List
 * @author Caillot M. Rozovyk A.
 */
public class List<T extends Comparable<T>> {
    /**
     * Tête de la List
     *
     * <p>Aller voir la class Maillon pour plus d'informations</p>
     * @see Maillon
     */
    Maillon<T> tete;

    /**
     * Constructeur par défault
     * <p>La tête est mis a null à sa création</p>
     * @see List#tete
     */
    public List(){
        tete = null;
    }

    /**
     * Renvoie une copie de la List
     * @return la List copié
     */
    public List copyOf(){
        List l = new List();
        Maillon a = tete;
        while(a!=null){
            l.addMaillon(a);
            a=a.getSuiv();
        }
        return l;
    }

    /**
     * Permet d'ajouter un Maillon à la List
     *
     * <p>Chaque Maillon est inséré dans sa place en respectant l'ordre de la liste</p>
     *
     * @param maillon Maillon a ajouté dans la List
     */
    public void addMaillon(Maillon maillon){
        if(tete == null){
            tete = maillon;
        }
        else{
            if(maillon.getInfo().compareTo(tete.getInfo())==-1){
                maillon.setSuivant(tete);
                tete = maillon;
            }else{
                int comp=-1;
                Maillon a =tete.getSuiv();
                Maillon b = tete;
                while(a!=null&&((comp = maillon.compareTo(a))>0)){
                    a =a.getSuiv();
                    b = b.getSuiv();

                }
                if(comp != 0) {
                    maillon.setSuivant(a);
                    b.setSuivant(maillon);
                }
            }
        }
    }

    /**
     * Renvoie le String de la List
     * @return renvoie la List sous forme d'une chaîne de caractères
     */
    @Override
    public String toString(){
        String s = "[";
        Maillon a = tete;
        while(a!=null) {
            if (a.getSuiv() == null) {
                s += a.getInfo().toString();
            } else {
                s += a.getInfo().toString() + ",";
            }
            a = a.getSuiv();
        }
        s+="]";
        return s;
    }


}
