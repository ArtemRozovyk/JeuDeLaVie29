import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Jeu {

    static int  taille=2515144;
    static boolean circ=false;

    private List<Cellule> grille ;

    public Jeu (String nom){
        grille=new List<>();
        lireFichier(grille,nom);
    }

    public Jeu (List grille){
        this.grille=grille;

    }

    public static void jouerDuree(String nom, int duree){
        Jeu jeu = new Jeu("Folder/"+nom);
        Frame frame = new Frame(jeu.getGrille());
        Frame.dessinerMatrice(frame, jeu.getGrille());
        for (int i = 0; i < duree; i++) {
            jeu.setGrille(Jeu.nextGen(jeu.getGrille()));
            try {
                Frame.resetMatrice(frame);
                Frame.dessinerMatrice(frame, jeu.getGrille());
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void options(String[] args) {
        if(args.length==1&&args[0].equals("-name"))
            System.out.println("Lolkas");
        if(args.length==1&&args[0].equals("-h")){
            System.out.println("-name :\n  pour afficher les noms");
            System.out.println("-s d fichier.lif : \n exécute une simulation du jeu" +
                    "d’une durée d affichant les configurations du jeu avec le numéro de génération. ");
            System.out.println("-c max fichier.lif : \n calcule le type d’évolution");
            System.out.println("-w maxdossier :\n  calcule le type d’évolution de tous les\n" +
                    "jeux contenus dans le dossier");
            System.out.println("-circ t d fichier:\n  pour definir les mondes circulaires avec le bord de taille t et la durée d");
            System.out.println("-front t d fichier:\n  pour definir les mondes circulaires avec le bord de taille t et la durée d");
            System.out.println("-f :\n pour donner les nombs des fichiers disponibles");
        }
        if(args.length==3&&args[0].equals("-s")){
            jouerDuree(args[2],Integer.parseInt(args[1]));
        }
        if(args.length==1&&args[0].equals("-f")){
            listerFichiers();
        }
        if(args.length==3&&args[0].equals("-c")){
            System.out.println(TypeDEvolution.detecter("Folder/",args[2],Integer.parseInt(args[1])));
        }
        if(args.length==3&&args[0].equals("-w")){
            lesJeuxDuDossier(args[2],Integer.parseInt(args[1]));
        }

        if(args.length==4&&args[0].equals("-circ")||args[0].equals("-front")) {
            circ = (args[0].equals("-circ"));
            taille = Integer.parseInt(args[1]);
            jouerDuree(args[3], Integer.parseInt(args[2]));

        }
    }

    private static void listerFichiers() {

        File folder = new File("Folder");
        File[] listOfFiles = folder.listFiles();
        for(File x: listOfFiles){
            if(x.isFile()&&x.getName().substring(x.getName().length()-3).equals("LIF")){
                System.out.println(x.getName());
            }
        }
    }

    private static void lesJeuxDuDossierx(String arg, int max) {

        File folder = new File(arg);
        File[] listOfFiles = folder.listFiles();
        arg=arg+"/";
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("Fichier: "+listOfFiles[i].getName());
                System.out.println("-------");
                System.out.println(TypeDEvolution.detecter(arg,listOfFiles[i].getName(),max));
                System.out.println("-------");
            }
        }

    }


    private static void lesJeuxDuDossier(String arg, int max) {

        File folder = new File(arg);
        File[] listOfFiles = folder.listFiles();
        arg=arg+"/";
        String tab="<table>";
        String res="";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                res=res+"<p>Fichier:"+listOfFiles[i].getName()+"</p><p>"+TypeDEvolution.detecter(arg,listOfFiles[i].getName(),max)+"<p>";
                res=res+"<p>--------------------------</p>";
            }
        }
        modifyFile(res);

    }

    static public void modifyFile(String res){
        File fileToBeModified = new File("html/new.html");
        String oldContent = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String newContent = oldContent.replaceAll("RepBody", res);
            FileWriter writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            Desktop.getDesktop().browse(fileToBeModified.toURI());
            reader.close();
            writer.close();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FileWriter writerInit = new FileWriter(fileToBeModified);
            writerInit.write(oldContent);
            writerInit.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }}
