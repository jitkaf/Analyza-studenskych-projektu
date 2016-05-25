/**
 * 
 */
package bp.nactenidat;


import java.time.*;
import bp.MozneVysledekyValidace;

/** 
 * Tato třída reprezezentuje výstup validace odevzdaného souboru.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class VysledekValidaceSouboru{
	private String osobniCislo;
	private String nazevOdevzdanehoSouboru;
	private LocalTime casValidace;
	private LocalDate datumValidace;
	private LocalDateTime casaDatumValidace;
	private MozneVysledekyValidace vysledek;
	private String idUlohy;  
	private String jmenoAutora;
	private LocalDate deadline;
	private LocalDate datumZacatku;
	private boolean odevzdanoVcas;
	
	/**
	 * Nastaví parametry.
	 * @param osobniCislo
	 * @param nazevOdevzdanehoSouboru
	 * @param idUlohy
	 * @param casdatum
	 * @param vysledekValidace
	 * @param jmeno
	 * @param datumZacatku
	 * @param deadline
	 */
	public VysledekValidaceSouboru(String osobniCislo, String nazevOdevzdanehoSouboru,
			String idUlohy, LocalDateTime casdatum, String vysledekValidace, String jmeno,
			LocalDate datumZacatku, LocalDate deadline){
		this.osobniCislo = osobniCislo;
		this.nazevOdevzdanehoSouboru = nazevOdevzdanehoSouboru;
		this.jmenoAutora = jmeno;
		this.datumZacatku = datumZacatku;
		this.deadline = deadline;
		setDatumaCas(casdatum);	
		this.idUlohy = idUlohy;
		this.vysledek = MozneVysledekyValidace.getVysledekValidace(vysledekValidace);
		setOdevzdanoVcas();
	}
	
	/**
	 * Nastaví přepínač včasného odevzdání na příslušnou hodnotu.
	 */
	private void setOdevzdanoVcas(){
		if((deadline == null) || ((deadline != null) && (deadline.getDayOfYear() >= datumValidace.getDayOfYear()))){
			odevzdanoVcas = true;
		}
		else{
			odevzdanoVcas = false;
		}
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
	 * Vrátí nazevOdevzdanehoSouboru jako řetězec.
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
	 * Vrátí výsledek validace.
	 * @return vysledek
	 */
	public MozneVysledekyValidace getVysledek(){
		return vysledek;
	}
	
	/**
	 * Vrátí deadline validace.
	 * @return
	 */
	public LocalDate getDeadline(){
		return deadline;
	}
	
	/**
	 * Vrátí true, pokud byla úloha odevzdána včas, jinak false.
	 * @return
	 */
	public boolean getOdevzdazdanoVcas(){
		return odevzdanoVcas;
	}

	/**
	 * Vrátí jméno autora.
	 * @return
	 */
	public String getJmenoAutora(){
		return jmenoAutora;
	}

	/**
	 * Vrátí datum začátku úlohy.
	 * @return
	 */
	public LocalDate getDatumZacatku(){
		return datumZacatku;
	}
	
	@Override
	public String toString() {
		return " [osobniCislo=" + osobniCislo
				+ ", nazevOdevzdanehoSouboru=" + nazevOdevzdanehoSouboru
				+ ", casValidace=" + casValidace + ", datumValidace="
				+ datumValidace + ", casaDatumValidace=" + casaDatumValidace
				+ ", vysledek=" + vysledek + ", idUlohy=" + idUlohy + ", deadline=" 
				+ deadline  +", odevzdanoVcas=" +odevzdanoVcas +"] \n";
	}
}
