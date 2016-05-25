package bp.celkoveodevzdani;

import java.io.File;
import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.TreeMap;

import bp.Konstanty;
import knihovna.*;
/**
 * Vytvoří graf pro daného studenta.
 * @author Jitka Fürbacherová
 * @version 1.0
 *
 */
public class VykresliGraf{
	private SpravcePlatna platno;
	private String osCislo;
	private String jmeno;
	private Obdelnik souradnice, podklad;
	private Trojuhelnik naX, naY;
	private int celkovyPocetPokusu = 0;
	private int maxPocetOdevzdaniZaDen = 0;
	private int vyskaJednotkovehopokusu;
	
	/**
	 * V tomto konstruktoru jsou jako parametr přijaty mapy s daty obsahující informace o validacích. 
	 * Postupně je voláno vykreslení pozadí plátna a celého grafu. 
	 * @param rok - kalendářní rok, ve kterém student odevzdával domácí úlohy
	 * @param osCislo - osobní číslo studenta
	 * @param jmeno - jméno studenta
	 * @param ulohaDenPocet - mapa s počty odevzdání úloh v jednotlivé dny
	 * @param ulohaDenPocetSpravne - mapa s počty úspěšných odevzdání úloh v jednotlivé dny, 
	 * obsahuje zápornou hodnotu, pokud byla úloha odevzdána po limitu
	 * @param celkovyPocetDnuOdevzdavani - celkový počet dní, kdy student úlohy odevzdával
	 * @param pocetDnuOdevzdavani - celkový počet dnů odevzdání
	 * @param vystupniAdresar - kořenový adresář, do kterého mají být grafy generovány.
	 */
	public VykresliGraf(int rok, String osCislo, String jmeno, TreeMap<String,
			TreeMap<LocalDate, Integer>> ulohaDenPocet,
			TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocetSpravne,
			int celkovyPocetDnuOdevzdavani, TreeMap<String, Integer> pocetDnuOdevzdavani,
			String vystupniAdresar){
		this.osCislo = osCislo;
		this.jmeno = (jmeno == null ? "Jméno nerozpoznáno" : jmeno);
		if(celkovyPocetDnuOdevzdavani < 1){
			return;
		}
		
		nastavMaxPocetOdevzdaniZaDen(ulohaDenPocet);
		vyskaJednotkovehopokusu = (Konstanty.VYSKA - 2*Konstanty.POCATEK_X)/maxPocetOdevzdaniZaDen;
		vykresliPozadi(celkovyPocetDnuOdevzdavani, pocetDnuOdevzdavani);
		vykresliSloupce(ulohaDenPocet, ulohaDenPocetSpravne, celkovyPocetDnuOdevzdavani, pocetDnuOdevzdavani);
		int delka = Konstanty.DELKA_PRAZDNEHO_JMENA;
		if(jmeno != null){
			 delka = jmeno.length();
		}
		int xPosuv = Konstanty.X/2 - Konstanty.POSUN_TITULKU - Konstanty.KOEFICIENT_TITULKU*delka;
		Text popisek = new Text(osCislo + " - " + jmeno, xPosuv,  Konstanty.OKRAJE/2);
		popisek.setFont("", Konstanty.POPISEK_REZ, Konstanty.POPISEK_VELIKOST);
		platno.pridej(popisek);
		uloz(rok, celkovyPocetPokusu, vystupniAdresar);
		platno.setViditelne(false);
		platno.odstranVse();
	}
	
	/**
	 * Zjistí maximální počet odevzdání v jeden den.
	 * @param ulohaDenPocet
	 */
	private void nastavMaxPocetOdevzdaniZaDen(TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocet){
		for(Entry<String, TreeMap<LocalDate, Integer>> ulohavDenPocet: ulohaDenPocet.entrySet()){
			TreeMap<LocalDate, Integer> denPocet = ulohavDenPocet.getValue();
			if(denPocet != null){
				for(Entry <LocalDate, Integer> pocetOdevzdani: denPocet.entrySet()){
					if(pocetOdevzdani.getValue() > maxPocetOdevzdaniZaDen){
						maxPocetOdevzdaniZaDen = pocetOdevzdani.getValue();
					}
				}
			}
		}
	}

	/**
	 * Uloží vygenerovaný graf do souboru.
	 * @param rok
	 * @param celkovyPocetPokusu
	 * @param vystupniAdresar
	 */
	private void uloz(int rok, int celkovyPocetPokusu, String vystupniAdresar){
		File nazevSlozky = new File (vystupniAdresar);
		if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
		nazevSlozky = new File (vystupniAdresar + "/" + rok);
		if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
		nazevSlozky = new File (vystupniAdresar + "/" + rok + Konstanty.VYKRESLI_GRAF_SLOZKA);
		if(!nazevSlozky.exists()) {
			nazevSlozky.mkdir();
		}
		String nazev = vystupniAdresar + "/" + rok + Konstanty.VYKRESLI_GRAF_SLOZKA 
				+ "/" + osCislo + "_" + celkovyPocetPokusu + Konstanty.KONCOVKA;
		platno.ulozJakoObrazek(new File(nazev));
		
	}
	
	/**
	 * Vykreslí jednotlivé sloupečky grafu, reprezentující počet odevzdávání jednotlivých úloh
	 * v konkrétní dny. Nejdříve spočte šířku jednoho dne. Poté vykreslí žlutě obdelníky
	 * reprezentující všechny pokusy a přes ně poté případně zelené/červené 
	 * obdelníky reprezentujících počet úspěšných včasných/opožděných odevzdání. 
	 * @param ulohaDenPocet - <úloha, <den, počet všech odevzdání úlohy v daný den>>
	 * @param ulohaDenPocetSpravne - počet úspěšných odevzdání úlohy v daný den, 
	 * pokud je po termínu, je vynásobeno -1
	 * @param celkovyPocetDnuOdevzdavani - <uloha, počet dnů, ve které student odevzdával danou ulohu>
	 * @param pocetDnuOdevzdavani - počet dnů, ve které student odevzdával úkoly
	 */
	private void vykresliSloupce(TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocet,
			TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocetSpravne, 
			int celkovyPocetDnuOdevzdavani, TreeMap<String, Integer> pocetDnuOdevzdavani){
		int prumer = (int) Konstanty.SIRKA/celkovyPocetDnuOdevzdavani;
		int pocetPokusu = 0;
		LocalDate index;
		int zacatekSloupecku = Konstanty.POCATEK_Y + prumer/2;
		Obdelnik sloupecek;
		int pocetSpravne = 0;	
		int sloupecekxl, sloupecekxp, sloupecekyl, sloupecekyp;
		for(String uloha: Konstanty.ID_ULOH){
			if(ulohaDenPocet.get(uloha) != null){
	        	 for(Entry<LocalDate, Integer> map: ulohaDenPocet.get(uloha).entrySet()){
			         pocetPokusu = map.getValue();
				     index = map.getKey();
				     if(pocetPokusu > 0){
				    	sloupecekxl = zacatekSloupecku-Konstanty.SIRKA_SLOUPCE/2;
				    	sloupecekyl = Konstanty.POCATEK_X - pocetPokusu * vyskaJednotkovehopokusu + Konstanty.VYSKA;
				    	sloupecekxp = Konstanty.SIRKA_SLOUPCE;
				    	sloupecekyp = vyskaJednotkovehopokusu*pocetPokusu;
				       	sloupecek = new Obdelnik(sloupecekxl, sloupecekyl, sloupecekxp, sloupecekyp, Barva.ZLUTA); 
				        platno.pridej(sloupecek);
				        Obdelnik sloupecekOK;
				       	if((ulohaDenPocetSpravne.get(uloha) !=  null) && (ulohaDenPocetSpravne.get(uloha).get(map.getKey())!= null)){
				       		pocetSpravne=ulohaDenPocetSpravne.get(uloha).get(map.getKey());
				       		int x = zacatekSloupecku -  Konstanty.SIRKA_SLOUPCE/2;
				       		int y = Konstanty.POCATEK_Y - pocetPokusu*vyskaJednotkovehopokusu  
				       				+ Konstanty.VYSKA - Konstanty.TLOUSTKA_OS;
				       		if(pocetSpravne < 0){
					       		pocetSpravne = Math.abs(pocetSpravne);
					       		int vyska = vyskaJednotkovehopokusu * pocetSpravne;
					       		sloupecekOK = new Obdelnik(x, y, Konstanty.SIRKA_SLOUPCE, vyska, Barva.CERVENA);
					       		platno.pridej(sloupecekOK);
				       		}
				       		else if(pocetSpravne > 0){
				       			int vyska = vyskaJednotkovehopokusu * pocetSpravne;
					       		sloupecekOK = new Obdelnik(x, y, Konstanty.SIRKA_SLOUPCE, vyska, Barva.ZELENA);
					       		platno.pridej(sloupecekOK);
				       		}
				       		celkovyPocetPokusu = celkovyPocetPokusu+pocetPokusu;
				       	}
			         
				       	String popisek = index.getDayOfMonth() + "." + index.getMonthValue() + ".";
				       	int x = zacatekSloupecku - Konstanty.SIRKA_SLOUPCE/2 - Konstanty.ZACATEK_SLOUPECKU_POSUN;
				       	int y = Konstanty.Y - Konstanty.OKRAJE;
				       	platno.pridej(new Text(popisek, x, y));
				       	zacatekSloupecku = zacatekSloupecku + prumer;
				     }
	        	 }
	         }
		}
	}

	/**
	 * Vykreslí pozadí plátna. Nejdříve jsou vykresleny souřadné osy. Do horní části grafu jsou 
	 * umístěny popisky jednotlivých úloh. Na plátno jsou vykresleny jednotlivé sloupce reprezentující 
	 * pozadí, do kterého budou vykreslovány sloupečky reprezentující počet odevzdání dané úlohy 
	 * v jednotlivé dny. Barva těchto sloupců se pro lepší přehlednost střídá.
	 * @param celkovyPocetDnuOdevzdavani
	 * @param pocetDnuOdevzdavani
	 */
	private void vykresliPozadi(int celkovyPocetDnuOdevzdavani, TreeMap<String, Integer> pocetDnuOdevzdavani){
		platno = platno.getInstance();
		platno.odstranVse();
		platno.setViditelne(false);
		platno.setKrok(1);
		platno.setMrizka(false);
		platno.setRozmer(new Rozmer(Konstanty.X, Konstanty.Y));
		
		int souradnicexl = Konstanty.OKRAJE;
		int souradniceyl = Konstanty.OKRAJE + Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
		int souradnicexp = Konstanty.X - 2*Konstanty.OKRAJE - Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
		int souradniceyp = Konstanty.Y - 2*Konstanty.OKRAJE - Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
		souradnice = new Obdelnik(souradnicexl, souradniceyl, souradnicexp, souradniceyp, Barva.CERNA);
		podklad = new Obdelnik(Konstanty.POCATEK_Y, Konstanty.POCATEK_X, Konstanty.SIRKA, 
				Konstanty.VYSKA, Barva.BILA);
		platno.pridej(souradnice);
		platno.pridej(podklad);
		int pocetOdevzdavanychUloh = 1;		
		int start = Konstanty.POCATEK_Y;
		
		for(String item: Konstanty.ID_ULOH){
		    int sloupecek=(int)(Konstanty.SIRKA/celkovyPocetDnuOdevzdavani*pocetDnuOdevzdavani.get(item));
		    int x = start + sloupecek/2 - Konstanty.DELKA_POPISKU/2;
		    if(pocetOdevzdavanychUloh % 2 == 0){
		    	if(sloupecek > 0){
		    		Obdelnik vypln = new Obdelnik(start, Konstanty.POCATEK_X, sloupecek, Konstanty.VYSKA, Barva.KOUROVA);
		    		platno.pridej(new Text(item, x, Konstanty.OKRAJE*2));
		    		platno.pridej(vypln);
		    	}
		    }
		    
		    if (sloupecek > 0){
		    	platno.pridej(new Text(item, x, Konstanty.OKRAJE*2));
		    	pocetOdevzdavanychUloh++;
		    }
		   start = start + sloupecek;
		}
	    int x = (int)(Konstanty.OKRAJE - Konstanty.X_TROJUHELNIK*Konstanty.TLOUSTKA_OS);
	    int sirka = Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
	    int vyska = Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
		naX = new Trojuhelnik(x, Konstanty.OKRAJE, sirka, vyska, Barva.CERNA);
		x = Konstanty.X-Konstanty.OKRAJE - Konstanty.NASOBEK_OS*Konstanty.TLOUSTKA_OS;
		int y = (int)(Konstanty.Y - Konstanty.OKRAJE - Konstanty.Y_TROJUHELNIK*Konstanty.TLOUSTKA_OS);
		naY = new Trojuhelnik(x,y, sirka, vyska, Barva.CERNA, Smer8.VYCHOD);
		platno.pridej(naX);
		platno.pridej(naY);
		
		int k = (int)(Konstanty.POCATEK_X + Konstanty.VYSKA - vyskaJednotkovehopokusu);
		int i = 1;
		while(k > Konstanty.POCATEK_X + Konstanty.OKRAJE*2 - Konstanty.TLOUSTKA_OS){ 
			souradnicexl = Konstanty.POCATEK_Y - Konstanty.DELKA_SOURADNICE_L*Konstanty.TLOUSTKA_OS;
			souradniceyl = k - Konstanty.TLOUSTKA_OS/2;
			souradnicexp = Konstanty.DELKA_SOURADNICE_P*Konstanty.TLOUSTKA_OS;
			souradniceyp = (int)Konstanty.TLOUSTKA_OS/2;
			
			Obdelnik sloupec = new Obdelnik(souradnicexl, souradniceyl, souradnicexp, souradniceyp, Barva.CERNA);
			platno.pridej(new Text("" + i, Konstanty.POCATEK_Y-8*Konstanty.TLOUSTKA_OS, k));
			platno.pridej(sloupec);
			k = k - vyskaJednotkovehopokusu;
			i++;
		}
	}
}
