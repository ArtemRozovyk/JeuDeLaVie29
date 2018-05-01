public class TypeDEvolution {

     static String detecter(String folder,String arg, int i) {
        Jeu jeu = new Jeu(folder+arg);

         String m=mort (jeu.getGrille(),i) ;
         String s=stable(jeu.getGrille(),i);
         String v=vaisseau(jeu.getGrille(),i);
         String o=oscillateur(jeu.getGrille(),i);
         if(!m.equals("")||!s.equals("")||!v.equals("")||!o.equals(""))
             return m+"\n"+s+"\n"+o+"\n"+v+"\n";
         else
             return "Inconnu";

    }
    /*
    Remarquer qu’un comportement du type
    mort est aussi du type stable,
    qu’un du type stable est du type oscillateur,
    qu’un du type oscillateur est du type vaisseau
     */

    private static String mort(List<Cellule> grille, int max) {
        Jeu testmort = new Jeu(grille.copyOf());
        int i = 0;
        while(i<max){
            if(testmort.getGrille().tete==null){
               return "<p>C'est mort après "+i+" gerenations</p>";
            }
            testmort.setGrille(Jeu.nextGen(testmort.getGrille()));
            i++;
        }

        return "";
    }



    public static String vaisseau(List grille, int max){
        int periode=0,queue=0;
        int i=0;
        Jeu unpas= new Jeu( grille.copyOf());
        Jeu deuxpas= new Jeu( grille.copyOf());
        do{
            unpas.setGrille(Jeu.nextGen(unpas.getGrille()));
            deuxpas.setGrille(Jeu.nextGen(deuxpas.getGrille()));
            deuxpas.setGrille(Jeu.nextGen(deuxpas.getGrille()));
            i++;
        }while(!diffVaiss(unpas.getGrille(),deuxpas.getGrille())&&i<max);

        do{
            deuxpas.setGrille(Jeu.nextGen(deuxpas.getGrille()));
            periode++;
        }while(!diffVaiss(unpas.getGrille(),deuxpas.getGrille())&&i<max);

        Jeu laqueue= new Jeu(grille.copyOf());
        while (!diffVaiss(laqueue.getGrille(),deuxpas.getGrille())&&i<max){
            laqueue.setGrille(Jeu.nextGen(laqueue.getGrille()));
            queue++;
        }
        if(i<max)
            return "<p>C'est vaisseau de periode: "+periode+" et de queue "+queue+"</p>";
        else
            return "";

    }




    public static String stable (List grille, int max){
        int i =0;
        int queue=0;
        Jeu p= new Jeu( grille.copyOf());
        Jeu d= new Jeu( grille.copyOf());
        do{
            p.setGrille(Jeu.nextGen(p.getGrille()));
            d.setGrille(Jeu.nextGen(d.getGrille()));
            d.setGrille(Jeu.nextGen(d.getGrille()));
            i++;
        }while(!egaux(p.getGrille(),d.getGrille())&&i<max);

        Jeu laqueue= new Jeu(grille.copyOf());

        while (!egaux(laqueue.getGrille(),d.getGrille())&&i<max){
            laqueue.setGrille(Jeu.nextGen(laqueue.getGrille()));
            queue++;
        }

        d.setGrille(Jeu.nextGen(d.getGrille()));
        if(egaux(p.getGrille(),d.getGrille())){
            return "<p>C'est stable avec la queue "+queue+"</p>";
        }else
            return "";
    }



    public static String oscillateur(List grille, int max){
        int i =0;
        int periode=0,queue=0;
        Jeu premier= new Jeu( grille.copyOf());
        Jeu deuxieme= new Jeu( grille.copyOf());
        do{
            premier.setGrille(Jeu.nextGen(premier.getGrille()));
            deuxieme.setGrille(Jeu.nextGen(deuxieme.getGrille()));
            deuxieme.setGrille(Jeu.nextGen(deuxieme.getGrille()));
            i++;
        }while(!egaux(premier.getGrille(),deuxieme.getGrille())&&i<max);

        do{
            deuxieme.setGrille(Jeu.nextGen(deuxieme.getGrille()));
            periode++;
        }while(!egaux(premier.getGrille(),deuxieme.getGrille())&&i<max);

        Jeu laqueue= new Jeu(grille.copyOf());
        while (!egaux(laqueue.getGrille(),deuxieme.getGrille())&&i<max){
            laqueue.setGrille(Jeu.nextGen(laqueue.getGrille()));
            queue++;
        }

        if(i<max){
            return "<p>C'est oscillateur de periode "+periode +" la queue "+queue+"</p>";
        }else
        {
            return "";
        }

    }

    public static boolean egaux (List g1,List g2){
        Maillon a = g1.tete;
        Maillon b = g2.tete;
        if(a==null && b==null)
            return true;
        if(a==null&&b!=null)
            return false;
        if(b==null&&a!=null)
            return false;
        while (a!=null&&b!=null){
            if(a.compareTo(b)!=0) return false;
            a=(a.getSuiv());
            b=(b.getSuiv());
        }
        return (a==null || b==null);
    }

    public static boolean diffVaiss(List g1,List g2){
        List g3=new List();
        Maillon a = g1.tete;
        Maillon b = g2.tete;
        while (a!=null&&b!=null){
            int a3l = ((Cellule)a.getInfo()).getLigne()-((Cellule)b.getInfo()).getLigne();
            int a3c = ((Cellule)a.getInfo()).getColonne()-((Cellule)b.getInfo()).getColonne();
            g3.addMaillon(new Maillon(new Cellule(a3l,a3c,1)));
            a=(a.getSuiv());
            b=(b.getSuiv());
        }
        if (a!=null || b!=null)
            return false;
        Maillon c = g3.tete;
        while (c!=null&&c.getSuiv()!=null){
            if( c.compareTo(c.getSuiv()) !=0)
                return false;
            c=c.getSuiv();
        }
        return true;
    }




}
