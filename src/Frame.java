import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    JPanel2 pannel = new JPanel2();
    int lligne, lcolonne;


    public Frame(List grille) {
        super("Cyka nuggets");

        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width-900, Toolkit.getDefaultToolkit().getScreenSize().height - 400));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().add(pannel);
        pannel.setSize(getContentPane().getPreferredSize().width, getContentPane().getPreferredSize().height);



    }

    public void dessinerMatrice(List grille) {
        getContentPane().remove(pannel);
        pannel = new JPanel2(grille, lligne, lcolonne);
        getContentPane().add(pannel);
    }
    public static void resetMatrice(Frame frame) {
        frame.remove(frame.pannel);
        frame.revalidate();
        frame.repaint();
    }


    public static void dessinerMatrice(Frame frame, List grille) {
        frame.pannel.removeAll();
        frame.dessinerMatrice(grille);
        frame.add(frame.pannel);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }


    class JPanel2 extends JPanel {

        List grille;
        int longLigne, longColonne;

        public JPanel2() {
            grille = null;
            longLigne = longColonne = 0;
        }

        public JPanel2(List grille, int longLigne, int longColonne) {
            this.grille = grille;
            this.longLigne = longLigne;
            this.longColonne = longColonne;
        }

        @Override
        public void paintComponent(Graphics g) {
            Maillon a = grille.tete;

            while (a != null) {
                //ne pas chercher à comprendre comment ça marche(!)
                g.fillOval((getWidth()) / 2 + ((Cellule)a.getInfo()).getColonne() * 7,
                        (getHeight()) / 2 - ((Cellule)a.getInfo()).getLigne() * 7, 7, 7);
                a = a.getSuiv();
            }
        }

    }


}