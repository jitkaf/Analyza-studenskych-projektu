package bp.nactenidat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bp.Konstanty;


/**
 * Tato třída slouží k načtení a přípravě vstupních dat k dalšímu zpracování.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class PripravaDat{
	private final int POCET_ZNAKU_ROK = 4;
	private final int POCET_ZNAKU_DATUM = 10;
	
		/** <rok validace, pole všech validací tento rok> */
	private TreeMap<Integer, ArrayList<VysledekValidaceSouboru>> vysledky = 
			new TreeMap<Integer, ArrayList<VysledekValidaceSouboru>>();
		/** <rok, <jmeno, osCislo>> */
	private TreeMap<Integer, TreeMap<String, String>> jmenoOsCislo =
			new TreeMap <Integer, TreeMap<String, String>>();
		/** <rok, <idUlohy, datum>> */
	private  static TreeMap<String, TreeMap<String, LocalDate>> deadliny = 
			new TreeMap<String, TreeMap<String, LocalDate>>();
		/** <rok, <idUlohy, datum>> */
	private  static TreeMap<String, TreeMap<String, LocalDate>> zacatky =
			new TreeMap<String, TreeMap<String, LocalDate>>();
	int a = 0;

	/**
	 * V tomto konstruktoru jsou zavolány metody, ve kterých se načítají všechna vstupní data.
	 * Dále zavolá metodu najdiHtmlSoubory().
	 * @param vstup
	 * @param vstupniAdresarCharakteristikaRoku 
	 */
	public PripravaDat(String vstupniAdresatValidator, String vstupniAdresarCharakteristikaRoku){
		nastavDeadlinyAZacatky(vstupniAdresarCharakteristikaRoku);
		najdiSouborySeJmeny(vstupniAdresarCharakteristikaRoku);
		najdiHtmlSoubory(vstupniAdresatValidator);
	}

	/**
	 * Načte začátky a deadliny řešení úloh pro daný rok.
	 * @param vstupniAdresarCharakteristikaRoku
	 */
	private void nastavDeadlinyAZacatky(String vstupniAdresarCharakteristikaRoku){
		boolean zapisuj;
		BufferedReader br = null;
		FileInputStream fr;
		String s;
		try{
			fr = new FileInputStream(vstupniAdresarCharakteristikaRoku + Konstanty.KONFIGURACE);
			InputStreamReader ifr = new InputStreamReader(fr, "UTF-8");
			br = new BufferedReader(ifr);
			for(String id: Konstanty.ID_ULOH){
				zapisuj = false;
				while(((s = br.readLine()) != null)){
					if(s.equals("")){
						break;
					}				
					if(zapisuj){
						int index=s.indexOf(";") + 1;
						LocalDate start = LocalDate.parse(s.substring(0, index-1));
						String rok = s.substring(index, index + POCET_ZNAKU_ROK);
						LocalDate datum;
						datum = LocalDate.parse(s.substring(index, index + POCET_ZNAKU_DATUM));
						
						if(deadliny.get(id) == null){
							deadliny.put(id, null);
						}
				
						if(zacatky.get(id) == null){
							zacatky.put(id, null);
						}
					
						TreeMap <String, LocalDate> pomocnyZacatky = new TreeMap<String, LocalDate>();
						TreeMap <String, LocalDate> pomocnyKonce = new TreeMap<String, LocalDate>();
						pomocnyZacatky.put(rok, start);
						pomocnyKonce.put(rok, datum);
								
						if(deadliny.get(id) == null){
							deadliny.put(id, pomocnyKonce);
						}
						else{
							deadliny.get(id).putAll(pomocnyKonce);
						}
						
						if(zacatky.get(id) == null){
							zacatky.put(id, pomocnyZacatky);
						}
						else{
							zacatky.get(id).putAll(pomocnyZacatky);
						}
					}
			
					if(s.equals(id)){
						zapisuj=true;
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Najde soubor se jmény studentů.
	 * @param vstupniAdresarCharakteristikaRoku
	 */
	private void najdiSouborySeJmeny(String vstupniAdresarCharakteristikaRoku){
		File adresar = new File(vstupniAdresarCharakteristikaRoku);
		File[] soubory = adresar.listFiles();
		for(File s: soubory){
			if(!s.isDirectory()){
				//pokud soubor není složka, tak ještě zkontroluji zda je to .html soubor
				if(s.getName().endsWith(".csv")){
					if(s != null){		
						nactiJmenaAOsibniCisla(s.getName(), vstupniAdresarCharakteristikaRoku);
					}
				}
			}
		}
	}
		
	/**
	 * Nastaví idUlohy.
	 * @param nazevOdevzdanehoSouboru
	 */
	private String zjistiCisloUlohy(String nazevOdevzdanehoSouboru){
		return Konstanty.ID_JMENO_ULOHY.get(nazevOdevzdanehoSouboru);
	}
	
	/**
	 * Zjistí datum začátku řešení úlohy.
	 * @param idUlohy
	 * @param rok
	 * @return
	 */
	private LocalDate zjistiCasZacateku(String idUlohy, int rok){
		if(idUlohy != null){
			return zacatky.get(idUlohy).get("" + rok);
		}
		return null;
	}
	
	/**
	 * Zjistí deadliny řešení úlohy.
	 * @param idUlohy
	 * @param rok
	 * @return
	 */
	private LocalDate zjistiDeadline(String idUlohy, int rok){
		if(idUlohy != null){
				return deadliny.get(idUlohy).get("" + rok);
		}
		return null;
	}
	
	/**
	 * Nastaví správné jméno k patřičným osobním číslům.
	 * @param string
	 * @param vstupniAdresarCharakteristikaRoku
	 */
	private void nactiJmenaAOsibniCisla(String string, String vstupniAdresarCharakteristikaRoku){	 
		BufferedReader br = null;
		FileInputStream fr;
		TreeMap<String, String> pom = new TreeMap<String, String>();
		int rok = Integer.parseInt(string.substring(0, string.length() - POCET_ZNAKU_ROK));
		if(!jmenoOsCislo.containsKey(rok)){
			jmenoOsCislo.put(rok, null);
		}
		
		if(jmenoOsCislo.get(rok) != null){
			pom=jmenoOsCislo.get(rok);
		}
		
		try{
			fr = new FileInputStream(vstupniAdresarCharakteristikaRoku + "/" + string);
			InputStreamReader ifr = new InputStreamReader(fr, "UTF-8");
		    br = new BufferedReader(ifr);
			String radka;
			String[] udaje;
			radka = br.readLine();
			if(radka == null){
				return;
			}
			
			while((radka = br.readLine()) != null){
				udaje = radka.split("\";\"");
				pom.put(udaje[0].substring(1), udaje[1] + " " + udaje[2]);
				jmenoOsCislo.put(rok, pom);
			}
			
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
		
	/**
	 * Zjistí jméno autora.
	 * @param rok
	 * @param osCislo
	 * @return
	 */
	private String zjistiJmenoAutora(int rok, String osCislo){
		String jmeno = jmenoOsCislo.get(rok).get(osCislo);
		return jmeno;
	}

	/**
	 * Zjistí datum a čas validace.	
	 * @param casdatum
	 * @return
	 */
	private  LocalDateTime zjistiDatumaCas(String casdatum){
		String[] c = casdatum.split(", "); //na nulté pozici je datum, na první pozici je čas
		String[] d = c[0].split(". ");
		if(d[1].length() == 1){
			d[1] = "0" + d[1];
		}
		
		if(d[0].length() == 1){
			d[0] = "0" + d[0];
		}
			
		String datum = d[2] + "-" + d[1] + "-" + d[0];
		String[] cas = c[1].split(" ");
		LocalTime casValidace = LocalTime.parse(cas[0]);	
		LocalDate datumValidace = LocalDate.parse(datum);
		
		return casValidace.atDate(datumValidace);
	}
		
	/**
	 * Najde všechny .html soubory a nad každým zavolá metodu cteniDat()
	 * @param jmeno - souboru
	 * @throws IOException 
	 */
	private  void  najdiHtmlSoubory(String jmeno){
		 File adresar = new File(jmeno);
		 File[] soubory = adresar.listFiles();
		 for(File s: soubory) {
			  //pokud je soubor složka, tak rekurzivně hledám dál
			 if(s.isDirectory()) {
				 String novyAdresar = jmeno + "/" + s.getName();
				 najdiHtmlSoubory(novyAdresar);
			 }
			 else{
				 //pokud soubor není složka, tak ješte zkontroluji zfa je to .html soubor
				 if (s.getName().endsWith(".html")){
					 if (s != null){		
						 cteniDat(s);
					 }
				 }
			 }
		 }
	}
	
	/**
	 * Vybere požadované informace z .html souboru a uloží je do pole
	 * @param soubor - html soubor, ze kterého zrovna načítám data
	 * @throws IOException
	 */
	public void cteniDat(File soubor){
        Document doc;
		try{
			doc = Jsoup.parse(soubor, "UTF-8");
	        Elements tabulka = doc.select("table");   //požadovana data jsou v tabulce
	        //Elements pre = doc.select("pre");
	        
	        /** pole informací z html souboru
	         * 0-osobní číslo
	         * 1-název odevzdávaného souboru
	         * 2-datum a čas validace
	         * 3-výsledek validace
	         */
	        String pole[] = new String[4]; 
	        Element radek = tabulka.select("tr").first(); //vybere řádek z tabulky    
	         
	        for(int i = 0; i < 3; i++){
	        	if(radek != null){
	        		//vybere druhý sloupeček v řádku tabulky
	        		pole[i] = radek.select("th").first().nextElementSibling().text();
	        		radek = radek.nextElementSibling(); //přeskočí na další řádek
	        	}
	        }
	        
	        //poslední informace je v .html	jinak kodovana
	        if(radek != null) {
	        	pole[3] = radek.select("td").text();
	        }
	  
	       	if((pole[0] != null) && (pole[1] != null) && (pole[2] != null) && (pole[3] != null)){ 
	       		Pattern p = Pattern.compile(Konstanty.REGULARNI_VYRAZ); 
	       		Matcher m = p.matcher(pole[0]);  //osobní číslo majitele odevzdaného souboru
	            
	       		/* pokud osobní číslo v .html souboru splňuje podmínky regularního výrazu, je soubor dále zpracováván */
	       		if(m.find()){
	       			LocalDateTime casDatum = zjistiDatumaCas(pole[2]);
	       			int rok = casDatum.getYear();
	       			String jmeno = zjistiJmenoAutora(rok, pole[0]);
	       			String idUlohy = zjistiCisloUlohy(pole[1]);
	       			LocalDate casZacatku = zjistiCasZacateku(idUlohy, rok);
	       			LocalDate deadline = zjistiDeadline(idUlohy, rok);
	       			if(idUlohy != null){
		       			VysledekValidaceSouboru vysledek = new VysledekValidaceSouboru(pole[0],
		       					pole[1],idUlohy, casDatum, pole[3], jmeno, casZacatku, deadline);
		       			if(vysledky.containsKey(rok) == false){
		       				vysledky.put(rok, null);
		       			}
		            	
		       			ArrayList<VysledekValidaceSouboru> pom;
		       			if(vysledky.get(rok) == null){
		       				pom = new ArrayList<VysledekValidaceSouboru>();
		       				vysledky.put(rok, pom);
		       			}
		       			else{
		       				pom = vysledky.get(rok);
		       			}
		   				pom.add(vysledek);
	       			}
	       		}
	        }
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Vrátí údaje načtené ze vstupních souborů.
	 * @return vysledky
	 */
	public TreeMap<Integer, ArrayList<VysledekValidaceSouboru>> getVysledky(){
		return vysledky;
	}

	
	@Override
	public String toString(){
		return "PripravaDat [  odevzdavaniDenni=" + vysledky.toString() + "]";
	}
}
