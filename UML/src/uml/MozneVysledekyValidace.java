package uml;
/**
 * Výčtový typ obsahující možné výsledky validace.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public enum MozneVysledekyValidace{
	
	OK("OK"),SPATNE_VYSLEDKY("Špatné výsledky"), CHYBA_SERVERU("Chyba serveru"),
	CHYBA_PRI_VALIDACI("Chyba při validaci"), SPATNÝ_VSTUP_SERVERU("Špatný vstup serveru"),
	CHYBA_PRI_PREKLADU("Chyba při překladu"), VYPRSENI_CASU_TIMEOUT("Vypršení času (timeout)");
	
	private String popisVysledku;
	
	/**
	 * Nastaví výsledek validace.
	 * @param vysledek
	 */
	private MozneVysledekyValidace(String popisVysledku){
		this.popisVysledku = popisVysledku;
	}
	
	/**
	 * Vrátí popis výsledku validace jako String.
	 * @return popisVysledku
	 */
	public String getPopisVysledku(){
		return popisVysledku;
	}
	
	/**
	 * Nastaví odpovidajicí výsledek validace, který vrací jako svou návratovou hodnotu.
	 * @param vysledek
	 * @return
	 */
	public static MozneVysledekyValidace getVysledekValidace(String vysledek){
		for(MozneVysledekyValidace vys: MozneVysledekyValidace.values()){
		      if(vys.popisVysledku.equals(vysledek)){
		        return vys;
		      }
		    }
		return null;
	}
}
