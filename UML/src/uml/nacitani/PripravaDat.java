package uml.nacitani;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uml.Konstanty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tato třída slouží k načtení a přípravě vstupních dat k dalšímu zpracování.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class PripravaDat{
	/** <rok validace,pole všech validací tento rok> */
	private TreeMap<Integer, ArrayList<VysledekValidaceUML>> chybneVysledkyUML = 
			new TreeMap<Integer, ArrayList<VysledekValidaceUML>>();

	/**
	 * Konstruktor, který zavolá metodu najdiHtmlSoubory().
	 * @param vstup
	 * @throws IOException 
	 */
	public PripravaDat(String vstup){
		najdiHtmlSoubory(vstup);
	}
	
	/**
	 * Nastaví idUlohy.
	 * @param nazevOdevzdanehoSouboru
	 */
	private String zjistiCisloUlohy(String nazevOdevzdanehoSouboru){
		return Konstanty.ID_JMENO_ULOHY.get(nazevOdevzdanehoSouboru);
	}
	
	
	/**
	 * Zjistí datum a čas validace.	
	 * @param casdatum
	 * @return
	 */
	private  LocalDateTime zjistiDatumaCas(String casdatum){
		String[] c = casdatum.split(", "); //na nulté pozici je datum, na první pozici je čas
		String[] d = c[0].split(". ");
		if(d[1].length() == 1) {
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
		 * @param jmeno - jméno souboru
		 */
	
	public  void  najdiHtmlSoubory(String jmeno){
		 File adresar = new File(jmeno);
		 File[] soubory = adresar.listFiles();
		 for(File s: soubory){
			  //pokud je soubor složka, tak rekurzivně hledám dál
			 if(s.isDirectory()){
				 String novyAdresar = jmeno+"/"+s.getName();
				 najdiHtmlSoubory(novyAdresar);
			 }
			 
			 else{
				 //pokud soubor není složka, tak ještě zkontroluji zda je to .html soubor
				 if(s.getName().endsWith(".html")){
					 if(s != null){		
						 cteniDat(s);
					 }
				 }
			 }
		 }
	}
	
	
	/**
	 * Vybere požadované informace z .html souboru a uloží je do pole
	 * @param soubor - .html soubor, ze ktereho zrovna načítám data
	 * @throws IOException
	 */
	public void cteniDat(File soubor){
        Document doc;
		try{
			doc = Jsoup.parse(soubor, "UTF-8");
	        Elements tabulka = doc.select("table");   //požadovaná data jsou v tabulce
	        Elements pre=doc.select("pre");
	        
	        
	        /**pole informaci z html souboru
	         * 0-osobní číslo
	         * 1-název odevzdávaného souboru
	         * 2-datum a čas validace
	         * 3-výsledek validace
	         * 4-obecná chyba
	         * 5-UML chyba
	         */
	        String pole[] = new String[6]; 
	        Element radek = tabulka.select("tr").first(); //vybere radku z tabulky    
	         
	        for(int i = 0; i < 3; i++){
	        	if(radek != null){
	        		//vybere druýy sloupeček v řádku tabulky
	        		pole[i] = radek.select("th").first().nextElementSibling().text();
	        		radek = radek.nextElementSibling(); //preskočí na další řádek
	        	}
	        }
	        
	        //poslední informace je v html	jinak kodovana
	        if(radek != null){
	        	pole[3] = radek.select("td").text();
	        }
	  
	        String hledanyretezec = "Seznam chyb: ";
	        String pom = pre.text();
	
	        int pocatecniIndexChyby = pom.indexOf(hledanyretezec) + hledanyretezec.length() + 1;
	
	        /*pokud je v html nalezen retezec "hledany retezec" byla nalezena chyba v uml diagramu*/
	        if(pre.text().indexOf(hledanyretezec) > 0){
	        	String chyba = "";
	        	int i = pocatecniIndexChyby;
	        	while(pom.charAt(i) != ':'){
	        		chyba = chyba + pom.charAt(i);
	        		i++;
	        	}
	        	
	        	Pattern p = Pattern.compile(Konstanty.REGULARNI_VYRAZ); 
	       		Matcher m = p.matcher(pole[0]);  //osobní číslo majitele odevzdaného souboru
	    
	        	/* pokud osobni číslo v .html souboru spňluje podmínky regularniho vyrazu,
	        	 * je soubor dále zpracovaván*/
	        	if(m.find()){
	        		pole[4] = "0";
	        		pole[5] = chyba;
	        	}     	
	        	else{	
	        		return;
	        	}
	        }
	        else{ /* pokud řetězec nebyl nalezen odhalila validace obecnou chybu,
	         nebo se jedná o spraveně odevzdanou úlohu*/
	        	if(pole[3].compareTo("OK") == 0){ /*pokud je úloha spravně odevzdána*/
	        		pole[4] = "0";
	        	}
	        	else{ /* do této větve se dostanou validace s odhalenou obecnou chybou*/
	        		pole[4] = "1";
	        	}
	        }
	     
	        if((pole[5] != null) && (pole[5].charAt(0) == Konstanty.UmlChybaZacatek)){ 
	        	LocalDateTime casDatum = zjistiDatumaCas(pole[2]);
	   			int rok = casDatum.getYear();
	   			String idUlohy = zjistiCisloUlohy(pole[1]);
	   			boolean chyba = pole[4].equals("1") ? true :false;
	   			if(idUlohy != null){
		        	VysledekValidaceUML vysledekUML = new VysledekValidaceUML(pole[0], pole[1],
		        			idUlohy, casDatum, pole[3], chyba, pole[5]);
		        	if(chybneVysledkyUML.containsKey(rok) == false){
		        		chybneVysledkyUML.put(rok, null);
	       			}
	            	
	       			ArrayList<VysledekValidaceUML> pomocny;
	       			if(chybneVysledkyUML.get(rok) == null){
	       				pomocny = new ArrayList<VysledekValidaceUML>();
	       				chybneVysledkyUML.put(rok, pomocny);
	       			}
	       			else{
	       				pomocny = chybneVysledkyUML.get(rok);
	       			}
	   				pomocny.add(vysledekUML);
	   			}
	        }
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Vrátí kolekcí výsledků validací uml souborů odevzdaných s chybou v uml.
	 * @return
	 */
	public TreeMap<Integer, ArrayList<VysledekValidaceUML>> getVysledkyUML(){
		return chybneVysledkyUML;
	}
	
	@Override
	public String toString(){
		return "PripravaDat [  chybneVysledkyUML=" + chybneVysledkyUML.toString() + "]";
	}
}
