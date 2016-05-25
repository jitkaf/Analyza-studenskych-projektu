package uml;
/**
 * 
 */


import java.util.HashMap;

/***
 * Třída na zapouzdření konstant.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class Konstanty {

	public static final String [] JMENO_SOUBORU_PROJEKTU  = { "05_uml.jar", "06_uml.jar", "07_uml.jar", "08_uml.jar"};
	public static String [] ID_ULOH = {                          "UML-05",       "UML-06",      "UML-07", "UML-08"};

	/* symbolizuje, ze uloha byla odevzdana bez UML chyby*/
	public static final char UmlChybaZacatek = 't';
	public static final int pocetZnakuChyby = 8;
	public static final String REGULARNI_VYRAZ = "[AESPKFRZD]\\d\\d[NBPM]\\d\\d\\d\\d[PKD]"; 
	public static final int POCET_POVINNYCH_PARAMETRU = 2;
	public static final int MAX_POCET = 30;
	public static final int MIN_HODNOTA = 2;
	
	public static HashMap<String, String> ID_JMENO_ULOHY = new HashMap<String, String>(); 
	static{
		   // Vytvori prevodni tabulku pro nastavovani identifikacnich cisel uloh.
	     for(int i = 0;  i < JMENO_SOUBORU_PROJEKTU.length;  i++){
	    	 if(ID_ULOH[i].length() !=0){
	    		 ID_JMENO_ULOHY.put(JMENO_SOUBORU_PROJEKTU[i], ID_ULOH[i]);
	    	 }
	     }
	 }
}
