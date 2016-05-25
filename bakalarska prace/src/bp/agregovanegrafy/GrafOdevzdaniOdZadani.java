package bp.agregovanegrafy;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import knihovna.Barva;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import bp.Konstanty;
import bp.nactenidat.VysledekValidaceSouboru;

/**
 * Tato třída slouží pro vytvoření agregovaného grafu Odevzdání od zadání,
 * který slouží k přehledu po kolika dnech od zadání studenti úlohy validují.
 * Znázorňuje úspěšné validace i neúspěšné pokusy.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class GrafOdevzdaniOdZadani extends ApplicationFrame{
	private static final long serialVersionUID = 1L;
		/** <počet dní od zadání, počet pokusů v tento den> */
	static TreeMap<Integer, Integer> denOdZadaniKO = new TreeMap<Integer, Integer>();
		/** <počet dní od zadání, počet včasných uspěšných pokusů v tento den> */
	static TreeMap<Integer, Integer> denOdZadaniOKvcas = new TreeMap<Integer, Integer>();
		/** <počet dní od zadání, počet opožděných uspěšných pokusů v tento den> */
	static TreeMap<Integer, Integer> denOdZadaniOKpozde = new TreeMap<Integer, Integer>();
	private String titulek;
	private String idUlohy;
	private int max = 0; //největší počet odevzdání v jeden den
	
	/**
	 * V tomto konstruktoru je dle parametrů nastaven titulek, idUlohy a výstupní
	 * adresář. Dále jsou volány metody pro zpracování předaných výsledků validací.
	 * @param title - titulek grafu
	 * @param rok - aktuální rok, pro který je graf tvořen
	 * @param vysledky - výsledky validací daný rok, které budou v této třídě zpracovávány
	 * @param idUlohy - idUlohy, kterou zrovna zpracovávám, pokud je nastaveno na
	 * hodnotu null, zpracuji všechny úlohy
	 * @param jenUspesne - přepínač určující, zda zpracovávám jen úspěšné validace
	 * nebo i pokusy
	 * @param vystupniAdresar - kořenový adresář, do kterého mají být grafy tvořeny
	 */
	public GrafOdevzdaniOdZadani(final String title, int rok, ArrayList<VysledekValidaceSouboru> vysledky,
			String idUlohy, boolean jenUspesne, String vystupniAdresar){
		super(title);
		titulek = title;
		this.idUlohy = idUlohy;
	    VytvorMapu();
	    NaplnMapu(vysledky,jenUspesne);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, jenUspesne);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(Konstanty.SIRKA_A, Konstanty.VYSKA_A));
        setContentPane(chartPanel);
        uloz(rok, jenUspesne, chart, vystupniAdresar);
  	}

	/**
	 * V této metodě je vygenerován graf a uložen.
	 * @param rok
	 * @param jenUspesne
	 * @param chart
	 * @param vystupniAdresar
	 */
	private void uloz(int rok, boolean jenUspesne, JFreeChart chart, String vystupniAdresar){
		File nazevSlozky = new File (vystupniAdresar);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        
        nazevSlozky = new File (vystupniAdresar + "/" + rok);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        String nazev = "";
        if(jenUspesne != Konstanty.JEN_USPESNE){
        	 nazevSlozky = new File (vystupniAdresar + "/" + rok + Konstanty.GRAF_ODZADANI_VSE_SLOZKA);
        	 nazev = Konstanty.GRAF_ODEVZDANI_OD_ZADANI_VSE_SOUBOR;
        }
        else{
        	 nazevSlozky = new File (vystupniAdresar + "/" + rok + Konstanty.GRAF_OD_ZADANI_USPESNE_SLOZKA);
        	 nazev = Konstanty.GRAF_ODEVZDANI_OD_ZADANI_OK_SOUBOR;
        }
        
		if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
		String id = "" + rok + "_";
		if(idUlohy == null){
			id = id + "celkovy";
		}
		else{
			id = id + idUlohy;
		}
        File pieChart = new File(nazevSlozky + nazev + id + Konstanty.KONCOVKA); 
        try{
			ChartUtilities.saveChartAsPNG(pieChart, chart, Konstanty.SIRKA_A, Konstanty.VYSKA_A);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * V této metodě jsou naplněny mapy denOdZadaniOKvcas, denOdZadaniOKpozde 
	 * a denOdZadaniKO, jejich hodnotami jsou čísla od 0 do 30, které odpovídají 
	 * počtu dní odevzdání od zadání úlohy. Na ose Y je počet validací tento den.
	 * V případě že počet dní od zadání do odevzdání přesáhne 30, je hodnota vždy 
	 * zaznamenána na 30. index.
	 * @param vysledky
	 * @param jenUspesne
	 */
	private void NaplnMapu(List<VysledekValidaceSouboru> vysledky, boolean jenUspesne){
		for(VysledekValidaceSouboru vys: vysledky){
			String popisVysledku = vys.getVysledek().getPopisVysledku();
			if((vys.getIdUlohy()!= null) && (vys.getDatumZacatku()!= null)){
				//pokud se nemají zpracovávat jen úspěšné pokusy a zároveň výsledek validace není OK
				if((jenUspesne != Konstanty.JEN_USPESNE) && (popisVysledku.compareTo(Konstanty.OK) != 0)){
					//pokud nebyla specifikována úloha nebo aktuální úloha vyhovuje specifikaci
					if((idUlohy == null) || (vys.getIdUlohy().compareTo(idUlohy) == 0)){
						pridejDoMap(vys, denOdZadaniKO);
					}
				}

				if((vys.getVysledek().getPopisVysledku().compareTo("OK") == 0) && (vys.getOdevzdazdanoVcas())){
						if ((idUlohy == null) || (vys.getIdUlohy().compareTo(idUlohy) == 0)){
							pridejDoMap(vys, denOdZadaniOKvcas);
						}
						
				}	
				else if((vys.getVysledek().getPopisVysledku().compareTo("OK" )== 0) && (!vys.getOdevzdazdanoVcas())){
					if((idUlohy==null) || (vys.getIdUlohy().compareTo(idUlohy) == 0)){
						pridejDoMap(vys, denOdZadaniOKpozde);
					}
				}
			}	
		}
	}

	/**
	 * Připočte do požadované mapy zpracovávaný výsledek validace.
	 * @param vys - zpracovávaný výsledek validace
	 * @param map - mapa, klíčem je den od zadání úlohy a hodnotou počet odevzdání tento 
	 * den splňující požadavky (včas/ok ..)
	 */
	private void pridejDoMap(VysledekValidaceSouboru vys, TreeMap<Integer, Integer> map){
		int denOdevzdani = vys.getDatumValidace().getDayOfYear() - vys.getDatumZacatku().getDayOfYear();
		if((denOdevzdani >= 0) && (denOdevzdani < 30)){
			map.put(denOdevzdani, map.get(denOdevzdani) + 1);
		}
		else{
			map.put(30, map.get(30) + 1);
		}
	}

	/**
	 * Vytvoří mapy denOdZadaniKO, denOdZadaniOKvcas a denOdZadaniOKpozde.
	 * Klíčem jsou čísla od 0 do 30 a hodnota je vždy nastavena na 0.
     * Hodnota reprezentuje počet načtených úloh odevzdaných daný den. 
	 * @param vysledky
	 */
	private void VytvorMapu(){
		denOdZadaniKO.clear();
		denOdZadaniOKvcas.clear();
		denOdZadaniOKpozde.clear();
		for(int i = 0; i < 31; i++){
			denOdZadaniKO.put(i, 0);
			denOdZadaniOKvcas.put(i, 0);
			denOdZadaniOKpozde.put(i, 0);
		}
	}	
	
	/**
	 * Připraví dataset.
	 * @return
	 */
	private CategoryDataset createDataset(){
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for(Integer key: denOdZadaniKO.keySet()){
			if(key < 30){
				result.addValue(denOdZadaniKO.get(key), Konstanty.LEGENDA_NEUSPESNE, "" + (key));
				result.addValue(denOdZadaniOKvcas.get(key), Konstanty.LEGENDA_USPESNE_VCAS, "" + (key));
				result.addValue(denOdZadaniOKpozde.get(key), Konstanty.LEGENDA_USPESNE_POZDE, "" + (key));
			}
			else{
				result.addValue(denOdZadaniKO.get(key), Konstanty.LEGENDA_NEUSPESNE, "30...");
				result.addValue(denOdZadaniOKvcas.get(key),	Konstanty.LEGENDA_USPESNE_VCAS, "30...");
				result.addValue(denOdZadaniOKpozde.get(key), Konstanty.LEGENDA_USPESNE_POZDE, "30...");
			}
			int pocOdevzdani = denOdZadaniKO.get(key) + denOdZadaniOKvcas.get(key) + denOdZadaniOKpozde.get(key);
			if(pocOdevzdani > max){
				max = pocOdevzdani;
			}
		}
	    
	        return result;
    }
	
	/**
	 * Nastaví parametry grafu.
	 * @param dataset
	 * @param jenUspesne
	 * @return
	 */
	private JFreeChart createChart(final CategoryDataset dataset, boolean jenUspesne){
        final JFreeChart chart = ChartFactory.createStackedBarChart(
            titulek,  										// titulek grafu
            Konstanty.ODEVZDANI_OD_ZADANI_POPIS_X ,         // domain axis label
            Konstanty.POPIS_Y,                    			// range axis label
            dataset,                  					    // data
            PlotOrientation.VERTICAL,  					    // plot orientace
            true,                        					// legenda
            true,                					        // tooltipy
            false              						        // url
        );
       
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap();
      
        renderer.setSeriesToGroupMap(map);
        renderer.setItemMargin(0.10);
        renderer.setDrawBarOutline(false);
        Paint p1 = new GradientPaint(0.0f, 0.0f, Barva.ZLATA.getColor(),
                0.0f, 0.0f, Barva.ZLUTA.getColor());
        renderer.setSeriesPaint(0, p1);
        Paint p2 = new GradientPaint(0.0f, 0.0f, Barva.KHAKI.getColor(),
                0.0f, 0.0f, Barva.ZELENA.getColor());
        renderer.setSeriesPaint(1, p2);
        Paint p3 = new GradientPaint(0.0f, 0.0f, Barva.CIHLOVA.getColor(),
                0.0f, 0.0f, Barva.CERVENA.getColor());
        renderer.setSeriesPaint(2, p3);
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(createLegendItems(jenUspesne));
        if(max < Konstanty.MIN_POCET_POLOZEK){
        	NumberAxis range = (NumberAxis) plot.getRangeAxis();
        	range.setTickUnit(new NumberTickUnit(1));
        }
        
        return chart;
    }
	
	/**
	 * Nastaví legendu.
	 * @return
	 */
	private LegendItemCollection createLegendItems(boolean jenUspesne){
		LegendItemCollection result = new LegendItemCollection();
		if(!jenUspesne){
		LegendItem item1 = new LegendItem(Konstanty.LEGENDA_NEUSPESNE, "-", null, null,
				Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.ZLUTA.getColor());
		result.add(item1);
		}
		LegendItem item2 = new LegendItem(Konstanty.LEGENDA_USPESNE_VCAS,
				"-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.ZELENA.getColor());
		LegendItem item3 = new LegendItem(Konstanty.LEGENDA_USPESNE_POZDE,
				"-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.CERVENA.getColor());
		result.add(item2);
		result.add(item3);
	 
		return result;
	 }
}
