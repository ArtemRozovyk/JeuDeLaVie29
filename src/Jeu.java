import java.awt.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 */
public class Jeu {


    /**
     * Initialise l'affichage de generations d'une configuration
     * lue à partir d'un fichier LIF
     * @param nom Fichier avec la configuration initiale
     * @param duree Le nombre de génération à générer
     */
    public static void jouerDuree(String nom, int duree){
        Generation gen = new Generation("Folder/"+nom);
        //initialiser l'affichage
        Frame frame = new Frame(gen.getGrille());
        Frame.dessinerMatrice(frame, gen.getGrille());

        for (int i = 0; i < duree; i++) {
            //calculer la generation suivante
            gen.setGrille(Generation.nextGen(gen.getGrille()));
            try {
                //reinitialiser l'affichage et attendre n milisecondes
                Frame.resetMatrice(frame);
                Frame.dessinerMatrice(frame, gen.getGrille());
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Analyse les agrument donnés par l'utilisateur lors de l'execution du programme
     * (de l'executable .jar) Appele les fonction correspondante suivant le choix
     * @param args Les paramètres donnés par l'utilisateur
     */
    public static void options(String[] args) {
        //affichage des prenoms
        if(args.length==1&&args[0].equals("-name"))
            System.out.println("Rozovyk,Nouboussi,Caillot,(Salmon)");
        //mode d'emploi
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
        //Executer une simulation du jeu
        if(args.length==3&&args[0].equals("-s")){
            jouerDuree(args[2],Integer.parseInt(args[1]));
        }
        //Lister les fichier dans le dossier Folder
        if(args.length==1&&args[0].equals("-f")){
            listerFichiers();
        }
        //Detecter le type de comportement d'une cofiguration
        if(args.length==3&&args[0].equals("-c")){
            System.out.println(TypeDEvolution.detecter("Folder/",args[2],Integer.parseInt(args[1])));
        }
        //Detecter le type do comportement de toute les configurations d'un dossier
        if(args.length==3&&args[0].equals("-w")){
            lesJeuxDuDossier(args[2],Integer.parseInt(args[1]));
        }
        //Executeur une simulation avec les mondes cirulatire ou avec les frontières
        if(args.length==4&&args[0].equals("-circ")||args[0].equals("-front")) {
            Generation.setCirc(args[0].equals("-circ"));
            Generation.setTaille(Integer.parseInt(args[1]));
            jouerDuree(args[3], Integer.parseInt(args[2]));

        }
    }

    /**
     * Liste toute les fichiers disponibles du dossier Folder
     */
    private static void listerFichiers() {

        File folder = new File("Folder");
        File[] listOfFiles = folder.listFiles();
        for(File x: listOfFiles){
            if(x.isFile()&&x.getName().substring(x.getName().length()-3).equals("LIF")){
                System.out.println(x.getName());
            }
        }
    }

    /**
     * Calcule les comportements de chaque configuraion contenue dans le dossier Folder,
     * Affiche le resultat dans une page HTLM qui s'ouvre dans le navigateur par défaut.
     * @param nomDossier
     * @param max Le nombre maximale de génération à générer
     */
    private static void lesJeuxDuDossier(String nomDossier, int max) {
        File folder = new File(nomDossier);
        File[] listOfFiles = folder.listFiles();
        nomDossier=nomDossier+"/";
        String res="";
        //constuction de toutes les resultats sous forme d'un String
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                res=res+"<p>Fichier:"+listOfFiles[i].getName()+"</p><p>"+TypeDEvolution.detecter(nomDossier,listOfFiles[i].getName(),max)+"<p>";
                res=res+"<p>--------------------------</p>";
            }
        }
        modifyFile(res);
    }

    /**
     * Ecrire le resultat de comportmenets de configurations d'un dossier dans un ficher
     * HTML et l'afficher dans le navigateur par défaut
     * @param res Le String qui contient toutes les résultats des comportements
     */
    static public void modifyFile(String res){
        File fileToBeModified = new File("html/new.html");
        File fileToBeRead =new File("html/old.html");
        String oldContent = "";
        try {
            //Lire le fichier
            BufferedReader reader = new BufferedReader(new FileReader(fileToBeRead));
            String line = reader.readLine();
            //Lire toutes les lignes du fichier
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            //remplacer le contenu de la balise <body> par le res
            String newContent = oldContent.replaceAll("RepBody", res);
            //ecrire le résultat dans un fichier
            FileWriter writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            Desktop.getDesktop().browse(fileToBeModified.toURI());
            reader.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}