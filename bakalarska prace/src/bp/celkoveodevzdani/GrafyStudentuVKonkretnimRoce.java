/**
 * 
 */
package bp.celkoveodevzdani;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import bp.nactenidat.VysledekValidaceSouboru;
import bp.Konstanty;

/** 
 * Třída pro přípravu dat k vykreslení jednotlivých grafů.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class GrafyStudentuVKonkretnimRoce{
	private  String osCislo;
		/** <idUlohy, počet všech odevzdání této úlohy> */
	private  TreeMap<String, Integer> dnyAodevzdavani = new TreeMap<String, Integer>();
		/** <osobní číslo + jméno, výsledky validací daného studenta> */
	private  TreeMap<String, ArrayList<VysledekValidaceSouboru>> vysledkyStudenta =
		    new TreeMap<String, ArrayList<VysledekValidaceSouboru> >();
	private int celkovyPocetDnuOdevzdavani=0;
		/** <úloha <datum odevzdání, počet všech pokusů>> */
	private  TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocet =
			new TreeMap<String, TreeMap<LocalDate, Integer>>();
		/** <úloha <datum odevzdání, počet úspěšných pokusů>> */
	private	static  TreeMap<String, TreeMap<LocalDate, Integer>> ulohaDenPocetSpravne =
			new TreeMap<String, TreeMap<LocalDate, Integer>>();
	
	/**
	 * V tomto konstuktoru je nejprve vyvolána metoda pripravVysledkyStudenta(), ve které jsou
	 * výsledky rozřazeny k jednotlivým studentům. Poté je pro každého studenta vyvolána 
	 * třída VykresliGraf, ve které je vykreslen požadovaný graf.
	 * @param rok - kalendářní rok, pro který se data zpracovávají
	 * @param vysledky - všechny výsledky validací v daném roce
	 * @param vystupniAdresar - kořenový adresář, do kterého budou grafy tvořeny
	 * @param info - přepínač určující, zda má být vypisován průběh generování
	 */
	public GrafyStudentuVKonkretnimRoce(int rok, List<VysledekValidaceSouboru> vysledky, 
			String vystupniAdresar, boolean info){
		pripravVysledkyStudenta(rok, vysledky);
		int celkovyPocetStudentu = vysledkyStudenta.size();
		int j = 0;
		for(Entry<String, ArrayList<VysledekValidaceSouboru>> vys: vysledkyStudenta.entrySet()){
			this.osCislo = vys.getKey();
			ArrayList<VysledekValidaceSouboru> vysS = vys.getValue();
			for(int i = 0; i < Konstanty.ID_ULOH.length; i++){
				dnyAodevzdavani.put(Konstanty.ID_ULOH[i], 0);
			}
			if(vysS != null){
				vynullujData();
				naplnMapy(vysS);
				VykresliGraf vyskresli = new VykresliGraf(rok, osCislo, vysS.get(0).getJmenoAutora(), 
					ulohaDenPocet, ulohaDenPocetSpravne,celkovyPocetDnuOdevzdavani, dnyAodevzdavani, 
						vystupniAdresar);
			}
			if(info){
				System.out.print("Zpracováno " + ++j + "/" + celkovyPocetStudentu + " studentů. \r");
			}
		}
	}

	/**
	 * Vloží do map jako klič id všech úloh.
	 */
	private void vynullujData(){
		for(int i = 0; i < Konstanty.ID_ULOH.length; i++){
			ulohaDenPocet.put(Konstanty.ID_ULOH[i], null);
			ulohaDenPocetSpravne.put(Konstanty.ID_ULOH[i], null);
		}
		celkovyPocetDnuOdevzdavani = 0;
	}

	/**
	 * Připraví mapy s výsledky studenta.
	 * @param rok
	 * @param vysledky
	 */
	private void pripravVysledkyStudenta(int rok, List<VysledekValidaceSouboru> vysledky){
		for(VysledekValidaceSouboru vys: vysledky){
			String osCislo = vys.getOsobniCislo();
			if (vysledkyStudenta.containsKey(osCislo) == false){
				vysledkyStudenta.put(osCislo, null);
   			}
			
   			ArrayList<VysledekValidaceSouboru> pom;
   			if(vysledkyStudenta.get(osCislo) == null){
   				pom = new ArrayList();
   				vysledkyStudenta.put(osCislo, pom);
   			}
   			else{
   				pom = vysledkyStudenta.get(osCislo);
   			}
   			pom.add(vys);
		}
	}

	/**
	 * Parametrem jsou výsledky validací, postupně jsou procházeny a jejich počty
	 * zapisovány do příslušných map.
	 * @param vysS
	 */
	private void naplnMapy(ArrayList<VysledekValidaceSouboru> vysS){
		for(VysledekValidaceSouboru vys: vysS){
			if((ulohaDenPocet.get(vys.getIdUlohy()) == null)){
				ulohaDenPocet.put(vys.getIdUlohy(), vytvorMapicku(vys.getDatumValidace(), 1));
				dnyAodevzdavani.put(vys.getIdUlohy(), dnyAodevzdavani.get(vys.getIdUlohy()) + 1);
				celkovyPocetDnuOdevzdavani++;
			}
			else if((ulohaDenPocet.get(vys.getIdUlohy()).get(vys.getDatumValidace()) == null)){	
				ulohaDenPocet.get(vys.getIdUlohy()).put(vys.getDatumValidace(), 1);
				dnyAodevzdavani.put(vys.getIdUlohy(), dnyAodevzdavani.get(vys.getIdUlohy()) + 1);
				celkovyPocetDnuOdevzdavani++;
			}
			else{
				ulohaDenPocet.get(vys.getIdUlohy()).put(vys.getDatumValidace(),
						ulohaDenPocet.get(vys.getIdUlohy()).get(vys.getDatumValidace()) + 1);
			}
			// zde sčítím zvlášť úspěšné validace
			if(vys.getVysledek().getPopisVysledku().compareTo(Konstanty.OK) == 0){
				int vcas = vys.getOdevzdazdanoVcas() ? 1 : -1;
				if((ulohaDenPocetSpravne.get(vys.getIdUlohy()) == null)){
					ulohaDenPocetSpravne.put(vys.getIdUlohy(), vytvorMapicku(vys.getDatumValidace(), vcas));
				}
				else if((ulohaDenPocetSpravne.get(vys.getIdUlohy()).get(vys.getDatumValidace()) == null)){	
					ulohaDenPocetSpravne.get(vys.getIdUlohy()).put(vys.getDatumValidace(), vcas);
				}
				else{
					ulohaDenPocetSpravne.get(vys.getIdUlohy()).put(vys.getDatumValidace(), ulohaDenPocetSpravne.get(vys.getIdUlohy()).get(vys.getDatumValidace()) + vcas );
				}
			}
		}
	}
	

	/**
	 * Vytvoří mapu, kterou vkládám do map validací.
	 * @param j - datum
	 * @param i - počet
	 * @return
	 */
	private TreeMap<LocalDate, Integer> vytvorMapicku(LocalDate j, int i){
		TreeMap<LocalDate, Integer> pom = new TreeMap<LocalDate, Integer>();
		pom.put(j, i);
		return pom;
	}
}
