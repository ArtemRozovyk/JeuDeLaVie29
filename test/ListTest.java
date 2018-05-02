import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Vérification du bon fonctionnement de la liste chainée
 */
public class ListTest {
    /**
     * Teste si la duplication fonctionne de même façon que
     * dans la classe LinkedList de l'API Java
     */
    @Test
    public void copyOf() {
        //LinkedList de api Java pour comparer avec la liste initiale et la copie
        LinkedList ll=new LinkedList();
        ll.add("a");
        ll.add("b");
        ll.add("c");
        ll.add("d");
        int i=0;

        List l= new List();
        l.addMaillon(new Maillon("a"));
        l.addMaillon(new Maillon("b"));
        l.addMaillon(new Maillon("c"));
        l.addMaillon(new Maillon("d"));
        List lcp =l.copyOf();
        Maillon a =l.tete;
        Maillon b =lcp.tete;


        while(a!=null&&b!=null){
            //la copie est bien la copie de la liste "List"
            assertEquals(a.getInfo(),b.getInfo());
            //la copie est la meme que celle de l'api java
            assertEquals(ll.get(i),a.getInfo());
            //la liste initiale est la même que celle de l'api java
            assertEquals(ll.get(i),b.getInfo());

            a=a.getSuiv();
            b=b.getSuiv();
            i++;
        }



    }

    /**
     * Teste L'instertion basique, insertion au début, à la fin et au milieu de la liste
     * Conservation d'ordre
     */
    @Test
    public void addMaillon() {
        List l=new List();
        l.addMaillon(new Maillon(3));
        assertTrue(l.tete!=null);
        assertTrue(l.tete.getSuiv()==null);
        l.addMaillon(new Maillon(4));
        assertTrue(l.tete.getSuiv()!=null);
        assertTrue(l.tete.getSuiv().getSuiv()==null);


        l.addMaillon(new Maillon(5));

        //comparaison avec API JAVA (l'ordre n'est pas à verifier ici)
        LinkedList ll=new LinkedList();
        ll.add(3);
        ll.add(4);
        ll.add(5);
        Maillon a =l.tete;
        for (int i = 0; i < ll.size(); i++) {
            assertEquals(ll.get(i),a.getInfo());
            a=a.getSuiv();
        }






        //Ordre
        List l1= new List();
        l1.addMaillon(new Maillon(1));
        l1.addMaillon(new Maillon(4));
        l1.addMaillon(new Maillon(7));
        l1.addMaillon(new Maillon(10));
         a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }
        //ajout au début
        l1.addMaillon(new Maillon(0));
        a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }
        //ajout au milieu
        l1.addMaillon(new Maillon(2));
        a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }
        //ajout a la fin
        l1.addMaillon(new Maillon(11));
        a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }




    }



}