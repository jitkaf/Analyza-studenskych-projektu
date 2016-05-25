package bp.nactenidat;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import bp.Konstanty;
import bp.MozneVysledekyValidace;

/**
 * JUnit test třídy VysledekValidaceSouboru.
 * @author Jitka Fürbacherová
 * @version 1.0
 *
 */
public class VysledekValidaceSouboruTest {
	private String osobniCislo;
	private String nazevOdevzdanehoSouboru;
	private String vysledekValidaceSpatneVysledky;
	private String vysledekValidaceOk;
	private String vysledekValidaceChybaPriValidaci;
	private String vysledekValidaceChybaServeru;
	private String vysledekValidaceSpatnyVstupServeru;
	private String vysledekValidaceChybaPriPrekladu;
	private String vysledekValidaceTimeout;
	private VysledekValidaceSouboru vysledekSpatneVysledky;
	private VysledekValidaceSouboru vysledekOk;
	private VysledekValidaceSouboru vysledekOkPozde;
	private VysledekValidaceSouboru vysledekOkDeadlineNull;
	private VysledekValidaceSouboru vysledekChybaPriValidaci;
	private VysledekValidaceSouboru vysledekChybaServeru;
	private VysledekValidaceSouboru vysledekSpatnyVstupServeru;
	private VysledekValidaceSouboru vysledekChybaPriPrekladu;
	private VysledekValidaceSouboru vysledekTimeout;
	
	private LocalDateTime casDatum;
	private LocalDateTime casDatumPozde;
	private String idUlohy;  
	private String jmeno;
	private LocalDate deadline;
	private LocalDate deadlineNull;
	private LocalDate datumZacatku;
	private LocalDate datum;
	private LocalTime cas;
	

	/**
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		osobniCislo = "A13B0230P";
		nazevOdevzdanehoSouboru = "TestOsoby.java";
		idUlohy = Konstanty.ID_JMENO_ULOHY.get(nazevOdevzdanehoSouboru);
		casDatum = LocalDateTime.of(2015, 9, 26, 10, 15, 8);
		casDatumPozde = LocalDateTime.of(2015, 10, 6, 10, 15, 8);
	    vysledekValidaceSpatneVysledky = "Špatné výsledky";
	    vysledekValidaceOk = "OK";
	    vysledekValidaceChybaServeru = "Chyba serveru";
	    vysledekValidaceChybaPriValidaci = "Chyba při validaci";
		vysledekValidaceSpatnyVstupServeru = "Špatný vstup serveru";
		vysledekValidaceChybaPriPrekladu = "Chyba při překladu";
		vysledekValidaceTimeout = "Vypršení času (timeout)";
	    jmeno = "Zkušební Žák";
	    datumZacatku = LocalDate.of(2015, 9, 25);
	    deadline = LocalDate.of(2015, 9, 30);
	    deadlineNull=null;
	    datum = casDatum.toLocalDate();
	    cas = casDatum.toLocalTime();
	    
		vysledekSpatneVysledky = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceSpatneVysledky, jmeno, datumZacatku, deadline);
		vysledekOk = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceOk, jmeno, datumZacatku, deadline);
		vysledekOkPozde = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatumPozde, vysledekValidaceOk, jmeno, datumZacatku, deadline);
		vysledekOkDeadlineNull = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceOk, jmeno, datumZacatku, deadlineNull);
		vysledekChybaServeru = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceChybaServeru, jmeno, datumZacatku, deadline);
		vysledekChybaPriValidaci = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceChybaPriValidaci, jmeno, datumZacatku, deadline);
		vysledekSpatnyVstupServeru = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceSpatnyVstupServeru, jmeno, datumZacatku, deadline);
		vysledekChybaPriPrekladu = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceChybaPriPrekladu, jmeno, datumZacatku, deadline);
		vysledekTimeout = new VysledekValidaceSouboru(osobniCislo, nazevOdevzdanehoSouboru,
				idUlohy, casDatum, vysledekValidaceTimeout, jmeno, datumZacatku, deadline);
		

	}

	/**
	 * Test existence instance.
	 */
	@Test
	public void testVysledekValidaceSouboru(){
	    assertNotNull("Instanci se nepodařilo vytvořit", vysledekSpatneVysledky); 
	}

	/**
	 * Test správného nastavení osobního čísla.
	 */
	@Test
	public void testGetOsobniCislo(){
		assertEquals("Osobní číslo je chybné.", osobniCislo, vysledekSpatneVysledky.getOsobniCislo());
	}

	/**
	 * Test správného nastavení identifikačního čísla.
	 */
	@Test
	public void testGetIdUlohy(){
		assertEquals("Identifikační číslo úlohy je chybné", idUlohy, vysledekSpatneVysledky.getIdUlohy());
	}

	/**
	 * Test správného nastavení názvu souboru.
	 */
	@Test
	public void testGetNazevOdevzdanehoSouboru(){
		assertEquals("Název odevzdaného souboru je chybný.", nazevOdevzdanehoSouboru, vysledekSpatneVysledky.getNazevOdevzdanehoSouboru());
	}

	/**
	 * Test správného nastavení času validace.
	 */
	@Test
	public void testGetCasValidace(){
		assertEquals("Čas validace je chybný.", cas, vysledekSpatneVysledky.getCasValidace());
	}

	/**
	 * Test správného nastavení data validace.
	 */
	@Test
	public void testGetDatumValidace(){
		assertEquals("Datum validace je chybný.", datum, vysledekSpatneVysledky.getDatumValidace());
	}

	/**
	 * Test správného nastavení data a času validace.
	 */
	@Test
	public void testGetCasaDatumValidace(){
		assertEquals("Čas a datum validace je chybný.", casDatum, vysledekSpatneVysledky.getCasaDatumValidace());
	}

	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Špatné výsledky".
	 */
	@Test
	public void testGetVysledekSpatneVysledky(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceSpatneVysledky), vysledekSpatneVysledky.getVysledek());
	}

	/**
	 * Test instance VysledekValidaceSouboru při úspěšném pokusu o odevzdání s výsledkem "Ok".
	 */
	@Test
	public void testGetVysledekOk(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceOk), vysledekOk.getVysledek());
	}
	
	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Chyba serveru".
	 */
	@Test
	public void testGetVysledekChybaServeru(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceChybaServeru), vysledekChybaServeru.getVysledek());
	}

	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Chyba při validaci".
	 */
	@Test
	public void testGetVysledekChybaPriValidaci(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceChybaPriValidaci), vysledekChybaPriValidaci.getVysledek());
	}
	
	/**
	 * Test instance VýsledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Špatný vstup serveru".
	 */
	@Test
	public void testGetVysledekSpatnyVstupServeru(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceSpatnyVstupServeru), vysledekSpatnyVstupServeru.getVysledek());
	}

	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Chyba při překladu".
	 */
	@Test
	public void testGetVysledekChybaPriprekladu(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceChybaPriPrekladu), vysledekChybaPriPrekladu.getVysledek());
	}
	
	/**
	 * Test instance VysledekValidaceSouboru při neúspěšném pokusu o odevzdání s výsledkem "Vypršení času (timeout)".
	 */
	@Test
	public void testGetVysledekTimeout(){
		assertEquals("Výsledek validace je chybný.", MozneVysledekyValidace.getVysledekValidace(vysledekValidaceTimeout), vysledekTimeout.getVysledek());
	}
	
	
	/**
	 * Test správného nastavení deadline úlohy.
	 */
	@Test
	public void testGetDeadline(){
		assertEquals("Deadline úlohy je chybný.", deadline, vysledekSpatneVysledky.getDeadline());
	}

	/**
	 * Test správného nastavení přepínače "odevzdanoVcas" při včasném odevzdání.
	 */
	@Test
	public void testGetOdevzdazdanoVcasAno(){
		assertTrue("Přepínač \"odevzdánoVcas\" je nastaven chybně.", vysledekOk.getOdevzdazdanoVcas());
	}
	
	/**
	 * Test správného nastavení přepínače "odevzdanoVcas" při deadlinu nastaveném na null (nenalezen v konfiguračním souboru).
	 */
	@Test
	public void testGetOdevzdazdanoVcasDeadlineNull(){
		assertTrue("Přepínač \"odevzdánoVcas\" je nastaven chybně.", vysledekOkDeadlineNull.getOdevzdazdanoVcas());
	}
	
	/**
	 * Test správného nastavení přepínače "odevzdanoVcas" při opožděném odevzdání.
	 */
	@Test
	public void testGetOdevzdazdanoVcasNe(){
		assertFalse("Přepínač \"odevzdánoVcas\" je nastaven chybně.", vysledekOkPozde.getOdevzdazdanoVcas());
	}

	/**
	 * Test správného nastavení jména autora.
	 */
	@Test
	public void testGetJmenoAutora(){
		assertEquals("Jméno autora je chybné.", jmeno, vysledekSpatneVysledky.getJmenoAutora());
	}

	/**
	 * Test správného nastavení data začátku řešení úlohy (zveřejnění na validátoru).
	 */
	@Test
	public void testGetDatumZacatku(){
		assertEquals("Datum začátku úlohy je nastaven chybně.", datumZacatku, vysledekSpatneVysledky.getDatumZacatku());
	}
}
