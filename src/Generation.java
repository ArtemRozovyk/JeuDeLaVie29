import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Generation {
    /**
     * Les lignes et les collones de la Cellule étant des entiers
     * ils ne peuvent pas depasser la valeur maximale de Integer,on la prend
     * comme limite pour la configuration "infinie";
     */
    private static int  taille=Integer.MAX_VALUE;
    /**
     * Indique si les mondes sont circulaire
     * Si la valeur de "taille" est modifié
     * @see #taille
     * et circ est à faux, alors on a les mondes avec les bords.
     */
    private static boolean circ=false;

    private List<Cellule> grille ;

    /**
     * Initialise une génération à partir d'un ficher avec le nom donné en paramètre
     * @param nom Fichier à initialiser
     */
    public Generation (String nom){
        grille=new List<>();
        lireFichier(grille,nom);
    }

    /**
     * Initialise une generation à partir d'une grille existante
     * @param grille La generation à copier
     */
    public Generation (List grille){
        this.grille=grille;

    }

    /**
     * Modifer la grille principale de génération
     * @param grille nouvelle génération
     */
    public void setGrille(List<Cellule> grille){
        this.grille=grille;
    }

    /**
     *
     * @return Génération correspondate
     */
    public List<Cellule> getGrille() {
        return grille;
    }


    /**
     * Rassamblment de toutes les fonction déstinées à calculer
     * la génération suivante
     * @param grille Génération initiale
     * @return Generation d'après
     * @see #calculerSomme(List[]) {@link #sommeM0Nbvois(List, List)}{@link #eliminerNonConformes(List)}
     */
    public static List nextGen(List grille){
        List ng=calculerSomme(calculerProjections(grille));
        ng = sommeM0Nbvois(grille,ng);
        ng=eliminerNonConformes(ng);
        return ng;
    }


    /**
     * Supprime les maillons qui contiennent les Cellules qui ne vérifient pas les régles du jeu
     *
     * @param grille Génération à "purifier"
     * @return La génération finale
     */
    public static List eliminerNonConformes(List grille) {
        Maillon a = grille.tete;
        List ng = new List();
        while(a!=null){
            if(((Cellule)a.getInfo()).getNbvois()==3 || ((Cellule)a.getInfo()).getNbvois()==1002 || ((Cellule)a.getInfo()).getNbvois()==1003){
                //si le nombre de voisins d'une case morte est 3 (vivante 3 ou 2) on la garde dans une nouvelle grille
                Cellule cell=new Cellule( ((Cellule)a.getInfo()).getLigne(),((Cellule)a.getInfo()).getColonne(),1);
                ng.addMaillon(new Maillon(cell));
            }
            a=a.getSuiv();
        }
        return ng;
    }


    /**
     * Recherche de la valeur minimale parmi les 8 pointeurs
     * sur des génétaions "projetés" afin d'en trouver des doublons eventuels
     * parmi les autres pointeurs.
     * @param listM Liste de maillons-pointerus sur des généraion projetes d'un vecteur (a,b) a,b={-1,1,0},a!=b!=0;
     * @return l'indice du maillon minimale
     * @see #sommeM0Nbvois(List, List)
     */
    public static int min(Maillon[] listM){
        Maillon min = listM[0];
        int i=0;
        for (int j = 0; j < 8; j++) {
            if (listM[j]!=null && listM[j].compareTo(min) < 0){
                min = listM[j];
                i = j;
            }
        }
        return i;
    }

    /**
     * Construit la liste qui indique le
     * @param projects le tableu avec les générations projetés
     * @return la Liste résultante de la somme de toutes les maillons des 8 listes
     *      * des générations projetés d'un vecteur (a,b) a,b={-1,1,0},a!=b!=0;
     * @see
     * #chercherVoisins(int, Maillon[])
     */
    public static List calculerSomme(List[] projects){
        List somme = new List<Cellule>();
        Maillon[] listM= new Maillon[8];
        //initialise la liste de pointeurs sur chaque génération projeté
        for (int i = 0; i < 8; i++) {
            listM[i]=projects[i].tete;
        }
        //tanqu'il existent des cellules dans une des 8 géneration
        //chercher les maillons avec le meme nombre de voisins et stocker dans
        // une nouvelle liste somme
        while (!estTraite(listM)){
            int min =min(listM);
            somme.addMaillon(chercherVoisins(min,listM));
        }
        return somme;
    }

    /**
     * Sert à trouver les doublons dans la liste des projections
     * @param min l'indice du pointeur qui sert du "pivot"
     * @param listM la liste des projections
     * @return le Maillon resultant de l'addition de toutes Cellules
     * ayant les mêmes coordonées que le pivot
     */
    private static Maillon chercherVoisins(int min, Maillon[] listM) {
        int ligne =((Cellule)listM[min].getInfo()).getLigne();
        int colonne=((Cellule)listM[min].getInfo()).getColonne();

        Cellule cell=new Cellule(ligne,colonne,0);
        Maillon m= new Maillon(cell);
        for (int i = 0; i < 8; i++) {
            if (listM[i]!=null&&listM[i].compareTo(m)==0) {
                ((Cellule)m.getInfo()).setNbvois(((Cellule)m.getInfo()).getNbvois()+1);
                listM[i]=listM[i].getSuiv();
            }
        }
        return m;
    }

    private static boolean estTraite(Maillon[] listM) {
        for (Maillon x :listM){
            if (x!=null) return false;
        }
        return true;
    }

    public static List initM0(List m0){
        List nl= new List();
        Maillon a = m0.tete;
        while(a!=null){
            Cellule cell=new Cellule( ((Cellule)a.getInfo()).getLigne(),((Cellule)a.getInfo()).getColonne(),1000);
            nl.addMaillon(new Maillon(cell));
            a=a.getSuiv();
        }
        return nl;
    }

    private static List sommeM0Nbvois(List m0, List somme){
        List m0mille=initM0(m0);
        Maillon a= m0mille.tete;
        while (a!=null){
            Maillon b = somme.tete;
            while(b!=null){
                if(a.compareTo(b)==0){
                    ((Cellule)b.getInfo()).setNbvois( ((Cellule)b.getInfo()).getNbvois() +1000);
                    break;
                }
                b=b.getSuiv();
            }
            a=a.getSuiv();
        }
        return somme;
    }

    private static List[] calculerProjections( List grille) {
        List [] project = new List[8];
        //couples qui designent les vecteur pour les 8 directions
        Couple[] tc = {
                new Couple(-1, -1),
                new Couple(-1, 1),
                new Couple(1, -1),
                new Couple(1, 1),
                new Couple(0, 1),
                new Couple(0, -1),
                new Couple(1, 0),
                new Couple(-1, 0)};
        for (int i = 0; i < 8; i++) {
            project[i] = projeter(grille,tc[i]);
        }

        return project;
    }

    public static List projeter(List grille, Couple couple) {
        List l = new List();
        Maillon a = grille.tete;
        boolean surfrontiere=false;
        int cply=couple.getLigne();
        int cplx=couple.getColonne();
        while(a!=null){
            int clgn=((Cellule)a.getInfo()).getLigne();
            int ccln=((Cellule)a.getInfo()).getColonne();

            if (cplx+ccln>taille){
                ccln=-taille-cplx;
                surfrontiere=true;
            }

            if (cplx+ccln<-taille){
                ccln=taille-cplx;
                surfrontiere=true;
            }

            if (cply+clgn>taille){
                clgn=-taille-cply;
                surfrontiere=true;
            }
            if (cply+clgn<-taille){
                clgn=taille-cply;
                surfrontiere=true;
            }
            Cellule cell=new Cellule(cply+clgn,cplx+ccln,1);
            if(circ || !surfrontiere){
                l.addMaillon(new Maillon(cell));
            }else
            {
                surfrontiere=false;
            }
            a=a.getSuiv();
        }
        return l;
    }

    @Override
    public String toString() {
        return grille.toString();
    }

    public static void lireFichier(List grille, String nom) {
        try {
            int ligne = 0;
            int colonne = 0;
            Scanner fs = new Scanner(new File(nom));
            while (fs.hasNextLine()) {
                String s = fs.nextLine();
                if (s.matches("^#P.*")) {
                    String[] s2 = s.split(" ");
                    colonne = Integer.parseInt(s2[1]);
                    ligne = Integer.parseInt(s2[2]);
                } else {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c == '*') {
                            if (ligne<=taille && ligne>=-taille && i+colonne <= taille && i+colonne>=-taille){
                                Cellule cel=new Cellule(ligne, i + colonne,1);
                                Maillon maillon = new Maillon(cel);
                                grille.addMaillon(maillon);
                            }
                        }
                    }
                    ligne--;
                }
            }
            fs.close();
        } catch (FileNotFoundException e) {
            System.out.print(e.getMessage());
        }
    }
    public static void setCirc(boolean circ){
       Generation.circ=circ;
    }
    public static void setTaille(int taille){
        Generation.taille=taille;
    };
}


