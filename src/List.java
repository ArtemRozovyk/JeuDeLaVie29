public class List<T extends Comparable<T>> {

    Maillon<T> tete;





    public List(){
        tete = null;
    }

    public List copyOf(){
        List l = new List();
        Maillon a = tete;
        while(a!=null){
            l.addMaillon(a);
            a=a.getSuiv();
        }
        return l;
    }

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
