import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Generation {
    private static int  taille=Integer.MAX_VALUE;
    private static boolean circ=false;

    private List<Cellule> grille ;

    public Generation (String nom){
        grille=new List<>();
        lireFichier(grille,nom);
    }

    public Generation (List grille){
        this.grille=grille;

    }

    public void setGrille(List<Cellule> grille){
        this.grille=grille;
    }

    public List<Cellule> getGrille() {
        return grille;
    }

    public static List nextGen(List grille){
        List ng=calculerSomme(calculerProjections(grille));
        ng = sommeM0Nbvois(grille,ng);
        ng=eliminerNonConformes(ng);
        return ng;
    }

    public static List eliminerNonConformes(List grille) {
        Maillon a = grille.tete;
        List ng = new List();
        while(a!=null){
            if(((Cellule)a.getInfo()).getNbvois()==3 || ((Cellule)a.getInfo()).getNbvois()==1002 || ((Cellule)a.getInfo()).getNbvois()==1003){
                Cellule cell=new Cellule( ((Cellule)a.getInfo()).getLigne(),((Cellule)a.getInfo()).getColonne(),1);
                ng.addMaillon(new Maillon(cell));
            }
            a=a.getSuiv();
        }
        return ng;
    }

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

    public static List calculerSomme(List[] projects){
        List somme = new List<Cellule>();
        Maillon[] listM= new Maillon[8];
        for (int i = 0; i < 8; i++) {
            listM[i]=projects[i].tete;
        }
        while (!estTraite(listM)){
            int min =min(listM);
            somme.addMaillon(chercherVoisins(min,listM));
        }
        return somme;
    }

    private static Maillon chercherVoisins(int min, Maillon[] listM) {

        Cellule cell=new Cellule(((Cellule)listM[min].getInfo()).getLigne(),((Cellule)listM[min].getInfo()).getColonne(),0);
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


