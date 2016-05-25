package uml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;


import uml.nacitani.PripravaDat;
import uml.nacitani.VysledekValidaceUML;
import uml.tvorbaGrafu.ChybyUmlHistogram;


/**
 * Hlavní třída programu, ze které je spuštěna třída PripravaDat.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class Hlavni{
	/*proměnné načítané na vstupu programu*/
	private static String vstupniAdresarValidator;
	private static String vystupniAdresar;
	private static int rok = 0;
	/** <rok, <VysledekValidaceSouboru v daném roce>> */
	private static TreeMap<Integer, ArrayList<VysledekValidaceUML>> chybneVysledkyUML =
			new TreeMap<Integer, ArrayList<VysledekValidaceUML>>();

	public static  Scanner sc = new Scanner(System.in);
	
	/**
	 * Main programu.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args){
		while(args.length < 2){
			napoveda();
			System.out.println("Doplňte prosím vstupní parametry: ");
			args =sc.nextLine().replace("\"", "").split(" ");
		}

		if ((!zpracujVstup(args)) || (!kontrolaExistenceAdresaru())){
			return;
		}
		
		PripravaDat priprava = new PripravaDat(vstupniAdresarValidator);
		chybneVysledkyUML = priprava.getVysledkyUML();
		zpracujPozadavkyVstupu();
		System.out.println("Program skončil.");
	}
	
	
	/**
	 * Kontrola existence adresářů zadaných uživatelem aplikace. Pokud se adresář nepodaří
	 * nalézt, vrátí false. Při úspěšném nalezení adresáře vrátí true.
	 * @return
	 */
	private static boolean kontrolaExistenceAdresaru(){
		boolean chyba = false;
		File validator = new File(vstupniAdresarValidator);
		if(!validator.exists()){
			chyba = true;
			System.out.println("Adresář " + vstupniAdresarValidator + " nebyl nalezen.");
		}
		
		File vystup = new File(vystupniAdresar);
		if(!vystup.exists()){
			chyba = true;
			System.out.println("Adresář " + vystupniAdresar + " nebyl nalezen.");
		}
		
		return !chyba;
	}
	
	/**
	 * Zpracuje požadavky na vstupu programu. V případě zadaného roku, zpracuje validace jen v tento rok
	 * v opačném případě všechna dostupná data.
	 * 
	 */
	private static void zpracujPozadavkyVstupu(){
		if(rok == 0){
			for (Entry<Integer, ArrayList<VysledekValidaceUML>> vysledkyValidaciVRoce: chybneVysledkyUML.entrySet()){
				int pomocnyRok = vysledkyValidaciVRoce.getKey();
				System.out.println("Zpracovávám rok: " + pomocnyRok);
				ArrayList<VysledekValidaceUML> vysledekzaRok = vysledkyValidaciVRoce.getValue();
				tvorGrafy(pomocnyRok, vysledekzaRok);
			}
		}
		else{
			if(chybneVysledkyUML.get(rok) == null){
				System.out.println("Byl zadán chybný rok. V dostupných datech se nevyskytují"
						+ "žádné validace odpovídající zadanému roku." );
			}
			else{
				tvorGrafy(rok, chybneVysledkyUML.get(rok));
			}
		}
	}

	/**
	 * V této metodě procházím celý seznam úloh a pro každou zvlášť zavolám třídu
	 * ChybyZmlHistogram. 
	 * @param pomocnyRok
	 * @param vysledekzaRok
	 */
	private static void tvorGrafy(int pomocnyRok, ArrayList<VysledekValidaceUML> vysledekzaRok){
		File vystupH = new File(vystupniAdresar + "/" + pomocnyRok);
		for(String idUlohy: Konstanty.ID_ULOH){
			ChybyUmlHistogram demoHistogram = new ChybyUmlHistogram("Histogram chyb v úloze " + idUlohy,
					vysledekzaRok, idUlohy, vystupH);
		}
	}
	
	/**
	 * V této metodě jsou zpracovány argumenty předané na příkazové řádce. 
	 * První dva parametry jsou povinné a po řadě znamenají umístění souborů s validacemi,
	 * a výstupní adresář. Dále může být uveden rok, pro který mají být grafy generovány.
	 * V případě absence tohoto parametru jsou zpracována všechna dostupná data. 
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
		vystupniAdresar = args[1];
		if(pocetArgumentu == Konstanty.POCET_POVINNYCH_PARAMETRU){
			return true;
		}
		else{
			if(zbyvPocOcekArgumen < pocetArgumentu){
				rok = prevedCislo(args[zbyvPocOcekArgumen]);
			}
		}
		
		return true;
	}

	/**
	 * Převede řetězec na celé číslo. V případě, že se převod nepodaří, vrací -1.
	 * @param string
	 * @return rok
	 */
	public static int prevedCislo(String string){
		int rok;
		try{
			rok = Integer.parseInt(string);
		}
		catch(NumberFormatException e){
			return -1;
		}
		return rok;
	}
	
	/**
	 * Vypíše nápovědu k zadávání vstupu.
	 */
	private static void napoveda(){
		System.out.println("--- Nápověda --- ");
		System.out.println("Program je nutno spustit s minimálně dvěmi parametry v následujícím pořadí: ");
		System.out.println("  1. adresář souborů s validacemi, např. C:\\data-oop\\validator");
		System.out.println("  2. výstupní adresář, např. D:\\zzz");
		System.out.println("Dále následuje volitelný parametr požadovaný rok, např. 2015 ");
	}

}
