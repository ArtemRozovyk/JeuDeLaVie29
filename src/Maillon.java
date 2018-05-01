public class Maillon<T extends Comparable<T>> implements Comparable {

    private T info;
    private Maillon suiv;


    public Maillon(T info){
        this.info=info;
        suiv = null;
    }

    public void setSuivant(Maillon i){
        this.suiv = i;
    }

    public T getInfo() {
        return info;
    }

    public Maillon getSuiv() {
        return suiv;
    }


    @Override
    public int compareTo(Object o) {

        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        return info.compareTo(((Maillon<T>)o).getInfo());
    }
}
