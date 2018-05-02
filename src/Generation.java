import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Permet de calculer la génération suivante en appliquant les contraintes du jeu
 * @author Rozovyk A. Nouboussi L. Caillot M.
 */
public class Generation {
    /**
     * Les lignes et les collones de la Cellule étant des entiers
     * ne pouvant pas dépasser la valeur maximale de Integer,on la prend
     * comme limite pour la configuration "infinie";
     *
     *
     */
    private static int  taille=Integer.MAX_VALUE;
    /**
     * Indique si les mondes sont circulaires
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
     * Initialise une génération à partir d'une grille existante
     * @param grille La génération à copier
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
     * @return Génération correspondante
     */
    public List<Cellule> getGrille() {
        return grille;
    }


    /**
     * Rassemblment de toutes les fonctions destinées à calculer
     * la génération suivante
     * @param grille Génération initiale
     * @return Generation d'après
     * @see #calculerSomme(List[])
     * @see #sommeM0Nbvois(List, List)
     * @see #eliminerNonConformes(List)
     */
    public static List nextGen(List grille){
        List ng=calculerSomme(calculerProjections(grille));
        ng = sommeM0Nbvois(grille,ng);
        ng=eliminerNonConformes(ng);
        return ng;
    }


    /**
     * Supprime les maillons qui contiennent les Cellules qui ne vérifient pas les règles du jeu
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
     * sur des générations "projetés" afin d'en trouver des éventuels doublons
     * parmi les autres pointeurs.
     * @param listM Liste de maillons-pointeurs sur des génération projetés d'un vecteur (a,b) a,b={-1,1,0},a!=b!=0;
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
     * Construit la liste qui indique le nombre de voisins de chaque case
     * @param projects le tableau avec les générations projetés
     * @return la Liste résultante de la somme de toutes les maillons des 8 listes
     * des générations projetés d'un vecteur (a,b) a,b={-1,1,0},a!=b!=0;
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
     * Sert à trouver des doublons dans les listes des projections
     * @param min l'indice du pointeur qui sert du "pivot"
     * @param listM Le tableau des pointeurs
     * @return le Maillon résultant de l'addition de toutes Cellules
     * ayant les mêmes coordonnées que le pivot
     */
    private static Maillon chercherVoisins(int min, Maillon[] listM) {
        int ligne =((Cellule)listM[min].getInfo()).getLigne();
        int colonne=((Cellule)listM[min].getInfo()).getColonne();

        Cellule cell=new Cellule(ligne,colonne,0);
        Maillon m= new Maillon(cell);
        for (int i = 0; i < 8; i++) {
            //si l'un des pointeurs est égal à la celulle minimale
            //alors on incremente le nombre de voisins de cette cellule
            if (listM[i]!=null&&listM[i].compareTo(m)==0) {
                ((Cellule)m.getInfo()).setNbvois(((Cellule)m.getInfo()).getNbvois()+1);
                listM[i]=listM[i].getSuiv();
            }
        }
        return m;
    }

    /**
     * Sert à vérifier s'il reste encore des maillons dans le tableau des pointeurs
     * @param listM Le tableau des pointeurs
     * @return Vrai si tout les pointeurs sont null
     */
    private static boolean estTraite(Maillon[] listM) {
        for (Maillon x :listM){
            if (x!=null) return false;
        }
        return true;
    }


    /**
     * Additionnement de la configuration initiale
     * et de la liste résultant de la somme de tout les maillons dans des listes des projections.
     * @param m0 configuration initiale
     * @param somme La somme des 8 projections
     * @return La liste qui permet de déduire la génération suivante
     * (nécessite l'élimination des cellules qui restent pas dans la génération suivante)
     */
    private static List sommeM0Nbvois(List m0, List somme){

        Maillon a= m0.tete;
        while (a!=null){
            Maillon b = somme.tete;
            while(b!=null){
                //les celulles de la génération initiale ont le nombre de voisins+1000
                //pour pouvoir les distanguer des celulles mortes lors de l'élimination
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

    /**
     * Projeter la configuration initiale dans les 8 direction afin de pouvoir
     * détecter le nombre de voisins
     * @param grille configuration initiale
     * @return le tableau de génération projetés d'un vecteur (a,b) a,b={-1,1,0}, a!=b!=0;
     * @see #projeter(List, Couple)
     *
     */
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

    /**
     * Ajouter le vecteur donné à chaque couple (ligne,colonne) de la generation
     * @param grille Génération initialle
     * @param couple Le vector à appliquer
     * @return La génération projeté d'un vecteur
     */
    public static List projeter(List grille, Couple couple) {
        List l = new List();
        Maillon a = grille.tete;
        boolean surfrontiere=false;
        int cplLigne=couple.getLigne();
        int cplColonne=couple.getColonne();
        while(a!=null){

            int cellueLgn=((Cellule)a.getInfo()).getLigne();
            int celulleCln=((Cellule)a.getInfo()).getColonne();

           //si on est dans les mondes fini
            if(taille!=Integer.MAX_VALUE){
                //si la celulle projete de vecteur est depasse la taille ou la taille*(-1)
                //on concult qu'elle est sur le bord
            if (cplColonne+celulleCln>taille){
                celulleCln=-taille-cplColonne;
                surfrontiere=true;
            }
            if (cplColonne+celulleCln<-taille){
                celulleCln=taille-cplColonne;
                surfrontiere=true;
            }
            if (cplLigne+cellueLgn>taille){
                cellueLgn=-taille-cplLigne;
                surfrontiere=true;
            }
            if (cplLigne+cellueLgn<-taille){
                cellueLgn=taille-cplLigne;
                surfrontiere=true;
            }
            }

            Cellule cell=new Cellule(cplLigne+cellueLgn,cplColonne+celulleCln,1);
            //si les mondes circulaires ou la celulle est à l'interieur on l'ajoute
            if(circ || !surfrontiere){
                l.addMaillon(new Maillon(cell));
            }else
            {
                //on a pas ajoute le maillon donc on reinitialise
                //l'indicateur du fait que la cellule soit sur la frontière
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

    /**
     * Initialisation d'une configuration à partir d'un fichier LIF
     *
     * @param grille la Liste à remplir
     * @param nomFichier Le fichier qui va être lu
     */
    public static void lireFichier(List grille, String nomFichier) {
        try {
            int ligne = 0;
            int colonne = 0;
            Scanner fs = new Scanner(new File(nomFichier));
            while (fs.hasNextLine()) {
                String s = fs.nextLine();
                //le debut de lecture
                if (s.matches("^#P.*")) {
                    String[] s2 = s.split(" ");
                    //les coordonées du début
                    colonne = Integer.parseInt(s2[1]);
                    ligne = Integer.parseInt(s2[2]);
                } else {

                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        //enregistrer la position quand on retrouve "*"
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

    /**
     * Sert à passer aux mondes circulaires
     * @param circ vrai indique les mondes circulaires
     */
    public static void setCirc(boolean circ){
       Generation.circ=circ;
    }

    /**
     * Sert à définir les mondes fini(avec les frontières ou circulaires)
     * @param taille la taille des frontières
     */
    public static void setTaille(int taille){
        Generation.taille=taille;
    };
}


