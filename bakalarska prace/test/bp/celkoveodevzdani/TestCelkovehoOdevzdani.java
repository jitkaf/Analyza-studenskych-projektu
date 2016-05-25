/**
 * 
 */
package bp.celkoveodevzdani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import bp.Konstanty;
import bp.nactenidat.VysledekValidaceSouboru;

/**
 * Testovací třída. Všechny mapy jsou zda nastaveny ručně na požadované hodnoty dle zadaného
 * ukázkového obrázku (mimo program). Následně je vytvořen graf. Pomocí shodnosti/neshodnosti zadaného
 * obrázku a vygenerovaného grafu lze ověřit/vyvrátit funčknost tříd GrafyStudentuVKonkretnimRoce a 
 * VykresliGraf.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class TestCelkovehoOdevzdani {
	private static ArrayList<VysledekValidaceSouboru> vysledky = new  ArrayList<VysledekValidaceSouboru>();
	private static Map<String, String> jmenoOsCislo = new TreeMap<String, String>();
	private static Map<String, LocalDate> deadliny = new TreeMap<String, LocalDate>();
	private static Map<String, LocalDate>zacatky = new  TreeMap<String, LocalDate>();
	private static String osCislo = "A13B0230P";
	private static String jmeno = "Zkušební Žák";
	
	/**
	 * Main testovacího programu.
	 * @param args
	 */
	public static void main(String[] args){
		nastavDeadlinyAZacatky();
		nastavVysledky();
		new GrafyStudentuVKonkretnimRoce(2015, vysledky, "testCelkovyGraf", true);
	}

	/**
	 * Nastaví výsledky na příslušné hodnoty, které jsou zde přímo zadány.
	 */
	private static void nastavVysledky(){
		String idUlohy = Konstanty.ID_JMENO_ULOHY.get("TestOsoby.java");
		String vysledekValidace = "Špatné výsledky";
		LocalDateTime casdatum = LocalDateTime.of(2015, 9, 25, 10, 15, 8);
		VysledekValidaceSouboru vysledek = new VysledekValidaceSouboru(osCislo, jmeno,
				idUlohy, casdatum, vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		vysledky.add(vysledek);

		casdatum = LocalDateTime.of(2015, 9, 26, 11, 10, 9);
		vysledekValidace = "OK";
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		casdatum = LocalDateTime.of(2015, 9, 30, 11, 10, 9);
		vysledekValidace = "Špatné výsledky";
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		vysledky.add(vysledek);
				
		vysledekValidace = "OK";
		vysledek=new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		
		idUlohy = Konstanty.ID_JMENO_ULOHY.get("Osoba.java");
		casdatum = LocalDateTime.of(2015, 10, 1, 11, 10, 9);
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		vysledekValidace = "Špatné výsledky";
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		vysledky.add(vysledek);
		vysledky.add(vysledek);
		
		idUlohy = Konstanty.ID_JMENO_ULOHY.get("03_VylepseniTridyOsoba.jar");
		casdatum = LocalDateTime.of(2015, 10, 7, 11, 10, 9);
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		idUlohy = Konstanty.ID_JMENO_ULOHY.get("04_PresouvaniOsob.jar");
		casdatum = LocalDateTime.of(2015, 10, 8, 11, 10, 9);
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		vysledky.add(vysledek);
		
		casdatum = LocalDateTime.of(2015, 10, 9, 11, 10, 9);
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		casdatum = LocalDateTime.of(2015, 10, 10, 11, 10, 9);
		vysledekValidace = "OK";
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
		
		casdatum = LocalDateTime.of(2015, 10, 15, 11, 10, 9);
		vysledek = new VysledekValidaceSouboru(osCislo, jmeno, idUlohy, casdatum,
				vysledekValidace, jmeno, zacatky.get(idUlohy), deadliny.get(idUlohy));
		vysledky.add(vysledek);
	}

	/**
	 * Nastaví začátky řešení a deadliny validací.
	 */
	private static void nastavDeadlinyAZacatky(){
		zacatky.put(Konstanty.ID_ULOH[0], LocalDate.of(2015, 9, 25));
		deadliny.put(Konstanty.ID_ULOH[0], LocalDate.of(2015, 9, 30));
		zacatky.put(Konstanty.ID_ULOH[1], LocalDate.of(2015, 9, 25));
		deadliny.put(Konstanty.ID_ULOH[1], LocalDate.of(2015, 10, 30));
		zacatky.put(Konstanty.ID_ULOH[2], LocalDate.of(2015, 10, 1));
		deadliny.put(Konstanty.ID_ULOH[2], LocalDate.of(2015, 10, 6));
		zacatky.put(Konstanty.ID_ULOH[3], LocalDate.of(2015, 10, 6));
		deadliny.put(Konstanty.ID_ULOH[3], LocalDate.of(2015, 10, 12));
	}
}
