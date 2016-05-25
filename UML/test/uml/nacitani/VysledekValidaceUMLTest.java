/**
 * 
 */
package uml.nacitani;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import uml.Konstanty;
import uml.MozneVysledekyValidace;

/**
 * JUnit test třídy VysledekValidaceUML.
 * @author Jitka Fürbacherová
 * @version 1.0
 *
 */
public class VysledekValidaceUMLTest {

	private String osobniCislo;
	private String nazevOdevzdanehoSouboru;
	private String vysledekValidaceSpatneVysledky;
	private VysledekValidaceUML vysledek1;
	private VysledekValidaceUML vysledek2;
	private LocalDateTime casDatum;
	private String idUlohy;  
	private LocalDate datum;
	private LocalTime cas;
	private boolean obecnaChybaNE;
	private String chybaUML_1;
	private String chybaUML_2;


	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception{
		
		osobniCislo = "A13B0230P";
		nazevOdevzdanehoSouboru = "06_uml.jar";
		idUlohy = Konstanty.ID_JMENO_ULOHY.get(nazevOdevzdanehoSouboru);
		casDatum = LocalDateTime.of(2015, 9, 26, 10, 15, 8);
		vysledekValidaceSpatneVysledky =  "Špatné výsledky";
	    datum = casDatum.toLocalDate();
	    cas = casDatum.toLocalTime();
	    obecnaChybaNE = false;
	    chybaUML_1 = "t0301001_hasCorrectRelationsCountBetween__IMeritelny_IZvyrazneny";
	    chybaUML_2 = "t0301006_hasNoRelationsBetween__IZvyrazneny_Osoba";
		vysledek1 = new VysledekValidaceUML(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceSpatneVysledky , obecnaChybaNE, chybaUML_1);
		vysledek2 = new VysledekValidaceUML(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum,vysledekValidaceSpatneVysledky , obecnaChybaNE, chybaUML_2);
	}

	/**
	 * Test existence instance.
	 */
	@Test
	public void testVysledekValidaceUML() {
	    assertNotNull("Instanci se nepodařilo vytvořit", vysledek1); 
	}

	/**
	 * Test správného nastavení osobního čísla.
	 */
	@Test
	public void testGetOsobniCislo(){
		assertEquals("Osobní číslo je chybné.", osobniCislo, vysledek1.getOsobniCislo());
	}

	/**
	 * Test správného nastavení identifikačního čísla.
	 */
	@Test
	public void testGetIdUlohy() {
		assertEquals("Identifikační číslo úlohy je chybné", idUlohy, vysledek1.getIdUlohy());
	}

	/**
	 * Test správného nastavení názvu souboru.
	 */
	@Test
	public void testGetNazevOdevzdanehoSouboru(){
		assertEquals("Název odevzdaného souboru je chybný.", nazevOdevzdanehoSouboru, vysledek1.getNazevOdevzdanehoSouboru());
	}

	/**
	 * Test správného nastavení času validace.
	 */
	@Test
	public void testGetCasValidace(){
		assertEquals("Čas validace je chybný.", cas, vysledek1.getCasValidace());
	}

	/**
	 * Test správného nastavení data validace.
	 */
	@Test
	public void testGetDatumValidace(){
		assertEquals("Datum validace je chybný.", datum, vysledek1.getDatumValidace());
	}

	/**
	 * Test správného nastavení data a času validace.
	 */
	@Test
	public void testGetCasaDatumValidace(){
		assertEquals("Čas a datum validace je chybný.", casDatum, vysledek1.getCasaDatumValidace());
	}

	/**
	 * Test instance VysledekValidaceUML při neúspěšném pokusu o odevzdání s chybou chybaUML_1.
	 */
	@Test
	public void testGetVysledek1(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceSpatneVysledky), vysledek1.getVysledek());
	}


	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s chybou chybUML_2.
	 */
	@Test
	public void testGetVysledek2(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceSpatneVysledky), vysledek2.getVysledek());
	}

	
	/**
	 * Test správnosti nastavení identifikátoru obecné chyby, tento parametr by 
	 * vzhledem k povaze programu měl být vždy "0".
	 */
	@Test
	public void testGetObecnachyba1(){
		assertFalse("Identifikátor obecné chyby je chybně nastaven.", vysledek1.getObecnachyba());
	}
	
	/**
	 * Test správnosti nastavení identifikátoru obecné chyby, tento parametr by
	 * vzhledem k povaze programu měl být vždy "0".
	 */
	@Test
	public void testGetObecnachyba2() {
		assertFalse("Identifikátor obecné chyby je chybně nastaven.", vysledek2.getObecnachyba());
	}

	/**
	 * Test nastavení UML chyby.
	 */
	@Test
	public void testGetChybaUML1(){
		assertEquals("UML chyba je nastavena chybně. ",chybaUML_1, vysledek1.getChybaUML());
	}
	
	/**
	 * Test nastavení UML chyby.
	 */
	@Test
	public void testGetChybaUML2(){
		assertEquals("UML chyba je nastavena chybně. ",chybaUML_2, vysledek2.getChybaUML());
	}
	

	/**
	 * Test správnosti metody toString.
	 */
	@Test
	public void testToString1(){
		String string = "[osobniCislo=" + osobniCislo + ", nazevOdevzdanehoSouboru=" +
		nazevOdevzdanehoSouboru + ", casValidace=" + vysledek1.getCasValidace() + 
		", datumValidace=" + vysledek1.getDatumValidace() + ", casaDatumValidace=" +
		vysledek1.getCasaDatumValidace() + ", vysledek=" + vysledek1.getVysledek() +
		", idUlohy=" + vysledek1.getIdUlohy() + ", obecnaChyba=" + obecnaChybaNE +
		", chybaUML=" + chybaUML_1 + "]\n";
		assertEquals("Chyba v metodě toString().", string, vysledek1.toString());
	}
	
	/**
	 * Test správnosti metody toString.
	 */
	@Test
	public void testToString2(){
		String string = "[osobniCislo=" + osobniCislo + ", nazevOdevzdanehoSouboru=" +
		nazevOdevzdanehoSouboru + ", casValidace=" + vysledek2.getCasValidace() + 
		", datumValidace=" + vysledek2.getDatumValidace() + ", casaDatumValidace=" +
		vysledek2.getCasaDatumValidace() + ", vysledek=" + vysledek2.getVysledek() +
		", idUlohy=" + vysledek2.getIdUlohy() + ", obecnaChyba=" + obecnaChybaNE +
		", chybaUML=" + chybaUML_2 + "]\n";
		assertEquals("Chyba v metodě toString().", string, vysledek2.toString());
	}

}
