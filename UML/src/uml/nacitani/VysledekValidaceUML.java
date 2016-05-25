/**
 * 
 */
package uml.nacitani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import uml.MozneVysledekyValidace;

/** 
 * Tato třída reprezezentuje výstup validace odevdaného UML souboru.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class VysledekValidaceUML{
	/**osobní číslo studenta, který ulohu odevzdal*/
	private String osobniCislo;
	private String nazevOdevzdanehoSouboru;
	private LocalTime casValidace;
	private LocalDate datumValidace;
	private LocalDateTime casaDatumValidace;
	private MozneVysledekyValidace vysledek;
	private String idUlohy;   
	boolean obecnaChyba; /* chyba v bodě 1-7 nebo v 8, ale nejedná se o uml chybu - na začátku
	.html souboru býva ERR*/
	String chybaUML; //každý v7sledek validace souboru má jednu
	
	/**
	 * Nastaví proměnné dle předaných parametrů.
	 * @param osobniCislo
	 * @param nazevOdevzdanehoSouboru
	 * @param idUlohy
	 * @param casDatum
	 * @param vysledekValidace
	 * @param obecnaChyba
	 * @param chybaUML
	 */
	public VysledekValidaceUML(String osobniCislo, String nazevOdevzdanehoSouboru,  String idUlohy,
			LocalDateTime casDatum, String vysledekValidace, boolean obecnaChyba, String chybaUML){
		this.osobniCislo = osobniCislo;
		this.nazevOdevzdanehoSouboru = nazevOdevzdanehoSouboru;
		this.idUlohy = idUlohy;
		setDatumaCas(casDatum);		
		this.vysledek = MozneVysledekyValidace.getVysledekValidace(vysledekValidace); 
		this.obecnaChyba = setObecnaChyba(obecnaChyba);
		this.chybaUML = chybaUML;
	}
	
	/**
	 * Nastaví datum a čas validace.
	 * @param casdatum
	 */
	private void setDatumaCas(LocalDateTime casDatum){
		this.casValidace = casDatum.toLocalTime();	
		this.datumValidace = casDatum.toLocalDate();
		this.casaDatumValidace = casValidace.atDate(datumValidace);
	}
	
	/**
	 * Nastaví příznak obecné chyby. V současné verzy programu je tato hodnota vždy false,
	 * neboť se zpravovávají jen soubory s chybou v uml.
	 * @param obecnaChyba
	 * @return pžíznak obecné chyby
	 */
	private boolean setObecnaChyba(boolean obecnaChyba){
		if (obecnaChyba){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Vrátí osobní číslo jako řetězec.
	 * @return osobniCislo
	 */
	public String getOsobniCislo(){
		return osobniCislo;
	}
	
	/**
	 * Vrátí idUlohy jako řetězec.
	 * @return idUlohy
	 */
	public String getIdUlohy(){
		return idUlohy;
	}

	/**
	 * Vrátí nazevodevzdanehoSouboru jako řetězec.
	 * @return nazevOdevzdanehoSouboru
	 */
	public String getNazevOdevzdanehoSouboru(){
		return nazevOdevzdanehoSouboru;
	}

	/**
	 * Vrátí čas validace.
	 * @return casValidace
	 */
	public LocalTime getCasValidace(){
		return casValidace;
	}

	/**
	 * Vrátí datum validace.
	 * @return datumValidace
	 */
	public LocalDate getDatumValidace(){
		return datumValidace;
	}

	/**
	 * Vrátí datum a čas validace.
	 * @return casaDatumValidace
	 */
	public LocalDateTime getCasaDatumValidace(){
		return casaDatumValidace;
	}

	/**
	 * Vráti výsledek validace.
	 * @return vysledek
	 */
	public MozneVysledekyValidace getVysledek(){
		return vysledek;
	}
	
	/**
	 * Vrátí příznak existence obecné chyby.
	 * @return
	 */
	public boolean getObecnachyba(){
		return obecnaChyba;
	}
	
	/**
	 * Vrátí popis uml chyby.
	 * @return chybaUML
	 */
	public String getChybaUML(){
		return chybaUML;
	}

   	@Override
	public String toString(){
		return "[osobniCislo=" + osobniCislo
				+ ", nazevOdevzdanehoSouboru=" + nazevOdevzdanehoSouboru
				+ ", casValidace=" + casValidace + ", datumValidace="
				+ datumValidace + ", casaDatumValidace=" + casaDatumValidace
				+ ", vysledek=" + vysledek + ", idUlohy=" + idUlohy
						+ ", obecnaChyba=" + obecnaChyba + ", chybaUML=" + chybaUML+ "]\n";
	}
}