package bp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import bp.agregovanegrafy.GrafDenniDoba;
import bp.agregovanegrafy.GrafOdevzdaniOdZadani;
import bp.agregovanegrafy.GrafStavOdevzdaniDomacichUloh;
import bp.celkoveodevzdani.GrafyStudentuVKonkretnimRoce;
import bp.nactenidat.PripravaDat;
import bp.nactenidat.VysledekValidaceSouboru;

/**
 * Hlavní třída celé aplikace. Zpracovává vstup, zavolá načítání dat a poté dle požadavků
 * na vstupu vytvoří grafy.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class Hlavni{
	/**proměnné načítané na vstupu programu*/
	private static String vstupniAdresarValidator;
	private static String vstupniAdresarKonfigurace;
	private static String vystupniAdresar;
	private static int rok = 0;
	private static boolean info;
	private static boolean agregovane = true;
	private static boolean jednotlive = true;
	/** <rok,<VysledekValidaceSouboru v daném roce>> */
	private static TreeMap<Integer, ArrayList<VysledekValidaceSouboru>> vysledky =
			new TreeMap<Integer, ArrayList<VysledekValidaceSouboru>>();
	/**<rok, <seznam studentů daný rok>>*/
	private static 	TreeMap<Integer,Set<String>> seznamOsStudentu =
			new TreeMap<Integer, Set<String>>();

	public static  Scanner sc = new Scanner(System.in);
	
	/**
	 * Main celého programu.
	 * @param args - vstup je nutné zadat v pořadí: in-validator in-charakteristika out a/j rok -info
	 * @throws IOException
	 */
	public static void main(String[] args){
		while(args.length < 3){
			napoveda();
			System.out.println("Doplňte prosím vstupní parametry: ");
			args =sc.nextLine().replace("\"", "").split(" ");
		}

		if ((!zpracujVstup(args))||(!kontrolaExistenceAdresaru())){
			return;
		}
		
		if(info){
			System.out.println("Informace o průběhu zpracování požadavku: "
					+ "\nNačítám vstupní data.");
		}
		
		PripravaDat priprava = new PripravaDat(vstupniAdresarValidator, vstupniAdresarKonfigurace);
		
		if(info){
			System.out.println("Všechno data byla načtena.");
		}
		vysledky = priprava.getVysledky();
		zpracujPozadavkyVstupu();
		if(info){
			System.out.println("Všechna data byla zpracována. Program úspěšně doběhl.");
		}
		System.exit(0);
	}

	
	/**
	 * Kontrola existence adresářů zadaných uživatelem aplikace. Pokud se adresář nepodaří
	 * nalézt, vrátí false. Při úspěšném nalezení adresáře vrátí true.
	 * @return
	 */
	private static boolean kontrolaExistenceAdresaru(){
		boolean chyba = false;
	
		File charakteristika = new File(vstupniAdresarKonfigurace + Konstanty.KONFIGURACE);
		if (!charakteristika.exists()){
			chyba = true;
			System.out.println("Adresář " + vstupniAdresarKonfigurace + " nebyl nalezen.");
		}
		
		File validator = new File(vstupniAdresarValidator);
		if (!validator.exists()){
			chyba = true;
			System.out.println("Adresář " + vstupniAdresarValidator + " nebyl nalezen.");
		}
		
		File vystup = new File(vystupniAdresar);
		if (!vystup.exists()){
			chyba = true;
			System.out.println("Adresář " + vystupniAdresar + " nebyl nalezen.");
		}
		
		return !chyba;
	}


	/**
	 *  V této metodě je zavolána metoda tvorGrafy() s ohledem na rok zadaný při spuštění programu.
	 *  Pokud je program spuštěn s parametrem značící rok, zavolá metodu tvorGrafy(rok, vysledky),
	 *  přičemž metodě předá pouze výsledky odpovídající zadanému roku.
	 *  V případě že je program spuštěný bez parametru specifikující rok (proměnná rok zůstane 
	 *  nastavena na 0) je metoda tvorGrafy(rok, vysledky) postupně zavolána pro všechny roky
	 *   dostupné ze vstupních dat s odpovídajícímy výsledky validací.
	 */
	private static void zpracujPozadavkyVstupu(){
		if(rok == 0){
			for (Entry<Integer, ArrayList<VysledekValidaceSouboru>> vysledkyValidaciVRoce: vysledky.entrySet()){
				int pomocnyRok = vysledkyValidaciVRoce.getKey();
				System.out.println("Zpracovávám rok: " + pomocnyRok);
				ArrayList<VysledekValidaceSouboru> vysledekzaRok = vysledkyValidaciVRoce.getValue();
				tvorGrafy(pomocnyRok, vysledekzaRok);
			}
		}
		else{
			if(vysledky.get(rok) == null){
				System.out.println("Byl zadán chybný rok. V dostupných datech se nevyskytují"
						+ "žádné validace odpovídající zadanému roku." );
			}
			else{
				tvorGrafy(rok, vysledky.get(rok));
			}
		}
	}


	/**
	 * V této metodě je s ohledem na zadané parametry vyvolána tvorba agregovaných
	 * a jednoltivých grafů. Pokud byl na vstupu zadán přepínač "a" jsou tvořeny pouze 
	 * agregované grafy. Pro případ přepínače "j" pouze jednotlivé grafy studentů. 
	 * Pokud není zadán přepínač a/j jsou vygenerovány agregované i jednotlivé grafy.
	 * @param pomocnyRok - rok pro který se grafy tvoří
	 * @param vysledekzaRok - výsledky validací daný rok
	 */
	private static void tvorGrafy(int pomocnyRok, ArrayList<VysledekValidaceSouboru> vysledekzaRok) {
		if(jednotlive){
			if (info){
				System.out.println("Zpracovávám jednotlivé grafy za rok " + pomocnyRok);
			}
		  new GrafyStudentuVKonkretnimRoce(pomocnyRok, vysledekzaRok, vystupniAdresar, info);
		}
		
		if(agregovane){
			for(String idUlohy: Konstanty.ID_ULOH){
				if(info){
					System.out.print("Zpracovávám agregované grafy: " + idUlohy + '\r');
				}
				 new GrafDenniDoba(Konstanty.GRAF_DENNI_DOBA_POPIS + idUlohy, vysledekzaRok, idUlohy,
						 pomocnyRok, vystupniAdresar);
				 new GrafOdevzdaniOdZadani(Konstanty.GRAF_ODZADANI_POPIS_VSE + pomocnyRok + " "  + idUlohy,
						 pomocnyRok, vysledekzaRok, idUlohy, !Konstanty.JEN_USPESNE, vystupniAdresar);
				 new GrafOdevzdaniOdZadani(Konstanty.GRAF_ODZADANI_POPIS_USPESNE + pomocnyRok + " " + idUlohy,
						 pomocnyRok, vysledekzaRok, idUlohy, Konstanty.JEN_USPESNE, vystupniAdresar);
			}
			
			if (info){
				System.out.println();
			}
			
	        new GrafOdevzdaniOdZadani(Konstanty.GRAF_ODZADANI_POPIS_VSE+ pomocnyRok, pomocnyRok,
	        		vysledekzaRok, null,!Konstanty.JEN_USPESNE, vystupniAdresar);
		    new GrafOdevzdaniOdZadani(Konstanty.GRAF_ODZADANI_POPIS_USPESNE  + pomocnyRok, pomocnyRok,
		    		vysledekzaRok, null,Konstanty.JEN_USPESNE, vystupniAdresar);
	    	new GrafStavOdevzdaniDomacichUloh(Konstanty.GRAF_OD_ZADANI_POPIS_CELKOVY_VSE+ pomocnyRok,
	    			pomocnyRok, vysledekzaRok,!Konstanty.JEN_USPESNE, vystupniAdresar);
		    new GrafStavOdevzdaniDomacichUloh(Konstanty.GRAF_OD_ZADANI_POPIS_CELKOVY_USPESNE + pomocnyRok,
		    		pomocnyRok, vysledekzaRok, Konstanty.JEN_USPESNE, vystupniAdresar);
		    new GrafDenniDoba(Konstanty.GRAF_DENNI_DOBA_POPIS + pomocnyRok, vysledekzaRok, null, pomocnyRok,
		    		vystupniAdresar);
		}
	}


	/**
	 * V této metodě jsou zpracovány argumenty předané na příkazové řádce. 
	 * První tří parametry jsou povinné a po řadě znamenají umístění souborů s validacemi,
	 * umístění souborů s konfigurací roku a výstupní adresář. Dále se může vyskytovat
	 * valitelný přepínač a/j, který udává, zda mají být generovány agregované nebo 
	 * jednotlivé grafy. V případě absence přepínače jsou generovány oba druhy. Dále
	 * může být uveden rok, pro který mají být grafy generovány. V případě absence tohoto 
	 * parametru jsou zpracována všechna dostupná data. Posledním parametrem je "-info".
	 * Pokud je tento parametr přítomen je postup generování grafů vypisován na o obrazovku,
	 *  v opačném případě nikoliv.
	 * @param args - argumenty příkazové řádky
	 * @return - vrací false při chybném zadání vstupu v opačném případě true
	 */
	private static boolean zpracujVstup(String[] args){
		int pocetArgumentu = args.length;
		int zbyvPocOcekArgumen = Konstanty.POCET_POVINNYCH_PARAMETRU;
		if(pocetArgumentu < Konstanty.POCET_POVINNYCH_PARAMETRU){
			napoveda();
			return false;
		}
		vstupniAdresarValidator = args[0];
		vstupniAdresarKonfigurace = args[1];
		vystupniAdresar = args[2];
		if (pocetArgumentu == Konstanty.POCET_POVINNYCH_PARAMETRU){
			return true;
		}
		else{
			if(args[pocetArgumentu-1].compareTo("-info") == 0){
				info = true;
				pocetArgumentu--;
			}
			
			if(pocetArgumentu > 3){
				if(args[3].compareTo("a") == 0){
					jednotlive = false;
					zbyvPocOcekArgumen++;
				}
				else if(args[3].compareTo("j") == 0){
					agregovane = false;
					zbyvPocOcekArgumen++;
				}
				
				if(zbyvPocOcekArgumen < pocetArgumentu){
					rok = prevedCislo(args[zbyvPocOcekArgumen]);
				}
			}
		}
		
		return  true;
	}
	
	/**
	 * Převede string na celé číslo. V případě, že se převod nepodaří, vrací -1.
	 * @param string
	 * @return rok
	 */
	public static int prevedCislo(String string){
		int rok;
		try{
			rok = Integer.parseInt(string);
		}
		catch(NumberFormatException e) {
			return -1;
		}
		return rok;
	}

	/**
	 * Vypíše nápovědu k zadávání vstupu.
	 */
	private static void napoveda(){
		System.out.println("--- Nápověda --- ");
		System.out.println("Program je nutno spustit s minimálně třemi parametry v následujícím pořadí: ");
		System.out.println("  1. adresář souborů s validacemi, např. C:\\data-oop\\validator");
		System.out.println("  2. adresář konfiguračních souborů, např. C:\\data-oop\\konfigurace");
		System.out.println("  3. výstupní adresář, např. D:\\zzz");
		System.out.println("Dále následují volitelné parametry v následujícím pořadí: ");
		System.out.println("  4. přepínač a/j agregované nebo jednotlivé grafy, bez přepínače vytvoří oba druhy, např. a");
		System.out.println("  5. požadovaný rok, např. 2015");
		System.out.println("  6. -info budou vypisovány průběžné informace o zpracování programu");
	}

	
	/**
	 * Vrací vstupní adresář s konfigurací roku.
	 * @return vstupniAdresarKonfigurace
	 */
	public String getVstupniAdresarKonfigurace(){
		return vstupniAdresarKonfigurace;
	}
	
	/**
	 * Vrací vstupní adresář se soubory validací.
	 * @return vstupniAdresarValidator
	 */
	public String getVstupniAdresarValidator(){
		return vstupniAdresarValidator;
	}

	/**
	 * Vrací výstupní adresář.
	 * @return vystupniAdresar
	 */
	public String getVystupniAdresar(){
		return vystupniAdresar;
	}

	
	/**
	 * Vrací rok, ke kterému se grafy tvoří. V případě nespecifikovaného roku vrací 0.
	 * @return rok
	 */
	public int getRok(){
		return rok;
	}

	/**
	 * Vrací true pro případ, že je zadán požadavek na vypisování postupných informací 
	 * o průběhu generování grafů. V opačném případě vrací false.
	 * @return  info
	 */
	public static boolean isInfo(){
		return info;
	}

	/**
	 * Vrací seznam osobních čísel studentů v aktuálně poslední zpracovávaný rok.
	 * @return seznamOsStudentu
	 */
	public static TreeMap<Integer,Set<String>> getSeznamOsStudentu(){
		return seznamOsStudentu;
	}


	@Override
	public String toString(){
		return "Hlavni [getVstupniAdresarKonfigurace()=" + getVstupniAdresarKonfigurace()
				+ ", getVstupniAdresarValidator()=" + getVstupniAdresarValidator()
				+ ", getVystupniAdresar()=" + getVystupniAdresar()
				+ ", agregovane= " + agregovane + ", jednotlive= " + jednotlive
				+ ", rok= " + rok + ", info= " + info + "]";
	}
}
