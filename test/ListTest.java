import org.junit.Test;

import static org.junit.Assert.*;

public class ListTest {

    @Test
    public void copyOf() {
        List l= new List();
        l.addMaillon(new Maillon("a"));
        l.addMaillon(new Maillon("b"));
        l.addMaillon(new Maillon("c"));
        l.addMaillon(new Maillon("d"));
        List lcp =l.copyOf();
        Maillon a =l.tete;
        Maillon b =lcp.tete;
        while(a!=null&&b!=null){
            assertEquals(a.getInfo(),b.getInfo());
            a=a.getSuiv();
            b=b.getSuiv();
        }
    }

    @Test
    public void addMaillon() {
        List l=new List();
        l.addMaillon(new Maillon(3));
        assertTrue(l.tete!=null);
        assertTrue(l.tete.getSuiv()==null);
        l.addMaillon(new Maillon(4));
        assertTrue(l.tete.getSuiv()!=null);
        assertTrue(l.tete.getSuiv().getSuiv()==null);
    }


    @Test
    public void dansOrdre() {
        List l1= new List();
        l1.addMaillon(new Maillon(1));
        l1.addMaillon(new Maillon(4));
        l1.addMaillon(new Maillon(7));
        l1.addMaillon(new Maillon(10));
        Maillon a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }
        l1.addMaillon(new Maillon(0));
        l1.addMaillon(new Maillon(2));
        l1.addMaillon(new Maillon(11));
        a =l1.tete;
        while(a.getSuiv()!=null){
            assertTrue(a.compareTo(a.getSuiv()) <0);
            a=a.getSuiv();

        }
    }
}