/**
 * Cette classe contient des methodes qui servent à
 * detecter le type d'évolution d'une configuration du Generation
 */

public class TypeDEvolution {
    /*
Remarquer qu’un comportement du type
mort est aussi du type stable,
qu’un du type stable est du type oscillateur,
qu’un du type oscillateur est du type vaisseau
 */

    /**
     * Sert à déterminer le type d'evoulution en appelant les methodes
     * qui testent le comportement de la génération.
     *
     * @param folder Le nom du dossier dans lequel se trouve le fichier
     * @param nomFichier
     * @param i Le nombre maximal d'evolutions à générer pour chaque test
     * @return La chaine de caractère composé d'un ou plusieurs type d'evolution
     * detecté
     */
     static String detecter(String folder,String nomFichier, int i) {
        Generation gen = new Generation(folder+nomFichier);

         String m=mort (gen.getGrille(),i) ;
         String s=stable(gen.getGrille(),i);
         String v=vaisseau(gen.getGrille(),i);
         String o=oscillateur(gen.getGrille(),i);
         if(!m.equals("")||!s.equals("")||!v.equals("")||!o.equals(""))
             return m+"\n"+s+"\n"+o+"\n"+v+"\n";
         else
             return "Inconnu";

    }

    /**
     * Le Comportement correspondant à une grille vide
     * @param grille Configuration initiale
     * @param max Le nombre maximal d'evolutions à générer
     * @return La chaine de caractères indiquand si c'est mort (la chaine vide sinon)
     * aisni que le nombre de generations avant de mourir
     */
    private static String mort(List<Cellule> grille, int max) {
        //on avance une génération justqu'à détécter que la liste est vide
        //ou dépasser le nombre de générations maximal
        Generation testmort = new Generation(grille.copyOf());
        int i = 0;
        while(i<max){
            if(testmort.getGrille().tete==null){
               return "<p>C'est mort après "+i+" gerenations</p>";
            }
            testmort.setGrille(Generation.nextGen(testmort.getGrille()));
            i++;
        }

        return "";
    }


    /**
     * Le Comportement correspondant à un vaisseau
     * i.e. une strucure qui se déplace infiniment dans une direction
     *
     * @param grille Configuration initiale
     * @param max Le nombre maximal d'evolutions à générer
     * @return La chaine de caractères indiquand si c'est un vaisseau (la chaine vide sinon)
     *      * aisni que la période et la queue
     */
    public static String vaisseau(List grille, int max){
        int periode=0,queue=0;
        int i=0;
        //on fais avancer une generation(initallement identique à
        // la configuration initiale) d'un pas et l'autre de deux pas
        // en verifiant à chaque fois si la difference de deux
        // generations ne montre pas un déplacement "(A,B)"
        //
        Generation unpas= new Generation( grille.copyOf());
        Generation deuxpas= new Generation( grille.copyOf());
        do{
            avancerUnPasDeuxPas(unpas,deuxpas);
            i++;
        }while(!diffVaiss(unpas.getGrille(),deuxpas.getGrille())&&i<max);

        //on continue à avancer la deuxième generation pour calculer la periode
        do{
            deuxpas.setGrille(Generation.nextGen(deuxpas.getGrille()));
            periode++;
        }while(!diffVaiss(unpas.getGrille(),deuxpas.getGrille())&&i<max);
        //ayant la generation "unpas" qui est dans l'état de se repruduire à
        // un deplacement (A,B) près, on calcule le nombre de générations
        //entre la configuration initiale et "unpas"
        Generation laqueue= new Generation(grille.copyOf());
        while (!diffVaiss(laqueue.getGrille(),unpas.getGrille())&&i<max){
            laqueue.setGrille(Generation.nextGen(laqueue.getGrille()));
            queue++;
        }
        if(i<max)
            return "<p>C'est vaisseau de periode: "+periode+" et de queue "+queue+"</p>";
        else
            return "";

    }

    /**
     * Algorithme servant à trouver la similitude de deux generations
     * après un certain nombre de génération
     * @param unPas La generation qui avance une fois a chaque éxécution
     * @param deuxPas Cette génération avance deux fois
     * @see {@link #vaisseau(List, int)} (List, int)} {@link #oscillateur(List, int)} {@link #stable(List, int)}
     */
    public static void avancerUnPasDeuxPas(Generation unPas,Generation deuxPas){
        unPas.setGrille(Generation.nextGen(unPas.getGrille()));
        deuxPas.setGrille(Generation.nextGen(deuxPas.getGrille()));
        deuxPas.setGrille(Generation.nextGen(deuxPas.getGrille()));
    }

    /**
     * Configuration qui n'évolue plus
     * @param grille Configuration initiale
     * @param max Le nombre maximal d'evolutions à générer
     * @return La chaine de caractères indiquand si
     * la configuration est  stable (la chaine vide sinon)
     * aisni que la queue associé
     */
    public static String stable (List grille, int max){
        int i =0;
        int queue=0;
        Generation unPas= new Generation( grille.copyOf());
        Generation deuxPas= new Generation( grille.copyOf());
        //on avance justqu'à ce que les configurations
        // ne soient pas les mêmes
        do{
            avancerUnPasDeuxPas(unPas,deuxPas);
            i++;
        }while(!egaux(unPas.getGrille(),deuxPas.getGrille())&&i<max);
        //On calcule le nombre de génération être la configuration initiale
        // et la génération supposé stable
        Generation laqueue= new Generation(grille.copyOf());
        while (!egaux(laqueue.getGrille(),deuxPas.getGrille())&&i<max){
            laqueue.setGrille(Generation.nextGen(laqueue.getGrille()));
            queue++;
        }
        //si en avançant une fois la génération supposé stable
        //on retrouve la même configuration, alors c'est un comportement stable
        deuxPas.setGrille(Generation.nextGen(deuxPas.getGrille()));
        if(egaux(unPas.getGrille(),deuxPas.getGrille())){
            return "<p>C'est stable avec la queue "+queue+"</p>";
        }else
            return "";
    }


    /**
     * Configuration que l’on retrouve à intervalle régulier
     * @param grille Configuration initiale
     * @param max Le nombre maximal d'evolutions à générer
     * @return La chaine de caractères indiquand si
     * la configuration est oscillante (la chaine vide sinon)
     * aisni que la queue et la période associé
     */
    public static String oscillateur(List grille, int max){
        int i =0;
        int periode=0,queue=0;
        Generation unPas= new Generation( grille.copyOf());
        Generation deuxPas= new Generation( grille.copyOf());
        //on avance jusqu'à ce que les configurations
        // ne deviennent pas les mêmes
        do{
            avancerUnPasDeuxPas(unPas,deuxPas);
            i++;
        }while(!egaux(unPas.getGrille(),deuxPas.getGrille())&&i<max);
        //La periode c'est l'interval entre la configuration
        //etant dans l'état de se reproduire et une configuration identique après
        //un certain nombre d'avancement de génération.
        do{
            deuxPas.setGrille(Generation.nextGen(deuxPas.getGrille()));
            periode++;
        }while(!egaux(unPas.getGrille(),deuxPas.getGrille())&&i<max);
        //La queue est la difference etre la gen. initiale et la configuration
        //supposé oscilliante
        Generation laqueue= new Generation(grille.copyOf());
        while (!egaux(laqueue.getGrille(),deuxPas.getGrille())&&i<max){
            laqueue.setGrille(Generation.nextGen(laqueue.getGrille()));
            queue++;
        }

        if(i<max){
            return "<p>C'est oscillateur de periode "+periode +" la queue "+queue+"</p>";
        }else
        {
            return "";
        }

    }

    /**
     * Detecte si deux configurations sont exactement les mêmes
     * @param g1 La première configuration
     * @param g2 La deuxième configuration
     * @return Vrai si les deux generation sont identiques
     * @see #oscillateur(List, int) {@link #stable(List, int)}
     */
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

    /**
     * Detecte si la diference de deux générations montre un déplacement 
     * de A lignes et B colones
     * @param g1 La première configuration
     * @param g2 La deuxième configuration
     * @return Vrai si la deuxième génération 
     * est la pemière déplaces de A lignes et B colones
     * @see #vaisseau(List, int) 
     */
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
