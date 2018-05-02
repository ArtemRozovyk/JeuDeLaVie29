import javax.swing.*;
import java.awt.*;

/**
 * Affichage dans la fenetre
 * @author Nouboussi L. Rozovyk A.
 */
public class Frame extends JFrame {
    JPanel2 panel = new JPanel2();


    public Frame(List grille) {
        super("Priviet");
        //la taille de fenetre
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width-900, Toolkit.getDefaultToolkit().getScreenSize().height - 400));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(panel);
        panel.setSize(getContentPane().getPreferredSize().width, getContentPane().getPreferredSize().height);



    }

    /**
     * Reinitialise le contenu du Jrame par un nouveau panel
     * @param grille la liste avec les coordonnées à insérer
     */
    public void reset(List grille) {
        getContentPane().remove(panel);
        panel = new JPanel2(grille);
        getContentPane().add(panel);
    }

    /**
     * Retire le contenu de la fenêtre
     * @param frame
     */
    public static void viderMatrice(Frame frame) {
        frame.remove(frame.panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Reafiche le contenu de la grille qui vient d'être initialisé par une génération
     * @param frame la fenetre
     * @param grille La génération avec les coordonnées
     */
    public static void dessinerMatrice(Frame frame, List grille) {
        frame.panel.removeAll();
        frame.reset(grille);
        frame.add(frame.panel);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * L'element contenu dans le JFrame
     */
    class JPanel2 extends JPanel {

        List grille;
        int longLigne, longColonne;

        /**
         * Un panel vide
         */
        public JPanel2() {
            grille = null;
            longLigne = longColonne = 0;
        }

        /**
         * Creer un panel à partir d'une configuration
         * @param grille la configuration
         */
        public JPanel2(List grille) {
            this.grille = grille;

        }

        /**
         * Remplir le panel avec les "Oval" suivant les coordonnées de la grille
         * @param g
         */
        @Override
        public void paintComponent(Graphics g) {
            Maillon a = grille.tete;

            while (a != null) {

                g.fillOval((getWidth()) / 2 + ((Cellule)a.getInfo()).getColonne() * 7,
                        (getHeight()) / 2 - ((Cellule)a.getInfo()).getLigne() * 7, 7, 7);
                a = a.getSuiv();
            }
        }

    }


}