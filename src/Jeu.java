import java.awt.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 */
public class Jeu {



    public static void jouerDuree(String nom, int duree){
        Generation gen = new Generation("Folder/"+nom);
        Frame frame = new Frame(gen.getGrille());
        Frame.dessinerMatrice(frame, gen.getGrille());
        for (int i = 0; i < duree; i++) {
            gen.setGrille(Generation.nextGen(gen.getGrille()));
            try {
                Frame.resetMatrice(frame);
                Frame.dessinerMatrice(frame, gen.getGrille());
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
            Generation.setCirc(args[0].equals("-circ"));
            Generation.setTaille(Integer.parseInt(args[1]));
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

}