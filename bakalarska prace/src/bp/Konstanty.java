/**
 * 
 */
package bp;

import java.util.HashMap;

/**
 * Třída na zapouzdření konstant.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class Konstanty{
																																																																						
	public static final String [] JMENO_SOUBORU_PROJEKTU = {"TestOsoby.java", "Osoba.java", "03_VylepseniTridyOsoba.jar","04_PresouvaniOsob.jar","05_PlynulePosuvy.jar","06_HlavniTrida.jar","07_RozsireniADedicnost.jar", "08_Polymorfismus.jar","09_Kolekce.jar", "10_KolekceCele.jar", "11_Lambda.jar", "05_uml.jar", "06_uml.jar", "07_uml.jar", "08_uml.jar"};
	public static final String [] ID_ULOH = {  				  "DU-01",        "DU-02",               "DU-03",                 "DU-04",                   "DU-05",            "DU-06",              "DU-07",                      "DU-08",          "DU-09",            "DU-10",               "DU-11",     "UML-05",       "UML-06",      "UML-07",    "UML-08"};
		// symbolizuje, že úloha byla odevzdána bez UML chyby
	public static final String BEZ_UML_CHYBY = "t0000000";	
	public static final String OK = "OK";
	public static final String KONFIGURACE = "/project-data-start-end.txt";
	public static final int POCET_POVINNYCH_PARAMETRU = 3;
		// regulární výraz odpovídající osobnímu číslu studentů
	public static final String REGULARNI_VYRAZ = "[AESPKFRZD]\\d\\d[NBPM]\\d\\d\\d\\d[PKD]";  
		// přepínače tvorby grafu
	public static boolean JEN_USPESNE = true;
	public static HashMap<String, String> ID_JMENO_ULOHY = new HashMap<String, String>(); 
		static{
			   // Vytvori převodní tabulku pro nastavování identifikačních čísel úloh.
		     for(int i = 0;  i < JMENO_SOUBORU_PROJEKTU.length;  i++){
		    	 if(ID_ULOH[i].length() !=0) {
		    		 ID_JMENO_ULOHY.put(JMENO_SOUBORU_PROJEKTU[i], ID_ULOH[i]);
		    	 }
		     }
		}
	
		/* pokud je počet položek v největším sloubci menší než toto číslo,
		    vypínám v grafech automatickou tvorbu popisků */
	public static final int MIN_POCET_POLOZEK = 25; 
	public static String KONCOVKA = ".png";
	
		/* popisky grafu */
	public static final String GRAF_ODZADANI_POPIS_VSE = "Počet dní od zadání - všechny pokusy ";
	public static final String GRAF_ODZADANI_POPIS_USPESNE = "Počet dní od zadání - úspěšné pokusy ";
	public static final String GRAF_OD_ZADANI_POPIS_CELKOVY_VSE = "Odevzdané domácí úlohy ";
	public static final String GRAF_OD_ZADANI_POPIS_CELKOVY_USPESNE = "Počet úspěšných odevzdání ";
	public static final String GRAF_DENNI_DOBA_POPIS = "Hodina, kdy byl projekt odevzdán ";
	public static final String GRAF_DENNI_DOBA_POPIS_X = "Hodina validace ";
	public static final String POPIS_Y = "Počet pokusů";
	public static final String ODEVZDANI_OD_ZADANI_POPIS_X = "Den odevzdání od zadání";
	public static final String ODEVZDANI_DOMACICH_ULOH_POPIS_X = "Domácí úloha";
	public static final String LEGENDA_NEUSPESNE = "neúspěšné pokusy";
	public static final String LEGENDA_USPESNE_VCAS = "úspěšné včasné pokusy";
	public static final String LEGENDA_USPESNE_POZDE = "úspěšné opožděné pokusy";

		/* názvy souborů s grafy */
	public static final String GRAF_ODEVZDANI_OD_ZADANI_OK_SOUBOR = "/ok_";
	public static final String GRAF_ODEVZDANI_OD_ZADANI_VSE_SOUBOR = "/vse_";
	public static final String GRAF_STAV_DOMACICH_ULOH_VSE_SOUBOR = "/odevzdane-DU_";
	public static final String GRAF_STAV_DOMACICH_ULOH_OK_SOUBOR = "/uspesne-odevzdane-DU_";
	public static final String GRAF_DENNI_DOBA_SOUBOR = "/";
	
		/* podsložky grafu */
	public static final String GRAF_DENNI_DOBA_VSE_SLOZKA = "/denni-doba-pokusu";
	public static final String GRAF_ODZADANI_VSE_SLOZKA = "/dnu-od-zadani_vse";
	public static final String GRAF_OD_ZADANI_USPESNE_SLOZKA = "/dnu-od-zadani_ok";
	public static final String VYKRESLI_GRAF_SLOZKA = "/celkovy-graf";
	public static final String GRAF_STAV_ODEVZDANI_DOMACICH_ULOH_SLOZKA = "/stav-odevzdavani";
	
		/* konstanty pro VykresliGraf */
	public static final int X = 1900;
	public static final int Y = 1000;
	public static final int OKRAJE = 100;
	public static final int SIRKA_SLOUPCE = 10;
	public static final int TLOUSTKA_OS = 4;
	public static final int POCATEK_X = OKRAJE;
	public static final int POCATEK_Y = OKRAJE + TLOUSTKA_OS;
	public static final int SIRKA = X - 2 * OKRAJE - TLOUSTKA_OS;
	public static final int VYSKA = -TLOUSTKA_OS + Y - 2 * OKRAJE;
	public static final int DELKA_POPISKU = 50;
	public static final int POSUN_TITULKU = 95;
	public static final int KOEFICIENT_TITULKU = 10;
	public static final int Y_POSUN_ID_ULOHY = Y/15;
	public static final int DELKA_SOURADNICE_P = 5;
	public static final int DELKA_SOURADNICE_L = 3;
	public static final int POPISEK_REZ = 10;
	public static final int POPISEK_VELIKOST = 40;
	public static final int ZACATEK_SLOUPECKU_POSUN =10;
	public static final int NASOBEK_OS = 4;
	public static final double X_TROJUHELNIK = 1.5;
	public static final double Y_TROJUHELNIK = 2.5;
	public static final int DELKA_PRAZDNEHO_JMENA = 5;
	
		/* konstanty pro agregované grafy */
	public static final int SIRKA_A = 1550;
	public static final int VYSKA_A = 900;
	
	
	private Konstanty(){
		// knihovni trida
	}
}
