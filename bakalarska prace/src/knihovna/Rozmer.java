package knihovna;
/*******************************************************************************
 * Instance třídy {@code Rozmer} představují přepravky uchovávající informace
 * o rozměrech objektu.
 * Proto jsou jejich atributy deklarovány jako veřejné konstanty.
 * Převzato od R.Pecinovského 
 * a doplněno o defaultní konstruktor kvůli duck-testům.
 *
 * @author  P.Herout
 * @version 2013-10-03
 */
public class Rozmer
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Šířka objektu. */
    public final int sirka;

    /** Výška objektu. */
    public final int vyska;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří přepravku uchovávající nulové rozměry.
     * Použito kvůli duck-testům.
     */
    public Rozmer()
    {
        this(0, 0);
    }

    /***************************************************************************
     * Vytvoří přepravku uchovávající zadané rozměry.
     *
     * @param sirka  Šířka objektu
     * @param vyska  Výška objektu
     */
    public Rozmer( int sirka, int vyska )
    {
        this.sirka = sirka;
        this.vyska = vyska;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí uloženo velikost šířky objektu.
     *
     * @return  Velikost šířky
     */
    public int getSirka()
    {
        return sirka;
    }


    /***************************************************************************
     * Vrátí velikost výšky objektu.
     *
     * @return  Velikost výšky
     */
    public int getVyska()
    {
        return vyska;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí informaci o tom, představuje-li instance zadaná v parametru
     * stejný rozměr.
     *
     * @param  o   Testovaná instance
     * @return Je-li zadaná instance rozměrem se stejnými hodnotami atributů,
     *         vrátí {@code true}, jinak vrátí {@code false}.
     */
    @Override
    public boolean equals( Object o )
    {
        return (o instanceof Rozmer)              &&
               (((Rozmer)o).sirka == this.sirka)  &&
               (((Rozmer)o).vyska == this.vyska);
    }


    /***************************************************************************
     * Vrací textovou reprezentaci (podpis) dané instance
     * používanou především k ladicím účelům.
     *
     * @return Požadovaná textová reprezentace
     */
    @Override
    public String toString()
    {
        return "Rozmer[sirka=" + sirka + ", vyska=" + vyska + "]";
    }

//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
//
//    /***************************************************************************
//     * Testovací metoda.
//     */
//    public static void test()
//    {
//        Rozměr inst = new Rozměr();
//    }
//    /** @param args Parametry příkazového řádku - nepoužívané. */
//    public static void main( String[] args )  {  test();  }
}
